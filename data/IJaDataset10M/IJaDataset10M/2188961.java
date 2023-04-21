package netgest.bo.xwc.xeo.components;

import static netgest.bo.xwc.components.HTMLAttr.CELLPADDING;
import static netgest.bo.xwc.components.HTMLAttr.CELLSPACING;
import static netgest.bo.xwc.components.HTMLTag.COL;
import static netgest.bo.xwc.components.HTMLTag.TABLE;
import static netgest.bo.xwc.components.HTMLTag.TD;
import static netgest.bo.xwc.components.HTMLTag.TR;
import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import netgest.bo.def.boDefAttribute;
import netgest.bo.runtime.boObject;
import netgest.bo.runtime.boRuntimeException;
import netgest.bo.xwc.components.HTMLAttr;
import netgest.bo.xwc.components.HTMLTag;
import netgest.bo.xwc.components.annotations.Required;
import netgest.bo.xwc.components.annotations.Values;
import netgest.bo.xwc.components.classic.Attribute;
import netgest.bo.xwc.components.classic.AttributeBase;
import netgest.bo.xwc.components.classic.AttributeLabel;
import netgest.bo.xwc.components.classic.AttributeNumberLookup;
import netgest.bo.xwc.components.classic.Rows;
import netgest.bo.xwc.framework.XUIBaseProperty;
import netgest.bo.xwc.framework.XUIBindProperty;
import netgest.bo.xwc.framework.XUIMessage;
import netgest.bo.xwc.framework.XUIRenderer;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.XUIResponseWriter;
import netgest.bo.xwc.framework.XUIViewProperty;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import netgest.bo.xwc.xeo.beans.XEOEditBean;
import netgest.bo.xwc.xeo.localization.XEOComponentMessages;

/**
 * 
 * An input field that allows to Lookup a given XEO Object as well as finding
 * a XEO Object through one of its attributes (that XEO Object is defined as
 * an AttributeObject or AttributeObjectCollection off the XEO Object
 * in the targetObject property.
 * 
 * 
 * 
 * <xeo:splitedLookup 
		targetLookupAttribute="username"  <-- Make the search by this attribute
		objectAttribute="security"  <-- Store the boObject found in this attribute of the current object (required)
		></xeo:splitedLookup>
 * 
 * @author jcarreira
 *
 */
public class SplitedLookup extends Attribute {

    /**
	 * The target XEO Object (boObject) where to keep the lookup object (in an attribute
	 * declared in the <code>objectAttribute</code> property
	 * 
	 */
    private XUIBindProperty<boObject> targetObject = new XUIBindProperty<boObject>("targetObject", this, boObject.class, "#{viewBean.XEOObject}");

    /**
	 * Situation:
	 *  1) The {@link boObject} returned by {@link SplitedLookup#getTargetObject()} is of XEO Model 'A'.
	 *  2) <code>XEO Model A</code> has an attributeObject/attributeObjectCollection (named 'P') to <code>XEO Model B</code>.
	 *  3) <code>XEO Model B</code> has an attribute 'X' of any type (usually Text/Number)
	 *  
	 *  The value of this property is 'X', so that one can search instances of <code>XEO Model B</code>
	 *  by the given X attribute and assign that object to the 'P' attributeObject
	 * 
	 */
    @Required
    private XUIBaseProperty<String> targetLookupAttribute = new XUIBaseProperty<String>("targetLookupAttribute", this);

    /**
	 * The width of the lookup box
	 */
    private XUIViewProperty<String> lookupWidth = new XUIViewProperty<String>("lookupWidth", this, "60%");

    /**
	 * The width of the box to write the search value
	 */
    private XUIViewProperty<String> keyInputWidth = new XUIViewProperty<String>("keyInputWidth", this, "30%");

    /**
	 * The input type for the search field
	 */
    @Values({ "attributeText", "attributeNumber" })
    private XUIViewProperty<String> keyInputType = new XUIViewProperty<String>("keyInputType", this, "attributeText");

    public String getLookupWidth() {
        return this.lookupWidth.getValue();
    }

    public void setLookupWidth(String lookupWidth) {
        this.lookupWidth.setValue(lookupWidth);
    }

    @Override
    public String getRendererType() {
        return super.getRendererType();
    }

    public String getKeyInputWidth() {
        return this.keyInputWidth.getValue();
    }

    public void setKeyInputWidth(String fieldWidth) {
        this.keyInputWidth.setValue(fieldWidth);
    }

    public void setKeyInputType(String keyInputType) {
        this.keyInputType.setValue(keyInputType);
    }

    public String getKeyInputType() {
        return this.keyInputType.getValue();
    }

    public void setTargetObject(String beanExpr) {
        this.targetObject.setExpressionText(beanExpr);
    }

    public boObject getTargetObject() {
        return this.targetObject.getEvaluatedValue();
    }

    public void setTargetLookupAttribute(String attribute) {
        this.targetLookupAttribute.setValue(attribute);
    }

