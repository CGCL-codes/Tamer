package com.bluebrim.swing.client;

import java.util.EventListener;

/**
 	Interface f�r de klasser som vill lyssna p� CoChooserEvent fr�n en CoChooserPanel.
 */
public interface CoChooserEventListener extends EventListener {

    /**
 */
    public void handleChooserEvent(CoChooserEvent e);
}
