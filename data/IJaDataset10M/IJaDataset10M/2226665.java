package vademecum.ui.visualizer.vgraphics.D2.scatter.features;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.core.experiment.ExperimentNode;
import vademecum.data.IDataGrid;
import vademecum.extensionPoint.ExtensionFactory;
import vademecum.extensionPoint.IDataNode;
import vademecum.ui.project.DataNavigation;
import vademecum.ui.visualizer.VisualizerFrame;
import vademecum.ui.visualizer.utils.VEllipse;
import vademecum.ui.visualizer.utils.VPolygonPath;
import vademecum.ui.visualizer.utils.VRectangle;
import vademecum.ui.visualizer.vgraphics.AbstractInteraction;
import vademecum.ui.visualizer.vgraphics.IActiveDrawable;
import vademecum.ui.visualizer.vgraphics.IMouseOverDrawable;
import vademecum.ui.visualizer.vgraphics.D2.scatter.ScatterPlot2D;

public class DataExtraction extends AbstractInteraction implements IMouseOverDrawable, IActiveDrawable, Runnable {

    /** Logger */
    private static Log log = LogFactory.getLog(DataExtraction.class);

    boolean selectionActive = false;

    boolean shapeExists = false;

    boolean shapeVisible = false;

    int selectionType = 1;

    int selectionMode = 1;

    static final int SM_NOTHING = 0;

    static final int SM_CREATE = 1;

    static final int SM_ARRANGE = 2;

    VEllipse ellipse;

    VRectangle rect;

    VPolygonPath poly;

    int activeRectanglePointIndex = 0;

    int activeEllipsePointIndex = 0;

    int activePolyPointIndex = 0;

    double scaleX = 1;

    double scaleY = 1;

    static final int E_RECTANGLE = 1;

    static final int E_ELLIPSE = 2;

    static final int E_POLY = 5;

    ArrayList<Point2D> extractionList = new ArrayList<Point2D>();

    private Point screenPosition = new Point(0, 0);

    private Point lastDragPoint = new Point(0, 0);

    private Point lastMousePoint = new Point(0, 0);

    ExperimentNode selection = null;

    JPopupMenu pMenu;

    JMenuItem extrVisibleMenu;

    JMenuItem extrActionMenu;

    static final int DETECTRADIUS = 4;

    ArrayList<Point2D> dataList;

    ArrayList<Point> screenList;

    public DataExtraction() {
        this.setExtractionType(5);
        this.setExtractionMode(0);
    }

    private void updateScreenList() {
        dataList = ((ScatterPlot2D) refBase).getDataPoints();
        screenList = ((ScatterPlot2D) refBase).getDataScreenPoints();
    }

    @Override
    public int getTriggerID() {
        return 17;
    }

    @Override
    public JLabel getInteractionLabel() {
        return new JLabel("DataExtraction");
    }

    @Override
    public String getInteractionType() {
        return "Interactive";
    }

    @Override
    public JMenuItem getMenuItem() {
        return null;
    }

    @Override
    public String getName() {
        return "DataExtraction";
    }

    @Override
    public int getPriority() {
        return 155;
    }

    public void setExtractionType(int typeFlag) {
        this.selectionType = typeFlag;
        if (typeFlag == DataExtraction.E_RECTANGLE) {
            rect = new VRectangle();
        }
        if (typeFlag == DataExtraction.E_ELLIPSE) {
            ellipse = new VEllipse();
        }
        if (typeFlag == DataExtraction.E_POLY) {
            poly = new VPolygonPath();
        }
    }

    public int getExtractionType() {
        return this.selectionType;
    }

    public void setExtractionMode(int modeFlag) {
        this.selectionMode = modeFlag;
        if (modeFlag == DataExtraction.SM_ARRANGE) {
            shapeExists = true;
            shapeVisible = true;
        } else if (modeFlag == DataExtraction.SM_CREATE) {
            shapeExists = false;
            shapeVisible = true;
        }
    }

    public void setEShapeVisible(boolean b) {
        this.shapeVisible = b;
    }

    public boolean getEShapeVisible() {
        return this.shapeVisible;
    }

    public int getExtractionMode() {
        return this.selectionMode;
    }

    public void drawWhenMouseOver(Graphics2D g2) {
        if (this.selectionMode == DataExtraction.SM_CREATE) {
            Color saveColor = g2.getColor();
            g2.setColor(Color.green);
            Point aPt = lastMousePoint;
            g2.fillRect(aPt.x - 3, aPt.y - 3, 6, 6);
            g2.setColor(saveColor);
        }
    }

