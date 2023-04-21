package com.lc.util.swing;

/**
 * Behavior for validating a {@link DialogDisplay}.
 * @author Laurent Caillette
 * @version $Revision: 1.1.1.1 $  $Date: 2002/02/19 22:12:04 $
 */
public interface IDialogValidator {

    /**
 * Decides if the displayed dialog should be validated.
 * @param message The message Object as passed to the <tt>JOptionPane</tt>
 *     class for a modal display.
 * @param choice The <tt>int</tt> value representing the user choice. May be
 *     defined by a {@link javax.swing.JOptionPane} constant, or by a custom
 *     definition. Its value is set to {@link #FORCE_CLOSING_CHOICE} when
 *     the user clicked on the 'close' widget.
 * @return <tt>true</tt> if the dialog should be closed, false otherwise. The
 *     returned value has no effect when <tt>choice</tt> parameter is set to
 *     <tt>FORCE_CLOSING_CHOICE</tt>.
 */
    boolean validate(Object message, Object choice);

    /**
 * 'Tag' value for the <tt>choice</tt> parameter of
 * <tt>validate(&nbsp;Object&nbsp;message,&nbsp;Object&nbsp;choice&nbsp;)</tt>
 * methode, avoiding passing a <tt>null</tt> value.
 */
    Object FORCE_CLOSING_CHOICE = new Object();
}
