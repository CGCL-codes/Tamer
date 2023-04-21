package org.gjt.btools.gui.lookandfeel;

/**
 * Represents an object that can change the swing look and feel to a
 * given look and feel.
 */
public interface LookAndFeelSetter {

    /**
     * Changes the swing look and feel to the given look and feel.
     * It is assumed that no action at all has already been taken to set
     * the look and feel.
     * <p>
     * This routine will generally call
     * <tt>lookAndFeel.setLookAndFeel()</tt> as well as
     * <tt>lookAndFeel.updateLookAndFeel()</tt> for each top-level
     * container.
     *
     * @param lookAndFeel the look and feel to change to.
     * @return <tt>true</tt> if and only if the change to the new look
     * and feel was successful.
     */
    public boolean setLookAndFeel(LookAndFeelItem lookAndFeel);
}
