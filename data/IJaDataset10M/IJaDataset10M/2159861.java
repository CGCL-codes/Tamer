package org.gems.designer.metamodel.gen;

import org.gems.designer.model.*;
import org.gems.designer.metamodel.gen.*;
import java.util.List;

public class PluginXMLTemplate {

    protected static String nl;

    public static synchronized PluginXMLTemplate create(String lineSeparator) {
        nl = lineSeparator;
        PluginXMLTemplate result = new PluginXMLTemplate();
        nl = null;
        return result;
    }

    protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected final String TEXT_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL + "<?eclipse version=\"3.0\"?>" + NL + "<plugin" + NL + "   id=\"";

    protected final String TEXT_2 = "\"" + NL + "   name=\"";

    protected final String TEXT_3 = " Plug-in\"" + NL + "   version=\"1.0.0\"" + NL + "   provider-name=\"GEMS\"" + NL + "   class=\"";

    protected final String TEXT_4 = ".";

    protected final String TEXT_5 = "Plugin\">" + NL + "" + NL + "   <runtime>" + NL + "      <library name=\"";

    protected final String TEXT_6 = ".jar\">" + NL + "         <export name=\"*\"/>" + NL + "      </library>" + NL + "   </runtime>" + NL + "" + NL + "   <requires>" + NL + "      <import plugin=\"org.gems.designer.dsml\" export=\"false\"/>" + NL + "      <import plugin=\"org.eclipse.ui\"/>" + NL + "      <import plugin=\"org.eclipse.core.runtime\"/>" + NL + "      <import plugin=\"org.eclipse.jface.text\"/>" + NL + "      <import plugin=\"org.eclipse.core.resources\"/>" + NL + "      <import plugin=\"org.eclipse.ui.editors\"/>" + NL + "      <import plugin=\"org.eclipse.ui.ide\"/>" + NL + "      <import plugin=\"org.eclipse.ui.workbench.texteditor\"/>" + NL + "      <import plugin=\"org.eclipse.draw2d\"/>" + NL + "      <import plugin=\"org.eclipse.gef\"/>" + NL + "      <import plugin=\"org.eclipse.ui.views\"/>" + NL + "      <import plugin=\"org.eclipse.emf\"/>" + NL + "      <import plugin=\"org.eclipse.emf.ecore\"/>" + NL + "      <import plugin=\"org.eclipse.emf.ecore.xmi\"/>" + NL + "   </requires>" + NL + " " + NL + "   <extension" + NL + "         point=\"org.eclipse.ui.editors\">" + NL + "      <editor" + NL + "            default=\"true\"" + NL + "            name=\"Generic Eclipse Modeling System\"" + NL + "            extensions=\"";

    protected final String TEXT_7 = "\"" + NL + "            icon=\"";

    protected final String TEXT_8 = ".gif\"" + NL + "            class=\"";

    protected final String TEXT_9 = ".DSMLEditor\"" + NL + "            contributorClass=\"org.gems.designer.actions.LogicActionBarContributor\"" + NL + "            id=\"";

    protected final String TEXT_10 = ".editor\">" + NL + "      </editor>" + NL + "   </extension>" + NL + "  " + NL + "   <extension" + NL + "         point=\"org.eclipse.ui.newWizards\">" + NL + "      <category" + NL + "            name=\"";

    protected final String TEXT_11 = "\"" + NL + "            id=\"";

    protected final String TEXT_12 = "\">" + NL + "      </category>" + NL + "      <wizard" + NL + "            name=\"";

    protected final String TEXT_13 = " Model\"" + NL + "            icon=\"";

    protected final String TEXT_14 = ".gif\"" + NL + "            category=\"";

    protected final String TEXT_15 = "\"" + NL + "            class=\"";

    protected final String TEXT_16 = ".GemsCreationWizard\"" + NL + "            id=\"";

    protected final String TEXT_17 = ".NewWizard\">" + NL + "      </wizard>" + NL + "   </extension>";

    protected final String TEXT_18 = NL + "    <extension" + NL + "         point=\"org.gems.designer.dsml.userinvokedaction\">" + NL + "      <user-action" + NL + "            class=\"";

    protected final String TEXT_19 = ".VisitorActionSet\"" + NL + "            eventID=\"";

    protected final String TEXT_20 = ".VisitorActionSet\"" + NL + "            id=\"";

    protected final String TEXT_21 = ".VisitorActionSet\"" + NL + "            models=\"";

    protected final String TEXT_22 = "\"" + NL + "            name=\"";

    protected final String TEXT_23 = "\"/>" + NL + "   </extension>";

    protected final String TEXT_24 = NL + NL + "</plugin>";

    protected final String TEXT_25 = NL;

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        ModelProviderInfo model = (ModelProviderInfo) argument;
        String vtype = (String) GenerationContext.getInstance().getProperty(GenerationContext.VISITOR_TYPE_KEY);
        String mname = (String) GenerationContext.getInstance().getProperty(GenerationContext.VISITOR_MENU_KEY);
        stringBuffer.append(TEXT_1);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_2);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_3);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_4);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_5);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_6);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_EXTENSIONS)));
        stringBuffer.append(TEXT_7);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_8);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_9);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_10);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_CATEGORY)));
        stringBuffer.append(TEXT_11);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_12);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_13);
        stringBuffer.append(model.getName());
        stringBuffer.append(TEXT_14);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_15);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_16);
        stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
        stringBuffer.append(TEXT_17);
        if (vtype != null) {
            stringBuffer.append(TEXT_18);
            stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
            stringBuffer.append(TEXT_19);
            stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
            stringBuffer.append(TEXT_20);
            stringBuffer.append((GenerationContext.getInstance().getProperty(PluginInfo.TARGET_PLUGIN_PACKAGE)));
            stringBuffer.append(TEXT_21);
            stringBuffer.append(model.getModelID());
            stringBuffer.append(TEXT_22);
            stringBuffer.append(mname);
            stringBuffer.append(TEXT_23);
        }
        stringBuffer.append(TEXT_24);
        stringBuffer.append(TEXT_25);
        return stringBuffer.toString();
    }
}
