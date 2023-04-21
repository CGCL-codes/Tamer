package org.apache.myfaces.trinidadinternal.ui.collection;

import java.io.IOException;
import org.apache.myfaces.trinidadinternal.ui.AttributeKey;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.ui.UINodeProxy;
import org.apache.myfaces.trinidadinternal.ui.data.DataObject;

/**
 * General strategy:  set the current DataObject before
 * any call to getAttributeValue(), getNamedChild(), getIndexedChild(),
 * or getIndexedChildCount().  Wrap any children (indexed or named) to
 * do the same thing.
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
class DataObjectUINodeProxy extends UINodeProxy {

    public static UINode createWrappedNode(UINode baseNode, DataObject data, DataObject current) {
        if (baseNode == null) return null;
        if (baseNode instanceof DataObjectUINodeProxy) {
            ((DataObjectUINodeProxy) baseNode)._current = current;
            return baseNode;
        }
        return new DataObjectUINodeProxy(baseNode, data, current);
    }

    private DataObjectUINodeProxy(UINode baseNode, DataObject data, DataObject current) {
        if (baseNode == null) throw new NullPointerException();
        _baseNode = baseNode;
        _data = data;
        _current = current;
    }

    @Override
    public Object getAttributeValue(UIXRenderingContext context, AttributeKey attrKey) {
        Object value;
        if (context == null) {
            value = super.getAttributeValue(null, attrKey);
        } else {
            DataObject oldDataObject = context.getCurrentDataObject();
            if (oldDataObject != _current) {
                value = super.getAttributeValue(context, attrKey);
            } else {
                context.setCurrentDataObject(_data);
                value = super.getAttributeValue(context, attrKey);
                context.setCurrentDataObject(oldDataObject);
            }
        }
        return value;
    }

    @Override
    public int getIndexedChildCount(UIXRenderingContext context) {
        int count;
        if (context == null) {
            count = super.getIndexedChildCount(null);
        } else {
            DataObject oldDataObject = context.getCurrentDataObject();
            if (oldDataObject != _current) {
                count = super.getIndexedChildCount(context);
            } else {
                context.setCurrentDataObject(_data);
                count = super.getIndexedChildCount(context);
                context.setCurrentDataObject(oldDataObject);
            }
        }
        return count;
    }

    @Override
    public void render(UIXRenderingContext context, UINode node) throws IOException {
        DataObject oldDataObject = context.getCurrentDataObject();
        if (oldDataObject != _current) {
            super.render(context, node);
        } else {
            context.setCurrentDataObject(_data);
            super.render(context, node);
            context.setCurrentDataObject(oldDataObject);
        }
    }

    @Override
    public UINode getIndexedChild(UIXRenderingContext context, int childIndex) {
        UINode child;
        if (context == null) {
            child = createWrappedNode(super.getIndexedChild(null, childIndex), _data, _current);
        } else {
            DataObject oldDataObject = context.getCurrentDataObject();
            if (oldDataObject != _current) {
                child = super.getIndexedChild(context, childIndex);
            } else {
                context.setCurrentDataObject(_data);
                child = createWrappedNode(super.getIndexedChild(context, childIndex), _data, _current);
                context.setCurrentDataObject(oldDataObject);
            }
        }
        return child;
    }

    @Override
    public UINode getNamedChild(UIXRenderingContext context, String childName) {
        UINode child;
        if (context == null) {
            child = createWrappedNode(super.getNamedChild(null, childName), _data, _current);
        } else {
            DataObject oldDataObject = context.getCurrentDataObject();
            if (oldDataObject != _current) {
                child = super.getNamedChild(context, childName);
            } else {
                context.setCurrentDataObject(_data);
                child = createWrappedNode(super.getNamedChild(context, childName), _data, _current);
                context.setCurrentDataObject(oldDataObject);
            }
        }
        return child;
    }

    @Override
    protected UINode getUINode() {
        return _baseNode;
    }

    private UINode _baseNode;

    private DataObject _data;

    private DataObject _current;
}
