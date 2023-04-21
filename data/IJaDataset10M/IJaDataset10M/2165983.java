package geovista.category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import geovista.common.classification.MultiGaussian;
import geovista.geoviz.scatterplot.DataArray;

public class MultiClassDistributions2D extends JPanel implements MouseListener, ComponentListener {

    private static double AXISSPACEPORTION = 1.0 / 6.0;

    private Color background;

    private Color foreground;

    private boolean axisOn = true;

    private int plotOriginX;

    private int plotOriginY;

    private int plotEndX;

    private int plotEndY;

    private Vector[] dataVector;

    private String[] variableNames;

    private int classNumber;

    private Color[] classColors;

    private Vector CovarianceVector = new Vector();

    private Vector meanVector = new Vector();

    private Vector meanIntVector = new Vector();

    private Vector dataXYVector = new Vector();

    private int displayXVarIdx;

    private int displayYVarIdx;

    private double xAxisMin;

    private double xAxisMax;

    private double yAxisMin;

    private double yAxisMax;

    private Vector exsIntVector = new Vector();

    private Vector whyIntVector = new Vector();

    private boolean gauss;

    private JPopupMenu popup;

    private JCheckBoxMenuItem[] classCheckBox;

    private int currentCheckBox;

    private MultiGaussian[] eachClassGaussian;

    private DenseDoubleMatrix2D[] covarianceMatrices;

    private EigenvalueDecomposition[] eigenDecomposition;

    private Vector eigenVectors = new Vector();

    private Vector sdVectors = new Vector();

    private Vector sdIntVectors = new Vector();

    public MultiClassDistributions2D() {
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
        popup = new JPopupMenu();
    }

    public void setDisplayXVariableIndex(int index) {
        this.displayXVarIdx = index;
        this.initialize();
    }

    public int getDisplayXVariableIndex() {
        return this.displayXVarIdx;
    }

    public void setDisplayYVariableIndex(int index) {
        this.displayYVarIdx = index;
        this.initialize();
    }

    public int getDisplayYVariableIndex() {
        return this.displayYVarIdx;
    }

    public void setVariableNames(String[] variableNames) {
        this.variableNames = variableNames;
    }

    public void setClassColors(Color[] colors) {
        this.classColors = colors;
    }

    public void setDataVector(Vector[] dataVector) {
        this.dataVector = dataVector;
        this.classNumber = this.dataVector.length;
        this.classCheckBox = new JCheckBoxMenuItem[this.classNumber];
        for (int i = 0; i < this.classNumber; i++) {
            this.classCheckBox[i] = new JCheckBoxMenuItem("class" + new Integer(i).toString());
            this.classCheckBox[i].setName(new Integer(i).toString());
            this.classCheckBox[i].setSelected(true);
            this.popup.add(this.classCheckBox[i], i);
            this.currentCheckBox = i;
            classCheckBox[currentCheckBox].addActionListener(new ActionListener() {

                /**
				 * put your documentation comment here
				 * @param e
				 */
                public void actionPerformed(ActionEvent e) {
                    boolean isSelected;
                    if (classCheckBox[currentCheckBox] != null) {
                        isSelected = classCheckBox[currentCheckBox].isSelected();
                        classCheckBox[currentCheckBox].setSelected(!isSelected);
                    }
                    repaint();
                }
            });
        }
        addMouseListener(this);
        this.initialize();
    }

    public Vector[] getDataVector() {
        return this.dataVector;
    }

    public void setAxisOn(boolean axisOn) {
        this.axisOn = axisOn;
    }

    public boolean getAxisOn() {
        return this.axisOn;
    }

    public void setGaussOn(boolean gauss) {
        this.gauss = gauss;
    }

    public boolean getGaussOn() {
        return this.gauss;
    }

    /**
	 * put your documentation comment here
	 * @param c
	 */
    public void setBackground(Color c) {
        if (c == null) return;
        this.background = c;
        int colorTotal = c.getRed() + c.getGreen() + c.getBlue();
        int greyColor = 128 * 3;
        if (colorTotal < greyColor) this.foreground = Color.white; else this.foreground = Color.black;
        this.repaint();
    }

