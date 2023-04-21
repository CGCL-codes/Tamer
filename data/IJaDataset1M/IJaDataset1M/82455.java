package geocosm.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import uk.me.jstott.jcoord.LatLng;
import geocosm.osm.BoundingBox;
import geocosm.osm.WayPointList;

public class HintPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -717955244448979526L;

    Point offset = new Point(0, 0);

    int maxXTiles = 10;

    int maxYTiles = 10;

    int xTile = 8642;

    int yTile = 5400;

    int zoom = 14;

    List<HintDialog> hints = new ArrayList<HintDialog>();

    List<HintFlag> hintFlags = new ArrayList<HintFlag>();

    List<HintCircle> hintCircles = new ArrayList<HintCircle>();

    HintDialogListener listener;

    WayPointList wayPointList = null;

    HintCircle editCircle = null;

    public HintPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxXTiles = screenSize.width / 256 + 2;
        maxYTiles = screenSize.height / 256 + 2;
        setLayout(null);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 255));
        listener = new HintDialogListener(this);
    }

    public void newZoom(int zoom, int xTile, int yTile) {
        this.zoom = zoom;
        this.xTile = xTile;
        this.yTile = yTile;
        positionHints();
        repaint();
    }

    public void setOffset(Point newOffset) {
        if (!this.offset.equals(newOffset)) {
            this.offset.setLocation(newOffset);
            positionHints();
            repaint();
        }
    }

    protected Point getPosition(LatLng coord) {
        Point ret = null;
        double tempX = BoundingBox.lon2Tile(coord.getLng(), zoom);
        double tempY = BoundingBox.lat2Tile(coord.getLat(), zoom);
        if ((tempX >= xTile - 1) && (tempX < xTile + maxXTiles) && (tempY >= yTile - 1) && (tempY < yTile + maxYTiles)) {
            int xOffs = (int) (Math.floor((tempX - xTile) * 256));
            int yOffs = (int) (Math.floor((tempY - yTile) * 256));
            ret = new Point(xOffs + offset.x, yOffs + offset.y);
        }
        return ret;
    }

    protected Point getAnyPosition(LatLng coord) {
        Point ret = null;
        double tempX = BoundingBox.lon2Tile(coord.getLng(), zoom);
        double tempY = BoundingBox.lat2Tile(coord.getLat(), zoom);
        int xOffs = (int) (Math.floor((tempX - xTile) * 256));
        int yOffs = (int) (Math.floor((tempY - yTile) * 256));
        ret = new Point(xOffs + offset.x, yOffs + offset.y);
        return ret;
    }

    protected void positionHints() {
        if (editCircle != null) {
            LatLng coord = editCircle.getCenterCoord();
            editCircle.setCenterPosition(getAnyPosition(coord));
            coord = editCircle.getRadiusCoord();
            editCircle.setRadiusPosition(getAnyPosition(coord));
        }
        for (Iterator<HintCircle> iterator = hintCircles.iterator(); iterator.hasNext(); ) {
            HintCircle circle = iterator.next();
            LatLng coord = circle.getCenterCoord();
            circle.setCenterPosition(getAnyPosition(coord));
            coord = circle.getRadiusCoord();
            circle.setRadiusPosition(getAnyPosition(coord));
        }
        for (Iterator<HintDialog> iterator = hints.iterator(); iterator.hasNext(); ) {
            HintDialog hintDialog = iterator.next();
            LatLng coord = hintDialog.getCoord();
            Point location = getPosition(coord);
            if (null == location) {
                hintDialog.setVisible(false);
            } else {
                hintDialog.setLocation(location.x, location.y);
                hintDialog.setVisible(true);
            }
        }
        for (Iterator<HintFlag> iterator = hintFlags.iterator(); iterator.hasNext(); ) {
            HintFlag hintFlag = iterator.next();
            LatLng coord = hintFlag.getCoord();
            Point location = getPosition(coord);
            if (null == location) {
                hintFlag.setVisible(false);
            } else {
                hintFlag.setPosition(location);
                hintFlag.setVisible(true);
            }
        }
    }

    public boolean mousePressed(MouseEvent e) {
        Point hintPoint = e.getPoint();
        int xTileRelOfZoom = ((hintPoint.x - offset.x) / 256);
        int yTileRelOfZoom = ((hintPoint.y - offset.y) / 256);
        int xInTile = (hintPoint.x - offset.x) % 256;
        int yInTile = (hintPoint.y - offset.y) % 256;
        double xAbsTile = (double) (xTile) + (double) (xTileRelOfZoom) + ((double) (xInTile)) / 256.0;
        double yAbsTile = (double) (yTile) + (double) (yTileRelOfZoom) + ((double) (yInTile)) / 256.0;
        double lat = BoundingBox.tile2lat(yAbsTile, zoom);
        double lon = BoundingBox.tile2lon(xAbsTile, zoom);
        boolean consumed = false;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (null == editCircle) {
                HintFlag hitFlag = null;
                for (Iterator<HintFlag> iterator = hintFlags.iterator(); (null == hitFlag) && iterator.hasNext(); ) {
                    HintFlag hintFlag = (HintFlag) iterator.next();
                    if (hintFlag.isInRect(e.getPoint())) {
                        hitFlag = hintFlag;
                    }
                }
                if (null != hitFlag) {
                    editCircle = new HintCircle();
                    editCircle.setCenterCoord(hitFlag.getCoord());
                    editCircle.setRadiusCoord(new LatLng(lat, lon));
                    positionHints();
                    repaint();
                    consumed = true;
                }
            } else {
                hintCircles.add(editCircle);
                editCircle = null;
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            HintCircle hitCircle = null;
            for (Iterator<HintCircle> iterator = hintCircles.iterator(); (null == hitCircle) && iterator.hasNext(); ) {
                HintCircle hintCircle = (HintCircle) iterator.next();
                if (hintCircle.isInRect(e.getPoint())) {
                    hitCircle = hintCircle;
                }
            }
            if (null == hitCircle) {
                HintDialog hintDialog = new HintDialog(new LatLng(lat, lon));
                hintDialog.setSize(hintDialog.getPreferredSize());
                add(hintDialog, null);
                hints.add(hintDialog);
                hintDialog.setListener(listener);
                positionHints();
                revalidate();
            } else {
                hintCircles.remove(hitCircle);
            }
            consumed = true;
        }
        return consumed;
    }

    public boolean mouseMoved(MouseEvent e) {
        boolean ret = false;
        if (editCircle == null) {
            ret = false;
        } else {
            Point hintPoint = e.getPoint();
            int xTileRelOfZoom = ((hintPoint.x - offset.x) / 256);
            int yTileRelOfZoom = ((hintPoint.y - offset.y) / 256);
            int xInTile = (hintPoint.x - offset.x) % 256;
            int yInTile = (hintPoint.y - offset.y) % 256;
            double xAbsTile = (double) (xTile) + (double) (xTileRelOfZoom) + ((double) (xInTile)) / 256.0;
            double yAbsTile = (double) (yTile) + (double) (yTileRelOfZoom) + ((double) (yInTile)) / 256.0;
            double lat = BoundingBox.tile2lat(yAbsTile, zoom);
            double lon = BoundingBox.tile2lon(xAbsTile, zoom);
            editCircle.setRadiusCoord(new LatLng(lat, lon));
            positionHints();
            repaint();
            ret = true;
        }
        return ret;
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (Iterator<HintFlag> iterator = hintFlags.iterator(); iterator.hasNext(); ) {
            HintFlag hintFlag = (HintFlag) iterator.next();
            if (hintFlag.isVisible()) {
                hintFlag.paint(g);
            }
        }
        for (Iterator<HintCircle> iterator = hintCircles.iterator(); iterator.hasNext(); ) {
            HintCircle hintCircle = iterator.next();
            hintCircle.paint(g);
        }
        if (null != editCircle) {
            editCircle.paint(g);
        }
    }

    protected void hintDialogActionPerformed(HintDialog dialog, boolean add) {
        remove(dialog);
        dialog.setVisible(false);
        dialog.setListener(null);
        repaint();
        if (add && (wayPointList != null)) {
            WayPointList.WayPoint wayPoint = new WayPointList.WayPoint();
            wayPoint.setCoord(dialog.getCoord());
            wayPoint.setName(dialog.getName());
            wayPoint.setDescription(dialog.getDescription());
            wayPointList.addWayPoint(wayPoint);
        }
    }

    protected void wayPointListChanged() {
        hintFlags.clear();
        for (Iterator<WayPointList.WayPoint> iterator = wayPointList.wayPointIterator(); iterator.hasNext(); ) {
            WayPointList.WayPoint wayPoint = iterator.next();
            HintFlag hintFlag = new HintFlag();
            hintFlag.setCoord(wayPoint.getCoord());
            hintFlag.setName(wayPoint.getName());
            hintFlags.add(hintFlag);
        }
        positionHints();
        repaint();
    }

    protected class HintDialogListener implements HintDialog.Listener {

        HintPanel adaptee;

        public HintDialogListener(HintPanel adaptee) {
            this.adaptee = adaptee;
        }

        @Override
        public void actionPerformed(HintDialog dialog, boolean add) {
            adaptee.hintDialogActionPerformed(dialog, add);
        }
    }

    /**
	 * @return the wayPointList
	 */
    public WayPointList getWayPointList() {
        return wayPointList;
    }

    /**
	 * @param wayPointList the wayPointList to set
	 */
    public void setWayPointList(WayPointList wayPointList) {
        if (null != wayPointList) {
            this.wayPointList = wayPointList;
            wayPointList.addListener(new WayPointList.WayPointListListener() {

                @Override
                public void listChanged() {
                    wayPointListChanged();
                }

                public void focusCoord(LatLng coord) {
                }
            });
        }
    }
}
