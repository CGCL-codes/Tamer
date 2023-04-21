package org.dcm4chex.archive.ejb.conf;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.VRs;
import org.dcm4cheri.util.StringUtils;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 1006 $ $Date: 2004-02-22 15:09:10 -0500 (Sun, 22 Feb 2004) $
 * @since 28.12.2003
 */
public class AttributeCoercion {

    private static Logger log = Logger.getLogger(AttributeCoercion.class);

    ArrayList list = new ArrayList();

    Hashtable params = new Hashtable();

    private abstract static class Coerce {

        final int tag;

        Coerce(int tag) {
            this.tag = tag;
        }

        void coerce(Dataset ds, Dataset coercedElements) {
            DcmElement oldEl = ds.get(tag);
            DcmElement newEl = ds.putXX(tag, value(ds));
            log.info("Coerce " + oldEl + " to " + newEl);
            coercedElements.putXX(tag, newEl.getByteBuffer());
        }

        protected abstract String[] value(Dataset ds);
    }

    private static class LiteralCoerce extends Coerce {

        final String[] value;

        LiteralCoerce(int tag, String value) {
            super(tag);
            this.value = value != null ? StringUtils.split(value, '\\') : null;
        }

        protected String[] value(Dataset ds) {
            return value;
        }
    }

    private static class CopyCoerce extends Coerce {

        final int srcTag;

        CopyCoerce(int tag, String value) {
            super(tag);
            this.srcTag = Tags.valueOf(value.substring(1));
        }

        protected String[] value(Dataset ds) {
            DcmElement el = ds.get(srcTag);
            if (el == null) {
                return null;
            }
            if (el.vm() == VRs.UN) {
                return new String[] { el.getByteBuffer().asCharBuffer().toString() };
            }
            return ds.getStrings(srcTag);
        }
    }

    private static class Substring {

        final int beginIndex;

        final int endIndex;

        Substring(int beginIndex, int endIndex) {
            if (beginIndex < 0 || endIndex < beginIndex) {
                throw new IllegalArgumentException();
            }
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        String applyTo(String s) {
            final int l = s.length();
            return s.substring(Math.min(l, beginIndex), Math.min(l, endIndex));
        }
    }

    private static class GeneralCoerce extends Coerce {

        final String[] literals;

        final int[] srcTags;

        final int[] srcIndex;

        final Substring[] substring;

        final CoercionLUT[] luts;

        GeneralCoerce(int tag, String value, Hashtable lutsByName) {
            super(tag);
            ArrayList tokens = new ArrayList();
            int left = 0, pos = -2, srcTag;
            while ((pos = value.indexOf("$(", pos + 2)) != -1 && pos + 12 <= value.length() && (srcTag = Tags.valueOf(value.substring(pos + 1, pos + 12))) != -1) {
                tokens.add(value.substring(left, pos));
                left = pos;
            }
            tokens.add(value.substring(left));
            literals = new String[tokens.size()];
            srcTags = new int[literals.length - 1];
            srcIndex = new int[literals.length - 1];
            substring = new Substring[literals.length - 1];
            luts = new CoercionLUT[literals.length - 1];
            literals[0] = (String) tokens.get(0);
            for (int i = 0; i < srcTags.length; i++) {
                String tk = (String) tokens.get(i + 1);
                srcTags[i] = Tags.valueOf(tk.substring(1, 12));
                int startLiteral = 12;
                if (tk.length() > startLiteral + 2 && tk.charAt(startLiteral) == '[') {
                    final int last = tk.indexOf(']', startLiteral + 2);
                    if (last != -1) {
                        try {
                            int tmp = Integer.parseInt(tk.substring(13, last));
                            if (tmp >= 0) {
                                srcIndex[i] = tmp;
                                startLiteral = last + 1;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                if (tk.length() > startLiteral + 2 && tk.charAt(startLiteral) == '[') {
                    final int comma = tk.indexOf(',', startLiteral + 2);
                    if (comma != -1 && tk.length() > comma + 2) {
                        final int last = tk.indexOf(']', comma + 2);
                        if (last != -1) {
                            try {
                                substring[i] = new Substring(Integer.parseInt(tk.substring(startLiteral + 1, comma)), Integer.parseInt(tk.substring(comma + 1, last)));
                                startLiteral = last + 1;
                            } catch (IllegalArgumentException e) {
                            }
                        }
                    }
                }
                if (tk.length() > startLiteral + 3 && tk.charAt(startLiteral) == '{') {
                    final int last = tk.indexOf('}', startLiteral + 2);
                    if (last != -1) {
                        luts[i] = (CoercionLUT) lutsByName.get(tk.substring(startLiteral + 1, last));
                        startLiteral = last + 1;
                    }
                }
                literals[i + 1] = tk.substring(startLiteral);
            }
        }

        protected String[] value(Dataset ds) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < srcTags.length; ++i) {
                sb.append(literals[i]);
                DcmElement el = ds.get(srcTags[i]);
                String val = el == null ? "" : (el.vm() == VRs.UN) ? el.getByteBuffer().asCharBuffer().toString() : ds.getString(srcTags[i], srcIndex[i]);
                if (substring[i] != null) {
                    val = substring[i].applyTo(val);
                }
                sb.append(luts[i] != null ? luts[i].lookup(val) : val);
            }
            sb.append(literals[srcTags.length]);
            return StringUtils.split(sb.toString(), '\\');
        }
    }

    void add(int tag, String val, Hashtable lutsByName) {
        list.add(makeCoerce(tag, val, lutsByName));
    }

    private Coerce makeCoerce(int tag, String val, Hashtable lutsByName) {
        int pos;
        if (val == null || (pos = val.indexOf("$(")) == -1) {
            return new LiteralCoerce(tag, val);
        }
        if (pos == 0 && Tags.valueOf(val.substring(1)) != -1) {
            return new CopyCoerce(tag, val);
        }
        return new GeneralCoerce(tag, val, lutsByName);
    }

    public void coerce(Dataset ds, Dataset coercedElements) {
        for (int i = 0, n = list.size(); i < n; ++i) {
            ((Coerce) list.get(i)).coerce(ds, coercedElements);
        }
    }
}
