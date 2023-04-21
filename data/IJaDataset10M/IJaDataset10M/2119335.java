package pl.edu.amu.wmi.kino.visualjavafx.model.objects.shapes;

import pl.edu.amu.wmi.kino.visualjavafx.model.objects.*;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.path.Path;

public interface Shape extends pl.edu.amu.wmi.kino.visualjavafx.model.animation.transformations.Shape, VisualObject {

    public Path toPath();
}
