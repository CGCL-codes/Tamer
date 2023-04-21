package kr.ac.ssu.imc.whitehole.report.designer.items;

import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import kr.ac.ssu.imc.whitehole.report.designer.*;
import kr.ac.ssu.imc.whitehole.report.designer.workbench.ReportDesignerWindow;
import org.w3c.dom.*;
import org.xml.sax.*;

public class RDOLine extends RDObject {

    private Point tStartPoint, tEndPoint;

    public int nLineWidth;

    public int nLineKind;

    private boolean move;

    public RDOLine(Point tStartLoc, Dimension tSize, Point tStartPoint, Point tEndPoint, Color tColor, int nWidth, int nKind) {
        super(tStartLoc, tSize);
        oShape.nType = RDShapeInfo.LINE;
        oSelectView.setStyle(RDSelectView.LINE);
        oShape.oColorFg = Color.black;
        nLineWidth = 1;
        nLineKind = 0;
        this.tStartPoint = new Point(tStartPoint);
        this.tEndPoint = new Point(tEndPoint);
    }

    public void setPoints(Point tStart, Point tEnd) {
        Point SPoint = new Point(), EPoint = new Point();
        move = false;
        setObjectLocation(RDOLine.getStartLoc(tStart, tEnd));
        setObjectSize(RDOLine.getStartSize(tStart, tEnd));
        tStartPoint = new Point(tStart);
        tEndPoint = new Point(tEnd);
        revalidate();
        repaint();
    }

    public Point getStartPoint(Point tStart, Point tEnd) {
        Point tReturn = new Point();
        if (tStart.x <= tEnd.x) tReturn.x = oShape.nXRelative; else tReturn.x = oShape.nXRelative + tStart.x - tEnd.x;
        if (tStart.y <= tEnd.y) tReturn.y = oShape.nYRelative; else tReturn.y = oShape.nYRelative + tStart.y - tEnd.y;
        return tReturn;
    }

    public Point getStartPoint() {
        return tStartPoint;
    }

    public Point getStartPointLocal() {
        return getStartPoint(tStartPoint, tEndPoint);
    }

    public Point getEndPoint(Point tStart, Point tEnd) {
        Point tReturn = new Point();
        if (tStart.x <= tEnd.x) tReturn.x = oShape.nXRelative - tStart.x + tEnd.x; else tReturn.x = oShape.nXRelative;
        if (tStart.y <= tEnd.y) tReturn.y = oShape.nYRelative - tStart.y + tEnd.y; else tReturn.y = oShape.nYRelative;
        return tReturn;
    }

    public Point getEndPoint() {
        return tEndPoint;
    }

    public Point getEndPointLocal() {
        return getEndPoint(tStartPoint, tEndPoint);
    }

    public void changeLineWidth(int nWidth) {
        nLineWidth = nWidth;
        invalidate();
        validate();
        repaint();
    }

    public void changeLineKind(int nKind) {
        nLineKind = nKind;
        invalidate();
        validate();
        repaint();
    }

    public void changeColor(Color tColor) {
        oShape.oColorFg = tColor;
        invalidate();
        validate();
        repaint();
    }

    public RDObject getAClone() {
        RDOLine tNewObject = new RDOLine(getObjectLocation(), getObjectSize(), tStartPoint, tEndPoint, oShape.oColorFg, nLineWidth, nLineKind);
        tNewObject.changeColor(oShape.oColorFg);
        tNewObject.changeLineWidth(nLineWidth);
        tNewObject.changeLineKind(nLineKind);
        return tNewObject;
    }

    public RDObject getAClone(int nTimes) {
        RDOLine tNewObejct = new RDOLine(new Point(getObjectLocation().x * nTimes, getObjectLocation().y * nTimes), new Dimension(getObjectSize().width * nTimes, getObjectSize().height * nTimes), new Point(tStartPoint.x * nTimes, tStartPoint.y * nTimes), new Point(tEndPoint.x * nTimes, tEndPoint.y * nTimes), oShape.oColorFg, nLineWidth * nTimes, nLineKind * nTimes);
        return tNewObejct;
    }

    public Element createElementNode(Document tDocument) {
        super.createElementNode(tDocument);
        Element tLineElement = tDocument.createElement("rdoLine");
        tLineElement.setAttribute("startX", Integer.toString(tStartPoint.x));
        tLineElement.setAttribute("startY", Integer.toString(tStartPoint.y));
        tLineElement.setAttribute("endX", Integer.toString(tEndPoint.x));
        tLineElement.setAttribute("endY", Integer.toString(tEndPoint.y));
        tLineElement.setAttribute("lineWidth", Integer.toString(nLineWidth));
        tLineElement.setAttribute("lineKind", Integer.toString(nLineKind));
        oElement.appendChild(tLineElement);
        return oElement;
    }

