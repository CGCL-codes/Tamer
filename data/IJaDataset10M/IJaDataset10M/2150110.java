package org.jlense.util;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

/**
 * Delegates LAF methods calls to an underlying LAF.
 */
public class FilterLookAndFeel extends LookAndFeel {

    LookAndFeel m_laf = null;

    public FilterLookAndFeel(LookAndFeel laf) {
        m_laf = laf;
    }

    public String getName() {
        return m_laf.getName();
    }

    public String getID() {
        return m_laf.getID();
    }

    public String getDescription() {
        return m_laf.getDescription();
    }

    public boolean isNativeLookAndFeel() {
        return m_laf.isNativeLookAndFeel();
    }

    public boolean isSupportedLookAndFeel() {
        return m_laf.isSupportedLookAndFeel();
    }

    public void initialize() {
        m_laf.initialize();
    }

    public void uninitialize() {
        m_laf.uninitialize();
    }

    public UIDefaults getDefaults() {
        return m_laf.getDefaults();
    }

    public String toString() {
        return m_laf.toString();
    }
}
