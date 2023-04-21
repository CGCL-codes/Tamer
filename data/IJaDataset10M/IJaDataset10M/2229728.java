package org.gems.designer.metamodel.gen;

import org.gems.designer.model.*;
import org.gems.designer.metamodel.gen.*;
import java.util.*;

public class CreatePropertyAccessorsTemplate {

    protected static String nl;

    public static synchronized CreatePropertyAccessorsTemplate create(String lineSeparator) {
        nl = lineSeparator;
        CreatePropertyAccessorsTemplate result = new CreatePropertyAccessorsTemplate();
        nl = null;
        return result;
    }

    protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "\t";

    protected final String TEXT_2 = NL + "    \tprivate ";

    protected final String TEXT_3 = " ";

    protected final String TEXT_4 = "_ = ";

    protected final String TEXT_5 = ";";

    protected final String TEXT_6 = NL + "    ";

    protected final String TEXT_7 = NL + "   public void set";

    protected final String TEXT_8 = "(int val){" + NL + "      if(val < ";

    protected final String TEXT_9 = "_VALUES.length &&" + NL + "         val >= 0){" + NL + "         set";

    protected final String TEXT_10 = "(";

    protected final String TEXT_11 = "_VALUES[val]);" + NL + "      }" + NL + "   }" + NL + "         \t";

    protected final String TEXT_12 = NL + "   public ";

    protected final String TEXT_13 = " get";

    protected final String TEXT_14 = "(){" + NL + "   \treturn ";

    protected final String TEXT_15 = "_;" + NL + "   }" + NL + "   public void set";

    protected final String TEXT_16 = "(";

    protected final String TEXT_17 = " val){" + NL + "    Object old = ";

    protected final String TEXT_18 = "_;" + NL + "   \t";

    protected final String TEXT_19 = "_ = val;" + NL + "   \tfirePropertyChange(ATTRIBUTE_PREFIX+\"";

    protected final String TEXT_20 = "\", old, val);" + NL + "   \t" + NL + "   \tObject[] valuepair = {\"";

    protected final String TEXT_21 = "\",old};        " + NL + "    org.gems.designer.model.event.ModelChangeEvent event = new org.gems.designer.model.event.ModelChangeEvent(this," + NL + "                org.gems.designer.model.event.ModelChangeEvent.ELEMENT_ATTRIBUTE_CHANGED,false,(Object)valuepair);" + NL + "    org.gems.designer.model.event.ModelEventDispatcher.dispatch(event);" + NL + "   }";

    protected final String TEXT_22 = NL + "    public Object getAttribute(String attr){";

    protected final String TEXT_23 = NL + "         \tif(attr != null &&" + NL + "\t           attr.equals(\"";

    protected final String TEXT_24 = "\")){" + NL + "\t           return new Integer(org.gems.designer.model.Enumeration.getIndex(" + NL + "\t             ";

    protected final String TEXT_25 = "_VALUES," + NL + "\t             get";

    protected final String TEXT_26 = "()" + NL + "\t             ));" + NL + "\t  }" + NL + "         \t";

    protected final String TEXT_27 = NL + "\t if(attr != null &&" + NL + "\t    attr.equals(\"";

    protected final String TEXT_28 = "\")){" + NL + "\t    return get";

    protected final String TEXT_29 = "().toString();" + NL + "\t  }";

    protected final String TEXT_30 = NL + "     return super.getAttribute(attr);" + NL + "   }" + NL + "   " + NL + "    public void setAttribute(String attr, Object val){" + NL + "         if(attr != null){" + NL + "           AttributeValidator validator = AttributeValidators.getInstance().getValidator(attr);" + NL + "           if(validator != null &&" + NL + "              !validator.validValue(this,attr,val)){" + NL + "                return;" + NL + "              }" + NL + "         }\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    protected final String TEXT_31 = NL + "\t ";

    protected final String TEXT_32 = "else ";

    protected final String TEXT_33 = "if(attr != null &&" + NL + "\t    attr.equals(\"";

    protected final String TEXT_34 = "\")){" + NL + "\t    set";

    protected final String TEXT_35 = "(";

    protected final String TEXT_36 = ");" + NL + "\t  }";

