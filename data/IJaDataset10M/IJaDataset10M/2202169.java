package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import preferences.Preference;
import common.Dialogs;
import common.Utils;

public class WaveformViewHandler implements ViewHandler {

    private class LockObj {
    }

    ;

    private static final int BYTES_IN_DOUBLE = Double.SIZE / 8;

    private static final String VIEW = "Wave";

    private final int m_trackNo;

    private JPanel m_viewPanel;

    private TrackProperties m_trackProps;

    private String m_intermediateFile;

    private WaveFormResult m_wavFormRes;

    private MaxMin m_trackMaxMin;

    private final LockObj m_maxMinLock = new LockObj();

    private final LockObj m_intermedFileLock = new LockObj();

    private ComputeWaveFormWorker m_worker;

    String getView() {
        return VIEW;
    }

    private void setTrackMaxMin(MaxMin newVal) {
        synchronized (m_maxMinLock) {
            m_trackMaxMin = newVal;
        }
    }

    private boolean isTrackMaxMinNull() {
        synchronized (m_maxMinLock) {
            return m_trackMaxMin == null;
        }
    }

    private MaxMin getTrackMaxMin() {
        synchronized (m_maxMinLock) {
            return m_trackMaxMin == null ? null : new MaxMin(m_trackMaxMin);
        }
    }

    private void setIntermediateFile(String newVal) {
        synchronized (m_intermedFileLock) {
            m_intermediateFile = newVal;
        }
    }

    private boolean isIntermediateFileNull() {
        synchronized (m_intermedFileLock) {
            return m_intermediateFile == null;
        }
    }

    private String getIntermediateFile() {
        synchronized (m_intermedFileLock) {
            return m_intermediateFile;
        }
    }

    Color getGraphColor() {
        return Color.BLUE;
    }

    double getStartTime(TrackProperties trackProps) {
        return trackProps.getStartTime();
    }

    boolean skipFirstSample() {
        return false;
    }

    private class WaveFormGraph extends JComponent {

        private final WaveFormResult m_wavFormRes;

