package de.javacus.grafmach.threeD.sym;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.List;

/**
 * ermoeglicht das Projizieren im 3D-Raum
 * 
 * @author 2009 Burkhard Loesel
 * 
 */
public interface Projectable {

    /**
	 * y-Axis not yet mirrored
	 * 
	 * @return
	 */
    public List<GeneralPath> getGeneralPathsNormal();

    /**
	 * y-Axis not yet mirrored, but projected
	 * 
	 * @return
	 */
    public List<GeneralPath> getGeneralPaths();

    public Color getColor();

    public float getTransparency();
}