    protected final String TEXT_37 = NL + "      else {";

    protected final String TEXT_38 = NL + "        super.setAttribute(attr,val);";

    protected final String TEXT_39 = " }";

    protected final String TEXT_40 = NL + "   }";

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        AttributedObject model = (AttributedObject) argument;
        PropertyTemplate propgen = new PropertyTemplate();
        stringBuffer.append(TEXT_1);
        List attributes = model.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInfo curr = (AttributeInfo) attributes.get(i);
            String type = curr.getType();
            if (curr.getType().equals(AttributeInfo.ENUMERATION_TYPE)) {
                type = "String";
            }
            stringBuffer.append(TEXT_2);
            stringBuffer.append(type);
            stringBuffer.append(TEXT_3);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_4);
            stringBuffer.append(GeneratorUtilities.asObject(curr.getType(), curr.getDefaultValue()));
            stringBuffer.append(TEXT_5);
        }
        stringBuffer.append(TEXT_6);
        attributes = model.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInfo curr = (AttributeInfo) attributes.get(i);
            String type = curr.getType();
            if (curr.getType().equals(AttributeInfo.ENUMERATION_TYPE)) {
                type = "String";
                stringBuffer.append(TEXT_7);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
                stringBuffer.append(TEXT_8);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()).toUpperCase());
                stringBuffer.append(TEXT_9);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
                stringBuffer.append(TEXT_10);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()).toUpperCase());
                stringBuffer.append(TEXT_11);
            }
            stringBuffer.append(TEXT_12);
            stringBuffer.append(type);
            stringBuffer.append(TEXT_13);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_14);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_15);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_16);
            stringBuffer.append(type);
            stringBuffer.append(TEXT_17);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_18);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_19);
            stringBuffer.append(curr.getName());
            stringBuffer.append(TEXT_20);
            stringBuffer.append(curr.getName());
            stringBuffer.append(TEXT_21);
        }
        stringBuffer.append(TEXT_22);
        attributes = model.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInfo curr = (AttributeInfo) attributes.get(i);
            String type = curr.getType();
            if (curr.getType().equals(AttributeInfo.ENUMERATION_TYPE)) {
                type = "String";
                stringBuffer.append(TEXT_23);
                stringBuffer.append(curr.getName());
                stringBuffer.append(TEXT_24);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()).toUpperCase());
                stringBuffer.append(TEXT_25);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
                stringBuffer.append(TEXT_26);
            } else {
                stringBuffer.append(TEXT_27);
                stringBuffer.append(curr.getName());
                stringBuffer.append(TEXT_28);
                stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
                stringBuffer.append(TEXT_29);
            }
        }
        stringBuffer.append(TEXT_30);
        attributes = model.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInfo curr = (AttributeInfo) attributes.get(i);
            String type = curr.getType();
            String settext = "(" + type + ")" + "val";
            if (curr.getType().equals(AttributeInfo.ENUMERATION_TYPE)) {
                settext = "((Integer)val).intValue()";
            } else if (curr.getType().equals(AttributeInfo.BOOLEAN_TYPE)) {
                settext = "new Boolean((String)val)";
            } else if (curr.getType().equals(AttributeInfo.INT_TYPE)) {
                settext = "new Integer((String)val)";
            } else if (curr.getType().equals(AttributeInfo.DECIMAL_TYPE)) {
                settext = "new Double((String)val)";
            }
            stringBuffer.append(TEXT_31);
            if (i != 0) {
                stringBuffer.append(TEXT_32);
            }
            stringBuffer.append(TEXT_33);
            stringBuffer.append(curr.getName());
            stringBuffer.append(TEXT_34);
            stringBuffer.append(GeneratorUtilities.getIdentifier(curr.getName()));
            stringBuffer.append(TEXT_35);
            stringBuffer.append(settext);
            stringBuffer.append(TEXT_36);
        }
        if (attributes.size() > 0) {
            stringBuffer.append(TEXT_37);
        }
        stringBuffer.append(TEXT_38);
        if (attributes.size() > 0) {
            stringBuffer.append(TEXT_39);
        }
        stringBuffer.append(TEXT_40);
        return stringBuffer.toString();
    }
}
