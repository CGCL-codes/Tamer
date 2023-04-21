package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
import com.icesoft.faces.context.DOMContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.icesoft.util.pooling.ClientIdPool;

public class RadioRenderer extends SelectManyCheckboxListRenderer {

    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_SELECTONERADIO);

    protected void renderOption(FacesContext facesContext, UIComponent uiComponent, SelectItem selectItem, boolean renderVertically, Element rootTable, Element rootTR, int counter, Object[] submittedValue, Object componentValue) throws IOException {
        UISelectOne uiSelectOne = (UISelectOne) uiComponent;
        DOMContext domContext = DOMContext.getDOMContext(facesContext, uiSelectOne);
        if (renderVertically) {
            rootTR = domContext.createElement("tr");
            rootTable.appendChild(rootTR);
        }
        String labelClass = null;
        boolean disabled = false;
        if (uiSelectOne.getAttributes().get("disabled") != null && uiSelectOne.getAttributes().get("disabled").equals(Boolean.TRUE)) {
            disabled = true;
        }
        if (selectItem.isDisabled()) {
            disabled = true;
        }
        labelClass = (String) uiSelectOne.getAttributes().get("styleClass");
        if (labelClass != null && disabled) {
            labelClass += "-dis";
        }
        Element td = domContext.createElement("td");
        rootTR.appendChild(td);
        Element input = domContext.createElement("input");
        td.appendChild(input);
        input.setAttribute("type", "radio");
        if (disabled) {
            input.setAttribute("disabled", "disabled");
        }
        boolean readonly = false;
        if (uiComponent.getAttributes().get("readonly") != null) {
            if ((uiComponent.getAttributes().get("readonly")).equals(Boolean.TRUE)) {
                readonly = true;
            }
        }
        if (readonly) {
            input.setAttribute("readonly", "readonly");
        }
        HashSet excludes = new HashSet();
        String accesskey = (String) uiComponent.getAttributes().get("accesskey");
        if (accesskey != null) {
            input.setAttribute("accesskey", accesskey);
            excludes.add("accesskey");
        }
        if (isValueSelected(facesContext, selectItem, uiSelectOne, submittedValue, componentValue)) {
            input.setAttribute("checked", "checked");
        } else {
            input.removeAttribute("checked");
        }
        input.setAttribute("name", uiSelectOne.getClientId(facesContext));
        String inputID = ClientIdPool.get(uiComponent.getClientId(facesContext) + ":_" + counter);
        input.setAttribute("id", inputID);
        input.setAttribute("value", (formatComponentValue(facesContext, uiSelectOne, selectItem.getValue())));
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        input.setAttribute("onkeypress", combinedPassThru((String) uiSelectOne.getAttributes().get("onkeypress"), "Ice.util.radioCheckboxEnter(form,this,event);"));
        Element label = domContext.createElement("label");
        td.appendChild(label);
        label.setAttribute("for", inputID);
        if (labelClass != null) {
            label.setAttribute("class", labelClass);
        }
        String itemLabel = selectItem.getLabel();
        if (itemLabel != null) {
            Text labelText = domContext.getDocument().createTextNode(itemLabel);
            label.appendChild(labelText);
        }
    }

    protected void renderOption(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        validateParameters(facesContext, uiComponent, null);
        UIComponent forComponent = findForComponent(facesContext, uiComponent);
        if (!(forComponent instanceof UISelectOne)) {
            throw new IllegalStateException("Could not find UISelectOne component for radio button.");
        }
        String layout = (String) forComponent.getAttributes().get("layout");
        if (layout == null || !layout.equals("spread")) {
            return;
        }
        List selectItemList = getSelectItemList(forComponent);
        if (selectItemList.isEmpty()) {
            throw new IllegalStateException("Could not find select items for UISelectOne component.");
        }
        Object[] submittedValue = getSubmittedSelectedValues(forComponent);
        Object componentValue = (submittedValue != null) ? null : getCurrentSelectedValues(forComponent);
        UISelectOne selectOne = (UISelectOne) forComponent;
        int radioIndex = ((Integer) uiComponent.getAttributes().get("index")).intValue();
        if (radioIndex < 0) radioIndex = 0;
        if (radioIndex >= selectItemList.size()) radioIndex = selectItemList.size() - 1;
        SelectItem selectItem = (SelectItem) selectItemList.get(radioIndex);
        String selectOneClientId = selectOne.getClientId(facesContext);
        String radioClientId = ClientIdPool.get(selectOneClientId + ":_" + radioIndex);
        String selectItemValue = formatComponentValue(facesContext, selectOne, selectItem.getValue());
        String selectItemLabel = selectItem.getLabel();
        DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
        if (domContext.isInitialized()) {
            DOMContext.removeChildren(domContext.getRootNode());
        } else {
            domContext.createRootElement(HTML.SPAN_ELEM);
        }
        Node rootNode = domContext.getRootNode();
        HashSet excludes = new HashSet();
        Object attrObj = selectOne.getAttributes().get("disabled");
        boolean disabled = attrObj != null && Boolean.valueOf(attrObj.toString()).booleanValue();
        if (!disabled) {
            disabled = selectItem.isDisabled();
        }
        Element input = domContext.createElement(HTML.INPUT_ELEM);
        input.setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO);
        input.setAttribute(HTML.ID_ATTR, radioClientId);
        input.setAttribute(HTML.NAME_ATTR, selectOneClientId);
        input.setAttribute(HTML.VALUE_ATTR, selectItemValue);
        if (disabled) {
            input.setAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR);
        }
        if (isValueSelected(facesContext, selectItem, selectOne, submittedValue, componentValue)) {
            input.setAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR);
        }
        addJavaScript(facesContext, selectOne, input, excludes);
        Element label = domContext.createElement(HTML.LABEL_ATTR);
        label.setAttribute(HTML.FOR_ATTR, radioClientId);
        attrObj = selectOne.getAttributes().get("styleClass");
        String labelClass = attrObj == null ? "" : attrObj.toString().trim();
        if (labelClass.length() > 0) {
            if (disabled) {
                String[] styleClasses = labelClass.split("\\s");
                labelClass = "";
                for (int i = 0; i < styleClasses.length; i++) {
                    if (i > 0) {
                        labelClass += " ";
                    }
                    labelClass += styleClasses[i];
                    if (!labelClass.endsWith("-dis")) {
                        labelClass += "-dis";
                    }
                }
            }
            label.setAttribute("class", labelClass);
        }
        if (selectItemLabel != null) label.appendChild(domContext.createTextNode(selectItemLabel));
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, selectOne, input, label, passThruAttributes);
        PassThruAttributeRenderer.renderBooleanAttributes(facesContext, selectOne, input, new String[] { "disabled" });
        input.setAttribute("onkeypress", combinedPassThru((String) selectOne.getAttributes().get("onkeypress"), "Ice.util.radioCheckboxEnter(form,this,event);"));
        rootNode.appendChild(input);
        rootNode.appendChild(label);
        domContext.stepOver();
    }

    protected void addJavaScript(FacesContext facesContext, UIComponent uiSelectOne, Element input, Set excludes) {
    }
}
