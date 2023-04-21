package org.sulweb.infureports.graphics;

import org.sulweb.awt.Utilities;
import org.sulweb.infumon.common.session.AlarmTypeCapabilities;
import org.sulweb.infumon.common.session.MeasureUnit;
import org.sulweb.infumon.common.session.PumpState;
import org.sulweb.infumon.common.session.Session;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.text.*;
import javax.swing.event.*;
import org.sulweb.infureports.AlarmPanel;
import org.sulweb.infureports.BarSelectionModel;
import org.sulweb.infureports.DarkGrayPanel;
import org.sulweb.infureports.DisplayedIntervalModel;
import org.sulweb.infureports.HistogramsData;
import org.sulweb.infureports.MultilineTooltip;
import org.sulweb.infureports.event.BitmapUpdateAdapter;
import org.sulweb.infureports.event.BitmapUpdateEvent;
import org.sulweb.infureports.event.BitmapUpdateListener;
import org.sulweb.infureports.event.MaxValueAdapter;
import org.sulweb.infureports.event.MaxValueModel;
import org.sulweb.infureports.event.SetMillisecondsToShowAdapter;
import org.sulweb.infureports.event.SetMillisecondsToShowEvent;
import org.sulweb.infureports.event.SetMillisecondsToShowModel;

/**
 *
 * @author lucio
 */
public final class HistogramsPanel extends JPanel {

    private HistogramBitmap hb;

    private boolean updatePending;

    private HistogramsData lastUpdate;

    private long lastTime;

    private DisplayedIntervalModel dim;

    private MaxValueModel mvm;

    private SetMillisecondsToShowModel smtsm;

    private BarSelectionModel bsm;

    private MouseMotionListener mouseml;

    private MouseEvent lastMouseEvent;

    private ActionListener toolTipRebuilder;

    private Runnable resizer;

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private static DecimalFormat df = new DecimalFormat("###0.0#");

    public static int X_AXIS_HEIGHT;

    private BitmapUpdateListener bul;

    private boolean selectionActive;

    private int visibleSeconds, visiblePixels;

    private int selectionX1, selectionX2;

