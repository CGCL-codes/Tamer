package fr.inria.zvtm.cluster;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import fr.inria.vit.pan.IPhodPan;
import fr.inria.vit.pan.PanEventSource;
import fr.inria.vit.point.ClutchEventType;
import fr.inria.vit.point.MouseLaserPoint;
import fr.inria.vit.point.PointEventSource;
import fr.inria.vit.point.PointListener;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.Location;
import fr.inria.zvtm.engine.SwingWorker;
import fr.inria.zvtm.engine.View;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.event.ViewListener;
import fr.inria.zvtm.fits.FitsHistogram;
import fr.inria.zvtm.fits.RangeSelection;
import fr.inria.zvtm.fits.filters.*;
import fr.inria.zvtm.fits.ZScale;
import fr.inria.zvtm.glyphs.JSkyFitsImage;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VCircle;
import fr.inria.zvtm.glyphs.VImage;
import fr.inria.zvtm.glyphs.VRectangle;
import fr.inria.zvtm.glyphs.VText;
import java.awt.Color;
import java.awt.Font;
import java.awt.LinearGradientPaint;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import jsky.coords.WorldCoords;

/**
 * A clustered viewer for FITS images.
 */
public class AstroRad {

    private static final double CTRLVIEW_XOFFSET = 7 * 2840;

    private static final double IMGVIEW_XOFFSET = 3 * 2840;

    private static final double VIEW_YOFFSET = 2 * 1800;

    static final int UNSEL_IMG_ZINDEX = 0;

    static final int SEL_IMG_ZINDEX = 1;

    static final int IMG_OVERLAY_ZINDEX = 2;

    private static final int IMGCURSOR_ZINDEX = 2;

    private static final int CTRLCURSOR_ZINDEX = 2;

    private VirtualSpace imageSpace;

    private VirtualSpace symbolSpace;

    private VirtualSpace detailSpace;

    private VirtualSpace controlSpace;

    private Camera imageCamera;

    private Camera symbolCamera;

    private Camera detailCamera;

    private Camera controlCamera;

    private final List<JSkyFitsImage> images = new ArrayList<JSkyFitsImage>();

    private RangeManager range;

    private SliderManager slider;

    private RadioGroup radio;

    private AODetailBox detailBox;

    private JSkyFitsImage selectedImage = null;

    private JSkyFitsImage draggedImage = null;

    private FitsHistogram hist;

    private VText wcsCoords;

    private WallCursor ctrlCursor;

    private WallCursor imgCursor;

    private MouseLaserPoint pointSource;

    private VirtualSpaceManager vsm = VirtualSpaceManager.INSTANCE;

    private AROptions options;

    private View masterView;

    public AstroRad(String fileOrUrl, AROptions options) {
        this.options = options;
        setup();
        addImage(fileOrUrl);
    }

