package de.mogwai.kias.web.serializer.components;

import de.mogwai.kias.resource.FormResourceException;
import de.mogwai.kias.web.serializer.WebNodeHandlerContext;

public class CheckboxNodeHandler extends ActionNodeHandler {

    public String serializeSkeleton(WebNodeHandlerContext aContext) throws FormResourceException {
        String theText = getText(aContext.getElement(), aContext);
        String thePropertyName = getPropertyName(aContext.getElement());
        String cssClass = "checkbox";
        String theAppend = "";
        theAppend += getTabIndexDefinition(aContext);
        if (TRUE.equals(getSendRefresh(aContext))) {
            theAppend += "onclick=\"" + getSubmitCommandAsEventHandler(aContext, "onRefresh") + "\"";
        }
        String theElementName = aContext.getFormContext().getForm().getId() + "." + thePropertyName;
        return "<input id=\"" + theElementName + "\" type=\"checkbox\" " + theAppend + " name=\"" + theElementName + "\" class=\"" + cssClass + " font\" value=\"true\">" + escapeHTML(theText);
    }

    @Override
    public void serializeCommands(WebNodeHandlerContext aContext) throws FormResourceException {
        String thePropertyName = getPropertyName(aContext.getElement());
        String theElementName = aContext.getFormContext().getForm().getId() + "." + thePropertyName;
        Object theValue = getProperty(aContext.getBean(), thePropertyName);
        aContext.getAjaxCommandList().add(commandFactory.createSetFormFieldSelectedCommand(aContext.getFormContext().getForm().getId(), theElementName, Boolean.TRUE.equals(theValue)));
        aContext.getAjaxCommandList().add(commandFactory.createSetElementVisibilityCommand(theElementName, isVisible(aContext)));
        aContext.getAjaxCommandList().add(commandFactory.createSetFormFieldEnabledCommand(aContext.getFormContext().getForm().getId(), theElementName, isEnabled(aContext)));
    }
}