    private void initialize() {
        this.eachClassGaussian = new MultiGaussian[this.classNumber];
        this.covarianceMatrices = new DenseDoubleMatrix2D[this.classNumber];
        this.eigenDecomposition = new EigenvalueDecomposition[this.classNumber];
        this.CovarianceVector.clear();
        this.dataXYVector.clear();
        this.eigenVectors.clear();
        this.exsIntVector.setSize(this.classNumber);
        this.whyIntVector.setSize(this.classNumber);
        for (int i = 0; i < this.classNumber; i++) {
            int len = this.dataVector[i].size();
            double[][] oneClassData = new double[2][len];
            for (int j = 0; j < len; j++) {
                if (((double[]) (this.dataVector[i].get(j))).length <= this.displayXVarIdx) {
                    return;
                }
                oneClassData[0][j] = ((double[]) (this.dataVector[i].get(j)))[this.displayXVarIdx];
                oneClassData[1][j] = ((double[]) (this.dataVector[i].get(j)))[this.displayYVarIdx];
            }
            this.dataXYVector.add(i, oneClassData.clone());
        }
        for (int i = 0; i < this.classNumber; i++) {
            this.eachClassGaussian[i] = new MultiGaussian();
            double[][] dataForGaussian = new double[this.dataVector[i].size()][2];
            for (int m = 0; m < 2; m++) {
                for (int n = 0; n < this.dataVector[i].size(); n++) {
                    dataForGaussian[n][m] = ((double[][]) dataXYVector.get(i))[m][n];
                }
            }
            this.eachClassGaussian[i].setTrainingData(dataForGaussian);
            double[][] covariance = new double[2][2];
            double[] mean = new double[2];
            DenseDoubleMatrix2D eigenVector;
            DenseDoubleMatrix1D eigenValues;
            covariance = this.eachClassGaussian[i].getCovarianceMatrix();
            mean = this.eachClassGaussian[i].getMeanVector();
            this.CovarianceVector.add(i, covariance.clone());
            this.meanVector.add(i, mean.clone());
            this.covarianceMatrices[i] = new DenseDoubleMatrix2D((double[][]) this.CovarianceVector.get(i));
            this.eigenDecomposition[i] = new EigenvalueDecomposition(this.covarianceMatrices[i]);
            eigenVector = (DenseDoubleMatrix2D) this.eigenDecomposition[i].getV();
            this.eigenVectors.add(i, eigenVector.toArray());
            eigenValues = (DenseDoubleMatrix1D) this.eigenDecomposition[i].getRealEigenvalues();
            double[] sd = new double[2];
            sd[0] = Math.sqrt(Math.abs(eigenValues.toArray()[0]));
            sd[1] = Math.sqrt(Math.abs(eigenValues.toArray()[1]));
            this.sdVectors.add(i, sd.clone());
        }
        this.addComponentListener(this);
        this.setBackground(Color.white);
        this.xAxisMin = 0;
        this.xAxisMax = 0;
        this.yAxisMin = 0;
        this.yAxisMax = 0;
        for (int i = 0; i < this.classNumber; i++) {
            DataArray xDataArray = new DataArray(((double[][]) this.dataXYVector.get(i))[0]);
            DataArray yDataArray = new DataArray(((double[][]) this.dataXYVector.get(i))[1]);
            double xMin = ((double[]) xDataArray.getExtent().clone())[0];
            double xMax = ((double[]) xDataArray.getExtent().clone())[1];
            double yMin = ((double[]) yDataArray.getExtent().clone())[0];
            double yMax = ((double[]) yDataArray.getExtent().clone())[1];
            this.yAxisMin = (this.yAxisMin <= yMin) ? yAxisMin : yMin;
            this.yAxisMax = (this.yAxisMax >= yMax) ? yAxisMax : yMax;
            this.xAxisMin = (this.xAxisMin <= xMin) ? xAxisMin : xMin;
            this.xAxisMax = (this.xAxisMax >= xMax) ? xAxisMax : xMax;
        }
        setupDataforDisplay();
        this.validate();
    }

