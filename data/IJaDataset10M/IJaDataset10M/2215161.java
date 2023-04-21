package org.plazmaforge.framework.client.swing.gui;

import javax.swing.*;

/**
 * @author Oleh Hapon
 * Date: 2.10.2003
 * Time: 13:55:59
 */
public class DateSpinner extends JSpinner {

    public DateSpinner() {
        setModel(new SpinnerDateModel());
    }
}
