package kfschmidt.imageoverlay;

import kfschmidt.ijcommon.ImageSource;
import kfschmidt.ijcommon.IJAdapter;
import kfschmidt.ijcommon.ImageSink;
import java.util.*;
import java.awt.image.*;

public class OverlayMgr {

    public static int PEEK = 4;

    public static int OUTLINE = 5;

    public static int ERASE = 6;

    public static int MOVE_SCALE = 7;

    public static int MOVE_LEGEND = 8;

    public static int SHOW_WHOLE_MAP = 9;

    int mLegendWidth = 100;

    int mLegendHeight = 300;

    String mCurrentSliceDisplayString;

    ImageSource mSource;

    OverlayPanel mPanel;

    OverlayViewer mViewer;

    Overlay[] mOverlay;

    ImageSink mSink;

    int mDisplayMode;

    IJAdapter mIJada = new IJAdapter();

    LUTHelper mLUTHelper;

    boolean mInterpolateMap = false;

    int[] mPortSliceDisplayPlan;

    Legend mLegend;

    LegendViewer mLegendViewer;

    public int getDisplayMode() {
        return mDisplayMode;
    }

    public void setDisplayMode(int mode) {
        mDisplayMode = mode;
    }

    public OverlayMgr(ImageSource source, ImageSink sink) {
        mSource = source;
        mSink = sink;
        mLUTHelper = new LUTHelper();
    }

    public Overlay getOverlay(int a) {
        if (mPortSliceDisplayPlan == null) return null;
        if (a > mOverlay.length || a < 0) return null;
        return mOverlay[a];
    }

    public String getMapName() {
        return mIJada.getNameForImageId(mPanel.getMapId());
    }

    public String getBgName() {
        return mIJada.getNameForImageId(mPanel.getBgId());
    }

    public void showLegend() {
        if (mLegend != null && mLegendViewer == null) {
            mLegendViewer = new LegendViewer(mLegend);
        }
        if (mLegendViewer != null) mLegendViewer.setVisible(true);
    }

    public void showUI() {
        if (mPanel == null) {
            mPanel = new OverlayPanel(this);
        }
        mPanel.setVisible(true);
    }

    public void showViewer() {
        if (mViewer == null) {
            mViewer = new OverlayViewer(this);
        }
        if (mViewer != null && getOverlay(0) != null) mViewer.setVisible(true);
    }

    public String[] getLUTNames() {
        return mLUTHelper.getLUTNames();
    }

    public String getLUT1Name() {
        return mLUTHelper.getLUT1();
    }

    public String getLUT2Name() {
        return mLUTHelper.getLUT2();
    }

    public double getMax1() {
        return mLUTHelper.getMax1();
    }

    public double getMin1() {
        return mLUTHelper.getMin1();
    }

    public double getMax2() {
        return mLUTHelper.getMax2();
    }

    public double getMin2() {
        return mLUTHelper.getMin2();
    }

    public boolean getInterpolated() {
        return mInterpolateMap;
    }

    public void setBgBrightness(float bright) {
        for (int a = 0; a < mOverlay.length; a++) {
            mOverlay[a].setBgBrightness(bright);
        }
        updateAll();
    }

    public void setBgContrast(float contr) {
        for (int a = 0; a < mOverlay.length; a++) {
            mOverlay[a].setBgContrast(contr);
        }
        updateAll();
    }

    public float getBgBrightness() {
        if (mOverlay != null && mOverlay.length > 0) return mOverlay[0].getBgBrightness(); else return 1f;
    }

    public float getBgContrast() {
        if (mOverlay != null && mOverlay.length > 0) return mOverlay[0].getBgContrast(); else return 0f;
    }

    public void userSetInterpolate(boolean val) {
        mInterpolateMap = val;
    }

    public String getCurrentSliceDisplayString() {
        return mCurrentSliceDisplayString;
    }

