package com.inxar.jenesis;

import java.util.Enumeration;
import java.util.Properties;
import org.inxar.jenesis.*;
import com.inxar.jenesis.util.BlockStyle;
import com.inxar.jenesis.util.MemberComparator;

/**
 * Base class for the block style classes.
 */
abstract class MStyle implements BlockStyle {

    static class Standard extends MStyle {

        public void toCode(CodeWriter out, Iterator iterator) {
            if (!out.isLineNew()) out.newLine();
            if (iterator.size() > 0) {
                out.write('{').indentLine().write(iterator).dedentLine().write('}');
            } else {
                out.write('{').newLine().write('}');
            }
        }
    }

    static class SameLine extends MStyle {

        public void toCode(CodeWriter out, Iterator iterator) {
            if (iterator.size() > 0) {
                out.space().write('{').indentLine().write(iterator).dedentLine().write('}').space();
            } else {
                out.space().write('{').newLine().write('}');
            }
        }
    }

    static class Optional extends MStyle {

        public void toCode(CodeWriter out, Iterator iterator) {
            if (iterator.size() == 1) {
                out.indentLine().write(iterator).dedentLine();
            } else {
                if (iterator.size() != 0) {
                    out.space().write('{').indentLine().write(iterator).dedentLine().write('}');
                } else {
                    out.space().write('{').newLine().write('}');
                }
            }
        }
    }

    static class MStyleMap {

        MStyleMap(Properties p) {
            this.p = p;
            initStyles();
            initMap();
        }

        void initStyles() {
            Enumeration e = p.propertyNames();
            String key = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.startsWith("style.")) {
                    addStyle(key, p.getProperty(key));
                }
            }
        }

        void initMap() {
            Enumeration e = p.propertyNames();
            String key = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.startsWith("style.")) {
                    continue;
                }
                p.put(key, p.get(p.getProperty(key)));
            }
        }

        void addStyle(String key, String className) {
            try {
                p.put(key, Class.forName(className).newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        BlockStyle get(String key) {
            return (BlockStyle) p.get(key);
        }

        Properties p;
    }

    MStyle() {
    }
}