        public WaveFormGraph(int panelHeight, int panelWidth, WaveFormResult wavFormRes) {
            m_wavFormRes = wavFormRes;
            setBounds(0, 0, panelWidth, panelHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            AxisResult ar = computeAxisResult(m_wavFormRes, g2d);
            if (ar != null) {
                g2d.setColor(getGraphColor());
                displayWaveForm(g, g2d, ar);
                drawAxis(g, g2d, m_wavFormRes, ar);
            }
            g2d.dispose();
        }

        private void displayWaveForm(Graphics g, Graphics2D g2d, AxisResult ar) {
            double scaleFactor = ar.getScalefactor();
            int axisY = ar.getAxisY();
            List<WaveLine> waveLineList = m_wavFormRes.getWaveLineList();
            if (waveLineList != null) {
                Iterator<WaveLine> iter = waveLineList.iterator();
                for (; iter.hasNext(); ) {
                    WaveLine line = iter.next();
                    int x = line.getX();
                    Point hiPoint = new Point(x, axisY - (int) Math.round(line.getHiY() * scaleFactor));
                    Point loPoint = new Point(x, axisY - (int) Math.round(line.getLoY() * scaleFactor));
                    g2d.drawLine(loPoint.x, loPoint.y, hiPoint.x, hiPoint.y);
                }
            }
        }

        private class AxisResult {

            private double m_range;

            private double m_scalefactor;

            private final int m_axisY;

            private final int m_lineStartX;

            private final int m_lineEndX;

            private final double m_maxVal;

            private final double m_minVal;

            private final int m_availHeight;

            /**
             * @param range
             * @param scalefactor
             * @param axisY 
             * @param availHeight 
             * @param minVal 
             * @param maxVal 
             * @param lineEndX 
             * @param lineStartX 
             */
            public AxisResult(double range, double scalefactor, int axisY, int lineStartX, int lineEndX, double maxVal, double minVal, int availHeight) {
                super();
                m_range = range;
                m_scalefactor = scalefactor;
                m_axisY = axisY;
                m_lineStartX = lineStartX;
                m_lineEndX = lineEndX;
                m_maxVal = maxVal;
                m_minVal = minVal;
                m_availHeight = availHeight;
            }

            /**
             * @return the range
             */
            public double getRange() {
                return m_range;
            }

            /**
             * @return the scalefactor
             */
            public double getScalefactor() {
                return m_scalefactor;
            }

            public boolean error() {
                return m_range <= 0.0;
            }

            /**
             * @return the axisY
             */
            public int getAxisY() {
                return m_axisY;
            }

            /**
             * @return the availHeight
             */
            public int getAvailHeight() {
                return m_availHeight;
            }

            /**
             * @return the lineEndX
             */
            public int getLineEndX() {
                return m_lineEndX;
            }

            /**
             * @return the lineStartX
             */
            public int getLineStartX() {
                return m_lineStartX;
            }

            /**
             * @return the maxVal
             */
            public double getMaxVal() {
                return m_maxVal;
            }

            /**
             * @return the minVal
             */
            public double getMinVal() {
                return m_minVal;
            }
        }

        private AxisResult computeAxisResult(WaveFormResult wavFormRes, Graphics2D g2d) {
            if (wavFormRes == null) return null;
            MaxMin trackMaxMin = getTrackMaxMin();
            if (trackMaxMin == null) return null;
            double maxVal = trackMaxMin.getMaxVal();
            double minVal = trackMaxMin.getMinVal();
            double range = maxVal - minVal;
            Insets insets = getInsets();
            int availHeight = getHeight() - insets.top - insets.bottom;
            double scalefactor = availHeight / range;
            int axisY = (int) (maxVal * scalefactor) + insets.top;
            int availWidth = getWidth() - insets.left - insets.right;
            int lineStartX = insets.left;
            int lineEndX = lineStartX + availWidth - 1;
            if (range == 0) {
                g2d.setColor(Color.red);
                g2d.drawString("Range of zero: Y value = " + maxVal, lineStartX + 4, availHeight / 2);
                return null;
            }
            if (range < 0) {
                g2d.setColor(Color.red);
                g2d.drawString("Negative range: Y max value = " + maxVal + ", Y min value = " + minVal, lineStartX + 4, availHeight / 2);
                return null;
            }
            return new AxisResult(range, scalefactor, axisY, lineStartX, lineEndX, maxVal, minVal, availHeight);
        }

        private void drawAxis(Graphics g, Graphics2D g2d, WaveFormResult wavFormRes, AxisResult ar) {
            MaxMin viewMaxMin = wavFormRes.getViewMaxMin();
            double maxVal = ar.getMaxVal();
            double minVal = ar.getMinVal();
            int lineStartX = ar.getLineStartX();
            int lineEndX = ar.getLineEndX();
            int axisY = ar.getAxisY();
            if (maxVal >= 0 && minVal <= 0) {
                g2d.setColor(Color.WHITE);
                g2d.drawLine(lineStartX, axisY, lineEndX, axisY);
            }
            g2d.setColor(getGraphColor());
            g2d.setXORMode(Color.BLACK);
            StringBuffer maxStrBuf = new StringBuffer(Double.toString(maxVal));
            double viewMaxVal = viewMaxMin.getMaxVal();
            if (viewMaxVal == maxVal) maxStrBuf.append(" = view max."); else {
                maxStrBuf.append(" view max. = ");
                maxStrBuf.append(viewMaxVal);
            }
            StringBuffer minStrBuf = new StringBuffer(Double.toString(minVal));
            double viewMinVal = viewMaxMin.getMinVal();
            if (viewMinVal == minVal) minStrBuf.append(" = view min."); else {
                minStrBuf.append(" view min. = ");
                minStrBuf.append(viewMinVal);
            }
            g2d.drawString(maxStrBuf.toString(), lineStartX + 4, 12);
            g2d.drawString(minStrBuf.toString(), lineStartX + 4, ar.getAvailHeight() - 4);
        }
    }

    private class WaveLine {

        private int m_x;

        private double m_loY;

        private double m_hiY;

        /**
             * @param x
             * @param loY
             * @param hiY
             */
        public WaveLine(int x, double loY, double hiY) {
            super();
            m_x = x;
            m_loY = loY;
            m_hiY = hiY;
        }

        /**
             * @return the hiY
             */
        public double getHiY() {
            return m_hiY;
        }

        /**
             * @return the loY
             */
        public double getLoY() {
            return m_loY;
        }

        /**
             * @return the x
             */
        public int getX() {
            return m_x;
        }
    }

    private static class MaxMin {

        private double m_minVal;

        private double m_maxVal;

        /**
             * @param minVal
             * @param maxVal
             */
        public MaxMin(double minVal, double maxVal) {
            super();
            m_minVal = minVal;
            m_maxVal = maxVal;
        }

        public MaxMin(MaxMin trackMaxMin) {
            m_minVal = trackMaxMin.m_minVal;
            m_maxVal = trackMaxMin.m_maxVal;
        }

        /**
             * @return the maxVal
             */
        public double getMaxVal() {
            return m_maxVal;
        }

        /**
             * @return the minVal
             */
        public double getMinVal() {
            return m_minVal;
        }
    }

    private static class WaveFormResult {

        private MaxMin m_viewMaxMin;

        private List<WaveLine> m_waveLineList;