    public void userChangedSliceDisplay(String display_string) {
        int bg_id = 0;
        int map_id = 0;
        if (mOverlay != null && mOverlay[0].getBackgroundImgId() != 0) bg_id = mOverlay[0].getBackgroundImgId();
        if (mOverlay != null && mOverlay[0].getMapImgId() != 0) map_id = mOverlay[0].getMapImgId();
        try {
            mCurrentSliceDisplayString = display_string;
            mPortSliceDisplayPlan = interpretSliceDisplayString(display_string);
            mOverlay = new Overlay[mPortSliceDisplayPlan.length];
            for (int a = 0; a < mPortSliceDisplayPlan.length; a++) {
                mOverlay[a] = new Overlay();
                mOverlay[a].setLUTHelper(mLUTHelper);
                mOverlay[a].setImageSlice(mPortSliceDisplayPlan[a]);
                if (bg_id != 0) mOverlay[a].setBackgroundImgId(bg_id);
                if (map_id != 0) mOverlay[a].setMapImgId(map_id);
            }
            mViewer.setNumberOfViewports(mOverlay.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateAll();
    }

    /** returns an array of up to 10 ints; array size specifies 
     *  the number of view ports open, and the content of each
     *  element is the 1 based slice number that is displayed in the 
     *  viewport at that index
     */
    private int[] interpretSliceDisplayString(String str) throws Exception {
        int[] ret = null;
        StringTokenizer tok = null;
        if (mCurrentSliceDisplayString.indexOf(" ") > -1) {
            tok = new StringTokenizer(mCurrentSliceDisplayString, " ");
        } else if (mCurrentSliceDisplayString.indexOf(",") > -1) {
            tok = new StringTokenizer(mCurrentSliceDisplayString, ",");
        } else {
            tok = new StringTokenizer(mCurrentSliceDisplayString);
        }
        Vector v = new Vector();
        Vector display_ports = new Vector();
        while (tok.hasMoreTokens()) {
            v.addElement(tok.nextToken());
        }
        for (int a = 0; a < v.size(); a++) {
            String s = (String) v.elementAt(a);
            if (s.indexOf("-") > -1) {
                StringTokenizer tok2 = new StringTokenizer(s, "-");
                int first_number = Integer.parseInt(tok2.nextToken());
                int second_number = Integer.parseInt(tok2.nextToken());
                for (int b = 0; first_number + b <= second_number; b++) {
                    display_ports.add(new Integer(first_number + b));
                }
            } else {
                display_ports.add(new Integer(s));
            }
        }
        if (display_ports.size() < 1) {
            ret = new int[1];
            ret[0] = 1;
            return ret;
        } else if (display_ports.size() >= 10) {
            ret = new int[10];
        } else {
            ret = new int[display_ports.size()];
        }
        for (int a = 0; a < ret.length; a++) {
            ret[a] = ((Integer) display_ports.elementAt(a)).intValue();
        }
        return ret;
    }

    public void userChangedLUT1(String lut_name) {
        mLUTHelper.setLUT1(lut_name);
        repaintMaps();
        updateAll();
    }

    public void userChangedLUT2(String lut_name) {
        mLUTHelper.setLUT2(lut_name);
        repaintMaps();
        updateAll();
    }

    public void setMax1(double val) {
        mLUTHelper.setMax1(val);
        repaintMaps();
        updateAll();
    }

    public void setMin1(double val) {
        mLUTHelper.setMin1(val);
        repaintMaps();
        updateAll();
    }

    public void setMax2(double val) {
        mLUTHelper.setMax2(val);
        repaintMaps();
        updateAll();
    }

    public void setMin2(double val) {
        mLUTHelper.setMin2(val);
        repaintMaps();
        updateAll();
    }

    public void repaintMaps() {
        for (int a = 0; a < mOverlay.length; a++) {
            mOverlay[a].repaintMap();
        }
    }

    public void userChangedBgImage(int new_id) {
        int[] dims = mIJada.getImageDims(new_id);
        if (mOverlay == null) {
            String str = "1-";
            if (dims[2] > 8) str += "8"; else if (dims[2] == 1) str = "1"; else str += dims[2];
            userChangedSliceDisplay(str);
        }
        for (int a = 0; a < mOverlay.length; a++) {
            mOverlay[a].setBackgroundImgId(new_id);
        }
        updateAll();
    }

    public void userChangedMapImage(int new_id) {
        if (mOverlay == null) return;
        for (int a = 0; a < mOverlay.length; a++) {
            mOverlay[a].setMapImgId(new_id);
        }
        setViewerMode(SHOW_WHOLE_MAP);
        mLegend = new Legend(mLegendWidth, mLegendHeight, mLUTHelper);
        showLegend();
    }

    public void sendImagesToIJ() {
        String name = "";
        if (getOverlay(0).getMapImgId() != 0) name = mIJada.getNameForImageId(getOverlay(0).getMapImgId()); else name = mIJada.getNameForImageId(getOverlay(0).getBackgroundImgId());
        if (name.indexOf(".") > -1) {
            StringTokenizer tok = new StringTokenizer(name, ".");
            name = tok.nextToken();
        }
        mIJada.takeImage(name + "_overlay", mViewer.getRGBOverlayImage());
        mIJada.takeImage(name + "_legend", mLegendViewer.getRGBLegend());
    }

    public void exit() {
        System.out.println("Overlay mgr exiting()");
        if (mPanel != null) mPanel.dispose();
        if (mViewer != null) mViewer.closeAll();
        if (mLegendViewer != null) mLegendViewer.dispose();
    }

    protected int getViewerMode() {
        return getDisplayMode();
    }

    protected void setViewerMode(int mode) {
        setDisplayMode(mode);
        updateAll();
    }

    public void updateAll() {
        mViewer.repaintAll();
        if (mLegendViewer != null) mLegendViewer.refresh();
        showLegend();
        showViewer();
    }
}