    private void setup() {
        vsm.setMaster("AstroRad");
        imageSpace = vsm.addVirtualSpace("imageSpace");
        symbolSpace = vsm.addVirtualSpace("symbolSpace");
        detailSpace = vsm.addVirtualSpace("detailSpace");
        controlSpace = vsm.addVirtualSpace("controlSpace");
        imageCamera = imageSpace.addCamera();
        symbolCamera = symbolSpace.addCamera();
        detailCamera = detailSpace.addCamera();
        imageCamera.stick(symbolCamera);
        imageCamera.stick(detailCamera);
        controlCamera = controlSpace.addCamera();
        ArrayList<Camera> imgCamList = new ArrayList<Camera>();
        imgCamList.add(imageCamera);
        imgCamList.add(symbolCamera);
        imgCamList.add(detailCamera);
        ArrayList<Camera> controlCamList = new ArrayList<Camera>();
        controlCamList.add(controlCamera);
        Vector<Camera> cameras = new Vector<Camera>();
        cameras.add(imageCamera);
        cameras.add(symbolCamera);
        cameras.add(detailCamera);
        cameras.add(controlCamera);
        masterView = vsm.addFrameView(cameras, "Master View", View.STD_VIEW, 800, 600, false, true, true, null);
        masterView.setListener(new PanZoomEventHandler());
        masterView.getCursor().setColor(Color.GREEN);
        masterView.setActiveLayer(symbolCamera);
        ClusterGeometry clGeom;
        ClusteredView imageView;
        ClusteredView controlView;
        if (options.debugView) {
            clGeom = new ClusterGeometry(600, 400, 2, 1);
            imageView = new ClusteredView(clGeom, 0, 1, 1, imgCamList);
            controlView = new ClusteredView(clGeom, 1, 1, 1, controlCamList);
        } else {
            clGeom = new ClusterGeometry(2840, 1800, 8, 4);
            imageView = new ClusteredView(clGeom, 3, 6, 4, imgCamList);
            imageView.setBackgroundColor(Color.BLACK);
            controlView = new ClusteredView(clGeom, 27, 2, 4, controlCamList);
            controlView.setBackgroundColor(new Color(0, 40, 0));
        }
        assert (clGeom != null);
        assert (imageView != null);
        assert (controlView != null);
        vsm.addClusteredView(imageView);
        vsm.addClusteredView(controlView);
        ctrlCursor = new WallCursor(controlSpace);
        ctrlCursor.onTop(CTRLCURSOR_ZINDEX);
        imgCursor = new WallCursor(imageSpace, 20, 160, Color.GREEN);
        imgCursor.onTop(IMGCURSOR_ZINDEX);
        detailBox = new AODetailBox(detailSpace);
        pointSource = new MouseLaserPoint(masterView.getPanel().getComponent());
        pointSource.addListener(new PointListener() {

            boolean dragging = false;

            public void coordsChanged(double x, double y, boolean relative) {
                assert (!relative);
                ctrlCursor.moveTo(x - CTRLVIEW_XOFFSET, -y + VIEW_YOFFSET);
                imgCursor.moveTo(x - IMGVIEW_XOFFSET, -y + VIEW_YOFFSET);
                Point2D.Double pos = ctrlCursor.getPosition();
                Point2D.Double imgCurPos = imgCursor.getPosition();
                if (dragging) {
                    range.onDrag(pos.x, pos.y);
                    slider.onDrag(pos.x, pos.y);
                    imgCursorDragged(imgCurPos.x, imgCurPos.y);
                }
                if (selectedImage != null) {
                    Point2D.Double coords = selectedImage.pix2wcs(imgCurPos.x - (selectedImage.vx - (selectedImage.getWidth() / 2)), imgCurPos.y - (selectedImage.vy - (selectedImage.getHeight() / 2)));
                    wcsCoords.setText(coords == null ? "unknown coords" : String.format("%+09.4f %+09.4f", coords.x, coords.y));
                }
            }

            public void pressed(boolean pressed) {
                Point2D.Double pos = ctrlCursor.getPosition();
                Point2D.Double imgCurPos = imgCursor.getPosition();
                if (pressed) {
                    dragging = true;
                    range.onPress1(pos.x, pos.y);
                    slider.onPress1(pos.x, pos.y);
                    imgCursorPressed(imgCurPos.x, imgCurPos.y);
                } else {
                    draggedImage = null;
                    dragging = false;
                    radio.onClick1(pos.x, pos.y);
                    range.onRelease1();
                    slider.onRelease1();
                }
            }

            public void clutched(ClutchEventType event) {
            }
        });
        pointSource.start();
        if (options.debugView) {
            setupControlZone(0, 0, 400, 300);
        } else {
            setupControlZone(0, 0, 4000, 5000);
        }
    }

