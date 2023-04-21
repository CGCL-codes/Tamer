package org.grailrtls.gui.display;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.grailrtls.gui.event.ConnectionEvent;
import org.grailrtls.gui.event.FingerprintEvent;
import org.grailrtls.gui.event.HubConnectionEvent;
import org.grailrtls.gui.event.ResultEvent;
import org.grailrtls.gui.event.ServerEvent;
import org.grailrtls.gui.event.ServerListener;
import org.grailrtls.gui.network.*;
import org.grailrtls.server.EventManager;
import org.grailrtls.server.MACAddress;

public class LocationMapPanel extends JPanel implements ServerListener {

    /**
	 * Double[] is a 4-tuple of x,y,xPrime,yPrime.
	 */
    private Map<MACAddress, List<PositionInfo>> transmitterLocations = new HashMap<MACAddress, List<PositionInfo>>();

    private List<MACAddress> displayedDevices = Collections.synchronizedList(new ArrayList<MACAddress>());

    private Map<MACAddress, Color> transmitterDisplayColor = new HashMap<MACAddress, Color>();

    private volatile RegionInfo region = null;

    private final ServerInfo serverInfo;

    private volatile int maxLocations = 1;

    private final int maxPossibleLocations = 100;

    public volatile boolean drawConnectors = true;

    public volatile boolean drawPointsOnly = false;

    public volatile boolean drawMAC = false;

    public volatile boolean drawRSSILines = false;

    private final Font addressFont = new Font("Serif", Font.PLAIN, 14);

    private final Map<TransmitterInfo, Map<LandmarkInfo, RSSILine>> rssiLines = Collections.synchronizedMap(new HashMap<TransmitterInfo, Map<LandmarkInfo, RSSILine>>());

    public LocationMapPanel(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        this.setBackground(Color.WHITE);
    }

    public void setRegion(RegionInfo region) {
        this.region = region;
        this.clear();
        this.repaint();
    }

    public void addTransmitterLocation(PositionInfo positionInfo) {
        if (this.region == null) return;
        if (positionInfo.transmitterInfo == null) throw new NullPointerException("Cannot draw a null transmitter.");
        synchronized (this.transmitterLocations) {
            List<PositionInfo> locations = this.transmitterLocations.get(positionInfo.transmitterInfo.address);
            if (locations == null) {
                locations = new ArrayList<PositionInfo>();
            }
            locations.add(0, positionInfo);
            this.transmitterLocations.put(positionInfo.transmitterInfo.address, locations);
            while (locations.size() > this.maxPossibleLocations) {
                locations.remove(locations.size() - 1);
            }
        }
        TransmitterInfo transmitter = positionInfo.transmitterInfo;
        synchronized (this.rssiLines) {
            Map<LandmarkInfo, RSSILine> lines = this.rssiLines.get(transmitter);
            if (lines == null) {
                lines = new HashMap<LandmarkInfo, RSSILine>();
                synchronized (this.serverInfo.hubInfo) {
                    for (HubInfo hub : this.serverInfo.hubInfo.values()) {
                        synchronized (hub.landmarks) {
                            for (LandmarkInfo landmark : hub.landmarks) {
                                if (landmark.physicalLayer == transmitter.phy) {
                                    lines.put(landmark, new RSSILine(this.region, transmitter, landmark, 0f));
                                }
                            }
                        }
                    }
                }
                this.rssiLines.put(transmitter, lines);
            } else {
                for (RSSILine line : lines.values()) {
                    line.updatePosition();
                }
            }
        }
        this.repaint();
    }

    public void clear() {
        this.transmitterLocations.clear();
        this.repaint();
    }

    public void setMaxLocations(int maxLocations) {
        if (maxLocations > 0) {
            this.maxLocations = maxLocations;
            this.repaint();
        }
    }

    public int getMaxLocations() {
        return this.maxLocations;
    }

    public void computeColors() {
        this.transmitterDisplayColor.clear();
        int index = 0;
        synchronized (this.transmitterLocations) {
            for (MACAddress address : this.transmitterLocations.keySet()) {
                float adjusted = (float) 0.66 * index++ / this.transmitterLocations.keySet().size();
                float hue = adjusted;
                float sat = 0.95f;
                float bright = 0.95f;
                Color c = new Color(Color.HSBtoRGB(hue, sat, bright));
                this.transmitterDisplayColor.put(address, c);
            }
        }
    }

    public void setDisplayedDevices(List<MACAddress> devices) {
        this.displayedDevices.clear();
        this.displayedDevices.addAll(devices);
        this.repaint();
    }

