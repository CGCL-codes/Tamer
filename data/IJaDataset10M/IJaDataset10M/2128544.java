package uk.ac.lkl.migen.mockup.shapebuilder.model.event;

import java.util.*;

public interface ShapeListener extends EventListener {

    public void shapeAdded(ShapeEvent e);

    public void shapeRemoved(ShapeEvent e);
}
