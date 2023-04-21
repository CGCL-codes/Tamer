package jp.locky.subway;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import jp.locky.subway.MapPanel;
import jp.locky.toolkit.PlaceEngineClient;
import jp.locky.util.WiFiData;

public class DisplayImage extends MapGUI {

    private static final long serialVersionUID = -5386681079971057387L;

    public static Image MapImage = null;

    static JPanel mapPanel;

    public static final JScrollPane scroll = new JScrollPane();

    static boolean FrameAddFlag = false;

    public static String ImgName = null;

    private static final Rectangle rect = new Rectangle();

    public static boolean flg = true;

    public static boolean Bflg = false;

    public static int startX;

    public static int startY;

    public static int endX;

    public static int endY;

    public static float largeness = 0;

    public static Point convPoint;

    public static Point startPoint;

    public static Point endPoint;

    public static int clickcount = 0;

    static int ff;

    public static WiFiData[] wifiData;

    public static String[] WData;

    public static float xp1 = -1, xp2 = -1;

    public static float yp1 = -1, yp2 = -1;

    public static float r_xp1 = -1, r_xp2 = -1, r_yp1 = -1, r_yp2 = -1;

    public static void displayImage(String ImageName, int magni) {
        if (ImageName == null) {
            return;
        }
        mapPanel = new MapPanel(ImageName, magni);
        if (magni == -1) {
            magni = ((MapPanel) mapPanel).calculatedScale;
            System.out.println("Now Scale is " + magni);
        }
        MagnifyScale = magni;
        ImgName = ImageName;
        if (flg == true) {
            moveScrollWindow();
        }
        scroll.getViewport().add(mapPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        if (!FrameAddFlag) {
            mainFrame.getContentPane().add(scroll, BorderLayout.CENTER);
            FrameAddFlag = true;
            mainFrame.setVisible(true);
        }
        LoggedLocation.ChangeScale();
        scroll.repaint();
    }

    public static synchronized void sleep(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
        }
    }

