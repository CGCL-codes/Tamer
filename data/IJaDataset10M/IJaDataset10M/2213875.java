package com.bluebrim.swing.client;

import java.util.EventListener;

/**
 	Interface f�r de klasser som vill lyssna p� CoChooserEvent fr�n en CoChooserPanel.
 */
public interface CoChooserSelectionListener extends EventListener {

    /**
 */
    public void valueChanged(CoChooserSelectionEvent e);
}