    private void setupControlZone(double x, double y, double width, double height) {
        range = new RangeManager(controlSpace, 0, height / 8, width);
        radio = new RadioGroup(controlSpace, -height / 4, -height / 5, new String[] { "gray", "heat", "rainbow" }, new LinearGradientPaint[] { NopFilter.getGradientS((float) height / 5f), HeatFilter.getGradientS((float) height / 5f), RainbowFilter.getGradientS((float) height / 5f) }, height / 5);
        slider = new SliderManager(controlSpace, 0, -2. * height / 5, width);
        range.addObserver(new RangeStateObserver() {

            public void onStateChange(RangeManager source, double low, double high) {
                if (selectedImage == null) {
                    return;
                }
                double min = selectedImage.getMinValue();
                double max = selectedImage.getMaxValue();
                selectedImage.setCutLevels(min + low * (max - min), min + high * (max - min));
            }
        });
        radio.addObserver(new RadioStateObserver() {

            public void onStateChange(RadioGroup source, int activeIdx, String label) {
                if (selectedImage == null) {
                    return;
                }
                if (activeIdx == 0) {
                    selectedImage.setColorLookupTable("Ramp");
                } else if (activeIdx == 1) {
                    selectedImage.setColorLookupTable("Heat");
                } else {
                    selectedImage.setColorLookupTable("Standard");
                }
            }
        });
        slider.addObserver(new SliderStateObserver() {

            public void onStateChange(SliderManager source, double value) {
                if (selectedImage == null) {
                    return;
                }
                selectedImage.setTranslucencyValue((float) value);
            }
        });
        wcsCoords = new VText(-width / 4 + 100, height / 3 - 100, 0, Color.YELLOW, "unknown coords", VText.TEXT_ANCHOR_MIDDLE, options.debugView ? 2 : 15);
        controlSpace.addGlyph(wcsCoords);
        wcsCoords.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void imgCursorPressed(double x, double y) {
        int highestZindex = -1;
        for (JSkyFitsImage img : images) {
            if (AstroUtil.isInside(img, x, y) && (img.getZindex() > highestZindex)) {
                draggedImage = img;
            }
        }
        if (draggedImage != null) {
            imageSpace.onTop(draggedImage, SEL_IMG_ZINDEX);
            imageFocusChanged(draggedImage);
        }
    }

    private void imgCursorDragged(double x, double y) {
        if (draggedImage == null) {
            return;
        }
        JSkyFitsImage snapTo = getSnapCandidate(draggedImage);
        double UNSNAP_DISTANCE = 200;
        if ((snapTo != null) && (distance(x, y, draggedImage) < UNSNAP_DISTANCE)) {
            draggedImage.moveTo(snapTo.vx, snapTo.vy);
        } else {
            draggedImage.moveTo(x, y);
        }
    }

    /**
     * Returns the first snap candidate.
     */
    private JSkyFitsImage getSnapCandidate(JSkyFitsImage image) {
        final double SNAP_DISTANCE = 90;
        JSkyFitsImage retval = null;
        for (JSkyFitsImage candidate : images) {
            if ((candidate != image) && (distance(candidate, image) <= SNAP_DISTANCE)) {
                retval = candidate;
            }
        }
        return retval;
    }

    private double distance(JSkyFitsImage a, JSkyFitsImage b) {
        return Math.abs(a.vx - b.vx) + Math.abs(a.vy - b.vy);
    }

    private double distance(double x, double y, JSkyFitsImage image) {
        return Math.abs(x - image.vx) + Math.abs(y - image.vy);
    }

    private void addImage(String fileOrUrl, double x, double y) {
        try {
            JSkyFitsImage image = new JSkyFitsImage(fileOrUrl);
            images.add(image);
            imageSpace.addGlyph(image);
            VText source = new VText(0, 0, 0, Color.PINK, fileOrUrl, VText.TEXT_ANCHOR_START, 6);
            source.moveTo(image.vx - image.getWidth() / 2, image.vy - (image.getHeight() / 2 + 70));
            imageSpace.addGlyph(source);
            image.stick(source);
            double[] scaleBounds = null;
            if (scaleBounds != null) {
                image.setCutLevels(scaleBounds[0], scaleBounds[1]);
            }
            imageFocusChanged(image);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    void addImage(String fileOrUrl) {
        addImage(fileOrUrl, 0, 0);
    }

    private void imageFocusChanged(JSkyFitsImage focused) {
        if (focused == null) {
            return;
        }
        for (JSkyFitsImage img : images) {
            img.setDrawBorder(false);
            imageSpace.atBottom(img, UNSEL_IMG_ZINDEX);
        }
        focused.setBorderColor(Color.PINK);
        focused.setDrawBorder(true);
        imageSpace.onTop(focused, SEL_IMG_ZINDEX);
        if (hist != null) {
            controlSpace.removeGlyph(hist);
        }
        slider.setTickVal(focused.getTranslucencyValue());
        double min = focused.getMinValue();
        double max = focused.getMaxValue();
        double[] scaleParams = focused.getCutLevels();
        range.setTicksVal((scaleParams[0] - min) / (max - min), (scaleParams[1] - min) / (max - min));
        selectedImage = focused;
    }

    private void removeSelectedImage() {
        if (selectedImage == null) {
            return;
        }
        images.remove(selectedImage);
        imageSpace.removeGlyph(selectedImage);
        selectedImage = null;
    }

    public static void main(String[] args) throws Exception {
        AROptions options = new AROptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
            parser.printUsage(System.err);
            return;
        }
        if (options.arguments.size() < 1) {
            System.err.println("Usage: AstroRad [options] image_URL");
            System.exit(0);
        }
        AstroRad ar = new AstroRad(args[0], options);
        new AstroServer(ar, 8000);
    }

    private void drawSymbols(List<AstroObject> objs) {
        if (selectedImage == null) {
            return;
        }
        for (AstroObject obj : objs) {
            Point2D.Double vsCoords = selectedImage.wcs2pix(obj.getRa(), obj.getDec());
            VCircle circle = new VCircle(vsCoords.getX(), vsCoords.getY(), 0, 5, Color.CYAN);
            circle.setOwner(obj);
            selectedImage.stick(circle);
            symbolSpace.addGlyph(circle);
        }
    }

    private class PanZoomEventHandler implements ViewListener {

        private int lastJPX;

        private int lastJPY;

        private CircleSelectionManager selMan = new CircleSelectionManager(imageSpace);

        public void press1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
            Point2D.Double spcCoords = masterView.getPanel().viewToSpaceCoords(controlCamera, jpx, jpy);
            range.onPress1(spcCoords.x, spcCoords.y);
            Point2D.Double spcImgCoords = masterView.getPanel().viewToSpaceCoords(imageCamera, jpx, jpy);
            selMan.onPress1(spcImgCoords.x, spcImgCoords.y);
        }

        public void release1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
            range.onRelease1();
            Point2D.Double spcImgCoords = masterView.getPanel().viewToSpaceCoords(imageCamera, jpx, jpy);
            if (selMan.onRelease1(spcImgCoords.x, spcImgCoords.y)) {
                if (selectedImage == null) {
                    return;
                }
                Point2D.Double vsCenter = selMan.getVsCenter();
                Point2D.Double wcsCenter = selectedImage.pix2wcs(vsCenter.x, vsCenter.y);
                Point2D.Double vsExt = selMan.getOuterPoint();
                Point2D.Double wcsExt = selectedImage.pix2wcs(vsExt.x, vsExt.y);
                final WorldCoords wc = new WorldCoords(wcsCenter.getX(), wcsCenter.getY());
                WorldCoords wcDummy = new WorldCoords(wcsExt.getX(), wcsExt.getY());
                final int distArcMin = (int) (wc.dist(wcDummy) + 1);
                System.err.println("Querying Simbad at " + wc + " with a radius of " + distArcMin + "arcminutes");
                symbolSpace.removeAllGlyphs();
                selectedImage.unstickAllGlyphs();
                new SwingWorker() {

                    @Override
                    public List<AstroObject> construct() {
                        List<AstroObject> objs = null;
                        try {
                            objs = CatQuery.makeSimbadCoordQuery(wc.getRaDeg(), wc.getDecDeg(), distArcMin);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } finally {
                            return objs;
                        }
                    }

                    @Override
                    public void finished() {
                        List<AstroObject> objs = (List<AstroObject>) get();
                        System.err.println("Result size: " + objs.size());
                        drawSymbols(objs);
                        for (AstroObject obj : objs) {
                            System.err.println(obj);
                        }
                    }
                }.start();
            }
        }

        public void click1(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
            Point2D.Double spcCoords = masterView.getPanel().viewToSpaceCoords(controlCamera, jpx, jpy);
            radio.onClick1(spcCoords.x, spcCoords.y);
        }

        public void press2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        }