    public static void moveScrollWindow() {
        final Cursor cc = mapPanel.getCursor();
        final Cursor hc = new Cursor(Cursor.HAND_CURSOR);
        final JViewport vport = scroll.getViewport();
        convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
        vport.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(final MouseEvent e) {
                if (ff == MouseEvent.BUTTON1 && ButtonFlag == 1) {
                    Rectangle vr = vport.getViewRect();
                    int w = vr.width;
                    int h = vr.height;
                    int x = e.getX();
                    int y = e.getY();
                    Point pt = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                    int moveX = pt.x - x + startX;
                    int moveY = pt.y - y + startY;
                    rect.setRect(moveX, moveY, w, h);
                    mapPanel.scrollRectToVisible(rect);
                    startX = x;
                    startY = y;
                } else if (ff == MouseEvent.BUTTON3 || ButtonFlag == 2 || ButtonFlag == 5) {
                    Bflg = true;
                    convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                    endPoint = SwingUtilities.convertPoint(vport, e.getX(), e.getY(), mapPanel);
                    scroll.repaint();
                }
            }
        });
        vport.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                ff = e.getButton();
                startX = e.getX();
                startY = e.getY();
                startPoint = SwingUtilities.convertPoint(vport, startX, startY, mapPanel);
                mapPanel.setCursor(hc);
            }

            public void mouseReleased(MouseEvent e) {
                if (ButtonFlag == 1 && ff == MouseEvent.BUTTON1) {
                    mapPanel.setCursor(cc);
                    scroll.repaint();
                } else if (ButtonFlag == 1 && ff == MouseEvent.BUTTON2) {
                    float clickedX = e.getX();
                    float clickedY = e.getY();
                    convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                    int locateX = (int) (((convPoint.x + clickedX) * 100) / MagnifyScale);
                    int locateY = (int) (((convPoint.y + clickedY) * 100) / MagnifyScale);
                    TagBox.jtf.setText("" + locateX + "," + locateY);
                } else if (ff == MouseEvent.BUTTON3 || ButtonFlag == 2) {
                    mapPanel.setCursor(cc);
                    endX = e.getX();
                    endY = e.getY();
                    scroll.repaint();
                    Bflg = false;
                    double magSizeX = scroll.getWidth() * MagnifyScale;
                    double magSizeY = scroll.getHeight() * MagnifyScale;
                    double fullSizeX = 100 * Math.abs(endX - startX);
                    double fullSizeY = 100 * Math.abs(endY - startY);
                    float RateX = (float) (magSizeX / fullSizeX);
                    float RateY = (float) (magSizeY / fullSizeY);
                    float xpoint;
                    float ypoint;
                    if (startX <= endX) {
                        xpoint = convPoint.x + startX;
                    } else {
                        xpoint = convPoint.x + endX;
                    }
                    if (startY <= endY) {
                        ypoint = convPoint.y + startY;
                    } else {
                        ypoint = convPoint.y + endY;
                    }
                    float noModifyX = xpoint * 100 / MagnifyScale;
                    float noModifyY = ypoint * 100 / MagnifyScale;
                    if (RateX >= RateY) {
                        largeness = RateY * 100;
                    } else {
                        largeness = RateX * 100;
                    }
                    int mscale = (int) largeness;
                    if (mscale <= 3000 && mscale >= 10) {
                        displayImage(ImgName, mscale);
                        if (MapScaling.jcb.getItemAt(8) != null) {
                            MapScaling.jcb.removeItemAt(8);
                        }
                        MapScaling.jcb.addItem(mscale);
                        MapScaling.jcb.setSelectedIndex(8);
                    } else {
                        System.err.println("�g��͈͂����������܂�");
                        System.err.println("�I���g��͈͂������Ƒ傫������ĉ������B");
                        return;
                    }
                    double realX = noModifyX * largeness / 100;
                    double realY = noModifyY * largeness / 100;
                    rect.setRect((int) realX, (int) realY, scroll.getWidth(), scroll.getHeight());
                    mapPanel.scrollRectToVisible(rect);
                    LoggedLocation.ChangeScale();
                    return;
                }
                if (FrameMenu.scaleFlag == true && clickcount < 2) {
                    if (clickcount == 0) {
                        FrameMenu.firstPosi.setLocation(e.getX(), e.getY());
                    }
                    if (clickcount == 1) {
                        FrameMenu.secondPosi.setLocation(e.getX(), e.getY());
                    }
                    clickcount++;
                    FrameMenu.crewin.show();
                    return;
                } else if (ButtonFlag == 3) {
                    if (FrameMenu.number < 0) {
                        FrameMenu.number = 0;
                    }
                    PlaceEngineClient pec_ = SubwayStumbler.getPec();
                    pec_.getMeasurement();
                    wifiData = pec_.getWiFiData();
                    int number = pec_.numberOfReadings();
                    WData = new String[number];
                    float clickedX = e.getX();
                    float clickedY = e.getY();
                    convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                    float locateX = (((convPoint.x + clickedX) * 100) / MagnifyScale);
                    float locateY = (((convPoint.y + clickedY) * 100) / MagnifyScale);
                    long ms = System.currentTimeMillis();
                    for (int i = 0; i < number; i++) {
                        WData[i] = "BSSID=" + wifiData[i].BSSID + "|RSSI=" + wifiData[i].RSSI + "|SSID=" + wifiData[i].SSID + "|location_x=" + locateX + "|location_y=" + locateY + "|imgName=" + ImgName + "|Time=" + Long.toString(ms) + "|Tag=" + TagBox.tagname;
                        System.out.println(WData[i]);
                        SubwayWiFiData.InputData(WData[i]);
                        FrameMenu.WdataBSSID[FrameMenu.number] = wifiData[i].BSSID;
                        FrameMenu.WdataRSSI[FrameMenu.number] = Integer.toString(wifiData[i].RSSI);
                        FrameMenu.WdataSSID[FrameMenu.number] = wifiData[i].SSID;
                        FrameMenu.WdataX[FrameMenu.number] = locateX;
                        FrameMenu.WdataY[FrameMenu.number] = locateY;
                        FrameMenu.WdataimgName[FrameMenu.number] = ImgName;
                        FrameMenu.WdataTime[FrameMenu.number] = Long.toString(ms);
                        FrameMenu.number++;
                    }
                    FrameMenu.TimeID[FrameMenu.idcount] = Long.toString(ms);
                    FrameMenu.idcount++;
                    OutputLog.main(WData, ImgName);
                    LoggedLocation.DisplayLocation(locateX, locateY, MagnifyScale, number);
                    WiFidataPanel.WiFiList();
                    if (WiFidataPanel.wifiListFlag == true) {
                        WiFidataPanel.getWiFi();
                    }
                } else if (ButtonFlag == 4) {
                    int endErase = 0;
                    int n = 0;
                    if (LoggedLocation.pointCounter != 0 && WiFidataPanel.wifiListFlag == false) {
                        int i = 0;
                        float curX = 1000;
                        float curY = 1000;
                        int erasestart = 0;
                        convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                        float x = (e.getX() + convPoint.x) / ((float) MagnifyScale / (float) 100);
                        float y = (e.getY() + convPoint.y) / ((float) MagnifyScale / (float) 100);
                        Double eraseDistance = LoggedLocation.point[i].distance((double) curX, (double) curY);
                        int eraseCounter = 0;
                        while (LoggedLocation.point[i] != null) {
                            Double subdist = LoggedLocation.point[i].distance((double) x, (double) y);
                            if (eraseDistance > subdist) {
                                eraseDistance = subdist;
                                eraseCounter = i;
                            }
                            i++;
                        }
                        int roop = eraseCounter;
                        int deleteNumber = LoggedLocation.wifiCounter[eraseCounter];
                        while (roop != LoggedLocation.pointCounter - 1 && roop <= 998) {
                            if (eraseCounter == LoggedLocation.pointCounter) {
                                break;
                            } else if (roop == 1000) {
                                break;
                            } else if (LoggedLocation.point[roop + 1] != null) {
                                LoggedLocation.point[roop].setLocation(LoggedLocation.point[roop + 1].x, LoggedLocation.point[roop + 1].y);
                                LoggedLocation.scaledPoint[roop].setLocation(LoggedLocation.scaledPoint[roop + 1].x, LoggedLocation.scaledPoint[roop + 1].y);
                                LoggedLocation.wifiCounter[roop] = LoggedLocation.wifiCounter[roop + 1];
                            }
                            roop++;
                        }
                        LoggedLocation.pointCounter--;
                        if (roop <= 998) {
                            LoggedLocation.point[roop + 1] = null;
                            LoggedLocation.scaledPoint[roop + 1] = null;
                            LoggedLocation.wifiCounter[roop + 1] = 0;
                        }
                        for (int j = 0; SubwayWiFiData.Data[j].BSSID != null; j++) {
                            if (SubwayWiFiData.Data[j].TIME.equals(FrameMenu.TimeID[eraseCounter])) {
                                FrameMenu.idcount--;
                                erasestart = j;
                                break;
                            }
                        }
                        for (n = eraseCounter; FrameMenu.TimeID[n + 1] != null; n++) {
                            FrameMenu.TimeID[n] = FrameMenu.TimeID[n + 1];
                        }
                        FrameMenu.TimeID[n] = null;
                        FrameMenu.number -= deleteNumber;
                        SubwayWiFiData.icount -= deleteNumber;
                        for (int u = erasestart; u < SubwayWiFiData.icount; u++) {
                            SubwayWiFiData.Data[u].BSSID = SubwayWiFiData.Data[u + deleteNumber].BSSID;
                            SubwayWiFiData.Data[u].RSSI = SubwayWiFiData.Data[u + deleteNumber].RSSI;
                            SubwayWiFiData.Data[u].SSID = SubwayWiFiData.Data[u + deleteNumber].SSID;
                            SubwayWiFiData.Data[u].LOCATION_X = SubwayWiFiData.Data[u + deleteNumber].LOCATION_X;
                            SubwayWiFiData.Data[u].LOCATION_Y = SubwayWiFiData.Data[u + deleteNumber].LOCATION_Y;
                            SubwayWiFiData.Data[u].IMGNAME = SubwayWiFiData.Data[u + deleteNumber].IMGNAME;
                            SubwayWiFiData.Data[u].TIME = SubwayWiFiData.Data[u + deleteNumber].TIME;
                            endErase = u;
                        }
                        if (endErase != 0) {
                            System.out.println("start erase point :" + erasestart);
                            for (int p = endErase + 1; p <= endErase + deleteNumber; p++) {
                                if (eraseCounter == 0 && erasestart == 0 && FrameMenu.WdataTime[1] == null) {
                                    SubwayWiFiData.Data[0].BSSID = null;
                                    SubwayWiFiData.Data[0].RSSI = -999;
                                    SubwayWiFiData.Data[0].SSID = null;
                                    SubwayWiFiData.Data[0].LOCATION_X = -999;
                                    SubwayWiFiData.Data[0].LOCATION_Y = -999;
                                    SubwayWiFiData.Data[0].IMGNAME = null;
                                    SubwayWiFiData.Data[0].TIME = null;
                                }
                                SubwayWiFiData.Data[p].BSSID = null;
                                SubwayWiFiData.Data[p].RSSI = -999;
                                SubwayWiFiData.Data[p].SSID = null;
                                SubwayWiFiData.Data[p].LOCATION_X = -999;
                                SubwayWiFiData.Data[p].LOCATION_Y = -999;
                                SubwayWiFiData.Data[p].IMGNAME = null;
                                SubwayWiFiData.Data[p].TIME = null;
                            }
                        } else if (endErase == 0) {
                            for (int q = erasestart; q < (erasestart + deleteNumber); q++) {
                                SubwayWiFiData.Data[q].BSSID = null;
                                SubwayWiFiData.Data[q].RSSI = -999;
                                SubwayWiFiData.Data[q].SSID = null;
                                SubwayWiFiData.Data[q].LOCATION_X = -999;
                                SubwayWiFiData.Data[q].LOCATION_Y = -999;
                                SubwayWiFiData.Data[q].IMGNAME = null;
                                SubwayWiFiData.Data[q].TIME = null;
                            }
                        }
                        scroll.repaint();
                    } else if (LoggedLocation.pointCounter != 0 && WiFidataPanel.wifiListFlag == true) {
                        int i = 0;
                        float curX = 1000;
                        float curY = 1000;
                        int erasestart = 0;
                        convPoint = SwingUtilities.convertPoint(vport, 0, 0, mapPanel);
                        float x = (e.getX() + convPoint.x) / ((float) MagnifyScale / (float) 100);
                        float y = (e.getY() + convPoint.y) / ((float) MagnifyScale / (float) 100);
                        Double eraseDistance = LoggedLocation.point[i].distance((double) curX, (double) curY);
                        int eraseCounter = 0;
                        while (LoggedLocation.point[i] != null) {
                            Double subdist = LoggedLocation.point[i].distance((double) x, (double) y);
                            if (eraseDistance > subdist) {
                                eraseDistance = subdist;
                                eraseCounter = i;
                            }
                            i++;
                        }
                        LoggedLocation.wifiCounter[eraseCounter] -= 1;
                        for (int j = 0; SubwayWiFiData.Data[j].BSSID != null; j++) {
                            if (SubwayWiFiData.Data[j].TIME.equals(FrameMenu.TimeID[eraseCounter])) {
                                if (WiFidataPanel.BSSID.equals(SubwayWiFiData.Data[j].BSSID)) {
                                    erasestart = j;
                                    break;
                                }
                            }
                        }
                        FrameMenu.number -= 1;
                        SubwayWiFiData.icount -= 1;
                        for (int u = erasestart; u < SubwayWiFiData.icount; u++) {
                            SubwayWiFiData.Data[u].BSSID = SubwayWiFiData.Data[u + 1].BSSID;
                            SubwayWiFiData.Data[u].RSSI = SubwayWiFiData.Data[u + 1].RSSI;
                            SubwayWiFiData.Data[u].SSID = SubwayWiFiData.Data[u + 1].SSID;
                            SubwayWiFiData.Data[u].LOCATION_X = SubwayWiFiData.Data[u + 1].LOCATION_X;
                            SubwayWiFiData.Data[u].LOCATION_Y = SubwayWiFiData.Data[u + 1].LOCATION_Y;
                            SubwayWiFiData.Data[u].IMGNAME = SubwayWiFiData.Data[u + 1].IMGNAME;
                            SubwayWiFiData.Data[u].TIME = SubwayWiFiData.Data[u + 1].TIME;
                            endErase = u;
                        }
                        if (endErase != 0) {
                            if (eraseCounter == 0 && erasestart == 0 && FrameMenu.WdataTime[1] == null) {
                                SubwayWiFiData.Data[0].BSSID = null;
                                SubwayWiFiData.Data[0].RSSI = -999;
                                SubwayWiFiData.Data[0].SSID = null;
                                SubwayWiFiData.Data[0].LOCATION_X = -999;
                                SubwayWiFiData.Data[0].LOCATION_Y = -999;
                                SubwayWiFiData.Data[0].IMGNAME = null;
                                SubwayWiFiData.Data[0].TIME = null;
                            }
                            SubwayWiFiData.Data[endErase + 1].BSSID = null;
                            SubwayWiFiData.Data[endErase + 1].RSSI = -999;
                            SubwayWiFiData.Data[endErase + 1].SSID = null;
                            SubwayWiFiData.Data[endErase + 1].LOCATION_X = -999;
                            SubwayWiFiData.Data[endErase + 1].LOCATION_Y = -999;
                            SubwayWiFiData.Data[endErase + 1].IMGNAME = null;
                            SubwayWiFiData.Data[endErase + 1].TIME = null;
                        } else if (endErase == 0) {
                            SubwayWiFiData.Data[0].BSSID = null;
                            SubwayWiFiData.Data[0].RSSI = -999;
                            SubwayWiFiData.Data[0].SSID = null;
                            SubwayWiFiData.Data[0].LOCATION_X = -999;
                            SubwayWiFiData.Data[0].LOCATION_Y = -999;
                            SubwayWiFiData.Data[0].IMGNAME = null;
                            SubwayWiFiData.Data[0].TIME = null;
                        }
                        WiFidataPanel.WiFiList();
                        WiFidataPanel.getWiFi();
                    }
                } else if (ButtonFlag == 5) {
                    mapPanel.setCursor(cc);
                    endX = e.getX();
                    endY = e.getY();
                    scroll.repaint();
                    Bflg = false;
                    float xpoint;
                    float ypoint;
                    if (startX <= endX) {
                        r_xp1 = (convPoint.x + startX) * 100 / MagnifyScale;
                        r_xp2 = (convPoint.x + endX) * 100 / MagnifyScale;
                        if (Math.abs(startX - endX) <= 5) {
                            System.err.println("�I��͈͂����������ł�");
                        } else {
                            xpoint = convPoint.x + startX;
                            xp1 = xpoint;
                            xp2 = convPoint.x + endX;
                        }
                    } else {
                        r_xp1 = (convPoint.x + endX) * 100 / MagnifyScale;
                        r_xp2 = (convPoint.x + startX) * 100 / MagnifyScale;
                        if (Math.abs(startX - endX) <= 5) {
                            System.err.println("�I��͈͂����������ł�");
                        } else {
                            xpoint = convPoint.x + endX;
                            xp1 = xpoint;
                            xp2 = convPoint.x + startX;
                        }
                    }
                    if (startY <= endY) {
                        r_yp1 = (convPoint.y + startY) * 100 / MagnifyScale;
                        r_yp2 = (convPoint.y + endY) * 100 / MagnifyScale;
                        ypoint = convPoint.y + startY;
                        yp1 = ypoint;
                        yp2 = convPoint.y + endY;
                    } else {
                        r_yp1 = (convPoint.y + endY) * 100 / MagnifyScale;
                        r_yp2 = (convPoint.y + startY) * 100 / MagnifyScale;
                        ypoint = convPoint.y + endY;
                        yp1 = ypoint;
                        yp2 = convPoint.y + startY;
                    }
                    scroll.repaint();
                    return;
                }
            }

            public void mouseClicked(MouseEvent e) {
            }
        });
        flg = false;
    }
}