        /**
             * @param trackMaxMin
             * @param viewMaxMin
             * @param waveLineList
             */
        public WaveFormResult(MaxMin viewMaxMin, List<WaveLine> waveLineList) {
            super();
            m_viewMaxMin = viewMaxMin;
            m_waveLineList = waveLineList;
        }

        /**
             * @return the viewMaxMin
             */
        public MaxMin getViewMaxMin() {
            return m_viewMaxMin;
        }

        /**
             * @return the waveLineList
             */
        public List<WaveLine> getWaveLineList() {
            return m_waveLineList;
        }
    }

    private class ComputeWaveFormWorker extends SwingWorker<WaveFormResult, String> {

        private String m_intermediateFile;

        private ProgressMonitor m_pm;

        public ComputeWaveFormWorker(String intermediateFile) {
            m_intermediateFile = intermediateFile;
            m_pm = new ProgressMonitor(MainProg.getFrame(), "Computing waveform for track: " + m_trackNo + " view: " + getView(), "initialising", 0, 100);
        }

        /**
             * @param nv
             * @see javax.swing.ProgressMonitor#setProgress(int)
             */
        public void setProgressMonitorVal(int nv) {
            m_pm.setProgress(nv);
        }

        private MaxMin getTrackMaxMin(String intermediateFile) throws Exception {
            double minVal = 0;
            double maxVal = 0;
            File intFile = new File(intermediateFile);
            int noOfSamples = (int) intFile.length() / BYTES_IN_DOUBLE;
            FileInputStream fin = new FileInputStream(intFile);
            DataInputStream in = new DataInputStream(new BufferedInputStream(fin));
            if (skipFirstSample()) {
                in.readDouble();
                noOfSamples--;
            }
            double progressFactor = 100.0 / noOfSamples;
            for (int i = 0; i < noOfSamples; i++) {
                double sample = in.readDouble();
                if (sample > maxVal) {
                    maxVal = sample;
                } else if (sample < minVal) {
                    minVal = sample;
                }
                if ((i & 0xFF) == 0) {
                    setProgress((int) ((i + 1) * progressFactor));
                }
            }
            in.close();
            fin.close();
            return new MaxMin(minVal, maxVal);
        }

        @Override
        protected WaveFormResult doInBackground() throws Exception {
            m_intermediateFile = Utils.validateIntermediateFile(m_intermediateFile);
            if (m_intermediateFile == null) {
                return null;
            }
            if (isTrackMaxMinNull()) {
                publish("Computing track max and min values");
                setTrackMaxMin(getTrackMaxMin(m_intermediateFile));
            }
            publish("Computing waveform data");
            List<WaveLine> wavLineList = new ArrayList<WaveLine>();
            double viewMinY = 0;
            double viewMaxY = 0;
            double trackStartTime = getStartTime(m_trackProps);
            double trackDuration = m_trackProps.getDuration();
            double viewStartTime = TimeRuler.getStartTime();
            double viewDuration = TimeRuler.getDuration();
            if (trackStartTime + trackDuration <= viewStartTime || viewStartTime + viewDuration <= trackStartTime) {
                return new WaveFormResult(new MaxMin(0.0, 0.0), null);
            }
            double overlappedPeriodStart = Math.max(trackStartTime, viewStartTime);
            double overlappedPeriodEnd = Math.min(trackStartTime + trackDuration, viewStartTime + viewDuration);
            int startX = TimeRuler.getXForTime(overlappedPeriodStart);
            RandomAccessFile interFile = new RandomAccessFile(m_intermediateFile, "r");
            int sampleRate = Preference.getSampleRate();
            int samplesToRead = (int) ((overlappedPeriodEnd - overlappedPeriodStart) * sampleRate);
            int prefSoundFileBufSize = Preference.getSoundFileBufferSize();
            int bufSizeInBytes = (prefSoundFileBufSize == 0) ? samplesToRead * BYTES_IN_DOUBLE : prefSoundFileBufSize;
            byte[] buf = new byte[bufSizeInBytes];
            int startSampleOfs = (int) ((overlappedPeriodStart - trackStartTime) * sampleRate);
            if (skipFirstSample()) {
                startSampleOfs++;
            }
            interFile.seek(startSampleOfs * BYTES_IN_DOUBLE);
            int samplesRemain = samplesToRead;
            double progressFactor = 100.0 / samplesToRead;
            int bytesRead;
            double curSampleTime = overlappedPeriodStart;
            double sampleTimeInc = 1.0 / sampleRate;
            int prevX = startX;
            double curMin = 0;
            double curMax = 0;
            boolean samplePending = false;
            int curX = startX;
            int accumSamplesProcessed = 0;
            do {
                bytesRead = interFile.read(buf);
                if (bytesRead == -1) {
                    break;
                }
                int doublesRead = bytesRead / BYTES_IN_DOUBLE;
                int samplesToProcess = doublesRead < samplesRemain ? doublesRead : samplesRemain;
                ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                DataInputStream dis = new DataInputStream(bais);
                for (int i = 0; i < samplesToProcess; i++) {
                    double sample = dis.readDouble();
                    if (!samplePending) {
                        curMin = curMax = sample;
                        samplePending = true;
                    }
                    curX = TimeRuler.getXForTime((double) curSampleTime);
                    if (curX == prevX) {
                        if (sample > curMax) curMax = sample; else if (sample < curMin) curMin = sample;
                    } else {
                        queueForPlotting(wavLineList, prevX, curMin, curMax);
                        if (curMax > viewMaxY) viewMaxY = curMax;
                        if (curMin < viewMinY) viewMinY = curMin;
                        samplePending = false;
                        prevX = curX;
                    }
                    curSampleTime += sampleTimeInc;
                }
                accumSamplesProcessed += samplesToProcess;
                setProgress((int) (accumSamplesProcessed * progressFactor));
                samplesRemain -= doublesRead;
            } while (samplesRemain > 0);
            interFile.close();
            setProgress(100);
            if (samplePending) {
                queueForPlotting(wavLineList, curX, curMin, curMax);
            }
            return new WaveFormResult(new MaxMin(viewMinY, viewMaxY), wavLineList);
        }

