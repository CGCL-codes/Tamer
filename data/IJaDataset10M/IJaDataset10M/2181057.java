package mipt.gui.graph.impl;

import mipt.gui.graph.Graph;
import mipt.gui.graph.GraphException;

/**
 * Algorithm for computation of X and Y limits of a Graph and wrapper of the algorithm settings.
 * Used in subpackages and not by Graph itself.
 * @author Evdokimov
 */
public class AutoLimits {

    public static interface Owner {

        AutoLimits getLimits();

        /**
		 * Call updateLimits() here
		 */
        public void update();
    }

    /**
	 * Scale the plot automatically. Set this values to <code>true</code> if you're 
	 * not planning to set borders manually. 
	 */
    protected boolean autoXmin = false, autoXmax = false, autoYmin = false, autoYmax = false;

    /**
	 * Eihter max*auto or min*auto must be set if auto*min != auto*max
	 */
    protected double maxXauto = Double.NaN, maxYauto = Double.NaN, minXauto = Double.NaN, minYauto = Double.NaN;

    /**
	 * Plot insets.
	 */
    protected double insetX = 0.1, insetY = 0.1;

    /**
	 * Sets RELATIVE insets for min&max - part of (max-min). Default values are <code>0.1</code> for both.
	 * @param insetX horizontal insets
	 * @param insetY vertical insets
	 */
    public void setAutolimitInsets(double insetX, double insetY) {
        this.insetX = insetX;
        this.insetY = insetY;
    }

    /**
	 * Set the automatical resizing for the plot. Default values are <code>false</code> for both.
	 * @param autoX resize to fit horisontaly
	 * @param autoY resize to fit verticaly
	 */
    public void setAutolimits(boolean autoX, boolean autoY) {
        autoXmin = autoXmax = autoX;
        autoYmin = autoYmax = autoY;
    }

    /**
	 * Set the automatical resizing for the plot assuming that one limit can be auto 
	 *   and the other limit of the same axis is not
	 * Default values are <code>false</code> for all.
	 * @param autoX resize to fit horisontaly
	 * @param autoY resize to fit verticaly
	 */
    public void setAutolimits(boolean autoXmin, boolean autoXmax, boolean autoYmin, boolean autoYmax) {
        this.autoXmin = autoXmin;
        this.autoXmax = autoXmax;
        this.autoYmin = autoYmin;
        this.autoYmax = autoYmax;
    }

    /**
	 * Returns "autoLimits" settings for all axes and their limits -
	 *   in the same order as setAutoLimits() method accepts
	 * @see mipt.gui.graph.Graph.getMinMaxXY()
	 */
    public final boolean[] getAutoLimits() {
        return new boolean[] { autoXmin, autoXmax, autoYmin, autoYmax };
    }

    /**
	 * 
	 */
    public final double[] getMinMaxXYAuto() {
        return new double[] { minXauto, maxXauto, minYauto, maxYauto };
    }

    /**
	 * @param maxXauto
	 */
    public void setXMaxForAutomin(double maxXauto) {
        this.maxXauto = maxXauto;
    }

    /**
	 * @param maxYauto
	 */
    public void setYMaxForAutomin(double maxYauto) {
        this.maxYauto = maxYauto;
    }

    /**
	 * @param minXauto
	 */
    public void setXMinForAutomax(double minXauto) {
        this.minXauto = minXauto;
    }

    /**
	 * @param minYauto
	 */
    public void setYMinForAutomax(double minYauto) {
        this.minYauto = minYauto;
    }

    /**
	 * Updates graph limits by limits of its objects (e.g. curves).
	 * Use this method to ensure that new settings (curves min/max)
	 *  are applied to the plot.
	 * @param minMaxXY[objectIndex][4]{minX,maxX,minY,maxY}
	 * Supports the situation when some subarrays (or even all) are null.
	 */
    public void updateLimits(Graph graph, double[][] minMaxXY) throws GraphException {
        if (!(autoXmin || autoXmax || autoYmin || autoYmax)) return;
        double mm[] = null;
        if (minMaxXY.length > 0 && (autoXmin || autoXmax || autoYmin || autoYmax)) {
            int i = 0;
            do {
                mm = minMaxXY[i++];
            } while (mm == null && i < minMaxXY.length);
            for (; i < minMaxXY.length; i++) {
                double nm[] = minMaxXY[i];
                if (nm == null) continue;
                for (int j = 0; j < nm.length; j++) {
                    if (j % 2 == 0) mm[j] = Math.min(mm[j], nm[j]); else mm[j] = Math.max(mm[j], nm[j]);
                }
            }
        }
        if (autoXmin || autoXmax) {
            boolean fixedMin = !autoXmin && !Double.isNaN(minXauto), fixedMax = !autoXmax && !Double.isNaN(maxXauto);
            if (mm != null || (fixedMin && fixedMax)) {
                double m, M;
                if (mm == null) {
                    m = minXauto;
                    M = maxXauto;
                } else {
                    m = autoXmin ? mm[0] : Math.min(mm[0], minXauto);
                    M = autoXmax ? mm[1] : Math.max(mm[1], maxXauto);
                }
                if (mm != null && m > M) {
                    if (fixedMin) {
                        m = mm[0];
                        M = Math.max(mm[1], minXauto);
                    }
                    if (fixedMax) {
                        M = mm[1];
                        m = Math.min(mm[0], maxXauto);
                    }
                }
                if (fixedMin) {
                    if (minXauto < m) m = minXauto;
                    if (minXauto > M) M = minXauto;
                }
                if (fixedMax) {
                    if (maxXauto < m) m = maxXauto;
                    if (maxXauto > M) M = maxXauto;
                }
                if (m > M) {
                    double m1 = M;
                    M = m;
                    m = m1;
                }
                double delta = (M - m) * insetX;
                m -= delta;
                M += delta;
                graph.setXLimits(m, M);
            }
        }
        if (autoYmin || autoYmax) {
            boolean fixedMin = !autoYmin && !Double.isNaN(minYauto), fixedMax = !autoYmax && !Double.isNaN(maxYauto);
            if (mm != null || (fixedMin && fixedMax)) {
                double m, M;
                if (mm == null) {
                    m = minYauto;
                    M = maxYauto;
                } else {
                    m = autoYmin ? mm[2] : Math.min(mm[2], minYauto);
                    M = autoYmax ? mm[3] : Math.max(mm[3], maxYauto);
                }
                if (mm != null && m > M) {
                    if (fixedMin) {
                        m = mm[2];
                        M = Math.max(mm[3], minYauto);
                    }
                    if (fixedMax) {
                        M = mm[3];
                        m = Math.min(mm[2], maxYauto);
                    }
                }
                if (fixedMin) {
                    if (minYauto < m) m = minYauto;
                    if (minYauto > M) M = minYauto;
                }
                if (fixedMax) {
                    if (maxYauto < m) m = maxYauto;
                    if (maxYauto > M) M = maxYauto;
                }
                if (m > M) {
                    double m1 = M;
                    M = m;
                    m = m1;
                }
                double delta = (M - m) * insetY;
                m -= delta;
                M += delta;
                graph.setYLimits(m, M);
            }
        }
    }
}
