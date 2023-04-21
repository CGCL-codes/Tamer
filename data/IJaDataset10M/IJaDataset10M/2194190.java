package com.pennychecker.presenter.swing;

import javax.swing.JComponent;
import com.pennychecker.presenter.Display;

/**
 * 
 * @author David Peterson, refactored by Steffen Kaempke
 *
 */
public interface SwingDisplay extends Display {

    JComponent asComponent();
}
