package org.akrogen.tkui.core.internal.dom.attributes;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import org.akrogen.tkui.core.ITkuiConfiguration;
import org.akrogen.tkui.core.converters.ITkuiConverter;
import org.akrogen.tkui.core.dom.ITkuiDocument;
import org.akrogen.tkui.core.dom.ITkuiWidgetElement;
import org.akrogen.tkui.core.internal.dom.events.TkuiMutationEventImpl;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

public class TkuiAttrImpl extends AbstractTkuiAttrImpl {

    protected Object eval;

    private static Log logger = LogFactory.getLog(TkuiAttrImpl.class);

    public TkuiAttrImpl(ITkuiDocument ownerDocument, String namespaceURI, String qualifiedName, String localName) {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public Object getEval() {
        return eval;
    }

    private Object internalSetValue(String value) {
        Object oldValue = eval;
        eval = (converter == null ? value : converter.parse(value));
        super.setValue(value);
        return oldValue;
    }

    public void setValue(String value) {
        Object oldValue = internalSetValue(value);
        if (fireEvent == false) return;
        fireEvent(oldValue);
    }

    public void setConverter(ITkuiConverter converter) {
        super.setConverter(converter);
        eval = null;
        String value = getValue();
        setValue(value);
    }

    public void synchroniseTkuiElementWithAttrValue() {
        Element element = this.getOwnerElement();
        if (element instanceof ITkuiWidgetElement) {
            String attrStringValue = this.getNodeValue();
            String propertyName = this.name;
            ITkuiWidgetElement tkuiElement = (ITkuiWidgetElement) element;
            try {
                PropertyDescriptor p = PropertyUtils.getPropertyDescriptor(tkuiElement, propertyName);
                if (p != null) {
                    Class c = p.getPropertyType();
                    ITkuiConverter converter = null;
                    ITkuiDocument tkuiDocument = tkuiElement.getTkuiDocument();
                    ITkuiConfiguration configuration = tkuiDocument.getConfiguration();
                    if (configuration == null) return;
                    converter = configuration.getConverter(c);
                    if (converter == null) return;
                    this.setConverter(converter);
                }
            } catch (IllegalAccessException e) {
                String msg = "Unable to ACCESS property setter for property " + propertyName + " in Element " + tkuiElement;
                logger.error(msg, e);
            } catch (InvocationTargetException e) {
                String msg = "Unable to INVOKE property setter for property " + propertyName + " in Element " + tkuiElement;
                logger.error(msg, e);
            } catch (NoSuchMethodException e) {
                String msg = "Unable to FIND property setter for property " + propertyName + " in Element " + tkuiElement;
                logger.error(msg, e);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void fireEvent() {
        fireEvent(null);
    }

    public void fireEvent(Object oldValue) {
        if (oldValue == null || !oldValue.equals(eval)) {
            TkuiMutationEventImpl me = new TkuiMutationEventImpl();
            me.initMutationEvent2(TkuiMutationEventImpl.DOM_TKUI_ATTR_MODIFIED, false, false, this, oldValue, eval, this.getNodeName(), MutationEvent.MODIFICATION);
            NodeImpl owner = (NodeImpl) this.getOwnerElement();
            if (owner != null) owner.dispatchEvent(me);
        }
    }
}