    private HistogramsPanel(HistogramBitmap _hb) {
        this.hb = _hb;
        bul = new BitmapUpdateAdapter() {

            public void bitmapUpdated(BitmapUpdateEvent bue) {
                if (bue.isSizeChanged()) SwingUtilities.invokeLater(resizer); else repaint();
            }
        };
        this.hb.getModel().addListener(bul);
        toolTipRebuilder = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rebuildToolTip();
            }
        };
        resizer = new Runnable() {

            public void run() {
                setSize(getPreferredSize());
                Container parent = getParent();
                while (parent != null) {
                    parent.validate();
                    parent = parent.getParent();
                }
            }
        };
        mouseml = new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                lastMouseEvent = e;
                if (selectionActive) repaint();
            }
        };
        addMouseMotionListener(mouseml);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (selectionActive) {
                    if (selectionX1 < 0) selectionX1 = 0;
                    if (selectionX2 > hb.getUsedWidth()) selectionX2 = hb.getUsedWidth();
                    long time1 = hb.getTimeForX(selectionX1);
                    long time2 = hb.getTimeForX(selectionX2);
                    changeResolution(time1, time2);
                    selectionActive = false;
                }
            }
        });
        setToolTipText(" ");
    }

    public HistogramsPanel(HistogramsData hidat, DisplayedIntervalModel dsm, MaxValueModel mvm, SetMillisecondsToShowModel p_smtsm, BarSelectionModel p_bsm) {
        this(new HistogramBitmap());
        lastUpdate = hidat;
        this.dim = dsm;
        this.mvm = mvm;
        this.smtsm = p_smtsm;
        this.bsm = bsm;
        updatePending = true;
        lastTime = new Date().getTime();
        mvm.addListener(new MaxValueAdapter() {

            public void valueChanged(double newmax) {
                hb.setLogicalMaxY(newmax);
                long updateTime = new java.util.Date().getTime();
                if (lastUpdate.getSession().isClosed()) updateTime = lastUpdate.getSession().getEnd();
                hb.updateBitmap(lastUpdate, updateTime);
            }
        });
        smtsm.addListener(new SetMillisecondsToShowAdapter() {

            public void setMillisecondsToShow(SetMillisecondsToShowEvent event) {
                final int visiblePixelsFixme = 400;
                visiblePixels = visiblePixelsFixme;
                if (lastUpdate != null) {
                    visibleSeconds = (int) (event.getMillisecondsToShow() / 1000);
                    Rectangle vrect = getVisibleRect();
                    long visibleTime1 = hb.getTimeForX(vrect.x);
                    int usedWidth = hb.getUsedWidth();
                    int calcWidth = vrect.x + vrect.width;
                    long visibleTime2 = hb.getTimeForX(Math.min(usedWidth, calcWidth));
                    if (!hb.isVisualResolutionFullyDisplayable(visibleSeconds, visiblePixels, lastUpdate)) {
                        selectionActive = true;
                        event.setLimited();
                    } else {
                        changeResolution(visibleTime1, visibleTime2);
                        if (hb.isViewLimitedByResolution()) event.setLimited();
                    }
                }
            }
        });
    }

    private void changeResolution(long visibleTime1, long visibleTime2) {
        selectionActive = false;
        hb.setVisualResolution(visibleSeconds, visiblePixels, lastUpdate, visibleTime1, visibleTime2);
        dim.setSelection(hb.getUsedWidth(), hb.getTimeForX(0), hb.getTimeForX(hb.getUsedWidth()));
        Container parent = getParent();
        if (parent != null) parent.validate();
    }

    public HistogramBitmap getBitmap() {
        return hb;
    }

    protected void paintComponent(Graphics g) {
        if (updatePending) {
            if (lastUpdate.getSession().isClosed()) lastTime = lastUpdate.getSession().getEnd();
            int oldWidth = hb.getWidth();
            hb.updateBitmap(lastUpdate, lastTime);
            int newWidth = hb.getWidth();
            updatePending = false;
            mvm.setMaxValue(mvm.getMaxValue(), lastUpdate.getMeasureUnit(lastUpdate.getSession().getEnd()));
            long time0 = hb.getTimeForX(0);
            long time1 = hb.getTimeForX(newWidth);
            dim.setSelection(hb.getUsedWidth(), time0, time1);
            if (oldWidth != newWidth) {
                getParent().validate();
                return;
            }
        }
        Graphics2D g2 = (Graphics2D) g.create();
        Dimension sz = getSize();
        g2.setColor(DarkGrayPanel.color);
        g2.fillRect(0, 0, sz.width, sz.height);
        Image img = hb.getCurrentBitmap();
        if (img != null) g2.drawImage(img, 0, 0, this);
        if (selectionActive && lastMouseEvent != null) {
            g2.setColor(Color.ORANGE);
            int selw = hb.getMaxZoomableWidth(visibleSeconds, visiblePixels);
            int centerx = lastMouseEvent.getX();
            int x1 = centerx - (selw >> 1);
            int x2 = x1 + selw;
            g2.drawLine(x1, 0, x1, sz.height);
            g2.drawLine(x2, 0, x2, sz.height);
            selectionX1 = x1;
            selectionX2 = x2;
        }
        g2.dispose();
    }

    public void update(HistogramsData data, long time) {
        updatePending = true;
        lastUpdate = data;
        lastTime = time;
    }

    public Dimension getPreferredSize() {
        int bw = hb.getWidth();
        int bh = hb.FIXED_IMAGE_HEIGHT;
        return new Dimension(bw, bh);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        Dimension pref = getPreferredSize();
        Dimension max = new Dimension(pref.width << 2, pref.height);
        return max;
    }

    public HistogramsData getHistogramData() {
        return lastUpdate;
    }

    public MaxValueModel getMaxValueModel() {
        return mvm;
    }

    public HistogramsPanel createZoomed() {
        return this;
    }

    public DisplayedIntervalModel getDisplayedIntervalModel() {
        return dim;
    }

    private void scheduleToolTipRebuild() {
        if (getToolTipText() != null) {
            setToolTipText(null);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    setToolTipText(" ");
                }
            });
        }
    }

    private String[] getToolTipLines() {
        if (lastUpdate == null || hb == null || lastMouseEvent == null) return null;
        long time = hb.getTimeForX(lastMouseEvent.getX());
        if (!lastUpdate.isTimeAfterSessionStart(time)) return null;
        double[] histData = new double[lastUpdate.getNumberOfTextLabels()];
        MeasureUnit[] histDataMU = new MeasureUnit[histData.length];
        String[] labels = new String[histData.length];
        for (int i = 0; i < histData.length; i++) {
            histData[i] = lastUpdate.getValueForLabel(i, time);
            histDataMU[i] = lastUpdate.getMeasureUnitForLabel(i, time);
            labels[i] = lastUpdate.getLabel(i);
        }
        int datalen = histData.length + 1;
        int alarmslen = 0;
        String[] alarms = null;
        Session s = lastUpdate.getSession();
        try {
            AlarmTypeCapabilities atc = (AlarmTypeCapabilities) s;
            PumpState ps = s.getState(time);
            if (ps.isAlarmSet()) {
                alarms = AlarmPanel.getAlarmsDescriptions(atc.getAlarms(time));
            }
        } catch (ClassCastException cce) {
        }
        if (alarms != null) alarmslen += alarms.length + 1;
        String[] result = new String[datalen + alarmslen];
        result[0] = sdf.format(time);
        if (histData.length > 0) {
            int hupperidx = lastUpdate.getHistogramNumberForUpperLimit();
            result[1] = labels[hupperidx] + ": " + df.format(histData[hupperidx]) + histDataMU[hupperidx].toString();
            int index = 2;
            for (int i = 0; i < histData.length; i++) {
                if (i == lastUpdate.getHistogramNumberForUpperLimit()) continue;
                result[index] = labels[i] + ": " + df.format(histData[i]) + histDataMU[i].toString();
                index++;
            }
        }
        if (alarms != null) {
            result[datalen] = "Allarmi:";
            for (int i = 0; i < alarms.length; i++) result[datalen + i + 1] = alarms[i];
        }
        return result;
    }

    private JToolTip myCreateToolTip() {
        JToolTip deftt = super.createToolTip();
        String[] tlines = getToolTipLines();
        JToolTip tooltip = null;
        if (tlines != null) tooltip = new MultilineTooltip(deftt.getForeground(), deftt.getBackground(), tlines); else {
            tooltip = new JToolTip();
            tooltip.setTipText("?");
        }
        return tooltip;
    }

    public JToolTip createToolTip() {
        Timer rebuildToolTipTimer = new Timer(4000, toolTipRebuilder);
        rebuildToolTipTimer.setRepeats(false);
        JToolTip result = myCreateToolTip();
        rebuildToolTipTimer.start();
        return result;
    }

    private void rebuildToolTip() {
        setToolTipText(null);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setToolTipText(" ");
            }
        });
    }
}
