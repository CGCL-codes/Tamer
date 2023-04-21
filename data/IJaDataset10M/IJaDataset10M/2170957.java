package org.apache.ws.jaxme.generator.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.ws.jaxme.generator.sg.Context;
import org.apache.ws.jaxme.generator.sg.ListTypeSG;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SGlet;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.SimpleTypeSG;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.js.DirectAccessible;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaQNameImpl;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.LocalJavaField;
import org.apache.ws.jaxme.js.TypedValue;
import org.apache.ws.jaxme.js.impl.TypedValueImpl;
import org.apache.ws.jaxme.xs.XSListType;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.xml.XsQName;
import org.xml.sax.SAXException;
import dfdl.exception.EndOfStreamException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class ListTypeSGImpl extends SimpleTypeSGImpl {

    public static final JavaQName LIST_TYPE = JavaQNameImpl.getInstance(List.class);

    public static final JavaQName ARRAYLIST_TYPE = JavaQNameImpl.getInstance(ArrayList.class);

    private class InnerListTypeSG implements ListTypeSG {

        private final Long length, minLength, maxLength;

        public InnerListTypeSG(XSListType pListType) {
            length = pListType.getLength();
            maxLength = pListType.getMaxLength();
            minLength = pListType.getMinLength();
        }

        public TypeSG getItemType() {
            return ListTypeSGImpl.this.getItemType();
        }

        public Long getLength() {
            return length;
        }

        public Long getMaxLength() {
            return maxLength;
        }

        public Long getMinLength() {
            return minLength;
        }
    }

    private final XSListType listType;

    private final ListTypeSG listTypeSG;

    private TypeSG itemType;

    private final Context classContext;

    private final XsQName name;

    public boolean hasSetMethod(SimpleTypeSG pController) {
        return "indexed".equals(pController.getCollectionType());
    }

    public boolean isList(SimpleTypeSG pController) {
        return true;
    }

    /** <p>Creates a new instance of ListTypeSG in the given {@link Context}.</p>
   */
    public ListTypeSGImpl(SGFactory pFactory, SchemaSG pSchemaSG, XSType pType, Context pClassContext, XsQName pName) throws SAXException {
        super(pFactory, pSchemaSG, pType);
        name = pName;
        classContext = pClassContext;
        listType = pType.getSimpleType().getListType();
        listTypeSG = new InnerListTypeSG(listType);
    }

    public void init(SimpleTypeSG pController) throws SAXException {
        itemType = getFactory().getTypeSG(listType.getItemType(), classContext, name);
    }

    protected TypeSG getItemType() {
        return itemType;
    }

    public ListTypeSG getListType(SimpleTypeSG pController) {
        return listTypeSG;
    }

    public JavaQName getRuntimeType(SimpleTypeSG pController) {
        if ("indexed".equals(pController.getCollectionType())) {
            return JavaQNameImpl.getArray(itemType.getSimpleTypeSG().getRuntimeType());
        } else {
            return LIST_TYPE;
        }
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, String pValue) throws SAXException {
        List list = new ArrayList();
        for (StringTokenizer st = new StringTokenizer(pValue); st.hasMoreElements(); ) {
            if (list.size() > 0) {
                list.add(", ");
            }
            list.add(itemType.getSimpleTypeSG().getCastFromString(st.nextToken()));
        }
        Object result = new Object[] { "new ", itemType.getSimpleTypeSG().getRuntimeType(), "[]{", list, "}" };
        String collectionType = pController.getCollectionType();
        if ("indexed".equals(collectionType)) {
            return new TypedValueImpl(result, itemType.getSimpleTypeSG().getRuntimeType());
        } else {
            JavaQName myListType = JavaQNameImpl.getInstance(collectionType, true);
            return new TypedValueImpl(new Object[] { "(new ", myListType, "(", Arrays.class, ".asList(", result, ")))" }, myListType);
        }
    }

    public TypedValue getCastFromString(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, Object pData) throws SAXException {
        LocalJavaField list = pMethod.newJavaField(LIST_TYPE);
        String collectionType = pController.getCollectionType();
        list.addLine("new ", ("indexed".equals(collectionType) ? ARRAYLIST_TYPE : JavaQNameImpl.getInstance(collectionType, true)), "()");
        DirectAccessible st = pMethod.addForEnumeration(StringTokenizer.class, new Object[] { "new ", StringTokenizer.class, "(", pValue, ")" });
        pMethod.addLine(list, ".add(", itemType.getSimpleTypeSG().getCastFromString(pMethod, new Object[] { st, ".nextToken()" }, pData), ");");
        pMethod.addEndFor();
        if ("indexed".equals(collectionType)) {
            JavaQName iType = itemType.getSimpleTypeSG().getRuntimeType();
            return new TypedValueImpl(new Object[] { "((", iType, ") ", list, ".toArray(new ", iType, "[", list, ".size()]))" }, itemType.getSimpleTypeSG().getRuntimeType());
        } else {
            JavaQName myListType = JavaQNameImpl.getInstance(collectionType, true);
            return new TypedValueImpl(list, myListType);
        }
    }

    public TypedValue getCastToString(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, DirectAccessible pData) throws SAXException {
        String collectionType = pController.getCollectionType();
        DirectAccessible value;
        if (pValue instanceof DirectAccessible) {
            value = (DirectAccessible) pValue;
        } else {
            LocalJavaField v = pMethod.newJavaField(pController.getRuntimeType());
            v.addLine(pValue);
            value = v;
        }
        LocalJavaField sb = pMethod.newJavaField(StringBuffer.class);
        sb.addLine("new ", StringBuffer.class, "()");
        Object v;
        DirectAccessible loopVar;
        if ("indexed".equals(collectionType)) {
            loopVar = pMethod.addForArray(value);
            v = new Object[] { value, "[", loopVar, "]" };
        } else {
            loopVar = pMethod.addForList(value);
            v = new Object[] { "(", itemType.getSimpleTypeSG().getRuntimeType(), ") ", value, ".get(", loopVar, ")" };
        }
        pMethod.addIf(loopVar, " > 0");
        pMethod.addLine(sb, ".append(' ');");
        pMethod.addEndIf();
        pMethod.addLine(sb, ".append(", itemType.getSimpleTypeSG().getCastToString(pMethod, v, pData), ");");
        pMethod.addEndFor();
        return new TypedValueImpl(new Object[] { sb, ".toString()" }, String.class);
    }

    public void forAllNonNullValues(SimpleTypeSG pController, JavaMethod pMethod, Object pValue, SGlet pSGlet) throws SAXException {
        LocalJavaField f = pMethod.newJavaField(LIST_TYPE);
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

    public Object getEqualsCheck(SimpleTypeSG pController, JavaMethod pMethod, Object pValue1, Object pValue2) throws SAXException {
        throw new IllegalStateException("Not implemented");
    }

    public Object getInitialValue(SimpleTypeSG pController, JavaSource p0) throws SAXException {
        String s = pController.getCollectionType();
        JavaQName listClass = "indexed".equals(s) ? ARRAYLIST_TYPE : JavaQNameImpl.getInstance(s, true);
        return new Object[] { "new ", listClass, "()" };
    }

    public boolean isCausingParseConversionEvent(SimpleTypeSG pController) {
        return itemType.getSimpleTypeSG().isCausingParseConversionEvent();
    }
}
