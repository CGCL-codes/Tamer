package net.sf.rcpforms.widgets2;

public interface IRCPSlider extends IRCPProgressBar {

    public static final String PROP_VALUE = PROP_SELECTION;

    /** @see int */
    public static final String PROP_INCREMENT = "increment";

    /** @see int */
    public static final String PROP_THUMB = "thumb";

    /** @see int */
    public static final String PROP_PAGE_INCREMENT = "pageIncrement";

    /** @see double */
    public static final String PROP_VALUE_FLOAT = "valueFloat";

    /** XXX */
    public void setThumb(int thumb);

    public int getThumb();

    /** XXX */
    public void setIncrement(int increment);

    public int getIncrement();

    /** XXX */
    public void setPageIncrement(int pageIncrement);

    public int getPageIncrement();

    /** XXX */
    public void setValueFloat(double valueFloat);

    public double getValueFloat();
}
