package net.sourceforge.javabits.graph;

import java.io.Writer;

public interface GraphWriter<N, E> {

    public void write(Writer out, Graph<N, E> graph);
}
