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
import net.sourceforge.olympos.diagramimageexporter.SVGGenerator;

@SuppressWarnings("serial")
public class ActivityInitial extends Figure {

    private InfoCoordinateSize circle1 = new InfoCoordinateSize(0, 0, 40, 40);

    private InfoCoordinateSize figureInfo = new InfoCoordinateSize(0, 0, 40, 40);

    public void draw(Graphics2D g2d, InfoFigureParameter createFig, ArrayList<InfoFigureParameter> children, SVGGenerator svg, ArrayList<String> existLine) {
        drawScaleEllipse(g2d, createFig, figureInfo, circle1, true);
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
