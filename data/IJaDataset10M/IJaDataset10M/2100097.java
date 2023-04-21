package com.mw3d.swt.event;

import com.jme.math.Vector2f;
import com.mw3d.swt.EditorGUI;

/**
 * A class that will manage terrain in reall time.
 * @author Tareq doufish
 * Created on May 27, 2005
 */
public class TerrainEventManager {

    /** The brush strength */
    private int brushStrength = 10;

    private float brushRaduis = 10f;

    private EditorGUI gui;

    private Vector2f mousePosition;

    public TerrainEventManager(EditorGUI gui) {
        this.gui = gui;
    }

    /**
	 * Do the terrain mouse picking.
	 * @param x
	 * @param y
	 */
    public void doMousePick(int x, int y) {
        if (gui.getRuntime().getRegisterCursorTrimesh() != null) {
        }
    }
}
