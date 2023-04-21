package de.grogra.docking;

public interface DockComponent {

    java.awt.Container getParent();

    int getHeight();

    int getWidth();

    DockContainer getDockParent();
}
