package jaxlib.swing.binding;

import java.math.BigDecimal;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import jaxlib.beans.BeanProperty;
import jaxlib.conversion.Converters;
import jaxlib.logging.Log;
import jaxlib.unit.DecimalValue;
import jaxlib.unit.One;
import jaxlib.unit.Unit;
import jaxlib.unit.UnitFormat;
import jaxlib.unit.Value;

/**
 * Binds a bean property of type {@link Value} to a text field and label.
 * The text field of bound to the numeric value and the label to the {@link Value#unit unit} symbol.
 * <p>
 * The unit symbol will be invisible if the unit is {@link One#ONE}.
 * </p>
 *
 * @author  jw
 * @version $Id: QuantityBinding.java 2947 2011-07-04 12:36:56Z joerg_wassmer $
 * @since   JaXLib 1.0
 */
@SuppressWarnings("unchecked")
public final class QuantityBinding<A> extends TextBinding<A> {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private static final Log log = Log.logger();

    /**
   * @serial
   * @since JaXLib 1.0
   */
    @CheckForNull
    private final JLabel unitLabel;

    QuantityBinding(final A leftBean, final String leftPropertyName, final JTextComponent rightComponent, final BeanProperty rightProperty, @Nullable final JLabel unitLabel) {
        super(leftBean, leftPropertyName, rightComponent, rightProperty);
        this.unitLabel = unitLabel;
        init(QuantityBinding.class);
    }

    @Override
    final void setComponentEnabled(final boolean v) {
        super.setComponentEnabled(v);
        if ((this.unitLabel != null) && (this.unitLabel.isEnabled() != v)) this.unitLabel.setEnabled(v);
    }

    @Override
    protected void callRightSetter(final Object v) throws Throwable {
        if (v == null) super.callRightSetter(null); else if (!(v instanceof Value) && (v instanceof Number)) super.callRightSetter(v); else {
            final Value q = Converters.convert(v, Value.class);
            if (q == null) super.callRightSetter(null); else {
                super.callRightSetter(q.getNumber());
                final JLabel unitLabel = getUnitLabel();
                if (unitLabel != null) {
                    final String u = q.getUnit().getSymbol();
                    if ((u == null) || u.isEmpty() || u.equals("1")) {
                        final String old = unitLabel.getText();
                        if ((old != null) && !old.isEmpty()) unitLabel.setText(" ");
                    } else if (!u.equals(unitLabel.getText())) unitLabel.setText(u);
                }
            }
        }
    }

    @Override
    protected Object convertLeftToRight(final Object leftValue) throws Exception {
        if (leftValue instanceof Value) return leftValue;
        return super.convertLeftToRight(leftValue);
    }

    @Override
    public Value getLeftValue() {
        return Converters.convert(super.getLeftValue(), Value.class);
    }

    @Override
    public Value getRightValue() {
        final JLabel unitLabel = getUnitLabel();
        String u = (unitLabel == null) ? null : unitLabel.getText();
        if (u != null) {
            u = u.trim();
            if (u.length() == 0) u = null;
        }
        Unit unit = null;
        try {
            if (u != null) unit = UnitFormat.getStandardInstance().parse(u);
        } catch (final Exception ex) {
            log.warning("ignoring exception", ex);
        }
        if (unit == null) {
            final Value v = getLeftValue();
            if (v != null) unit = v.unit;
        }
        Object v = super.getRightValue();
        if (v != null) {
            final BigDecimal n;
            try {
                n = Converters.convert(v, BigDecimal.class);
            } catch (final RuntimeException ex) {
                log.fine("ignoring exception", ex);
                return null;
            }
            v = null;
            if (n != null) return new DecimalValue(n, (unit == null) ? One.ONE : unit);
        }
        return null;
    }

    public JLabel getUnitLabel() {
        return this.unitLabel;
    }
}
