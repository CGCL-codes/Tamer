package org.apache.ws.jaxme.generator.types;

import org.apache.ws.jaxme.generator.sg.AtomicTypeSG;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SGlet;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.SimpleTypeSG;
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
import org.xml.sax.SAXException;
import dfdl.exception.EndOfStreamException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class StringSG extends AtomicTypeSGImpl {

    /** <p>Creates a new instance of StringTypeSG.java.</p>
   */
    public StringSG(SGFactory pFactory, SchemaSG pSchema, XSType pType) throws SAXException {
        super(pFactory, pSchema, pType);
    }

    /** The string type.
   */
    public static final JavaQName STRING_TYPE = JavaQNameImpl.getInstance(String.class);

    protected String getDatatypeName() {
        return "String";
    }

    protected JavaQName getDatatypeType() {
        return STRING_TYPE;
    }

    public JavaQName getRuntimeType(SimpleTypeSG pController) {
        return STRING_TYPE;
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, String pValue) {
        return new TypedValueImpl(JavaSource.getQuoted(pValue), STRING_TYPE);
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, Object pData) {
        return new TypedValueImpl(pValue, STRING_TYPE);
    }

    public TypedValue getCastToString(SimpleTypeSG pController, Object pValue, DirectAccessible pData) {
        return new TypedValueImpl(pValue, STRING_TYPE);
    }

    public void forAllNonNullValues(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, SGlet pSGlet) throws SAXException {
        LocalJavaField f = pMethod.newJavaField(STRING_TYPE);
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

    public boolean isCausingParseConversionEvent(SimpleTypeSG pController) {
        final AtomicTypeSG atomicType = pController.getAtomicType();
        return atomicType.getLength() != null || atomicType.getMinLength() != null || atomicType.getMaxLength() != null;
    }

    public void addValidation(SimpleTypeSG pController, JavaMethod pMethod, DirectAccessible pValue) throws SAXException {
        final AtomicTypeSG atomicType = pController.getAtomicType();
        Long length = atomicType.getLength();
        Long maxLength = atomicType.getMaxLength();
        Long minLength = atomicType.getMinLength();
        if (minLength != null && minLength.longValue() < 0) {
            throw new LocSAXException("Negative value for minLength detected: " + minLength, getLocator());
        }
        if (maxLength != null) {
            if (maxLength.longValue() < 0) {
                throw new LocSAXException("Negative value for maxLength detected: " + maxLength, getLocator());
            }
            if (minLength != null) {
                if (maxLength.longValue() < minLength.longValue()) {
                    throw new LocSAXException("MaxLength value of " + maxLength + " is smaller than minLength value of " + minLength, getLocator());
                }
                if (maxLength.longValue() == minLength.longValue()) {
                    length = maxLength;
                }
            }
        }
        if (length != null) {
            if (length.longValue() < 0) {
                throw new LocSAXException("Negative value for length detected: " + length, getLocator());
            }
            if (maxLength != null) {
                if (maxLength.longValue() < length.longValue()) {
                    throw new LocSAXException("MaxLength value of " + maxLength + " is smaller than length value of " + length, getLocator());
                }
                maxLength = null;
            }
            if (minLength != null) {
                if (minLength.longValue() > length.longValue()) {
                    throw new LocSAXException("MinLength value of " + minLength + " is larger than length value of " + length, getLocator());
                }
                minLength = null;
            }
        }
        if (length != null || maxLength != null || minLength != null) {
            if (pValue.isNullable()) {
                pMethod.addIf(pValue, " != null");
            }
            if (maxLength != null) {
                pMethod.addIf(pValue, ".length()", " > ", maxLength);
                pMethod.addThrowNew(IllegalArgumentException.class, JavaSource.getQuoted("Length of " + maxLength + " characters exceeded: "), " + ", pValue);
                pMethod.addEndIf();
            }
            if (minLength != null) {
                pMethod.addIf(pValue, ".length()", " < ", minLength);
                pMethod.addThrowNew(IllegalArgumentException.class, JavaSource.getQuoted("Length of " + minLength + " characters exceeded: "), " + ", pValue);
                pMethod.addEndIf();
            }
            if (length != null) {
                pMethod.addIf(pValue, ".length()", " != ", length);
                pMethod.addThrowNew(IllegalArgumentException.class, JavaSource.getQuoted("Length of " + length + " characters not matched: "), " + ", pValue);
                pMethod.addEndIf();
            }
            if (pValue.isNullable()) {
                pMethod.addEndIf();
            }
        }
    }
}
