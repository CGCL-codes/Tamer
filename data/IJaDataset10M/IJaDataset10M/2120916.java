package com.depthexplorer;

import java.awt.*;
import java.util.*;
import javax.vecmath.*;
import edu.tufts.cs.geometry.*;
import com.depthexplorer.registrar.*;

public class RTNProximityGraph extends RenderTreeNode {

    String proximityGraphId;

    String parameter;

    @Override
    protected void setAttribute(String name, String value) throws Exception {
        if (name.equals("graphtype")) {
            value = value.trim();
            String[] parts = value.split("\\s*:\\s*");
            if (parts.length > 1) parameter = parts[1];
            proximityGraphId = parts[0].trim();
            Registrar.getInstance("proximitygraph", proximityGraphId);
        }
        if (name.equals("parameter")) {
            parameter = value.trim();
        }
    }

    @Override
    protected void process() throws Exception {
        ProximityGraph pg = (ProximityGraph) Registrar.getInstance("proximitygraph", proximityGraphId);
        pg.setParameterString(parameter);
        GMatrix data = (GMatrix) ps.getMatrix().clone();
        data.setSize(data.getNumRow(), 2);
        pg.setData(data);
        pg.computeGraph();
        Iterator<int[]> graphEdges = pg.iterator();
        double[] vecTemp = new double[3];
        Vector2d start = new Vector2d(), end = new Vector2d();
        while (graphEdges.hasNext()) {
            int[] e = graphEdges.next();
            data.getRow(e[0], vecTemp);
            start.set(vecTemp[0], vecTemp[1]);
            data.getRow(e[1], vecTemp);
            end.set(vecTemp[0], vecTemp[1]);
            ps.drawLine(start, end, 2, Color.LIGHT_GRAY);
        }
    }

    @Override
    public void setDefaults() {
        proximityGraphId = "delaunay";
        parameter = "";
    }

    public static Properties getProperties() {
        String attrDesc;
        Properties p = new Properties();
        p.setProperty("name", "proximitygraph");
        p.setProperty("description", "Draws a proximity graph over a set of points");
        p.setProperty("attr_count", "1");
        p.setProperty("attr_0_name", "graphtype");
        p.setProperty("attr_0_type", "proximity graph type name");
        p.setProperty("attr_0_default", "<tt>delaunay</tt>");
        attrDesc = "tbd";
        p.setProperty("attr_0_desc", attrDesc);
        p.setProperty("attr_0_example", "type='gabriel'");
        return p;
    }
}
