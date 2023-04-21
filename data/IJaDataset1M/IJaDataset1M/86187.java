package geotagger;

import com.nutiteq.MapComponent;
import com.nutiteq.components.OnMapElement;
import com.nutiteq.components.Place;
import com.nutiteq.components.PlaceInfo;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.kml.KmlUrlReader;
import com.nutiteq.listeners.MapListener;
import com.nutiteq.listeners.OnMapElementListener;
import com.nutiteq.location.LocationSource;
import com.nutiteq.maps.MicrosoftMap;
import com.nutiteq.maps.OpenStreetMap;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.ImageWrapper;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import java.rmi.RemoteException;
import javax.microedition.lcdui.Font;
import tagnavigatorwsservice.TagNavigatorWSWSService_Stub;

public class MapElement extends Component implements MapListener, OnMapElementListener {

    private final MapComponent map;

    private Image buffer;

    private MapForm mapForm;

    private javax.microedition.lcdui.Graphics bufferGraphics;

    private YahooMap ym = new YahooMap();

    private boolean painted;

    private LocationSource dataSource;

    private int overlay = 0;

    private int mapType = 0;

    public MapElement(final MapComponent map, LocationSource datasource, MapForm mapForm) {
        this.map = map;
        this.mapForm = mapForm;
        this.dataSource = datasource;
    }

    public void paint(final Graphics g) {
        if (!painted) {
            map.resize(getWidth(), getHeight());
            buffer = Image.createImage(getWidth(), getHeight());
            bufferGraphics = new ImageWrapper(buffer).getGraphics();
            map.setMapListener(this);
            map.setOnMapElementListener(this);
        }
        map.paint(bufferGraphics);
        bufferGraphics.setColor(0xFFFF0000);
        bufferGraphics.setClip(0, 0, getWidth(), getHeight());
        bufferGraphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        if (mapForm.getmRoute() != null) {
            bufferGraphics.drawString(mapForm.getmRoute().getRouteSummary().getDistance().getValue() + "" + mapForm.getmRoute().getRouteSummary().getDistance().getUnitOfMeasure(), 2, 1, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
        }
        g.drawImage(buffer, 0, 0);
        painted = true;
    }

    public void mapClicked(final WgsPoint p) {
    }

    public void mapMoved() {
    }

    public void needRepaint(final boolean mapIsComplete) {
        repaint();
    }

    public void keyPressed(final int keyCode) {
        switch(keyCode) {
            case 48:
                break;
            case 49:
                if (mapType == 0) {
                    map.setMap(OpenStreetMap.MAPNIK);
                    mapType = 1;
                } else if (mapType == 1) {
                    map.setMap(new CycleMap());
                    mapType = 2;
                } else if (mapType == 2) {
                    map.setMap(MicrosoftMap.LIVE_MAP);
                    mapType = 3;
                } else if (mapType == 3) {
                    map.setMap(ym);
                    mapType = 0;
                }
                break;
            case 51:
                break;
            case 53:
                break;
            case 55:
                if (TagNavigatorMain.instance.getDataSource() instanceof BTLocationSource) {
                    ((BTLocationSource) TagNavigatorMain.instance.getDataSource()).updateLocation();
                }
                break;
            case 57:
                if (mapForm.getInstructionPlaces() != null) {
                    map.addPlaces(mapForm.getInstructionPlaces());
                }
                break;
            default:
        }
        map.keyPressed(keyCode);
    }

    public void keyReleased(final int keyCode) {
        map.keyReleased(keyCode);
    }

    public void keyRepeated(final int keyCode) {
        map.keyRepeated(keyCode);
    }

    public void pointerDragged(int x, int y) {
        map.pointerDragged(x, y);
    }

    public void pointerPressed(int x, int y) {
        map.pointerPressed(x, y);
    }

    public void pointerReleased(int x, int y) {
        System.out.println("pointerReleased " + x + " " + y);
        System.out.println("get " + getX() + " " + getY());
        System.out.println("getAbsolute " + this.getAbsoluteX() + " " + getAbsoluteY());
        map.pointerReleased(x - getAbsoluteX(), y - getAbsoluteY());
    }

    public void elementClicked(OnMapElement element) {
        final Place p = (Place) element;
        TagNavigatorMain.instance.setLat(p.getWgs().getLat());
        TagNavigatorMain.instance.setLon(p.getWgs().getLon());
        PlaceInfo pi = map.getAdditionalInfo(p);
        String desc = pi != null ? pi.getDescription() : null;
        if (desc != null) {
            TagNavigatorMain.instance.showException(new Exception(desc));
        } else {
            Form lf = new Form();
            final TextArea tf = new TextArea("", 3, 20);
            tf.setEditable(true);
            String desc2 = (String) TagNavigatorMain.instance.getDescs().get(new Integer(p.getId()));
            if (desc2 != null) {
                tf.setText(desc2);
            }
            lf.addComponent(tf);
            final Form tmp = mapForm;
            Command ok = new Command("Save changes") {

                public void actionPerformed(ActionEvent arg0) {
                    Thread t = new Thread(new Runnable() {

                        public void run() {
                            TagNavigatorWSWSService_Stub ws = new TagNavigatorWSWSService_Stub();
                            try {
                                ws.setInfo(tf.getText(), p.getId());
                                TagNavigatorMain.instance.showException(new Exception("" + "Info modified successfully"));
                            } catch (RemoteException ex) {
                                TagNavigatorMain.instance.showException(ex);
                            }
                        }
                    });
                    t.start();
                    tmp.show();
                }
            };
            Command cancel = new Command("Cancel") {

                public void actionPerformed(ActionEvent arg0) {
                    tmp.show();
                }
            };
            Command del = new Command("Delete Waypoint") {

                public void actionPerformed(ActionEvent arg0) {
                    Thread t = new Thread(new Runnable() {

                        public void run() {
                            TagNavigatorWSWSService_Stub ws = new TagNavigatorWSWSService_Stub();
                            try {
                                ws.delTag(p.getId());
                                TagNavigatorMain.instance.showException(new Exception("" + "Waypoint deleted successfully"));
                            } catch (RemoteException ex) {
                                TagNavigatorMain.instance.showException(ex);
                            }
                        }
                    });
                    t.start();
                    tmp.show();
                }
            };
            lf.addCommand(cancel);
            lf.addCommand(del);
            lf.addCommand(ok);
            lf.show();
        }
    }

    public void elementEntered(OnMapElement element) {
    }

    public void elementLeft(OnMapElement element) {
    }

    /**
     * @return the dataSource
     */
    public LocationSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(LocationSource dataSource) {
        this.dataSource = dataSource;
    }
}
