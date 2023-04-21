package com.reactiveplot.library.editor.views;

import com.reactiveplot.library.editor.nodes.Node;
import com.reactiveplot.library.events.InterpreterEvent;

public interface NodeList {

    public int size();

    public Node get(int index);

    public Node getStartNode();

    public Node findNodeFor(InterpreterEvent event);
}
