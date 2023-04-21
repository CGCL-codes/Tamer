package net.sourceforge.circuitsmith.actions;

public class EdaActionCustomization {

    private final String m_accelerator;

    public EdaActionCustomization(final String accelerator) {
        m_accelerator = accelerator;
    }

    public final String getAccelerator() {
        return m_accelerator;
    }
}
