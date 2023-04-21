package de.hs_mannheim.visualscheduler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import de.hs_mannheim.visualscheduler.scheduling.ProcessDefinition;
import de.hs_mannheim.visualscheduler.scheduling.ProcessResult;
import de.hs_mannheim.visualscheduler.scheduling.ProcessState;
import de.hs_mannheim.visualscheduler.scheduling.SchedulingResult;
import de.hs_mannheim.visualscheduler.scheduling.Timeblock;

/**
 * The Class ResultGUI is used to display the Scheduling-Results in a bar-chart.
 */
public class ResultGUI extends JPanel {

    private final class ProcessNamesPanel extends JPanel {

        private static final long serialVersionUID = 1848963269861546645L;

        private final Font font;

        ProcessNamesPanel() {
            font = getFont().deriveFont(Font.BOLD);
        }

        @Override
        public Dimension getPreferredSize() {
            int maxWidth = 20;
            for (final ProcessResult pd : scheduling.getProcesses()) {
                maxWidth = Math.max(getFontMetrics(font).stringWidth(pd.getName()) + 10, maxWidth);
            }
            return new Dimension(Math.min(maxWidth, 100), guiHeight);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            SubstanceLookAndFeel.setDecorationType(this, DecorationAreaType.PRIMARY_TITLE_PANE);
            super.paintComponent(g);
            int y = 33;
            g.setColor(Color.black);
            g.setFont(font);
            for (final ProcessResult pd : scheduling.getProcesses()) {
                int textX = getWidth() - g.getFontMetrics().stringWidth(pd.getName()) - 5;
                if (textX < 0) textX = 0;
                g.drawString(pd.getName(), textX, y);
                y += ROW_HEIGHT;
            }
        }
    }

    private final class TimeSlicePanel extends JPanel implements Scrollable {

        private static final long serialVersionUID = -270842034090706466L;

        TimeSlicePanel() {
        }

        @Override
        public Color getBackground() {
            return Color.WHITE;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(scheduling.getEndTime() * TIMESLICE_WIDTH + 10, guiHeight);
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 1;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 1;
        }

