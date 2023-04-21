package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.DoubleParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoubleParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R>, ActionListener {

    @NotNull
    private final JSpinner value;

    @NotNull
    private final AbstractButton config;

    @NotNull
    private final DoubleParameter<G, A, R> parameter;

    public DoubleParameterView(@NotNull final DoubleParameter<G, A, R> param) {
        parameter = param;
        final SpinnerModel mdl = new SpinnerNumberModel(0.0, param.getMin(), param.getMax(), (param.getMax() - param.getMin()) / 100.0);
        value = new TooltipSpinner(mdl);
        try {
            value.setValue(param.getValue());
        } catch (final IllegalArgumentException e) {
        }
        value.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                parameter.setValue((Double) ((SpinnerNumberModel) value.getModel()).getNumber());
            }
        });
        config = new JButton("...");
        config.setBorderPainted(false);
        config.setActionCommand("Config");
        config.addActionListener(this);
        updateTooltip();
    }

    @NotNull
    @Override
    public JComponent getConfigComponent() {
        return config;
    }

    @NotNull
    @Override
    public JComponent getValueComponent() {
        return value;
    }

    private void updateTooltip() {
        final String toolTip = "[" + Double.toString(parameter.getMin()) + "," + Double.toString(parameter.getMax()) + "]";
        config.setToolTipText(toolTip);
        value.setToolTipText(toolTip);
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (e.getActionCommand().equals("Config")) {
            final String min = JOptionPane.showInputDialog("Minimum value:", parameter.getMin());
            final String max = JOptionPane.showInputDialog("Maximum value:", parameter.getMax());
            try {
                parameter.setMax(Double.parseDouble(max));
                parameter.setMin(Double.parseDouble(min));
                ((SpinnerNumberModel) value.getModel()).setMinimum(parameter.getMin());
                ((SpinnerNumberModel) value.getModel()).setMaximum(parameter.getMax());
                updateTooltip();
            } catch (final Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not change Double configuration");
            }
        }
    }

    /**
     * Extends JSpinner to work around it's tooltip bug This bug has been fixed
     * since Java 5.0 but we need this workaround for java 4.x users.
     * @author tchize
     */
    private static class TooltipSpinner extends JSpinner {

        private static final long serialVersionUID = 1L;

        private TooltipSpinner(@NotNull final SpinnerModel model) {
            super(model);
        }

        /**
         * This override the JSpinner method to force the tooltip in all
         * sub-components.
         * @param text the tooltip to show
         */
        @Override
        public void setToolTipText(@Nullable final String text) {
            forceTooltip(this, text);
        }

        private void forceTooltip(@NotNull final JComponent c, @Nullable final String tip) {
            if (c == this) {
                super.setToolTipText(tip);
            } else {
                c.setToolTipText(tip);
            }
            final Component[] components = c.getComponents();
            for (final Component component : components) {
                if (component instanceof JComponent) {
                    forceTooltip((JComponent) component, tip);
                }
            }
        }
    }
}
