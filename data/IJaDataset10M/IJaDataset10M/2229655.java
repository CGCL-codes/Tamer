package net.sourceforge.olympos.diagramimageexporter.shapes;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import net.sourceforge.olympos.diagramimageexporter.ElementDiagram;
import net.sourceforge.olympos.diagramimageexporter.EnumFigureType;
import net.sourceforge.olympos.diagramimageexporter.Figure;
import net.sourceforge.olympos.diagramimageexporter.InfoAllowedConnection;
import net.sourceforge.olympos.diagramimageexporter.InfoCoordinateSize;
import net.sourceforge.olympos.diagramimageexporter.InfoFigureParameter;
import net.sourceforge.olympos.diagramimageexporter.InfoLine;
import net.sourceforge.olympos.diagramimageexporter.SVGGenerator;

@SuppressWarnings("serial")
public class BusinessProcess extends Figure {

    private InfoCoordinateSize rect1 = new InfoCoordinateSize(0, 0, 150, 50);

    private InfoCoordinateSize rect2 = new InfoCoordinateSize(0, 0, 0, 0);

    private InfoLine infLine1 = new InfoLine(10, 0, 10, 50);

    private InfoLine infLine2 = new InfoLine(15, 0, 15, 50);

    private InfoLine inLeft = new InfoLine(122, 5, 122, 13);

    private InfoLine inUp = new InfoLine(122, 5, 140, 5);

    private InfoLine inDown = new InfoLine(122, 13, 140, 13);

    private InfoLine inrightup = new InfoLine(140, 5, 145, 9);

    private InfoLine inrightdown = new InfoLine(140, 13, 145, 9);

    private InfoCoordinateSize figureInfo = new InfoCoordinateSize(0, 0, 150, 50);

    @Override
    public void draw(Graphics2D g2d, InfoFigureParameter createFig, ArrayList<InfoFigureParameter> children, SVGGenerator svg, ArrayList<String> existLine) {
        drawScaleLine(g2d, createFig, figureInfo, infLine1);
        drawScaleLine(g2d, createFig, figureInfo, infLine2);
        drawScaleLine(g2d, createFig, figureInfo, inLeft);
        drawScaleLine(g2d, createFig, figureInfo, inUp);
        drawScaleLine(g2d, createFig, figureInfo, inDown);
        drawScaleLine(g2d, createFig, figureInfo, inrightup);
        drawScaleLine(g2d, createFig, figureInfo, inrightdown);
        for (InfoFigureParameter currChild : children) {
            ElementDiagram elem = ElementDiagram.getCatalogEntry(createFig.getType());
            HashMap<EnumFigureType, InfoAllowedConnection> figAllowedCatal1 = elem.getAllowedConnection();
            InfoAllowedConnection allowedConnection = figAllowedCatal1.get(currChild.getType());
            String key = createFig.getTypeId() + createFig.getAlias() + currChild.getTypeId() + currChild.getAlias();
            if (!existLine.contains(key)) {
                String comment = allowedConnection.getLineLabel();
                drawCon.drawConnection(g2d, createFig, currChild, comment, allowedConnection.getSourceConnectionArrow(), allowedConnection.getTargetConnectionArrow(), svg);
                existLine.add(key);
                String key2 = currChild.getTypeId() + currChild.getAlias() + createFig.getTypeId() + createFig.getAlias();
                existLine.add(key2);
            }
        }
    }
}
