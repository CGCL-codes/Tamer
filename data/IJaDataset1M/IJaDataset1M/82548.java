package com.bluebrim.layout.impl.client;

import javax.swing.*;
import com.bluebrim.base.shared.*;

/**
 * @author G�ran St�ck
 */
public abstract class CoAbstractViewPanel extends JComponent {

    public abstract CoView getView();

    public abstract void setView(CoView view);
}
