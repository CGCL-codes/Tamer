package org.jfonia.structure;

import org.jfonia.connect.Node;
import org.jfonia.connect.Value;

/**
 * "time" slice of a family, representing the position(s) in all family-lists
 * 
 * 
 * @author wijnand.schepens@gmail.com
 * 
 */
public interface Slice extends Node {

    Node getProceeder();

    Value<Boolean> getProceedCondition();

    boolean hasProperty(String id);

    Value getProperty(String id);

    Value getLookaheadProperty(String id);

    Value<Integer> getKey(String id);

    Value<Integer> getLookaheadKey(String id);

    Node getFastForwarder(String id, Value target);
}
