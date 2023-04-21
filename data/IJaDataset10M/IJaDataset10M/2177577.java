package ru.cos.sim.ras.duo.digraph;

import ru.cos.sim.ras.duo.utils.Extendable;
import ru.cos.sim.ras.duo.utils.ExtensionCollection;

public abstract class Vertex implements Extendable {

    public abstract Iterable<? extends Edge> getOutgoingEdges();

    public abstract Iterable<? extends Edge> getIncomingEdges();

    private ExtensionCollection extensions = new ExtensionCollection();

    @Override
    public ExtensionCollection getExtensions() {
        return extensions;
    }
}
