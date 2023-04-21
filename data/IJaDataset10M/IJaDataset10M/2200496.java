package org.apache.ws.jaxme.generator.types;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SGlet;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.SimpleTypeSG;
import org.apache.ws.jaxme.impl.DatatypeConverterImpl;
import org.apache.ws.jaxme.impl.JMUnmarshallerImpl;
import org.apache.ws.jaxme.js.DirectAccessible;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaQNameImpl;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.LocalJavaField;
import org.apache.ws.jaxme.js.TypedValue;
import org.apache.ws.jaxme.js.impl.TypedValueImpl;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.parser.impl.LocSAXException;
import org.apache.ws.jaxme.xs.util.XsDateTimeFormat;
import org.xml.sax.SAXException;
import dfdl.exception.EndOfStreamException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class DateTimeSG extends AtomicTypeSGImpl {

    public static final JavaQName CALENDAR_TYPE = JavaQNameImpl.getInstance(Calendar.class);

    private static final JavaQName OBJECT_TYPE = JavaQNameImpl.getInstance(Object.class);

    /** <p>Creates a new instance of DurationSG.</p>
     */
    public DateTimeSG(SGFactory pFactory, SchemaSG pSchema, XSType pType) throws SAXException {
        super(pFactory, pSchema, pType);
    }

    protected String getDatatypeName() {
        return "DateTime";
    }

    protected JavaQName getDatatypeType() {
        return CALENDAR_TYPE;
    }

    protected Class getFormatClass() {
        return XsDateTimeFormat.class;
    }

    public JavaQName getRuntimeType(SimpleTypeSG pController) {
        return CALENDAR_TYPE;
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, String pValue) throws SAXException {
        try {
            Calendar calendar = new DatatypeConverterImpl().parseDate(pValue);
            return new TypedValueImpl(new Object[] { "new java.util.GregorianCalendar(" + calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DAY_OF_MONTH) + ")" }, getDatatypeType());
        } catch (RuntimeException e) {
            try {
                throw new LocSAXException("Failed to convert string value to " + getDatatypeName() + " instance: " + pValue, getLocator());
            } catch (Exception e1) {
                throw new SAXException("Failed to convert string value to " + getDatatypeName() + " instance: " + pValue);
            }
        }
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, Object pData) throws SAXException {
        boolean mayBeDate;
        Object format;
        if (pData == null) {
            format = new Object[] { "new ", getFormatClass(), "()" };
            mayBeDate = false;
        } else {
            format = new Object[] { "((", JMUnmarshallerImpl.class, ") ", pData, ".getJMUnmarshaller()).get" + getDatatypeName(), "Format()" };
            mayBeDate = true;
        }
        if (!(pValue instanceof DirectAccessible)) {
            LocalJavaField v = pMethod.newJavaField(String.class);
            v.addLine(pValue);
            pValue = v;
        }
        LocalJavaField pos = pMethod.newJavaField(ParsePosition.class);
        pos.addLine("new ", ParsePosition.class, "(0)");
        LocalJavaField cal = pMethod.newJavaField(mayBeDate ? OBJECT_TYPE : pController.getRuntimeType());
        cal.addLine(format, ".parseObject(", pValue, ", ", pos, ");");
        pMethod.addIf(cal, " == null");
        pMethod.addThrowNew(IllegalArgumentException.class, JavaSource.getQuoted("Failed to parse dateTime "), " + ", pValue, " + ", JavaSource.getQuoted(" at: "), " + ", pValue, ".substring(", pos, ".getErrorIndex())");
        pMethod.addEndIf();
        if (mayBeDate) {
            LocalJavaField result = pMethod.newJavaField(pController.getRuntimeType());
            pMethod.addIf(cal, " instanceof ", Calendar.class);
            pMethod.addLine(result, " = (", Calendar.class, ") ", cal, ";");
            pMethod.addElse();
            pMethod.addLine(result, " = ", Calendar.class, ".getInstance();");
            pMethod.addLine(result, ".setTime((", Date.class, ") ", cal, ");");
            pMethod.addEndIf();
            return result;
        } else {
            return cal;
        }
    }

    public void forAllNonNullValues(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, SGlet pSGlet) throws SAXException {
        LocalJavaField f = pMethod.newJavaField(CALENDAR_TYPE);
        f.addLine("null");
        pMethod.addTry();
        pMethod.addLine(f, " = ", pValue, ";");
        pMethod.addCatch(EndOfStreamException.class);
        pMethod.addLine("System.out.println(\"Warning: End of Stream encountered\");");
        pMethod.addEndTry();
        pMethod.addIf(f, " != null");
        pSGlet.generate(pMethod, f);
        pMethod.addLine("isEmpty = false;");
        pMethod.addElse();
        pMethod.addLine("foundTerm = true;");
        pMethod.addEndIf();
    }

    public void forAllValues(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, SGlet pSGlet) throws SAXException {
        pSGlet.generate(pMethod, pValue);
    }
}
