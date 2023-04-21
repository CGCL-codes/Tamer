package net.sf.compositor;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.sf.compositor.util.Config;

class SpinnerGenerator extends Generator {

    private Integer m_value = 1;

    private Integer m_min = null;

    private Integer m_max = null;

    private Integer m_step = 1;

    SpinnerGenerator(final App app) {
        super(app, "javax.swing.JSpinner", false);
    }

    @Override
    protected void setAttributes(final App app, final JComponent component, final String windowName, final String componentName, final Config infoMap, final int indent) {
        if (infoMap.containsKey("min")) {
            final String s = infoMap.getProperty("min");
            try {
                m_min = Integer.valueOf(s);
            } catch (final NumberFormatException e) {
                s_log.warn("Unrecognised spinner min value: " + s);
            }
        }
        if (infoMap.containsKey("max")) {
            final String s = infoMap.getProperty("max");
            try {
                m_max = Integer.valueOf(s);
            } catch (final NumberFormatException e) {
                s_log.warn("Unrecognised spinner max value: " + s);
            }
        }
        if (infoMap.containsKey("step")) {
            final String s = infoMap.getProperty("step");
            try {
                m_step = Integer.valueOf(s);
            } catch (final NumberFormatException e) {
                s_log.warn("Unrecognised spinner max value: " + s);
            }
        }
    }

    /**
	 * Uses content to set the initial value.
	 */
    @Override
    void setContent(final Component component, final String content, final int indent) {
        try {
            m_value = Integer.valueOf(content);
        } catch (final NumberFormatException e) {
            s_log.warn("Unrecognised spinner initial value: " + content);
        }
    }

    /**
	 * Sets spinner model.
	 */
    @Override
    protected void finishMaking(final JComponent component, final int indent) {
        super.finishMaking(component, indent);
        final SpinnerNumberModel model = new SpinnerNumberModel(m_value, m_min, m_max, m_step);
        final JSpinner spinner = (JSpinner) component;
        spinner.setModel(model);
    }
}
