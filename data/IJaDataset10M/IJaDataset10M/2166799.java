package net.sf.rcpforms.widgets2;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The Class <code><i>RCPSimpleSlider</i></code>.
 * 
 * <p>
 * 
 * @author spicherc (10.01.2010)
 */
public class RCPSimpleSlider extends RCPControl implements IRCPSlider {

    public static final String PROP_THUMB = "thumb";

    public static final String PROP_INCREMENT = "increment";

    public static final String PROP_PAGE_INCREMENT = "pageIncrement";

    public static final String PROP_VALUE = PROP_SELECTION;

    public static final String PROP_VALUE_FLOAT = "valueFloat";

    private int m_increment;

    private int m_minimum;

    private int m_maximum;

    private int m_thumb;

    private int m_selection;

    private int m_pageIncrement;

    private double m_valueFloat;

    private volatile Slider swtSlider = null;

    /**
	 * creates a {@link Slider} control with the given text as content
	 */
    public RCPSimpleSlider() {
        this(SWT.DEFAULT);
    }

    public RCPSimpleSlider(final int style) {
        this(style, true, 0, 100, 0);
    }

    public RCPSimpleSlider(final boolean horizontal, final int minValue, final int maxValue, final int initValue) {
        this(SWT.DEFAULT, horizontal, minValue, maxValue, initValue);
    }

    public RCPSimpleSlider(final int style, final boolean horizontal, final int minValue, final int maxValue, final int initValue) {
        super(null, style);
        m_increment = 5;
        m_minimum = minValue;
        m_maximum = maxValue;
        m_thumb = 10;
        m_selection = initValue;
        m_pageIncrement = 25;
    }

    public final Slider getSWTSlider() {
        return getTypedWidget();
    }

    @Override
    protected Widget createWrappedWidget(final FormToolkit toolkit) {
        Slider result;
        if (getStyle() == SWT.DEFAULT) {
            result = new Slider(getSWTParent(), SWT.DEFAULT | SWT.HORIZONTAL);
        } else {
            result = new Slider(getSWTParent(), getStyle());
        }
        initSlider(result);
        return result;
    }

    protected void proUpdateSelection(final int value) {
        final int oldValue = m_selection;
        m_selection = value;
        firePropertyChange(PROP_VALUE, oldValue, value);
        final double oldFrac = m_valueFloat;
        m_valueFloat = value / (double) (getMaximum() - getMinimum());
        System.out.println("slider: value=" + value + ", frac=" + m_valueFloat);
        firePropertyChange(PROP_VALUE_FLOAT, -1.0, m_valueFloat);
    }

    protected void initSlider(final Slider slider) {
        slider.setValues(getSelection(), getMinimum(), getMaximum(), getThumb(), getIncrement(), getPageIncrement());
        slider.addSelectionListener(new SelectionListener() {

            public void widgetSelected(final SelectionEvent e) {
                proUpdateSelection(slider.getSelection());
            }

            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
    }

    @Override
    protected void subInitBeanSupport() {
    }

    public int getIncrement() {
        return m_increment;
    }

    public void setIncrement(final int increment) {
        final Object oldValue = m_increment;
        m_increment = increment;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setIncrement(increment);
        }
        firePropertyChange(PROP_INCREMENT, oldValue, m_increment);
    }

    public int getMinimum() {
        return m_minimum;
    }

    public void setMinimum(final int minimum) {
        final Object oldValue = m_minimum;
        m_minimum = minimum;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setMinimum(minimum);
        }
        firePropertyChange(PROP_MINIMUM, oldValue, m_minimum);
    }

    public int getMaximum() {
        return m_maximum;
    }

    public void setMaximum(final int maximum) {
        final Object oldValue = m_maximum;
        m_maximum = maximum;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setMaximum(maximum);
        }
        firePropertyChange(PROP_MAXIMUM, oldValue, m_maximum);
    }

    public int getThumb() {
        return m_thumb;
    }

    public void setThumb(final int thumb) {
        final Object oldValue = m_thumb;
        m_thumb = thumb;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setThumb(thumb);
        }
        firePropertyChange(PROP_THUMB, oldValue, m_thumb);
    }

    public int getSelection() {
        return m_selection;
    }

    public void setSelection(final int selection) {
        final Object oldValue = m_selection;
        m_selection = selection;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setSelection(selection);
        }
        firePropertyChange(PROP_SELECTION, oldValue, m_selection);
        final double frac = (double) selection / (double) (getMaximum() - getMinimum());
        setValueFloat0(frac);
    }

    public double getValueFloat() {
        return m_valueFloat;
    }

    public void setValueFloat(final double valueFloat) {
        setValue(getMinimum() + (int) (valueFloat * (getMaximum() - getMinimum())));
        setValueFloat0(valueFloat);
    }

    protected void setValueFloat0(final double valueFloat) {
        if (valueFloat < 0.0 || valueFloat >= 1.0) {
            throw new IllegalArgumentException("value-float must be in [0.0 .. 1.0] but is: " + valueFloat);
        }
        final double oldValue = m_valueFloat;
        m_valueFloat = valueFloat;
        firePropertyChange(PROP_VALUE_FLOAT, oldValue, m_valueFloat);
    }

    public int getValue() {
        return getSelection();
    }

    public void setValue(final int value) {
        setSelection(value);
    }

    public int getPageIncrement() {
        return m_pageIncrement;
    }

    public void setPageIncrement(final int pageIncrement) {
        final Object oldValue = m_pageIncrement;
        m_pageIncrement = pageIncrement;
        if ((swtSlider = getSWTSlider()) != null) {
            swtSlider.setPageIncrement(pageIncrement);
        }
        firePropertyChange(PROP_PAGE_INCREMENT, oldValue, m_pageIncrement);
    }
}