    private synchronized void updateFingerprintInfo(FingerprintInfo fingerprintInfo) {
        TransmitterInfo transmitter = fingerprintInfo.transmitterInfo;
        Map<LandmarkInfo, RSSILine> lines = this.rssiLines.get(transmitter);
        if (lines == null) {
            lines = new HashMap<LandmarkInfo, RSSILine>();
            this.rssiLines.put(transmitter, lines);
        }
        for (LandmarkInfo landmark : fingerprintInfo.getLandmarkInfo()) {
            RSSILine line = lines.get(landmark);
            if (line == null) {
                line = new RSSILine(this.region, transmitter, landmark, fingerprintInfo.getRSSIValue(landmark));
                lines.put(landmark, line);
            } else line.setVariance(fingerprintInfo.getRSSIValue(landmark));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (this.region == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension dimension = this.getSize();
        int panelHeight = dimension.height;
        int panelWidth = dimension.width;
        if (this.region.mapImage != null) {
            g2.drawImage(this.region.mapImage, 0, 0, panelWidth, panelHeight, 0, 0, this.region.mapImage.getWidth(), this.region.mapImage.getHeight(), null);
        }
        float scaleX = panelWidth / (this.region.xMax - this.region.xMin);
        float scaleY = panelHeight / (this.region.yMax - this.region.yMin);
        g2.setBackground(Color.WHITE);
        g.setFont(this.addressFont);
        if (this.drawRSSILines) {
            synchronized (this.rssiLines) {
                for (TransmitterInfo transmitter : this.rssiLines.keySet()) {
                    if (this.displayedDevices.size() != 0 && !this.displayedDevices.contains(MACAddress.getAll()) && !this.displayedDevices.contains(transmitter.address)) continue;
                    Map<LandmarkInfo, RSSILine> txerLines = this.rssiLines.get(transmitter);
                    if (txerLines == null) continue;
                    for (RSSILine rssiLine : txerLines.values()) {
                        if (!(rssiLine.rssi <= 0.0)) continue;
                        Line2D line = rssiLine.getLine();
                        Line2D.Double drawLine = new Line2D.Double(line.getX1() * scaleX, panelHeight - line.getY1() * scaleY, line.getX2() * scaleX, panelHeight - line.getY2() * scaleY);
                        g2.setColor(Color.BLACK);
                        float ratio = 1f - ((rssiLine.rssi + 30f) / -70f);
                        if (!(ratio >= 0.25f)) ratio = 0.25f;
                        if (ratio > 1f) ratio = 1f;
                        g2.setStroke(new BasicStroke(ratio * 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                        g2.draw(drawLine);
                        ratio *= 0.95f;
                        if (!(ratio >= 0.23f)) ratio = 0.23f;
                        if (ratio > 1f) ratio = 1f;
                        g2.setStroke(new BasicStroke(ratio * 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                        ratio = ((rssiLine.rssi + 30f) / -70f);
                        if (!(ratio >= 0.0f)) ratio = 0.0f;
                        if (ratio > 1.0f) ratio = 1.0f;
                        g2.setColor(Color.getHSBColor(0.33f - ratio * 0.33f, 1f, 1f));
                        g2.draw(drawLine);
                        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                    }
                }
            }
        }
        synchronized (this.serverInfo.hubInfo) {
            for (HubInfo hubInfo : this.serverInfo.hubInfo.values()) {
                synchronized (hubInfo.landmarks) {
                    for (LandmarkInfo landmarkInfo : hubInfo.landmarks) {
                        Point2D landmarkPosition = landmarkInfo.locations.get(this.region);
                        if (landmarkPosition == null) continue;
                        float xPosition = (float) landmarkPosition.getX() * scaleX;
                        float yPosition = panelHeight - (float) landmarkPosition.getY() * scaleY;
                        if (landmarkInfo.iconConnected == null || landmarkInfo.iconDisconnected == null) {
                            xPosition -= 5;
                            yPosition += 5;
                            Ellipse2D.Float ellipse = new Ellipse2D.Float(xPosition, yPosition, 10, 10);
                            if (landmarkInfo.hubInfo.connected) g2.setColor(Color.GREEN); else g2.setColor(Color.RED);
                            g2.fill(ellipse);
                        } else {
                            BufferedImage icon = landmarkInfo.iconDisconnected;
                            if (landmarkInfo.hubInfo.connected) icon = landmarkInfo.iconConnected;
                            int iconWidth = icon.getWidth();
                            int iconHeight = icon.getHeight();
                            g2.drawImage(icon, (int) (xPosition - (iconWidth / 2f)), (int) (yPosition - (iconHeight / 2f)), (int) (xPosition + (iconWidth / 2f)), (int) (yPosition + (iconHeight / 2f)), 0, 0, icon.getWidth(), icon.getHeight(), null);
                        }
                    }
                }
            }
        }
        synchronized (this.transmitterLocations) {
            for (MACAddress address : this.transmitterLocations.keySet()) {
                if (this.displayedDevices.size() != 0 && !this.displayedDevices.contains(MACAddress.getAll()) && !this.displayedDevices.contains(address)) continue;
                float alpha = this.drawPointsOnly ? 1.0f : 0.5f;
                List<PositionInfo> locations = this.transmitterLocations.get(address);
                int index = 0;
                float lastCenterX = -1f;
                float lastCenterY = -1f;
                int drawnLocations = 0;
                for (PositionInfo location : locations) {
                    if (drawnLocations >= this.maxLocations) break;
                    float centerX = location.x;
                    float centerY = location.y;
                    float diameterX = location.xPrime * 5;
                    float diameterY = location.yPrime * 5;
                    String label = String.format("%s (%1.1f, %1.1f)", address.toString(), centerX, centerY);
                    float xPosition = centerX * scaleX - 4f;
                    float yPosition = panelHeight - centerY * scaleY - 4f;
                    float width = 8;
                    float height = 8;
                    if (!this.drawPointsOnly) {
                        xPosition = (centerX - diameterX / 2) * scaleX;
                        yPosition = panelHeight - (centerY + diameterY / 2) * scaleY;
                        width = diameterX * scaleX;
                        height = diameterY * scaleY;
                    }
                    Ellipse2D.Float ellipse = new Ellipse2D.Float(xPosition, yPosition, width, height);
                    Color c = this.transmitterDisplayColor.get(address);
                    if (c == null) {
                        this.computeColors();
                        c = this.transmitterDisplayColor.get(address);
                    }
                    g2.setColor(c);
                    Composite origComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2.fill(ellipse);
                    BufferedImage icon = null;
                    URL iconURL = this.getClass().getResource("/non-java/images/txer.png");
                    try {
                        icon = ImageIO.read(iconURL);
                    } catch (Exception e) {
                    }
                    if (icon != null) {
                        int iconWidth = icon.getWidth();
                        int iconHeight = icon.getHeight();
                        xPosition = centerX * scaleX;
                        yPosition = panelHeight - centerY * scaleY;
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                        g2.drawImage(icon, (int) (xPosition - (iconWidth / 2f)), (int) (yPosition - (iconHeight / 2f)), (int) (xPosition + (iconWidth / 2f)), (int) (yPosition + (iconHeight / 2f)), 0, 0, icon.getWidth(), icon.getHeight(), null);
                    }
                    if (index == 0) {
                        FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
                        if (this.drawMAC) {
                            Rectangle2D labelBounds = this.addressFont.getStringBounds(label, frc);
                            int labelX = (int) xPosition;
                            if (labelX < 0) labelX = 0;
                            if (labelX + labelBounds.getWidth() > panelWidth) labelX = (int) (panelWidth - labelBounds.getWidth());
                            int labelY = (int) (yPosition - labelBounds.getHeight());
                            if (labelY < 0) labelY = (int) labelBounds.getHeight();
                            if (labelY > panelHeight) labelY = panelHeight;
                            g2.setColor(Color.WHITE);
                            g2.fillRect((int) xPosition, (int) yPosition - (int) labelBounds.getHeight(), (int) labelBounds.getWidth(), (int) labelBounds.getHeight());
                            g2.setColor(Color.RED);
                            g2.drawRect((int) xPosition, (int) yPosition - (int) labelBounds.getHeight(), (int) labelBounds.getWidth(), (int) labelBounds.getHeight());
                            g2.setComposite(origComposite);
                            g.setColor(Color.BLACK);
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                            g.drawString(label, (int) xPosition, (int) yPosition - 2);
                        }
                        if (location.xGround != Float.NaN) {
                            float gx = (location.xGround) * scaleX;
                            float gy = panelHeight - (location.yGround) * scaleY;
                            GeneralPath path = new GeneralPath();
                            Stroke s = g2.getStroke();
                            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                            g2.setColor(Color.GREEN);
                            path.moveTo(centerX * scaleX, panelHeight - (centerY * scaleY));
                            path.lineTo(gx, gy);
                            g2.draw(path);
                            g2.setStroke(s);
                        }
                    } else {
                        if (this.drawConnectors) {
                            Stroke s = g2.getStroke();
                            GeneralPath path = new GeneralPath();
                            g2.setColor(Color.GREEN);
                            path.moveTo(lastCenterX, lastCenterY);
                            path.lineTo(location.x * scaleX, panelHeight - location.y * scaleY);
                            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                            g2.draw(path);
                            g2.setStroke(s);
                        }
                    }
                    ++drawnLocations;
                    ++index;
                    alpha *= 1 - (1 / this.maxLocations);
                    lastCenterX = location.x * scaleX;
                    lastCenterY = panelHeight - location.y * scaleY;
                }
            }
        }
    }

    public void serverEventPerformed(ServerEvent arg0) {
        if (arg0 instanceof ConnectionEvent) {
            ConnectionEvent event = (ConnectionEvent) arg0;
            if (!event.isConnected()) {
                this.transmitterLocations.clear();
                this.repaint();
            }
        } else if (arg0 instanceof HubConnectionEvent) {
            this.repaint();
        } else if (arg0 instanceof FingerprintEvent) {
            FingerprintEvent event = (FingerprintEvent) arg0;
            if (event.fingerprintInfo.fingerprint_type == EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN) {
                this.updateFingerprintInfo(event.fingerprintInfo);
                if (this.drawRSSILines) this.repaint();
            }
        }
    }
}
