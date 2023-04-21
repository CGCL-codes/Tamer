package com.awarix.gwt.dom.events;

import com.google.gwt.core.client.JavaScriptObject;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class MutationEventImpl extends EventImpl implements MutationEvent {

    protected MutationEventImpl(JavaScriptObject jso) {
        super(jso);
    }

    public native short getAttrChange();

    public native String getAttrName();

    public native String getNewValue();

    public native String getPrevValue();

    public native Node getRelatedNode();

    public native void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg, short attrChangeArg);

    private static MutationEvent wrapMutationEvent(JavaScriptObject jso) {
        return jso == null ? null : new MutationEventImpl(jso);
    }
}