    public void drawWhenActive(Graphics2D g2) {
        if (this.getEShapeVisible()) {
            if (this.selectionType == DataExtraction.E_RECTANGLE) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.black);
                if (this.getEShapeVisible()) {
                    g2.draw(rect.getShape());
                }
                g2.setColor(saveColor);
                if (this.selectionMode == DataExtraction.SM_CREATE) {
                    log.debug("Mode -> Creation");
                    int num = rect.getNumPoints();
                    if (num > 0) {
                        Point firstPt = rect.getPoint(0);
                        g2.setColor(Color.black);
                        g2.drawLine(firstPt.x, firstPt.y, lastMousePoint.x, firstPt.y);
                        g2.drawLine(lastMousePoint.x, firstPt.y, lastMousePoint.x, lastMousePoint.y);
                        g2.drawLine(firstPt.x, firstPt.y, firstPt.x, lastMousePoint.y);
                        g2.drawLine(firstPt.x, lastMousePoint.y, lastMousePoint.x, lastMousePoint.y);
                    }
                }
                if (this.selectionMode == DataExtraction.SM_ARRANGE || this.selectionMode == DataExtraction.SM_CREATE) {
                    drawRectangleAnchors(g2);
                }
            } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.black);
                if (ellipse.getNumPoints() > 2) {
                    g2.draw(ellipse.getShape());
                }
                g2.setColor(saveColor);
                if (this.selectionMode == DataExtraction.SM_CREATE) {
                    log.debug("mode->creation of form");
                    int num = ellipse.getNumPoints();
                    if (num > 0) {
                        log.debug("create->ellipse");
                        Point firstPt = ellipse.getPoint(0);
                        g2.setColor(Color.black);
                        g2.drawLine(firstPt.x, firstPt.y, lastMousePoint.x, firstPt.y);
                        g2.drawLine(lastMousePoint.x, firstPt.y, lastMousePoint.x, lastMousePoint.y);
                        g2.drawLine(firstPt.x, firstPt.y, firstPt.x, lastMousePoint.y);
                        g2.drawLine(firstPt.x, lastMousePoint.y, lastMousePoint.x, lastMousePoint.y);
                    }
                }
                if (this.selectionMode == DataExtraction.SM_ARRANGE || this.selectionMode == DataExtraction.SM_CREATE) {
                    drawEllipseAnchors(g2);
                }
            } else if (this.selectionType == DataExtraction.E_POLY) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.black);
                g2.draw(poly.getShape());
                g2.setColor(saveColor);
                if (this.selectionMode == DataExtraction.SM_CREATE) {
                    log.debug("Mode -> Polygon");
                    int num = poly.getNumPoints();
                    if (num > 0) {
                        Point lastPt = poly.getPoint(num - 1);
                        g2.setColor(Color.black);
                        g2.drawLine(lastPt.x, lastPt.y, lastMousePoint.x, lastMousePoint.y);
                    }
                }
                if (this.selectionMode == DataExtraction.SM_ARRANGE || this.selectionMode == DataExtraction.SM_CREATE) {
                    drawPolygonAnchors(g2);
                }
            }
        }
    }

    private void drawPolygonAnchors(Graphics2D g2) {
        g2.setColor(Color.black);
        int index = 0;
        for (Point aPt : poly.getPointsList()) {
            g2.fillOval(aPt.x - 3, aPt.y - 3, 6, 6);
            if (index == this.activePolyPointIndex) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.green);
                g2.fillRect(aPt.x - 3, aPt.y - 3, 6, 6);
                g2.setColor(saveColor);
            }
            index++;
        }
    }

    private void drawRectangleAnchors(Graphics2D g2) {
        g2.setColor(Color.black);
        int index = 0;
        for (Point aPt : rect.getPointsList()) {
            g2.fillOval(aPt.x - 3, aPt.y - 3, 6, 6);
            if (index == this.activeRectanglePointIndex) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.green);
                g2.fillRect(aPt.x - 3, aPt.y - 3, 6, 6);
                g2.setColor(saveColor);
            }
            index++;
        }
    }

    private void drawEllipseAnchors(Graphics2D g2) {
        g2.setColor(Color.black);
        int index = 0;
        for (Point aPt : ellipse.getPointsList()) {
            g2.fillOval(aPt.x - 3, aPt.y - 3, 6, 6);
            if (index == this.activeEllipsePointIndex) {
                Color saveColor = g2.getColor();
                g2.setColor(Color.green);
                g2.fillRect(aPt.x - 3, aPt.y - 3, 6, 6);
                g2.setColor(saveColor);
            }
            index++;
        }
    }

    public void extractData() {
        new Thread(this).start();
    }

    public void mouseClicked(MouseEvent e) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
                this.showPopupMenu(e.getX() - 20, e.getY() - 10);
                this.setExtractionMode(DataExtraction.SM_NOTHING);
            }
            if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
                if (this.selectionMode == DataExtraction.SM_CREATE) {
                    if (this.selectionType == DataExtraction.E_RECTANGLE) {
                        log.debug("Mouse clicked : Rect chosen");
                        if (rect.getNumPoints() == 0) {
                            rect.addPoint(e.getX(), e.getY());
                        } else {
                            rect.addPoint(e.getX(), e.getY());
                            this.setExtractionMode(DataExtraction.SM_ARRANGE);
                        }
                    } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
                        log.debug("Mouse clicked : Ellipse chosen");
                        if (ellipse.getNumPoints() == 0) {
                            ellipse.addPoint(e.getX(), e.getY());
                        } else {
                            ellipse.addPoint(e.getX(), e.getY());
                            this.setExtractionMode(DataExtraction.SM_ARRANGE);
                        }
                    } else if (this.selectionType == DataExtraction.E_POLY) {
                        log.debug("Mouse clicked : Polygon chosen");
                        if (poly.getNumPoints() >= 3) {
                            Point fPt = poly.getPoint(0);
                            if (e.getPoint().distance(fPt) < 5) {
                                poly.closePath();
                                this.setExtractionMode(DataExtraction.SM_ARRANGE);
                            } else {
                                poly.addPoint(e.getX(), e.getY());
                            }
                        } else {
                            poly.addPoint(e.getX(), e.getY());
                        }
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            refBase.setCursor(cursor);
        }
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            Point point = e.getPoint();
            lastDragPoint.x = screenPosition.x + point.x;
            lastDragPoint.y = screenPosition.y + point.y;
            selectionActive = true;
            scaleX = 1;
            scaleY = 1;
        }
    }

    public void mouseReleased(MouseEvent arg0) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            selectionActive = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            double deltaX = lastDragPoint.x - e.getX();
            double deltaY = lastDragPoint.y - e.getY();
            log.debug("delta x " + deltaX);
            log.debug("delta y " + deltaY);
            scaleX = scaleX - deltaX;
            scaleY = scaleY - deltaY;
            if (this.selectionMode == DataExtraction.SM_ARRANGE) {
                if (this.selectionType == DataExtraction.E_RECTANGLE) {
                    int ptno = this.activeRectanglePointIndex;
                    Point pt = rect.getPoint(ptno);
                    pt.setLocation(e.getX(), e.getY());
                    rect = rect.setPoint(ptno, pt);
                } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
                    int ptno = this.activeEllipsePointIndex;
                    Point pt = ellipse.getPoint(ptno);
                    pt.setLocation(e.getX(), e.getY());
                    ellipse = ellipse.setPoint(ptno, pt);
                } else if (this.selectionType == DataExtraction.E_POLY) {
                    int ptno = this.activePolyPointIndex;
                    Point pt = poly.getPoint(ptno);
                    pt.setLocation(e.getX(), e.getY());
                    poly = poly.setPoint(ptno, pt);
                }
            }
            lastDragPoint = e.getPoint();
            refBase.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            if (selectionMode == DataExtraction.SM_NOTHING) {
                updateScreenList();
                StringBuffer text = new StringBuffer();
                int index = 0;
                for (Point point : this.screenList) {
                    if ((point.getX() > e.getX() - DETECTRADIUS) && (point.getX() < e.getX() + DETECTRADIUS) && (point.getY() > e.getY() - DETECTRADIUS) && (point.getY() < e.getY() + DETECTRADIUS)) {
                        text.append(String.valueOf(dataList.get(index).toString()) + "\n");
                    }
                    index++;
                }
                refBase.setToolTipText(text.toString());
            }
            if (selectionMode == DataExtraction.SM_ARRANGE) {
                if (this.selectionType == DataExtraction.E_RECTANGLE) {
                    int nearest = rect.getNearestPointIndex(e.getPoint());
                    if (nearest != activeRectanglePointIndex) {
                        activeRectanglePointIndex = nearest;
                        log.debug("(mousemove_activeRectPtIndex : " + this.activeRectanglePointIndex);
                        refBase.repaint();
                    }
                } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
                    int nearest = ellipse.getNearestPointIndex(e.getPoint());
                    if (nearest != activeEllipsePointIndex) {
                        activeEllipsePointIndex = nearest;
                        log.debug("(mousemove_activeEllipsePtIndex : " + this.activeEllipsePointIndex);
                        refBase.repaint();
                    }
                } else if (this.selectionType == DataExtraction.E_POLY) {
                    int nearest = poly.getNearestPointIndex(e.getPoint());
                    if (nearest != activePolyPointIndex) {
                        activePolyPointIndex = nearest;
                        log.debug("activePolyPtIndex : " + this.activePolyPointIndex);
                        refBase.repaint();
                    }
                }
            } else if (selectionMode == DataExtraction.SM_CREATE) {
                lastMousePoint = e.getPoint();
                refBase.repaint();
            }
        }
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }

    @Override
    public void initPopupMenu() {
        pMenu = new JPopupMenu("Data-Selection");
        JMenu subMenu = new JMenu("Place new Extraction Form");
        JMenuItem eItem = new JMenuItem("Polygon");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                setExtractionType(5);
                setExtractionMode(1);
            }
        });
        subMenu.add(eItem);
        eItem = new JMenuItem("Rectangle");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                setExtractionType(1);
                setExtractionMode(1);
            }
        });
        subMenu.add(eItem);
        eItem = new JMenuItem("Ellipse");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                setExtractionType(2);
                setExtractionMode(1);
            }
        });
        subMenu.add(eItem);
        pMenu.add(subMenu);
        JMenuItem extrArrange = new JMenuItem("Reshape");
        extrArrange.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setExtractionMode(DataExtraction.SM_ARRANGE);
            }
        });
        pMenu.add(extrArrange);
        extrVisibleMenu = new JMenuItem("Show/Hide Shape");
        extrVisibleMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                shapeVisible = !shapeVisible;
            }
        });
        pMenu.add(extrVisibleMenu);
        pMenu.addSeparator();
        extrActionMenu = new JMenuItem("Extract");
        extrActionMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                extractData();
            }
        });
        pMenu.add(extrActionMenu);
    }

    @Override
    public void showPopupMenu(int x, int y) {
        if (shapeVisible == false) {
            extrVisibleMenu.setText("Show Shape");
        } else {
            extrVisibleMenu.setText("Hide Shape");
        }
        if (shapeExists == false) {
            extrVisibleMenu.setEnabled(false);
            extrActionMenu.setEnabled(false);
        } else {
            extrVisibleMenu.setEnabled(true);
            extrActionMenu.setEnabled(true);
        }
        pMenu.show(refBase, x, y);
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties p) {
    }

    public void run() {
        log.debug("begin Data Extraction");
        VisualizerFrame vf = (VisualizerFrame) refBase.getXplorePanel().getGraphicalViewer();
        vf.showWaitPanel("Data Exctraction ...");
        extractionList.clear();
        GeneralPath gp = null;
        if (this.selectionType == DataExtraction.E_RECTANGLE) {
            gp = new GeneralPath(rect.getShape());
        } else if (this.selectionType == DataExtraction.E_ELLIPSE) {
            gp = new GeneralPath(ellipse.getShape());
        } else if (this.selectionType == DataExtraction.E_POLY) {
            gp = new GeneralPath(poly.getShape());
        }
        if (gp == null) throw new IllegalArgumentException("Extraction failed : No general path created.");
        updateScreenList();
        log.debug("Size of dataList : " + dataList.size());
        log.debug("Size of screenList : " + screenList.size());
        for (int i = 0; i < screenList.size(); i++) {
            Point scp = screenList.get(i);
            if (gp.contains(scp) == true) {
                extractionList.add(dataList.get(i));
                log.debug("add  " + dataList.get(i));
            } else log.debug("not contained : " + dataList.get(i));
        }
        IDataNode dn = refBase.getXplorePanel().getSourceNode().getMethod();
        IDataGrid sourceGrid = (IDataGrid) dn.getOutput(IDataGrid.class);
        ArrayList<Integer> indicesList = new ArrayList<Integer>();
        for (Point2D ep : extractionList) {
            int[] attributeNo = ((ScatterPlot2D) refBase).getPlotDataVariables();
            int att1 = attributeNo[0];
            int att2 = attributeNo[1];
            Vector<Integer> canditatesX = sourceGrid.getRowIndex(att1, ep.getX());
            Vector<Integer> canditatesY = sourceGrid.getRowIndex(att2, ep.getY());
            for (Integer cx : canditatesX) {
                if (canditatesY.contains(cx) == true) {
                    indicesList.add(cx);
                }
            }
        }
        StringBuffer iStb = new StringBuffer();
        int iSize = indicesList.size();
        if (iSize >= 1) {
            iStb.append(indicesList.get(0));
        }
        for (int i = 1; i < iSize; i++) {
            Integer ix = indicesList.get(i);
            iStb.append("," + ix);
        }
        vf.textFlushToWaitPanel("Indices to Subselector...");
        String indices = iStb.toString();
        DataNavigation nav = refBase.getXplorePanel().getExpertice().getDataNavigation();
        if (selection == null) {
            selection = ExtensionFactory.createDataNode("vademecum.data.subsetSelector@subsetSelector");
            nav.addNode(refBase.getXplorePanel().getSourceNode(), selection);
        }
        selection.getMethod().setProperty("selection", indices);
        selection.setState(ExperimentNode.READY);
        log.debug("finished with extraction");
        vf.hideWaitPanel();
        dataList = null;
        screenList = null;
    }
}