        ;

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            for (final Timeblock tb : scheduling) {
                final int startX = tb.getStartTime() * TIMESLICE_WIDTH;
                for (final Entry<ProcessDefinition, ProcessState> entry : tb.entrySet()) {
                    if (entry.getValue() != ProcessState.COMPLETED && entry.getValue() != ProcessState.NOT_ARRIVE_YET) {
                        for (int x = startX; x < tb.getEndTime() * TIMESLICE_WIDTH; x += TIMESLICE_WIDTH) {
                            final int y = scheduling.getProcesses().indexOf(entry.getKey()) * ROW_HEIGHT + 20;
                            if (entry.getValue() == ProcessState.STARTED_AND_RUNNING) {
                                g.drawImage(running, x, y, null);
                            } else if (entry.getValue() == ProcessState.STARTED_AND_WAITING) {
                                g.drawImage(waiting, x, y, null);
                            } else {
                                g.drawImage(latency, x, y, null);
                            }
                        }
                    }
                }
            }
            g.setColor(Color.GRAY);
            int last = -1;
            for (final Timeblock tb : scheduling) {
                int startX = tb.getStartTime() * TIMESLICE_WIDTH;
                if (last != tb.getStartTime()) {
                    g.drawLine(startX, 0, startX, getHeight());
                }
                startX += tb.getDuration() * TIMESLICE_WIDTH;
                g.drawLine(startX, 0, startX, getHeight());
                last = tb.getEndTime();
            }
        }
    }

    private static final long serialVersionUID = -5075231924735712824L;

    private static final int ROW_HEIGHT = 40;

    private static final int TIMESLICE_WIDTH = 15;

    int guiHeight = 0;

    BufferedImage running;

    BufferedImage waiting;

    BufferedImage latency;

    /** The scheduling results to display */
    SchedulingResult scheduling = new SchedulingResult();

    private final JPanel overviewPanel = new JPanel() {

        private static final long serialVersionUID = 1848963269861546645L;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 100);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            double avgRuntime = 0;
            double avgWait = 0;
            for (final ProcessResult pd : scheduling.getProcesses()) {
                avgRuntime += (pd.getSleep() + pd.getLatency() + pd.getActive());
                avgWait += (pd.getSleep() + pd.getLatency());
            }
            avgRuntime /= scheduling.getProcesses().size();
            avgWait /= scheduling.getProcesses().size();
            g.drawString("Averages", 150, 13);
            g.drawString("Runtime  " + scheduling.getAverageRuntime(), 110, 60);
            g.drawString("Active   " + scheduling.getAverageActivePercentString(), 110, 75);
            g.drawString("Waiting  " + scheduling.getAverageWaitPercentString(), 110, 90);
            g.drawString("Sleeping " + scheduling.getAverageSleepPercentString(), 250, 75);
            g.drawString("Latent   " + scheduling.getAverageLatencyPercentString(), 250, 90);
            g.drawImage(waiting, 10, 10, null);
            g.drawImage(latency, 10, 35, null);
            g.drawImage(running, 10, 60, null);
            g.drawString("Waiting", 13 + TIMESLICE_WIDTH, 25);
            g.drawString("Latency", 13 + TIMESLICE_WIDTH, 50);
            g.drawString("Running", 13 + TIMESLICE_WIDTH, 75);
            g.setClip(0, 0, 110 + 250, 400);
            int xEdge = 110 + (int) (scheduling.getAverageActive() / scheduling.getAverageRuntime() * 250);
            int x = 110;
            while (x < xEdge) {
                g.drawImage(running, x, 20, null);
                x += TIMESLICE_WIDTH;
            }
            x = xEdge;
            xEdge = 110 + (int) ((scheduling.getAverageActive() + scheduling.getAverageLatency()) / scheduling.getAverageRuntime() * 250);
            while (x < xEdge) {
                g.drawImage(latency, x, 20, null);
                x += TIMESLICE_WIDTH;
            }
            x = xEdge;
            xEdge = 110 + 250;
            while (x < xEdge) {
                g.drawImage(waiting, x, 20, null);
                x += TIMESLICE_WIDTH;
            }
        }
    };

    private final JPanel processPanel = new ProcessNamesPanel();

    private final JPanel timeScalaPane = new JPanel() {

        private static final long serialVersionUID = -5057058021845762287L;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(10000, 20);
        }

        ;

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            int last = 10000;
            for (final Timeblock tb : scheduling) {
                if (tb.getStartTime() > last) g.drawString("" + last, last * TIMESLICE_WIDTH - 5, 13); else g.drawString("" + tb.getStartTime(), tb.getStartTime() * TIMESLICE_WIDTH - 5, 13);
                last = tb.getStartTime() + tb.getDuration();
            }
            g.drawString("" + last, last * TIMESLICE_WIDTH - 5, 13);
        }

        ;
    };

    private final JPanel timeslicePanel = new TimeSlicePanel();

    /**
	 * Instantiates a new result gui.
	 */
    public ResultGUI() {
        super(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(timeslicePanel);
        scrollPane.setRowHeaderView(processPanel);
        scrollPane.setColumnHeaderView(timeScalaPane);
        add(scrollPane, BorderLayout.CENTER);
        add(overviewPanel, BorderLayout.SOUTH);
        SubstanceLookAndFeel.setDecorationType(timeScalaPane, DecorationAreaType.HEADER);
        try {
            loadBuildinStyle();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, guiHeight);
    }

    /**
	 * Gets the scheduling.
	 * 
	 * @return the scheduling
	 */
    public SchedulingResult getScheduling() {
        return scheduling;
    }

    /**
	 * Load build in style Bar-Style.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public void loadBuildinStyle() throws IOException {
        final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("bars.png");
        running = new BufferedImage(TIMESLICE_WIDTH, 20, BufferedImage.TYPE_4BYTE_ABGR);
        latency = new BufferedImage(TIMESLICE_WIDTH, 20, BufferedImage.TYPE_4BYTE_ABGR);
        waiting = new BufferedImage(TIMESLICE_WIDTH, 20, BufferedImage.TYPE_4BYTE_ABGR);
        try {
            final BufferedImage styleBitmap = ImageIO.read(is);
            final BufferedImage extendedStyleBitmap = new BufferedImage(TIMESLICE_WIDTH, 3 * 20, BufferedImage.TYPE_4BYTE_ABGR);
            int x = 0;
            Graphics g = extendedStyleBitmap.getGraphics();
            do {
                g.drawImage(styleBitmap, x, 0, null);
                x += styleBitmap.getWidth();
            } while (x < TIMESLICE_WIDTH);
            g.dispose();
            int y = 0;
            for (final BufferedImage image : new BufferedImage[] { running, latency, waiting }) {
                g = image.getGraphics();
                g.drawImage(extendedStyleBitmap, 0, y, null);
                g.dispose();
                y -= 20;
            }
        } finally {
            is.close();
        }
    }

    /**
	 * Sets the scheduling-results to display.
	 * 
	 * @param scheduling
	 *            the new scheduling
	 */
    public void setScheduling(final SchedulingResult scheduling) {
        this.scheduling = scheduling;
        guiHeight = scheduling.getProcesses().size() * ROW_HEIGHT + 20;
    }
}
