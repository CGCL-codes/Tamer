package org.sinaxe.xupdate;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

public class XUpdateLoader {

    private Navigator navigator;

    private UpdateContext context;

    private XUpdateFactory factory;

    private Stack parentStack = new Stack();

    private XUpdate xupdate;

    public XUpdateLoader(Navigator navigator) {
        this.navigator = navigator;
        this.factory = new XUpdateFactory();
    }

    public XUpdateLoader(Navigator navigator, XUpdateFactory factory) {
        this.navigator = navigator;
        this.factory = factory;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public UpdateContext getContext() {
        return context;
    }

    public void setContext(UpdateContext context) {
        this.context = context;
    }

    public static boolean isInstruction(int type) {
        return (type & XUpdate.INSTRUCTION) == XUpdate.INSTRUCTION;
    }

    public static boolean isCommand(int type) {
        return (type & XUpdate.COMMAND) == XUpdate.COMMAND;
    }

    public static boolean isInstructable(int type) {
        return (type & XUpdate.INSTRUCTABLE) == XUpdate.INSTRUCTABLE;
    }

    public static int toXUpdateType(String qname) {
        if (qname.startsWith(XUpdate.NS_XUPDATE + ":")) {
            String name = qname.substring(XUpdate.NS_XUPDATE.length() + 1);
            if (name.equals("append")) return XUpdate.APPEND;
            if (name.equals("insert-after")) return XUpdate.INSERTAFTER;
            if (name.equals("insert-before")) return XUpdate.INSERTBEFORE;
            if (name.equals("remove")) return XUpdate.REMOVE;
            if (name.equals("rename")) return XUpdate.RENAME;
            if (name.equals("update")) return XUpdate.UPDATE;
            if (name.equals("variable")) return XUpdate.VARIABLE;
            if (name.equals("attribute")) return XUpdate.ATTRIBUTE;
            if (name.equals("comment")) return XUpdate.COMMENT;
            if (name.equals("element")) return XUpdate.ELEMENT;
            if (name.equals("processing-instruction")) return XUpdate.PROSSINGINSTRUCTION;
            if (name.equals("text")) return XUpdate.TEXT;
            if (name.equals("value-of")) return XUpdate.VALUEOF;
            if (name.equals("modifications")) return XUpdate.MODIFICATIONS;
        }
        return XUpdate.RAW;
    }

    public String getNamespaceURI(Object elem, String qname) {
        int idx = qname.indexOf(':');
        if (idx == -1) return null;
        String prefix = qname.substring(0, idx);
        return navigator.translateNamespacePrefixToUri(prefix, elem);
    }

    public XUpdate load(Object root) throws XUpdateException {
        Iterator it = null;
        String id = null;
        String version = "1.0";
        if (root instanceof List) it = ((List) root).iterator(); else {
            if (!navigator.isElement(root)) throw new XUpdateException("XUpdateLoader.load() root not a valid type!");
            String qname = navigator.getElementQName(root);
            if (toXUpdateType(qname) == XUpdate.MODIFICATIONS) {
                try {
                    Iterator attIt = navigator.getAttributeAxisIterator(root);
                    while (attIt.hasNext()) {
                        Object att = attIt.next();
                        if (navigator.getAttributeName(att).equals("id")) {
                            id = navigator.getAttributeStringValue(att);
                        } else if (navigator.getAttributeName(att).equals("version")) {
                            version = navigator.getAttributeStringValue(att);
                        }
                    }
                    it = navigator.getChildAxisIterator(root);
                } catch (UnsupportedAxisException e) {
                }
            }
        }
        xupdate = new XUpdateImpl(id, version, context);
        parentStack.push(xupdate);
        if (it == null) loadElem(root); else while (it.hasNext()) loadElem(it.next());
        return xupdate;
    }

    private void loadElem(Object elem) throws XUpdateException {
        String qname = navigator.getElementNamespaceUri(elem) + ":" + navigator.getElementName(elem);
        int type = toXUpdateType(qname);
        if (isCommand(type)) {
            Iterator it = null;
            try {
                it = navigator.getAttributeAxisIterator(elem);
            } catch (UnsupportedAxisException e) {
            }
            Command command;
            String select = null;
            String child = null;
            String name = null;
            String data = null;
            while (it.hasNext()) {
                Object att = it.next();
                if (navigator.getAttributeName(att).equals("select")) {
                    select = navigator.getAttributeStringValue(att);
                } else if (navigator.getAttributeName(att).equals("child")) {
                    child = navigator.getAttributeStringValue(att);
                } else if (navigator.getAttributeName(att).equals("name")) {
                    name = navigator.getAttributeStringValue(att);
                }
            }
            switch(type) {
                case XUpdate.APPEND:
                    command = factory.createAppend(select, child);
                    break;
                case XUpdate.INSERTAFTER:
                    command = factory.createInsertAfter(select);
                    break;
                case XUpdate.INSERTBEFORE:
                    command = factory.createInsertBefore(select);
                    break;
                case XUpdate.REMOVE:
                    command = factory.createRemove(select);
                    break;
                case XUpdate.RENAME:
                    data = navigator.getElementStringValue(elem);
                    command = factory.createRename(select, data);
                    break;
                case XUpdate.UPDATE:
                    data = navigator.getElementStringValue(elem);
                    command = factory.createUpdate(select, data);
                    break;
                case XUpdate.VARIABLE:
                    command = factory.createVariable(name, select);
                    break;
                default:
                    command = null;
                    break;
            }
            if (command != null) {
                Object parent = parentStack.peek();
                if (!(parent instanceof Commandable)) throw new XUpdateException("XUpdateDecorator: Parent not commandable!");
                ((Commandable) parent).addCommand(command);
                try {
                    it = navigator.getChildAxisIterator(elem);
                } catch (UnsupportedAxisException e) {
                }
                parentStack.push(command);
                while (it.hasNext()) {
                    Object node = it.next();
                    if (navigator.isElement(node)) loadElem(node);
                }
                parentStack.pop();
            }
        } else if (isInstruction(type)) {
            Instruction instruction = null;
            Iterator it = null;
            try {
                it = navigator.getAttributeAxisIterator(elem);
            } catch (UnsupportedAxisException e) {
            }
            String name = null;
            String select = null;
            String uri = null;
            String data = null;
            while (it.hasNext()) {
                Object att = it.next();
                if (navigator.getAttributeName(att).equals("select")) {
                    select = navigator.getAttributeStringValue(att);
                } else if (navigator.getAttributeName(att).equals("name")) {
                    name = navigator.getAttributeStringValue(att);
                }
            }
            switch(type) {
                case XUpdate.ATTRIBUTE:
                    uri = getNamespaceURI(elem, name);
                    data = navigator.getElementStringValue(elem);
                    instruction = factory.createAttribute(name, uri, data);
                    break;
                case XUpdate.COMMENT:
                    data = navigator.getElementStringValue(elem);
                    instruction = factory.createComment(data);
                    break;
                case XUpdate.ELEMENT:
                    uri = getNamespaceURI(elem, name);
                    instruction = factory.createElement(name, uri);
                    break;
                case XUpdate.PROSSINGINSTRUCTION:
                    data = navigator.getElementStringValue(elem);
                    instruction = factory.createProcessingInstruction(name, data);
                    break;
                case XUpdate.RAW:
                    instruction = factory.createRaw(elem, navigator);
                    break;
                case XUpdate.TEXT:
                    data = navigator.getElementStringValue(elem);
                    instruction = factory.createText(data);
                    break;
                case XUpdate.VALUEOF:
                    instruction = factory.createValueOf(select);
                    break;
                default:
                    break;
            }
            if (instruction != null) {
                Object parent = parentStack.peek();
                if (!(parent instanceof Instructable)) throw new XUpdateException("XUpdateLoader: Parent not instructable!");
                ((Instructable) parent).addInstruction(instruction);
                if (type != XUpdate.RAW) {
                    try {
                        it = navigator.getChildAxisIterator(elem);
                    } catch (UnsupportedAxisException e) {
                    }
                    parentStack.push(instruction);
                    while (it.hasNext()) {
                        Object node = it.next();
                        if (navigator.isElement(node)) loadElem(node);
                    }
                    parentStack.pop();
                }
            }
        }
    }
}