        public void release2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        }

        public void click2(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
        }

        public void press3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
            lastJPX = jpx;
            lastJPY = jpy;
            v.setDrawDrag(true);
            vsm.getActiveView().mouse.setSensitivity(false);
        }

        public void release3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
            Camera c = imageCamera;
            c.setXspeed(0);
            c.setYspeed(0);
            c.setZspeed(0);
            v.setDrawDrag(false);
            vsm.getActiveView().mouse.setSensitivity(true);
        }

        public void click3(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
        }

        public void mouseMoved(ViewPanel v, int jpx, int jpy, MouseEvent e) {
            if (options.debugView && selectedImage != null) {
            }
        }

        public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpx, int jpy, MouseEvent e) {
            if (buttonNumber == 1) {
                Point2D.Double spcCoords = masterView.getPanel().viewToSpaceCoords(controlCamera, jpx, jpy);
                range.onDrag(spcCoords.x, spcCoords.y);
                Point2D.Double spcImgCoords = masterView.getPanel().viewToSpaceCoords(imageCamera, jpx, jpy);
                selMan.onDrag(spcImgCoords.x, spcImgCoords.y);
            }
            if (buttonNumber == 3 || ((mod == META_MOD || mod == META_SHIFT_MOD) && buttonNumber == 1)) {
                Camera c = imageCamera;
                double a = (c.focal + Math.abs(c.altitude)) / c.focal;
                if (mod == META_SHIFT_MOD) {
                    c.setXspeed(0);
                    c.setYspeed(0);
                    c.setZspeed((c.altitude > 0) ? (lastJPY - jpy) * (a / 4.0) : (lastJPY - jpy) / (a * 4));
                } else {
                    c.setXspeed((c.altitude > 0) ? (jpx - lastJPX) * (a / 4.0) : (jpx - lastJPX) / (a * 4));
                    c.setYspeed((c.altitude > 0) ? (lastJPY - jpy) * (a / 4.0) : (lastJPY - jpy) / (a * 4));
                }
            }
        }

        public void mouseWheelMoved(ViewPanel v, short wheelDirection, int jpx, int jpy, MouseWheelEvent e) {
        }

        public void enterGlyph(Glyph g) {
            detailBox.onEnterGlyph(g);
        }

        public void exitGlyph(Glyph g) {
            detailBox.onExitGlyph(g);
        }

        public void Ktype(ViewPanel v, char c, int code, int mod, KeyEvent e) {
            selMan.onKeyType(c, e);
        }

        public void Kpress(ViewPanel v, char c, int code, int mod, KeyEvent e) {
        }

        public void Krelease(ViewPanel v, char c, int code, int mod, KeyEvent e) {
        }

        public void viewActivated(View v) {
        }

        public void viewDeactivated(View v) {
        }

        public void viewIconified(View v) {
        }

        public void viewDeiconified(View v) {
        }

        public void viewClosing(View v) {
            vsm.stop();
            System.exit(0);
        }
    }
}

class AROptions {

    @Option(name = "-d", aliases = { "--debug-view" }, usage = "debug view")
    boolean debugView = false;

    @Argument
    List<String> arguments = new ArrayList();
}
