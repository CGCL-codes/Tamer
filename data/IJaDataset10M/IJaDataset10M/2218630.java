package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane;

import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IVertex;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.impl.GraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.model.IGraphModel;
import ru.spbu.math.ontologycomparison.zhukova.visualisation.ui.graphpane.tools.Tool;
import java.awt.*;
import java.util.Set;

/**
 * @author Anna R. Zhukova
 */
public interface IGraphPane {

    Point checkPoint(Point p);

    void deselectVertices();

    void selectVertex(IVertex v);

    void setGraphModel(GraphModel gr);

    IGraphModel getGraphModel();

    Set<IVertex> getSelectedVertices();

    void setTool(Tool tool);
}
