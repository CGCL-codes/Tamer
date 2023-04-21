package preprocessing.automatic.gui;

import preprocessing.automatic.Chromosome;
import preprocessing.automatic.Gene;
import preprocessing.automatic.GeneSequence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Graphical panel containing chromosome to display in main automatic preprocessing dialog.
 *
 * @author Miloslav Pavlicek (26.11.2007 18:58:22)
 */
class ChromosomePanel extends JPanel {

    private final JPanel bar;

    private final JLabel fitnessLabel;

    private final JLabel rankLabel;

    private final ChromosomePanel chromosomePanel;

    private final ArrayList<Chromosome> chromosomes;

    private int chrIdx;

    private double fitnessRange;

    private double fitnessRangeStep;

    private double meanValue;

    private double standardDeviation;

    private boolean displayHints;

    private boolean displayAverage;

    private final boolean keepGenerationHistory;

    private final PreprocessorAbbreviationMap preprocessorAbbreviationMap;

    private final PanelMouseListener panelMouseListener;

    private final AutoPreprocessDialog autoPreprocessDialog;

    /**
     * Creates new panel containing chromosome information - label containing fitness, bar displaying fitness
     * graphically and popup dialog opened with right click and containing preprocessor sequences with configurations.
     *
     * @param autoPreprocessDialog        AutoPreprocessDialog containing this panel.
     * @param displayHints                true if short information about chromosome contents should be printed along
     *                                    with fitness bar.
     * @param keepGenerationHistory       true if all added chromosomes should be kept in memory for statistic purposes.
     * @param preprocessorAbbreviationMap map of preprocessor name to its abbreviation, used when displaying hints.
     */
    public ChromosomePanel(final AutoPreprocessDialog autoPreprocessDialog, boolean displayHints, boolean keepGenerationHistory, PreprocessorAbbreviationMap preprocessorAbbreviationMap) {
        this.autoPreprocessDialog = autoPreprocessDialog;
        this.displayHints = displayHints;
        this.keepGenerationHistory = keepGenerationHistory;
        this.preprocessorAbbreviationMap = preprocessorAbbreviationMap;
        chromosomes = new ArrayList<Chromosome>();
        meanValue = Double.NaN;
        standardDeviation = Double.NaN;
        setFitnessRange(100.0);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        chromosomePanel = this;
        bar = new BarPanel();
        fitnessLabel = new JLabel();
        rankLabel = new JLabel();
        bar.setAlignmentX(CENTER_ALIGNMENT);
        fitnessLabel.setAlignmentX(CENTER_ALIGNMENT);
        rankLabel.setAlignmentX(CENTER_ALIGNMENT);
        rankLabel.setMinimumSize(new Dimension(100, 100));
        add(bar);
        add(fitnessLabel);
        add(rankLabel);
        panelMouseListener = new PanelMouseListener();
        addMouseListener(panelMouseListener);
    }

    /**
     * Adds new chromosome to sequence of chromosomes displayed by this panel.
     *
     * @param chromosome new chromosome to add.
     */
    public void addChromosome(Chromosome chromosome) {
        chromosomes.add(chromosome);
        if (!keepGenerationHistory && chromosomes.size() == 3) {
            chromosomes.remove(0);
        }
        chrIdx = chromosomes.size() - 1;
    }

    /**
     * Forces panel to redraw its chromosome information. Typically called after setting new chromosome.
     */
    public void refresh() {
        bar.repaint();
        Formatter formatter = new Formatter(Locale.US);
        double forDisplay;
        if (displayAverage) {
            forDisplay = meanValue;
        } else {
            forDisplay = chromosomes.get(chrIdx).getFitness();
        }
        fitnessLabel.setText(formatter.format("%.2f", forDisplay).toString());
        String s = "# " + String.valueOf(chromosomes.get(chrIdx).getChromosomeNumber() + 1);
        rankLabel.setText(s);
        s = "Chromosome # " + String.valueOf(chromosomes.get(chrIdx).getChromosomeNumber() + 1) + " <br>\n";
        s = s + "Rank " + String.valueOf(chromosomes.get(chrIdx).getRank() + 1);
        rankLabel.setToolTipText(s);
        fitnessLabel.setToolTipText(s);
        bar.setToolTipText(s);
    }

    /**
     * Sets fitness display range of this panel to fitnessRange value. Chromosomes with higher fitness will be cut off.
     *
     * @param fitnessRange range of maximum displayable chromosome fitness.
     */
    public void setFitnessRange(double fitnessRange) {
        this.fitnessRange = fitnessRange;
        fitnessRangeStep = fitnessRange / 10;
    }

    /**
     * Apart from displaying fitness bar, short information about chromosome contents are printed. Each line consits
     * of one letter abbreviations of preprocessors for gene sequence. If no preprocessors are present or are disabled
     * for that line, dash is displayed.
     *
     * @param displayHints true if hints are about to be displayed.
     */
    public void setDisplayHints(boolean displayHints) {
        if (displayHints != this.displayHints) {
            this.displayHints = !this.displayHints;
            bar.repaint();
        }
    }

    /**
     * Makes this panel display 3 bars instead of one with fitness. Those bars visualize average fitness value over
     * generations (middle bar) and and mean -, + standard deviations (side bars).
     *
     * @param displayAverage true to make panel display average bars.
     */
    public void setDisplayAverage(boolean displayAverage) {
        this.displayAverage = displayAverage;
        if (displayAverage) {
            removeMouseListener(panelMouseListener);
            if (Double.valueOf(meanValue).isNaN()) {
                meanValue = 0.0;
                for (Chromosome chromosome : chromosomes) {
                    meanValue += chromosome.getFitness();
                }
                meanValue /= chromosomes.size();
                standardDeviation = 0.0;
                for (Chromosome chromosome : chromosomes) {
                    standardDeviation += Math.pow(chromosome.getFitness() - meanValue, 2);
                }
                standardDeviation /= chromosomes.size();
                standardDeviation = Math.sqrt(standardDeviation);
            }
            this.displayAverage = true;
        } else {
            addMouseListener(panelMouseListener);
        }
        refresh();
    }

