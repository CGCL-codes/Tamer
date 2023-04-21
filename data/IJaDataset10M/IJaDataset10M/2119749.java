package org.eclipse.emf.codegen.ecore.templates.model.tests;

import org.eclipse.emf.codegen.ecore.genmodel.*;

public class PluginProperties {

    protected static String nl;

    public static synchronized PluginProperties create(String lineSeparator) {
        nl = lineSeparator;
        PluginProperties result = new PluginProperties();
        nl = null;
        return result;
    }

    public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "# ";

    protected final String TEXT_2 = NL + "# <copyright>" + NL + "# </copyright>";

    protected final String TEXT_3 = NL + "#" + NL + "# ";

    protected final String TEXT_4 = "Id";

    protected final String TEXT_5 = NL + NL + "pluginName = ";

    protected final String TEXT_6 = " Tests" + NL + "providerName = www.example.org";

    protected final String TEXT_7 = NL;

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        GenModel genModel = (GenModel) argument;
        {
            GenBase copyrightHolder = argument instanceof GenBase ? (GenBase) argument : argument instanceof Object[] && ((Object[]) argument)[0] instanceof GenBase ? (GenBase) ((Object[]) argument)[0] : null;
            if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
                stringBuffer.append(TEXT_1);
                stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
            } else {
                stringBuffer.append(TEXT_2);
            }
        }
        stringBuffer.append(TEXT_3);
        stringBuffer.append("$");
        stringBuffer.append(TEXT_4);
        stringBuffer.append("$");
        stringBuffer.append(TEXT_5);
        stringBuffer.append(genModel.getModelName());
        stringBuffer.append(TEXT_6);
        stringBuffer.append(TEXT_7);
        return stringBuffer.toString();
    }
}