    public static RDObject createRDObject(org.w3c.dom.Element tElement) {
        RDOLine tRDOLine = new RDOLine(new Point(0, 0), new Dimension(1, 1), new Point(0, 0), new Point(1, 1), Color.black, 1, 1);
        tRDOLine.setupElementNode(tElement);
        org.w3c.dom.NodeList tNodeList = tElement.getChildNodes();
        for (int i = 0; i < tNodeList.getLength(); i++) if (tNodeList.item(i).getNodeName().equals("rdoLine")) {
            org.w3c.dom.Element tLineElem = (org.w3c.dom.Element) tNodeList.item(i);
            int nSX = Integer.parseInt(tLineElem.getAttribute("startX"));
            int nSY = Integer.parseInt(tLineElem.getAttribute("startY"));
            int nEX = Integer.parseInt(tLineElem.getAttribute("endX"));
            int nEY = Integer.parseInt(tLineElem.getAttribute("endY"));
            tRDOLine.setPoints(new Point(nSX, nSY), new Point(nEX, nEY));
            int lineWidth = Integer.parseInt(tLineElem.getAttribute("lineWidth"));
            tRDOLine.changeLineWidth(lineWidth);
            int lineKind = Integer.parseInt(tLineElem.getAttribute("lineKind"));
            tRDOLine.changeLineKind(lineKind);
            break;
        }
        tRDOLine.oShape.nType = RDShapeInfo.LINE;
        tRDOLine.oSelectView.setStyle(RDSelectView.LINE);
        return tRDOLine;
    }

    public boolean isInObject(Point tGlobalPos) {
        Dimension tLocalSize = getSize();
        Point tLocalRootPos = getLocation();
        Point tLocalPos = new Point(tGlobalPos.x - tLocalRootPos.x, tGlobalPos.y - tLocalRootPos.y);
        Shape tShape = new Line2D.Double(getStartPoint(tStartPoint, tEndPoint).x, getStartPoint(tStartPoint, tEndPoint).y, getEndPoint(tStartPoint, tEndPoint).x, getEndPoint(tStartPoint, tEndPoint).y);
        return (((Line2D) tShape).ptLineDist(tLocalPos) <= 4) ? true : false;
    }

    public int IsInObjectWhere(Point tGlobalPos) {
        Dimension tLocalSize = getSize();
        Point tLocalRootPos = getLocation();
        Point tLocalPos = new Point(tGlobalPos.x - tLocalRootPos.x, tGlobalPos.y - tLocalRootPos.y);
        return oSelectView.whereIsIn(tLocalPos.x, tLocalPos.y);
    }

    public void paintContent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints tQualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        tQualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(oShape.oColorFg);
        g2d.setStroke(new BasicStroke(nLineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, ReportDesignerWindow.dashes[nLineKind], 0));
        Shape tLine = new Line2D.Double(getStartPoint(tStartPoint, tEndPoint), getEndPoint(tStartPoint, tEndPoint));
        if (nLineWidth > 0) g2d.draw(tLine);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        if (tEndPoint == null) return;
        if (!move) {
            return;
        }
        x += 6;
        y += 6;
        if (tEndPoint.x > tStartPoint.x) {
            this.tEndPoint.x -= this.tStartPoint.x - x;
            this.tStartPoint.x = x;
        } else {
            this.tStartPoint.x -= this.tEndPoint.x - x;
            this.tEndPoint.x = x;
        }
        if (tEndPoint.y > tStartPoint.y) {
            this.tEndPoint.y -= this.tStartPoint.y - y;
            this.tStartPoint.y = y;
        } else {
            this.tStartPoint.y -= this.tEndPoint.y - y;
            this.tEndPoint.y = y;
        }
    }

    public void setSize(int x, int y) {
        super.setSize(x, y);
        if (tEndPoint == null) return;
        if (!move) {
            move = true;
            return;
        }
        if (tEndPoint.x > tStartPoint.x) {
            this.tEndPoint.x = this.tStartPoint.x + x - 12;
        } else {
            this.tStartPoint.x = this.tEndPoint.x + x - 12;
        }
        if (tEndPoint.y > tStartPoint.y) {
            this.tEndPoint.y = this.tStartPoint.y + y - 12;
        } else {
            this.tStartPoint.y = this.tEndPoint.y + y - 12;
        }
    }
}
