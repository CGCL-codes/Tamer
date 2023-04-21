package org.mc4j.console.dashboard.components;

import org.mc4j.console.bean.MBeanNode;
import org.mc4j.console.swing.graph.MeterCanvas;
import org.mc4j.ems.connection.bean.EmsBean;
import org.mc4j.ems.connection.bean.attribute.EmsAttribute;
import org.openide.windows.TopComponent;
import java.awt.BorderLayout;
import java.util.Map;

/**
 *
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), September 2002
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class NumericAttributeMeter extends TopComponent implements Runnable, BeanComponent {

    private EmsBean emsBean;

    private EmsAttribute maxAttribute, currentAttribute;

    private String currentAttributeName;

    private int maxAttributeValue = 100;

    private String maxAttributeName;

    private int updateInterval = 1000;

    private int failures = 0;

    private Thread updateThread = null;

    private MeterCanvas meter;

    private boolean isRunning = true;

    /** Creates a new instance of NumericAttributeMeter */
    public NumericAttributeMeter() {
        putClientProperty("PersistenceType", "Never");
        meter = new MeterCanvas();
        this.setLayout(new BorderLayout());
        this.add(meter, BorderLayout.CENTER);
    }

    public void componentClosed() {
        this.isRunning = false;
    }

    public void run() {
        while (isRunning && (failures < 5)) {
            try {
                MBeanNode node;
                int max;
                if (this.maxAttributeName != null) {
                    Number maxNumber = (Number) this.maxAttribute.getValue();
                    max = maxNumber.intValue();
                } else {
                    max = this.maxAttributeValue;
                }
                Number current = (Number) this.currentAttribute.getValue();
                this.meter.setMax(max);
                this.meter.setCurrent(current.intValue());
            } catch (Exception e) {
                e.printStackTrace();
                failures++;
            } finally {
                try {
                    Thread.currentThread().sleep(this.updateInterval);
                } catch (Exception e) {
                }
            }
        }
        if (isRunning) this.close();
    }

    public void setBean(EmsBean emsBean) {
        this.emsBean = emsBean;
    }

    public void setContext(Map context) {
        if (this.emsBean == null) {
            throw new IllegalStateException("NumericAttributeMeter: context atribute [bean] not set.");
        }
        if (this.maxAttributeName != null) this.maxAttribute = this.emsBean.getAttribute(this.maxAttributeName);
        this.currentAttribute = this.emsBean.getAttribute(currentAttributeName);
        Thread updater = new Thread(this, "mc4j NumericAttributeMeter Updater");
        updater.start();
    }

    /** Getter for property updateInterval.
     * @return Value of property updateInterval.
     *
     */
    public int getUpdateInterval() {
        return updateInterval;
    }

    /** Setter for property updateInterval.
     * @param updateInterval New value of property updateInterval.
     *
     */
    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    /** Getter for property label.
     * @return Value of property label.
     *
     */
    public java.lang.String getLabel() {
        return this.meter.getLabel();
    }

    /** Setter for property label.
     * @param label New value of property label.
     *
     */
    public void setLabel(java.lang.String label) {
        this.meter.setLabel(label);
    }

    /** Getter for property currentAttributeName.
     * @return Value of property currentAttributeName.
     *
     */
    public java.lang.String getCurrentAttributeName() {
        return currentAttributeName;
    }

    /** Setter for property currentAttributeName.
     * @param currentAttributeName New value of property currentAttributeName.
     *
     */
    public void setCurrentAttributeName(java.lang.String currentAttributeName) {
        this.currentAttributeName = currentAttributeName;
    }

    /** Getter for property maxAttributeValue.
     * @return Value of property maxAttributeValue.
     *
     */
    public int getMaxAttributeValue() {
        return maxAttributeValue;
    }

    /** Setter for property maxAttributeValue.
     * @param maxAttributeValue New value of property maxAttributeValue.
     *
     */
    public void setMaxAttributeValue(int maxAttributeValue) {
        this.maxAttributeValue = maxAttributeValue;
    }

    /** Getter for property maxAttributeName.
     * @return Value of property maxAttributeName.
     *
     */
    public java.lang.String getMaxAttributeName() {
        return maxAttributeName;
    }

    /** Setter for property maxAttributeName.
     * @param maxAttributeName New value of property maxAttributeName.
     *
     */
    public void setMaxAttributeName(java.lang.String maxAttributeName) {
        this.maxAttributeName = maxAttributeName;
    }

    public void refresh() {
    }
}
