package org.databene.document.fixedwidth;

import org.databene.script.AbstractScript;
import org.databene.script.Script;
import org.databene.script.ScriptException;
import org.databene.script.ScriptUtil;
import org.databene.script.ScriptedDocumentWriter;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.SystemInfo;
import org.databene.commons.bean.BeanToPropertyArrayConverter;
import org.databene.commons.converter.ArrayConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.FormatFormatConverter;
import org.databene.commons.converter.ToStringConverter;
import org.databene.commons.format.PadFormat;
import java.io.IOException;
import java.io.Writer;

/**
 * Writes JavaBeans as flat file columns.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 * @author Volker Bergmann
 */
public class BeanFixedWidthWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanFixedWidthWriter(Writer out, FixedWidthColumnDescriptor... descriptors) {
        this(out, (Script) null, (Script) null, descriptors);
    }

    public BeanFixedWidthWriter(Writer out, String headerScriptUrl, String footerScriptUrl, FixedWidthColumnDescriptor... descriptors) throws IOException {
        this(out, (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null), (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null), descriptors);
    }

    public BeanFixedWidthWriter(Writer out, Script headerScript, Script footerScript, FixedWidthColumnDescriptor... descriptors) {
        super(out, headerScript, new BeanFixedWidthScript(descriptors), footerScript);
    }

    private static class BeanFixedWidthScript extends AbstractScript {

        private Converter<Object, String[]> converter;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public BeanFixedWidthScript(FixedWidthColumnDescriptor[] descriptors) {
            int length = descriptors.length;
            String[] propertyNames = new String[length];
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++) {
                FixedWidthColumnDescriptor descriptor = descriptors[i];
                propertyNames[i] = descriptor.getName();
                propertyConverters[i] = new ConverterChain(new ToStringConverter(), new FormatFormatConverter(String.class, new PadFormat(descriptor.getWidth(), descriptor.getAlignment(), ' '), true));
            }
            this.converter = new ConverterChain(new BeanToPropertyArrayConverter(propertyNames), new ArrayConverter(Object.class, String.class, propertyConverters));
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                String[] cells = converter.convert(context.get("part"));
                for (int i = 0; i < cells.length; i++) out.write(cells[i]);
                out.write(SystemInfo.getLineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }
}