        private void queueForPlotting(List<WaveLine> wavLineList, int curX, double curMin, double curMax) {
            wavLineList.add(new WaveLine(curX, curMin, curMax));
        }

        @Override
        protected void process(List<String> messages) {
            for (String msg : messages) {
                m_pm.setNote(msg);
            }
        }

        @Override
        protected void done() {
            try {
                m_wavFormRes = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                Dialogs.showErrorDialog(null, "Error " + e.getLocalizedMessage() + " attempting to compute wave form");
                e.printStackTrace();
            } catch (CancellationException e) {
            } finally {
                super.done();
                m_pm.close();
            }
            if (!isCancelled()) {
                displayWaveGraph();
            }
        }
    }

    String getIntermediateFileName(TrackProperties trackProps) {
        return trackProps == null ? null : trackProps.getIntermediateFile();
    }

    public WaveformViewHandler(JPanel viewPanel, int trackNo) {
        m_viewPanel = viewPanel;
        m_trackNo = trackNo;
        m_trackProps = EditHandlerFactory.getTrackProperties(trackNo);
    }

    private synchronized void computeWaveForm() {
        if (!isIntermediateFileNull()) {
            if (m_worker != null && !m_worker.isDone()) {
                m_worker.cancel(true);
            }
            m_worker = new ComputeWaveFormWorker(getIntermediateFile());
            m_worker.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        if (!m_worker.isDone()) m_worker.setProgressMonitorVal((Integer) evt.getNewValue());
                    }
                }
            });
            m_worker.execute();
        }
    }

    public void setProperties(JPanel viewPanel) {
        m_viewPanel = viewPanel;
        if (getIntermediateFileName(m_trackProps) == null) {
            setIntermediateFile(null);
            renderView();
        } else {
            String intermediateFile = getIntermediateFile();
            if (intermediateFile == null || !intermediateFile.equals(getIntermediateFileName(m_trackProps))) {
                setIntermediateFile(getIntermediateFileName(m_trackProps));
                setTrackMaxMin(null);
                computeWaveForm();
            }
        }
    }

    public void clearView() {
        m_viewPanel.removeAll();
        m_viewPanel.validate();
        m_viewPanel.repaint();
    }

    private void displayWaveGraph() {
        m_viewPanel.removeAll();
        int panelHeight = m_viewPanel.getHeight();
        int panelWidth = m_viewPanel.getWidth();
        WaveFormGraph waveFormGraph = new WaveFormGraph(panelHeight, panelWidth, m_wavFormRes);
        m_viewPanel.add(waveFormGraph);
        m_viewPanel.validate();
        m_viewPanel.repaint();
    }

    public void renderView() {
        m_trackProps = EditHandlerFactory.getTrackProperties(m_trackNo);
        if (m_trackProps != null) {
            String newIntermedFname = getIntermediateFileName(m_trackProps);
            if (newIntermedFname == null || !newIntermedFname.equals(getIntermediateFile())) {
                setIntermediateFile(newIntermedFname);
                setTrackMaxMin(null);
            }
            computeWaveForm();
        }
        m_viewPanel.setBackground(Color.black);
        m_viewPanel.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                displayWaveGraph();
            }
        });
    }

    /**
     * @return the trackNo
     */
    public int getTrackNo() {
        return m_trackNo;
    }
}
