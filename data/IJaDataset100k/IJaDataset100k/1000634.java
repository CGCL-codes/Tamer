package cruise.ui.jsf.templates.impl.fragment.GUI;

import cruise.model.abstractObjects.IGenerator;
import cruise.umple.compiler.Attribute;

;

public class ImmutableEditInteger implements IGenerator {

    protected static String nl;

    public static synchronized ImmutableEditInteger create(String lineSeparator) {
        nl = lineSeparator;
        ImmutableEditInteger result = new ImmutableEditInteger();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "<h:outputText value=\"#{#1#Bundle.";

    protected final String TEXT_2 = "}\" />" + NL + "<h:outputText value=\"#{#1#Bean.edited#2#.";

    protected final String TEXT_3 = "}\" >" + NL + "<f:converter converterId=\"javax.faces.Integer\" />" + NL + "</h:outputText>";

    protected final String TEXT_4 = NL;

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        Attribute attVar = (Attribute) argument;
        stringBuffer.append(TEXT_1);
        stringBuffer.append(attVar.getUpperCaseName());
        stringBuffer.append(TEXT_2);
        stringBuffer.append(attVar.getName());
        stringBuffer.append(TEXT_3);
        stringBuffer.append(TEXT_4);
        return stringBuffer.toString();
    }
}
