package com.g3d.display;

import java.util.Collection;
import com.g3d.G3DGraphics;

public abstract class Node {

    public abstract Collection<Node> getChilds();

    public abstract int getChildCount();

    public abstract void update(G3DGraphics g);
}
