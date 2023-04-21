package net.sourceforge.freejava.ui;

public interface Renderer {

    Object render(Object context, Var<?> var) throws RenderException;
}