    /**
     * Displays chromosome from generation given by index.
     *
     * @param index chromosome's generation.
     */
    public void displayChromosomeOfGeneration(int index) {
        if (index < 0 || index > chromosomes.size()) {
            return;
        }
        chrIdx = index;
        refresh();
    }

    /**
     * Returns fitness of currently displayed chromosome.
     *
     * @return fitness of currently displayed chromosome.
     */
    public double getDisplayedChromosomeFitness() {
        return chromosomes.get(chrIdx).getFitness();
    }

    /**
     * Decreases panel's fitness range display ability. Fitness is displayed larger and higher values are more likely
     * to be cut off.
     */
    public void zoomIn() {
        fitnessRange -= fitnessRangeStep;
        if (fitnessRange < fitnessRangeStep) {
            fitnessRange = fitnessRangeStep;
        }
        bar.repaint();
    }

    /**
     * Increases panel's fitness range display ability. Fitness is displayed smaller and higher values are less likely
     * to be cut off.
     */
    public void zoomOut() {
        fitnessRange += fitnessRangeStep;
        bar.repaint();
    }

    /**
     * Returns hints as set of strings with preprocessor one letter abbreviations for each preprocessor sequence. Empty
     * or disabled sequence displayed as dash.
     *
     * @return set of strings with preprocessor abbreviations.
     */
    private ArrayList<String> getHints() {
        Chromosome chromosome = chromosomes.get(chrIdx);
        ArrayList<String> ret = new ArrayList<String>(chromosome.getGeneSequenceCount());
        for (GeneSequence geneSequence : chromosome) {
            if (!geneSequence.isLocal()) {
                ret.add("");
            }
            String hint = "";
            if (!geneSequence.isDisabled() && geneSequence.size() > 0) {
                for (Gene gene : geneSequence) {
                    String preprocessorName = gene.getAssociatedPreprocessor().getPreprocessingMethodName();
                    hint += preprocessorAbbreviationMap.getAbbreviationForName(preprocessorName);
                }
            }
            if (hint.length() == 0) {
                hint = "-";
            }
            ret.add(hint);
        }
        return ret;
    }

    /**
     * Panel representing colored fitness bar.
     */
    private class BarPanel extends JPanel {

        @Override
        public void paint(Graphics g) {
            if (chromosomes.size() == 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(chromosomePanel.getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            if (!displayAverage) {
                g2.setPaint(Color.RED);
                if (chrIdx > 0 && chromosomes.get(chrIdx - 1).getFitness() == chromosomes.get(chrIdx).getFitness()) {
                    g2.setPaint(Color.GREEN);
                }
                paintBar(g2, 0, getWidth(), chromosomes.get(chrIdx).getFitness());
            } else {
                g2.setPaint(Color.LIGHT_GRAY);
                paintBar(g2, 0, getWidth() / 3, meanValue - standardDeviation);
                g2.setPaint(Color.GRAY);
                paintBar(g2, getWidth() / 3, getWidth() / 3, meanValue);
                g2.setPaint(Color.LIGHT_GRAY);
                paintBar(g2, getWidth() * 2 / 3, getWidth() / 3, meanValue + standardDeviation);
            }
            if (displayHints) {
                g2.setPaint(Color.BLACK);
                int hintTextHeight = g2.getFont().getSize();
                ArrayList<String> hints = getHints();
                for (int i = 0; i < hints.size(); i++) {
                    g2.drawString(hints.get(i), 5, hintTextHeight * (i + 1));
                }
            }
        }

        /**
         * Paints colored bar into this panel.
         *
         * @param g2     graphical context.
         * @param x      left corner of bar.
         * @param width  width of bar.
         * @param height height of bar.
         */
        private void paintBar(Graphics2D g2, int x, int width, double height) {
            if (fitnessRange < height) {
                int[] xCoords = new int[4];
                int[] yCoords = new int[4];
                xCoords[0] = x;
                xCoords[1] = x;
                xCoords[2] = x + width;
                xCoords[3] = x + width;
                yCoords[0] = getHeight();
                yCoords[1] = getHeight() / 2 + 5 + width / 2;
                yCoords[2] = getHeight() / 2 + 5 - width / 2;
                yCoords[3] = getHeight();
                g2.fillPolygon(xCoords, yCoords, 4);
                yCoords[0] = getHeight() / 2 - 5 + width / 2;
                yCoords[1] = 0;
                yCoords[2] = 0;
                yCoords[3] = getHeight() / 2 - 5 - width / 2;
                g2.fillPolygon(xCoords, yCoords, 4);
            } else {
                int y = (int) (getHeight() / fitnessRange * height);
                g2.fillRect(x, getHeight() - y, width, getHeight());
            }
        }
    }

    /**
     * Class for managing mouse events to display chromosome popup.
     */
    private class PanelMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            maybeShowPopup(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent event) {
            if (event.isPopupTrigger()) {
                ChromosomePopup chromosomePopup = new ChromosomePopup(autoPreprocessDialog, chromosomes.get(chrIdx));
                Point mouseEventPoint = SwingUtilities.convertPoint(chromosomePanel, event.getPoint(), autoPreprocessDialog);
                mouseEventPoint.translate(autoPreprocessDialog.getX(), autoPreprocessDialog.getY());
                chromosomePopup.setLocation(mouseEventPoint);
                chromosomePopup.setVisible(true);
            }
        }
    }
}
