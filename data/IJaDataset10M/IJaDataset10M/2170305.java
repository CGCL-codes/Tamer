package org.kubiki.gui;

import javax.swing.*;
import org.kubiki.gui.*;

public class BasicFrame extends JFrame implements RefreshableComponent {

    public void refresh(Object o) {
        repaint();
    }
}
