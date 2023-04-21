package javax.microedition.lcdui;

/**
 * Look and Feel interface used by Gauge.
 * <p>
 * See <a href="doc-files/naming.html">Naming Conventions</a>
 * for information about method naming conventions.
 */
interface GaugeLF extends ItemLF {

    /**
     * Notifies L&F of a value change in the corresponding Gauge.
     * @param oldValue - the old value set in the Gauge
     * @param value - the new value set in the Gauge
     */
    void lSetValue(int oldValue, int value);

    /**
     * Notifies L&F of a maximum value change in the corresponding Gauge.
     * @param oldMaxValue - the old maximum value set in the Gauge
     * @param maxValue - the new maximum value set in the Gauge
     */
    void lSetMaxValue(int oldMaxValue, int maxValue);

    /**
     * Returns the current value in the gauge.
     * @return the current value in the Gauge
     */
    int lGetValue();
}
