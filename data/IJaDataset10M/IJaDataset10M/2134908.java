package edu.cmu.cs.bungee.client.viz;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEventListener;

interface GridElement {

    void setSize(int w, int h);

    double getImageWidth();

    double getImageHeight();

    GridElementWrapper getWrapper();

    PNode getParent();

    void addInputEventListener(PInputEventListener listener);

    void setOffset(double x, double y);

    void addChild(PNode pNode);

    double getXOffset();

    double getYOffset();

    double getScale();
}