    private void setupDataforDisplay() {
        if (axisOn) {
            plotOriginX = (int) (this.getWidth() * AXISSPACEPORTION);
            plotOriginY = (int) (this.getHeight() * (1 - AXISSPACEPORTION));
            plotEndX = (int) (this.getWidth()) - (int) (this.getWidth() * AXISSPACEPORTION / 2);
            plotEndY = (int) (this.getHeight() * AXISSPACEPORTION / 2);
        } else {
            plotOriginX = 0;
            plotOriginY = (int) (this.getSize().getHeight() - 2);
            plotEndX = (int) (this.getSize().getWidth()) - 3;
            plotEndY = 3;
        }
        double scaleX, scaleY;
        scaleX = getScale(plotOriginX, plotEndX, xAxisMin, xAxisMax);
        scaleY = getScale(plotOriginY, plotEndY, yAxisMin, yAxisMax);
        this.exsIntVector.clear();
        this.whyIntVector.clear();
        for (int cl = 0; cl < this.classNumber; cl++) {
            int len = this.dataVector[cl].size();
            int[] exsInt = new int[len];
            int[] whyInt = new int[len];
            exsInt = getValueScreen(((double[][]) dataXYVector.get(cl))[0], scaleX, plotOriginX, xAxisMin);
            whyInt = getValueScreen(((double[][]) dataXYVector.get(cl))[1], scaleY, plotOriginY, yAxisMin);
            this.exsIntVector.add(cl, exsInt.clone());
            this.whyIntVector.add(cl, whyInt.clone());
        }
        if (gauss == true) {
            for (int cl = 0; cl < this.classNumber; cl++) {
                int[] sdInt = new int[2];
                sdInt[0] = (int) (((double[]) this.sdVectors.get(cl))[0] * scaleX);
                sdInt[1] = (int) (((double[]) this.sdVectors.get(cl))[1] * scaleX);
                this.sdIntVectors.add(cl, sdInt.clone());
                int[] meanInt = new int[2];
                meanInt[0] = getValueScreen(((double[]) this.meanVector.get(cl))[0], scaleX, plotOriginX, xAxisMin);
                meanInt[1] = getValueScreen(((double[]) this.meanVector.get(cl))[1], scaleY, plotOriginY, yAxisMin);
                this.meanIntVector.add(cl, meanInt.clone());
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(foreground);
        if (this.axisOn == true) {
            drawAxis(g);
        }
        drawPlot(g);
    }

    private void drawPlot(Graphics g) {
        if (this.displayXVarIdx == this.displayYVarIdx) {
            int plotWidth, plotHeight;
            plotWidth = (int) this.getWidth();
            plotHeight = (int) this.getHeight();
            int size;
            size = (plotWidth < plotHeight) ? plotWidth : plotHeight;
            String attributeX = new String();
            attributeX = this.variableNames[this.displayXVarIdx];
            if (attributeX.length() > 12) {
                g.drawString(attributeX, 2, plotHeight / 2);
            } else if (attributeX.length() <= 7) {
                g.drawString(attributeX, plotWidth / 4, plotHeight / 2);
            } else {
                g.drawString(attributeX, plotWidth / 8, plotHeight / 2);
            }
            Font font1 = new Font("", Font.PLAIN, (int) size / 12);
            g.setFont(font1);
            g.drawLine(0, 0, 5, 5);
            String maxString = Float.toString((float) (xAxisMax));
            g.drawString(maxString, 6, (int) (plotHeight * AXISSPACEPORTION / 2) + 2);
            g.drawLine(0, plotHeight, 5, plotHeight - 5);
            String minString = Float.toString((float) (xAxisMin));
            g.drawString(minString, 6, plotHeight - 5);
            g.drawLine(plotWidth, plotHeight, plotWidth - 5, plotHeight - 5);
            g.drawString(maxString, plotWidth - (int) (plotWidth * AXISSPACEPORTION + 5), plotHeight - 5);
        } else {
            for (int cl = 0; cl < this.classNumber; cl++) {
                if (this.classColors != null) {
                    g.setColor(this.classColors[cl]);
                } else {
                    if (cl == 0) {
                        g.setColor(Color.red);
                    } else if (cl == 1) {
                        g.setColor(Color.BLUE);
                    } else if (cl == 2) {
                        g.setColor(Color.green);
                    } else if (cl == 3) {
                        g.setColor(Color.orange);
                    } else if (cl == 4) {
                        g.setColor(Color.darkGray);
                    }
                }
                if (this.classCheckBox[cl].isSelected() == true) {
                    for (int i = 0; i < this.dataVector[cl].size(); i++) {
                        g.drawOval(((int[]) this.exsIntVector.get(cl))[i], ((int[]) this.whyIntVector.get(cl))[i], 2, 2);
                    }
                    g.drawOval(((int[]) this.meanIntVector.get(cl))[0], ((int[]) this.meanIntVector.get(cl))[1], 4, 4);
                    if (gauss == true) {
                        int[] mean = (int[]) this.meanIntVector.get(cl);
                        int[] sd = (int[]) this.sdIntVectors.get(cl);
                        double[][] eigenVector = (double[][]) this.eigenVectors.get(cl);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.rotate((Math.atan(eigenVector[0][0] / eigenVector[1][0]) - Math.PI / 2), mean[0], mean[1]);
                        g2d.drawOval(mean[0] - sd[0], mean[1] - sd[1], 2 * sd[0], 2 * sd[1]);
                        g2d.drawOval(mean[0] - 2 * sd[0], mean[1] - 2 * sd[1], 4 * sd[0], 4 * sd[1]);
                        g2d.rotate((-Math.atan(eigenVector[0][0] / eigenVector[1][0]) + Math.PI / 2), mean[0], mean[1]);
                    }
                    g.setColor(foreground);
                }
            }
        }
    }

    private void drawAxis(Graphics g) {
        int plotWidth, plotHeight;
        plotWidth = (int) this.getSize().getWidth();
        plotHeight = (int) this.getSize().getHeight();
        g.setColor(foreground);
        g.drawLine(plotOriginX, plotEndY, plotOriginX, plotOriginY);
        g.drawLine(plotOriginX, plotOriginY, plotEndX, plotOriginY);
        int fontSize;
        if (plotWidth < plotHeight) {
            if (plotWidth < 300) {
                fontSize = 9;
            } else {
                fontSize = (int) (plotWidth / 32);
            }
        } else {
            if (plotHeight < 300) {
                fontSize = 9;
            } else {
                fontSize = (int) (plotHeight / 32);
            }
        }
        Font font = new Font("", Font.PLAIN, fontSize);
        g.setFont(font);
        String scaleStringX;
        g.drawLine(plotOriginX, plotOriginY, plotOriginX, plotOriginY + 3);
        if (Math.abs(xAxisMin) <= 1) {
            scaleStringX = Float.toString((float) xAxisMin);
        } else {
            scaleStringX = Integer.toString((int) xAxisMin);
        }
        g.drawString(scaleStringX, plotOriginX - 3, plotOriginY + (int) (plotHeight * AXISSPACEPORTION / 4));
        g.drawLine(plotEndX, plotOriginY, plotEndX, plotOriginY + 3);
        if (Math.abs(xAxisMax) <= 1) {
            scaleStringX = Float.toString((float) xAxisMax);
        } else {
            scaleStringX = Integer.toString((int) xAxisMax);
        }
        g.drawString(scaleStringX, plotEndX - 8, plotOriginY + (int) (plotHeight * AXISSPACEPORTION / 4));
        font = new Font("", Font.PLAIN, fontSize + 3);
        g.setFont(font);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-Math.PI / 2, plotOriginX - plotWidth / 9, plotOriginY - (plotOriginY - plotEndY) / 3);
        g2d.rotate(+Math.PI / 2, plotOriginX - plotWidth / 9, plotOriginY - (plotOriginY - plotEndY) / 3);
    }

    /**
	 * Calculate scale between real data and integer data for showing up on screen.
	 * @param min
	 * @param max
	 * @param dataMin
	 * @param dataMax
	 * @return scale
	 */
    private double getScale(int min, int max, double dataMin, double dataMax) {
        double scale;
        scale = (max - min) / (dataMax - dataMin);
        return scale;
    }

    /**
	 * Convert the single value to integer value worked on screen.
	 * @param data
	 * @param scale
	 * @param min
	 * @param dataMin
	 * @return valueScreen
	 */
    private int getValueScreen(double data, double scale, int min, double dataMin) {
        int valueScreen;
        if (Double.isNaN(data)) {
            valueScreen = Integer.MIN_VALUE;
        } else {
            valueScreen = (int) ((data - dataMin) * scale + min);
        }
        return valueScreen;
    }

    /**
	 * Convert the numeric values of observations to integer value worked on screen.
	 * @param dataArray
	 * @param scale
	 * @param min
	 * @param dataMin
	 * @return valueScreen
	 */
    private int[] getValueScreen(double[] dataArray, double scale, int min, double dataMin) {
        int[] valueScreen = new int[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            if (Double.isNaN(dataArray[i])) {
                valueScreen[i] = Integer.MIN_VALUE;
            } else {
                valueScreen[i] = (int) ((dataArray[i] - dataMin) * scale + min);
            }
        }
        return valueScreen;
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        this.setupDataforDisplay();
        this.repaint();
    }

    public void componentShown(ComponentEvent e) {
    }

    /**
	 * put your documentation comment here
	 * @param e
	 */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void mouseClicked(MouseEvent e) {
        int count = e.getClickCount();
        if (count == 2) {
            MultiClassDistributions2D detailSP = new MultiClassDistributions2D();
            detailSP.setAxisOn(true);
            detailSP.setBackground(background);
            detailSP.setGaussOn(true);
            detailSP.setDisplayXVariableIndex(this.displayXVarIdx);
            detailSP.setDisplayYVariableIndex(this.displayYVarIdx);
            detailSP.setDataVector(this.dataVector);
            JFrame dlgSP = new JFrame();
            dlgSP.setLocation(300, 300);
            dlgSP.setSize(300, 300);
            dlgSP.getContentPane().setLayout(new BorderLayout());
            dlgSP.getContentPane().add(detailSP, BorderLayout.CENTER);
            dlgSP.setVisible(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
