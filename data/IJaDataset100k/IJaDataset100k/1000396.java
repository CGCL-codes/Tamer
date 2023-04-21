package org.vrforcad;

/**
 * The main class :)
 *  
 * @version 1.1 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class VRforCAD {

    public static final int MODE_CAD = 0;

    public static final int MODE_VISUALIZATION = 1;

    public static final int MODE_ROBOTICS = 2;

    public static final int MODE_MEDICINE = 3;

    public static final int MODE_FREEFORM = 4;

    public static final int GEOMETRY_TRIANGLES = 0;

    public static final int GEOMETRY_QUADS = 1;

    public static final int GEOMETRY_TRIANGLES_BY_REF = 2;

    public static final int GEOMETRY_QUADS_BY_REF = 3;

    private StartingLogo sl = null;

    private ModelInterface mi;

    private ControllerInterface ci;

    /**
	 * Default constructor.
	 */
    public VRforCAD() {
        showStartingLogo();
        initialize();
    }

    /**
	 * Method to initialize the Graphical User Interface.
	 */
    private void initialize() {
        mi = new VRforCADModel();
        ci = new VRforCADController(this, mi);
    }

    /**
	 * Method to show the VRforCAD logo while the application starts.
	 */
    private void showStartingLogo() {
        sl = new StartingLogo(null);
        sl.setLocationRelativeTo(null);
        sl.setVisible(true);
    }

    /**
	 * Method that disposes the VRforCAD starting Logo.
	 */
    public void disposeStartingLogo() {
        sl.dispose();
    }
}