    public String getTargetLookupAttribute() {
        return this.targetLookupAttribute.getValue();
    }

    public AttributeLabel getLabelComponent() {
        return (AttributeLabel) findComponent(getId() + "_lbl");
    }

    public AttributeBase getInputComponent() {
        return (AttributeBase) findComponent(getId() + "_fld");
    }

    public AttributeBase getLookupComponent() {
        return (AttributeBase) findComponent(getId() + "_lk");
    }

    @Override
    public boolean wasStateChanged() {
        return super.wasStateChanged();
    }

    @Override
    public void initComponent() {
        boObject targetObject = getTargetObject();
        String attName = getObjectAttribute();
        boDefAttribute locAtt = targetObject.getAttribute(attName).getDefAttribute();
        boDefAttribute remAtt = locAtt.getReferencedObjectDef().getAttributeRef(getTargetLookupAttribute());
        if ("1".equals(getRenderLabel())) {
            AttributeLabel label = new AttributeLabel();
            label.setId(getId() + "_lbl");
            if (getStateProperty("label").isDefaultValue()) label.setText(locAtt.getLabel()); else label.setText(getLabel());
            propagateLabelProperties(label);
            getChildren().add(label);
        }
        String inputTypeName = getKeyInputType();
        AttributeBase input = (AttributeBase) getRequestContext().getApplicationContext().getViewerBuilder().createComponent(getRequestContext(), inputTypeName);
        if (getValueExpression("disabled") != null) input.setDisabled(getValueExpression("disabled").getExpressionString());
        if (getValueExpression("readOnly") != null) input.setReadOnly(getValueExpression("readOnly").getExpressionString());
        if (getValueExpression("visible") != null) input.setVisible(getValueExpression("visible").getExpressionString());
        input.setId(getId() + "_fld");
        if (getStateProperty("maxLength").isDefaultValue()) input.setMaxLength(getMaxLength()); else input.setMaxLength(remAtt.getLen());
        input.setOnChangeSubmit("true");
        input.addValueChangeListener(new ChangeListener());
        getChildren().add(input);
        AttributeNumberLookup numberLookup = new AttributeNumberLookup();
        numberLookup.setId(getId() + "_lk");
        propagateInputProperties(numberLookup);
        numberLookup.setOnChangeSubmit("true");
        numberLookup.addValueChangeListener(new AttributeChangeListener());
        getChildren().add(numberLookup);
    }

    @Override
    public void validate(FacesContext context) {
        super.validate(context);
    }

    @Override
    public void validateModel() {
        super.validateModel();
        if (isModelValid()) {
            String s = getInputComponent().getInvalidText();
            if (s != null && s.length() > 0) {
                XUIRequestContext c = XUIRequestContext.getCurrentContext();
                c.addMessage(getInputComponent().getClientId() + "_msg", new XUIMessage(XUIMessage.TYPE_ALERT, XUIMessage.SEVERITY_ERROR, "", getLabel() + " - " + s));
                setModelValid(false);
            }
        }
    }

