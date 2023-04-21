package src.backend.math.optimizer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import src.backend.EpntEntry;
import src.backend.Level;
import src.backend.LinsEntry;
import src.backend.ObjsEntry;
import src.backend.PolyEntry;
import src.backend.exception.kajiya.NoDataException;
import src.backend.math.GeometoryTool;
import src.backend.math.MapMath;
import src.backend.math.select.SelectContainer;
import src.backend.math.select.search.PolygonSearcher;

/**
 * optimize after moving map items(objects, end-points, lines, and polygons)
 * @author koji
 *
 */
public class MoveOptimizer {

    PolygonSearcher polySearcher;

    public MoveOptimizer(short floorHeight, short ceilingHeight) {
        polySearcher = new PolygonSearcher(floorHeight, ceilingHeight);
    }

    public void optimize(Level level, SelectContainer selectContainer) throws NoDataException {
        this.objectOptimize(level, selectContainer);
        this.endpointOptimize(level, selectContainer);
        this.lineOptimize(level, selectContainer);
        this.polygonOptimize(level, selectContainer);
    }

    /**
	 * optimize polygon
	 * - optimize end-points 
	 *    - optimize polygon's area
	 *    -                    center
	 *    - optimize line's    length
	 *    
	 * @param level
	 * @param selectContainer
	 * @throws NoDataException
	 */
    private void polygonOptimize(Level level, SelectContainer selectContainer) throws NoDataException {
        for (int i = 0; i < selectContainer.getPolygons().size(); i++) {
            PolyEntry polygon = level.getPolyChunk().getEntry(selectContainer.getPolygon(i).getIndex());
            for (int ep = 0; ep < polygon.getVertex_count(); ep++) {
                fixWithEndpoint(polygon.getEndpoint_indexes()[ep], level);
            }
        }
    }

    /**
	 * optimize line
	 * 
	 * - optimize end-points
	 *    -optimize line's length
	 * @param level
	 * @param selectContainer
	 * @throws NoDataException
	 */
    private void lineOptimize(Level level, SelectContainer selectContainer) throws NoDataException {
        for (int i = 0; i < selectContainer.getLines().size(); i++) {
            int lineIndex = selectContainer.getLine(i).getIndex();
            LinsEntry line = level.getLinsChunk().getEntry(lineIndex);
            for (int ep = 0; ep < LinsEntry.VERTEX_NUM; ep++) {
                fixWithEndpoint(line.getEndpoints()[ep], level);
            }
        }
    }

    /**
	 * optimize end-points
	 * @param level
	 * @param selectContainer
	 * @throws NoDataException
	 */
    private void endpointOptimize(Level level, SelectContainer selectContainer) throws NoDataException {
        for (int i = 0; i < selectContainer.getEndpoints().size(); i++) {
            int endpointIndex = selectContainer.getEndpoint(i).getIndex();
            fixWithEndpoint(endpointIndex, level);
        }
    }

    /**
	 * 
	 * @param endpointIndex
	 * @param level
	 */
    private void fixWithEndpoint(int endpointIndex, Level level) {
        fixLinesIncludeEndpoint(endpointIndex, level);
        fixPolygonsIncludeEndpoint(endpointIndex, level);
    }

    /**
	 * - fix polygon's area
	 * -               center
	 * @param endpointIndex
	 * @param level
	 */
    private void fixPolygonsIncludeEndpoint(int endpointIndex, Level level) {
        for (int i = 0; i < level.getPolyChunk().getNumEntries(); i++) {
            PolyEntry polygon = level.getPolyChunk().getEntry(i);
            boolean isFix = false;
            for (int ep = 0; ep < polygon.getVertex_count(); ep++) {
                if (endpointIndex == polygon.getEndpoint_indexes()[ep]) {
                    isFix = true;
                    break;
                }
            }
            if (isFix) {
                polygon.setArea(GeometoryTool.getArea(i, level));
                Point center = GeometoryTool.getCenter(i, level);
                polygon.setCenter_x((short) center.x);
                polygon.setCenter_y((short) center.y);
            }
        }
    }

    /**
	 * optimize line
	 * - fix line's length
	 * 
	 * @param endpointIndex
	 * @param level
	 */
    private void fixLinesIncludeEndpoint(int endpointIndex, Level level) {
        for (int l = 0; l < level.getLinsChunk().getNumEntries(); l++) {
            LinsEntry line = level.getLinsChunk().getEntry(l);
            boolean isFix = false;
            for (int ep = 0; ep < line.getEndpoints().length; ep++) {
                if (endpointIndex == line.getEndpoints()[ep]) {
                    isFix = true;
                    break;
                }
            }
            if (isFix) {
                line.setLength(GeometoryTool.getLength(l, level));
            }
        }
    }

    /**
	 * -
	 * @param level
	 * @param selectContainer 
	 * @throws NoDataException 
	 */
    private void objectOptimize(Level level, SelectContainer selectContainer) throws NoDataException {
        List polygonToRemoveList = new ArrayList();
        for (int i = 0; i < selectContainer.getObjects().size(); i++) {
            int objectIndex = selectContainer.getObject(i).getIndex();
            ObjsEntry obj = level.getObjsChunk().getEntry(objectIndex);
            short vertex[] = MapMath.getWorldPointFromObjectVertexRatio(obj.getX(), obj.getY());
            Point worldPoint = new Point(vertex[0], vertex[1]);
            int newPolygonID = polySearcher.getItemFromPoint(worldPoint, level);
            if (newPolygonID == -1) {
                polygonToRemoveList.add(new Integer(objectIndex));
            }
            int oldPolygonID = obj.getPolygon();
            obj.setPolygon((short) newPolygonID);
        }
    }

    private void fixPolygonFirstObjectToOtherObject(int polygonIndex, int objectIndex, Level level) {
        PolyEntry polygon = level.getPolyChunk().getEntry(polygonIndex);
        for (int o = 0; o < level.getObjsChunk().getNumEntries(); o++) {
            if (o == objectIndex) {
                continue;
            }
            ObjsEntry otherObject = level.getObjsChunk().getEntry(o);
            short vertex[] = MapMath.getWorldPointFromObjectVertexRatio(otherObject.getX(), otherObject.getY());
            Point worldPoint = new Point(vertex[0], vertex[1]);
            int objPolyIndex = polySearcher.getItemFromPoint(worldPoint, level);
            if (objPolyIndex == polygonIndex) {
                polygon.setFirst_object((short) o);
                return;
            }
        }
        polygon.setFirst_object((short) -1);
    }
}
