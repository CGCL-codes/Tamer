package org.kabeja.parser.entities;

import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFVertex;
import org.kabeja.parser.DXFValue;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 */
public class DXFLWPolylineHandler extends AbstractEntityHandler {

    public static final String ENTITY_NAME = "LWPOLYLINE";

    public static final int VERTEX_BULGE = 42;

    public static final int START_WIDTH = 40;

    public static final int END_WIDTH = 41;

    public static final int CONSTANT_WIDTH = 43;

    public static final int ELEVATION = 38;

    public static final int THICKNESS = 39;

    private DXFVertex vertex;

    private DXFLWPolyline lwpolyline;

    /**
     *
     */
    public DXFLWPolylineHandler() {
        super();
    }

    public void endDXFEntity() {
    }

    public DXFEntity getDXFEntity() {
        return lwpolyline;
    }

    public String getDXFEntityName() {
        return ENTITY_NAME;
    }

    public boolean isFollowSequence() {
        return false;
    }

    public void parseGroup(int groupCode, DXFValue value) {
        switch(groupCode) {
            case GROUPCODE_START_X:
                createVertex();
                vertex.setX(value.getDoubleValue());
                break;
            case GROUPCODE_START_Y:
                vertex.setY(value.getDoubleValue());
                break;
            case GROUPCODE_START_Z:
                vertex.setZ(value.getDoubleValue());
                break;
            case VERTEX_BULGE:
                vertex.setBulge(value.getDoubleValue());
                break;
            case START_WIDTH:
                vertex.setStartWidth(value.getDoubleValue());
                break;
            case END_WIDTH:
                vertex.setEndWidth(value.getDoubleValue());
                break;
            case CONSTANT_WIDTH:
                lwpolyline.setConstantWidth(value.getDoubleValue());
                break;
            case ELEVATION:
                lwpolyline.setElevation(value.getDoubleValue());
                break;
            case THICKNESS:
                lwpolyline.setThickness(value.getDoubleValue());
                break;
            default:
                super.parseCommonProperty(groupCode, value, lwpolyline);
        }
    }

    public void startDXFEntity() {
        lwpolyline = new DXFLWPolyline();
    }

    private void createVertex() {
        vertex = new DXFVertex();
        vertex.setDXFDocument(doc);
        lwpolyline.addVertex(vertex);
    }
}