    @Override
    public void preRender() {
        super.preRender();
        String targetAtt = getTargetLookupAttribute();
        try {
            AttributeBase att = getInputComponent();
            boObject obj = getTargetObject();
            boObject childObj = obj.getAttribute(getObjectAttribute()).getObject();
            if (childObj != null) {
                Object value = childObj.getAttribute(targetAtt).getValueObject();
                att.setValue(String.valueOf(value));
                att.setDisplayValue(String.valueOf(value));
                att.clearInvalid();
            } else {
                if (att.getInvalidText() == null) {
                    att.setValue(null);
                    att.setDisplayValue(null);
                } else {
                    if (att.getValue() != null) att.setDisplayValue(String.valueOf(att.getValue()));
                }
            }
        } catch (boRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static class XEOHTMLRenderer extends XUIRenderer {

        @Override
        public void encodeEnd(XUIComponentBase oComp) throws IOException {
            SplitedLookup oAttr = (SplitedLookup) oComp;
            if (!oAttr.isRenderedOnClient()) {
                XUIResponseWriter w = getResponseWriter();
                String labelPos = "left";
                int labelWidth = 100;
                Rows r = (Rows) oAttr.findParentComponent(Rows.class);
                if (r != null) {
                    labelPos = r.getLabelPosition();
                    labelWidth = r.getLabelWidth();
                }
                w.startElement(TABLE, oComp);
                w.writeAttribute(CELLPADDING, "0", null);
                w.writeAttribute(CELLSPACING, "0", null);
                w.writeAttribute(HTMLAttr.ID, oComp.getClientId(), null);
                w.writeAttribute(HTMLAttr.STYLE, "table-layout:fixed;width:100%", null);
                if (!"Top".equalsIgnoreCase(labelPos)) {
                    w.startElement("COLGROUP", oComp);
                    if ("1".equals(oAttr.getRenderLabel())) {
                        w.startElement(COL, oComp);
                        w.writeAttribute(HTMLAttr.WIDTH, labelWidth + "px", null);
                        w.endElement("COL");
                    }
                    w.endElement("COLGROUP");
                } else {
                    w.startElement("COLGROUP", oComp);
                    w.startElement(COL, oComp);
                    w.writeAttribute(HTMLAttr.WIDTH, "100%", null);
                    w.endElement("COL");
                    w.endElement("COLGROUP");
                }
                w.startElement(TR, oComp);
                if ("1".equals(oAttr.getRenderLabel())) {
                    w.startElement(TD, oComp);
                    if (oAttr.getLabelComponent() != null) {
                        oAttr.getLabelComponent().encodeAll();
                    }
                    w.endElement(TD);
                }
                if ("Top".equalsIgnoreCase(labelPos)) {
                    w.endElement(TR);
                    w.startElement(TR, oComp);
                }
                w.startElement(TD, oComp);
                w.startElement(TABLE, oComp);
                w.writeAttribute(CELLPADDING, "0", null);
                w.writeAttribute(CELLSPACING, "1", null);
                w.writeAttribute(HTMLAttr.STYLE, "table-layout:fixed;width:100%", null);
                w.startElement("COLGROUP", oComp);
                w.startElement(COL, oComp);
                w.writeAttribute(HTMLAttr.WIDTH, oAttr.getKeyInputWidth(), null);
                w.endElement("COL");
                w.startElement(COL, oComp);
                w.writeAttribute(HTMLAttr.WIDTH, oAttr.getLookupWidth(), null);
                w.endElement("COL");
                w.endElement(HTMLTag.COLGROUP);
                w.startElement(TR, oComp);
                w.startElement(TD, oComp);
                XUIComponentBase inpComp = oAttr.getInputComponent();
                if (inpComp != null) {
                    inpComp.encodeAll();
                }
                w.endElement(TD);
                w.startElement(TD, oComp);
                XUIComponentBase lookupComp = oAttr.getLookupComponent();
                if (lookupComp != null) {
                    lookupComp.encodeAll();
                }
                w.endElement(TD);
                w.endElement(TR);
                w.endElement(TABLE);
                w.endElement(TD);
                w.endElement(TR);
                w.endElement(TABLE);
            } else {
                oAttr.setDestroyOnClient(false);
            }
        }

        @Override
        public boolean getRendersChildren() {
            return true;
        }

        @Override
        public void encodeChildren(XUIComponentBase component) throws IOException {
        }

        @Override
        public void decode(XUIComponentBase component) {
            SplitedLookup s = (SplitedLookup) component;
            XUIRequestContext r = XUIRequestContext.getCurrentContext();
            if (r.getRequestParameterMap().containsKey(component.getClientId() + "_fld")) {
                s.getInputComponent().setDisplayValue(r.getRequestParameterMap().get(component.getClientId() + "_fld"));
            }
        }
    }

    public static class ChangeListener implements ValueChangeListener {

        @Override
        public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
            try {
                SplitedLookup lookup = (SplitedLookup) ((AttributeBase) arg0.getComponent()).findParentComponent(SplitedLookup.class);
                Object inputValue = lookup.getInputComponent().getValue();
                boObject objAtt = lookup.getTargetObject();
                XEOEditBean b = (XEOEditBean) XUIRequestContext.getCurrentContext().getViewRoot().getBean("viewBean");
                Object value = b.validateLookupValue(objAtt.getAttribute(lookup.getObjectAttribute()), new String[] { lookup.getTargetLookupAttribute() }, new Object[] { inputValue });
                if (value == null && (inputValue instanceof String && ((String) inputValue).length() > 0)) {
                    lookup.getInputComponent().setInvalidText(XEOComponentMessages.SPLTDLOOKUP_INVALID_VALUE.toString());
                    lookup.getLookupComponent().setValue(null);
                } else {
                    lookup.getInputComponent().clearInvalid();
                    AttributeBase lk = lookup.getLookupComponent();
                    lk.setValue(value);
                    lk.updateModel();
                }
            } catch (boRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AttributeChangeListener implements ValueChangeListener {

        @Override
        public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
            SplitedLookup lookup = (SplitedLookup) ((AttributeBase) arg0.getComponent()).findParentComponent(SplitedLookup.class);
            String targetAtt = lookup.getTargetLookupAttribute();
            try {
                AttributeBase att = lookup.getInputComponent();
                boObject obj = lookup.getTargetObject();
                boObject childObj = obj.getAttribute(lookup.getObjectAttribute()).getObject();
                if (childObj != null) {
                    Object value = childObj.getAttribute(targetAtt).getValueObject();
                    att.setDisplayValue(String.valueOf(value));
                    att.clearInvalid();
                } else {
                    if (att.getInvalidText() == null) {
                        att.setDisplayValue(null);
                    }
                }
            } catch (boRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
