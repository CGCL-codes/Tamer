package org.objectweb.fractal.fractalizer;

import org.objectweb.fractal.fractalizer.graph.BindingNode;
import org.objectweb.fractal.fractalizer.graph.ComponentGraph;

/**
 * Its adds {@link BindingNode}s to the {@link ComponentGraph}.
 * 
 * @author Alessio Pace
 */
public interface BindingsResolver {

    void resolveBindings(ComponentGraph componentGraph);
}
