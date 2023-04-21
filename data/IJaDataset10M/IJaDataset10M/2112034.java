package net.za.grasser.jface.viewers;

import java.text.MessageFormat;
import net.za.grasser.duplicate.util.Chars;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;

/**
 * A cell editor that presents slider to input int or double values. The cell editor's value is the selection of the <code>Scale</code>.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class ScaleCellEditor extends CellEditor {

    /**
   * <code>defaultStyle</code> ScaleCellEditor - Default ScaleCellEditor style
   */
    private static final int defaultStyle = SWT.NONE;

    /**
   * <code>increment</code> ScaleCellEditor - increment value of the <code>Scale</code>. Defines the jumps between one value and the next.
   */
    double increment = 0.01;

    /**
   * <code>maximum</code> ScaleCellEditor - maximum value of the <code>Scale</code>.
   */
    double maximum = 100.0;

    /**
   * <code>minimum</code> ScaleCellEditor - minimum value of the <code>Scale</code>.
   */
    double minimum = 0.0;

    /**
   * <code>pageIncrement</code> ScaleCellEditor - page increment value of the <code>Scale</code>. Defines the space between ticks.
   */
    double pageIncrement = 10.0;

    /**
   * <code>precision</code> ScaleCellEditor - used so that this editor can also input double values. The int value of the <code>Scale</code> is scaled so that
   * doubles with the given precision can be entered.
   */
    int precision = 2;

    /**
   * <code>multiplier</code> ScaleCellEditor - precision multiplier
   */
    double multiplier = Math.pow(10.0, precision);

    /**
   * <code>scale</code> ScaleCellEditor - the <code>Scale</code> widget.
   */
    Scale scale;

    /**
   * <code>selection</code> ScaleCellEditor - The selection/value of the underlying <code>Scale</code>.
   */
    double selection = 0.0;

    /**
   * Creates a new cell editor with no control. Initially, the cell editor has no cell validator.
   * 
   * @since 2.1
   * @see CellEditor#setStyle
   * @see CellEditor#create
   * @see CellEditor#dispose
   */
    public ScaleCellEditor() {
        setStyle(defaultStyle);
    }

    /**
   * Creates a new cell editor with a <code>Scale</code> where values are scaled to double and parented under the given control. The cell editor value is the
   * selection value of the <code>Scale</code>. Initially, the cell editor has no cell validator and the scale is zeroed.
   * 
   * @param parent - the parent control
   */
    public ScaleCellEditor(Composite parent) {
        super(parent, defaultStyle);
    }

    /**
   * Creates a new cell editor with a <code>Scale</code> where values are scaled to double and parented under the given control. The cell editor value is the
   * selection value of the <code>Scale</code>. Initially, the cell editor has no cell validator and the scale is zeroed.
   * 
   * @param parent - the parent control
   * @param style - the style of the component
   */
    public ScaleCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    /**
   * Applies the currently selected value and deactivates the cell editor
   */
    void applyEditorValueAndDeactivate() {
        selection = scale.getSelection() / multiplier;
        Object newValue = doGetValue();
        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);
        if (!isValid) {
            setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { new Double(selection) }));
        }
        fireApplyEditorValue();
        deactivate();
    }

    /**
   * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
   */
    @Override
    protected Control createControl(Composite parent) {
        scale = new Scale(parent, getStyle());
        setDefaultValues();
        scale.setFont(parent.getFont());
        scale.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });
        scale.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                applyEditorValueAndDeactivate();
            }

            @Override
            public void widgetSelected(SelectionEvent event) {
                selection = scale.getSelection() / multiplier;
            }
        });
        scale.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
        scale.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                ScaleCellEditor.this.focusLost();
            }
        });
        return scale;
    }

    /**
   * The <code>ScaleCellEditor</code> implementation of this <code>CellEditor</code> framework method returns the the current selection of the
   * <code>Scale</code> widget.
   * 
   * @return the selection wrapped as a <code>Double</code>
   */
    @Override
    protected Object doGetValue() {
        return new Double(selection);
    }

    /**
   * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
   */
    @Override
    protected void doSetFocus() {
        scale.setFocus();
    }

    /**
   * The <code>ScaleCellEditor</code> implementation of this <code>CellEditor</code> framework method accepts a Integer or a Double value.
   * 
   * @param value the zero-based index of the selection wrapped as an <code>Integer</code>
   */
    @Override
    protected void doSetValue(Object value) {
        Assert.isTrue(scale != null && (value instanceof Number));
        setSelection(((Number) value).doubleValue());
        System.out.println("doSetValue(" + value + ") -> " + selection);
        System.out.println(toString());
    }

    /**
   * @see org.eclipse.jface.viewers.CellEditor#focusLost()
   */
    @Override
    protected void focusLost() {
        if (isActivated()) {
            applyEditorValueAndDeactivate();
        }
    }

    /**
   * @return double - Returns the increment.
   */
    public double getIncrement() {
        return increment;
    }

    /**
   * The <code>ScaleCellEditor</code> implementation of this <code>CellEditor</code> framework method sets the minimum width of the cell. The minimum width is
   * (maximum-minimum/pageIncrement)*2 if <code>scale</code> is not <code>null</code> or <code>disposed</code> else it is 60 pixels to make sure that some ticks
   * are visible.
   */
    @Override
    public LayoutData getLayoutData() {
        LayoutData layoutData = super.getLayoutData();
        if ((scale == null) || scale.isDisposed()) {
            layoutData.minimumWidth = 60;
        } else {
            layoutData.minimumWidth = (int) (((maximum - minimum) / pageIncrement) * 2.0);
        }
        return layoutData;
    }

    /**
   * @return double - Returns the maximum.
   */
    public double getMaximum() {
        return maximum;
    }

    /**
   * @return double - Returns the minimum.
   */
    public double getMinimum() {
        return minimum;
    }

    /**
   * @return double - Returns the pageIncrement.
   */
    public double getPageIncrement() {
        return pageIncrement;
    }

    /**
   * @return int - Returns the precision.
   */
    public int getPrecision() {
        return precision;
    }

    /**
   * @return double - Returns the selection.
   */
    public double getSelection() {
        return selection;
    }

    /**
   * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
   */
    @Override
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '') {
            fireCancelEditor();
        } else if (keyEvent.character == '\t') {
            applyEditorValueAndDeactivate();
        }
    }

    /**
   * Initialises the <code>Scale</code> with default values.
   */
    private void setDefaultValues() {
        if (scale != null) {
            selection = minimum;
            scale.setMinimum((int) (minimum * multiplier));
            scale.setMaximum((int) (maximum * multiplier));
            scale.setSelection((int) (selection * multiplier));
            scale.setPageIncrement((int) (pageIncrement * multiplier));
            scale.setIncrement((int) (increment * multiplier));
            setValueValid(true);
        }
    }

    /**
   * Sets the increment to the value of pIncrement.
   * 
   * @param pIncrement double - The new value.
   */
    public void setIncrement(double pIncrement) {
        increment = pIncrement;
        scale.setIncrement((int) (increment * multiplier));
    }

    /**
   * Sets the maximum to the value of pMaximum.
   * 
   * @param pMaximum double - The new value.
   */
    public void setMaximum(double pMaximum) {
        maximum = pMaximum;
        scale.setMaximum((int) (maximum * multiplier));
    }

    /**
   * Sets the minimum to the value of pMinimum.
   * 
   * @param pMinimum double - The new value.
   */
    public void setMinimum(double pMinimum) {
        minimum = pMinimum;
        scale.setMinimum((int) (minimum * multiplier));
    }

    /**
   * Sets the pageIncrement to the value of pPageIncrement.
   * 
   * @param pPageIncrement double - The new value.
   */
    public void setPageIncrement(double pPageIncrement) {
        pageIncrement = pPageIncrement;
        scale.setPageIncrement((int) (pageIncrement * multiplier));
    }

    /**
   * Sets the precision to the value of pPrecision.
   * 
   * @param pPrecision int - The new value.
   */
    public void setPrecision(int pPrecision) {
        double store = multiplier;
        precision = pPrecision;
        multiplier = Math.pow(10.0, precision);
        scale.setMinimum((int) (minimum * multiplier));
        scale.setMaximum((int) (maximum * multiplier));
        scale.setSelection((int) (selection * multiplier));
        scale.setPageIncrement((int) (pageIncrement * multiplier));
        scale.setIncrement((int) (increment * multiplier));
    }

    /**
   * Sets the selection to the value of pSelection.
   * 
   * @param pSelection double - The new value.
   */
    public void setSelection(double pSelection) {
        selection = pSelection;
        scale.setSelection((int) (selection * multiplier));
    }

    /**
   * @see java.lang.Object#toString()
   */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append(Chars.OPN_BRACKET).append(minimum).append(Chars.SLASH).append(scale.getMinimum());
        sb.append(Chars.COMMA).append(maximum).append(Chars.SLASH).append(scale.getMaximum());
        sb.append(Chars.COMMA).append(selection).append(Chars.SLASH).append(scale.getSelection());
        sb.append(Chars.COMMA).append(increment).append(Chars.SLASH).append(scale.getIncrement());
        sb.append(Chars.COMMA).append(pageIncrement).append(Chars.SLASH).append(scale.getPageIncrement());
        sb.append(Chars.COMMA).append(precision).append(Chars.COLON).append(multiplier);
        sb.append(Chars.CLS_BRACKET);
        return sb.toString();
    }
}
