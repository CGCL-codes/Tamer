package org.yaoqiang.bpmn.editor.dialog;

import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.core.common.Expression;
import org.yaoqiang.bpmn.model.elements.core.common.ItemDefinition;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;
import org.yaoqiang.bpmn.model.elements.data.DataObjectReference;
import org.yaoqiang.bpmn.model.elements.data.DataStoreReference;
import com.mxgraph.util.mxResources;

/**
 * XMLElementView
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class XMLElementView {

    public static final int TONAME = 1;

    public static final int TOVALUE = 2;

    protected XMLElement element;

    protected String elementString = "";

    protected int type = 1;

    public XMLElementView(XMLElement el, int type) {
        this.element = el;
        this.type = type;
    }

    public XMLElementView(String el) {
        this.elementString = el;
    }

    public XMLElement getElement() {
        return element;
    }

    public void setElement(XMLElement el) {
        this.element = el;
    }

    public String getElementString() {
        return this.elementString;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof XMLElementView)) return false;
        if (getElement() != null) {
            XMLElement el = getElement();
            XMLElement el2 = (((XMLElementView) obj).getElement());
            if (el2 == null) {
                return false;
            } else if (el == el2) {
                return true;
            } else if (el instanceof BaseElement && el.getClass().equals(el2.getClass()) && el.toName().equals(el2.toName())) {
                return (((BaseElement) el).getId().equals(((BaseElement) el2).getId()));
            }
        } else if (getElementString() != null) {
            return getElementString().equals(((XMLElementView) obj).getElementString());
        }
        return false;
    }

    public String toString() {
        if (element != null) {
            if (type == TONAME) {
                if (element instanceof BaseElement) {
                    XMLElement name = ((BaseElement) element).get("name");
                    String id = ((BaseElement) element).getId();
                    String value = "";
                    if (name != null && name.toValue().length() != 0) {
                        if (id.length() != 0) {
                            value = id + " (" + name.toValue() + ")";
                        } else {
                            value = name.toValue();
                        }
                    } else if (id.length() != 0) {
                        if (element instanceof ItemDefinition) {
                            value = id + " (" + ((ItemDefinition) element).getStructureRef() + ")";
                        } else {
                            value = id;
                        }
                    } else if (element instanceof Expression) {
                        value = mxResources.get(element.toName(), element.toName());
                    }
                    if (element instanceof DataObjectReference || element instanceof DataStoreReference) {
                        value += " Reference";
                    }
                    return value;
                }
                return element.toName();
            }
            return element.toValue();
        } else {
            return elementString;
        }
    }
}
