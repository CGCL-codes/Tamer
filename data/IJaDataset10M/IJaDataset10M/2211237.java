package org.vrforcad.controller.gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Open/Import file chooser.
 * 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 *
 */
public class OpenFileChooser extends JFileChooser implements OpenFileFormat {

    private static final long serialVersionUID = 1L;

    private FileNameExtensionFilter allFilterOpen = new FileNameExtensionFilter("STL, DXF, X3D & VFC files", "stl", "dxf", "x3d", "vfc");

    private FileNameExtensionFilter vfcFilterOpen = new FileNameExtensionFilter("vfc", "vfc");

    private FileNameExtensionFilter dxfFilterOpen = new FileNameExtensionFilter("dxf", "dxf");

    private FileNameExtensionFilter x3dFilterOpen = new FileNameExtensionFilter("x3d", "x3d");

    private FileNameExtensionFilter stlFilterOpen = new FileNameExtensionFilter("stl", "stl");

    /**
	 * Default constructor.
	 */
    public OpenFileChooser() {
        initialize(ALL);
    }

    /**
	 * Constructor that accept just one file format.
	 * @param format
	 */
    public OpenFileChooser(int format) {
        initialize(format);
    }

    /**
	 * Initialize.
	 * @param format
	 */
    private void initialize(int format) {
        setMultiSelectionEnabled(true);
        if (format == STL || format == ALL) addChoosableFileFilter(stlFilterOpen);
        if (format == VFC || format == ALL) addChoosableFileFilter(vfcFilterOpen);
        if (format == X3D || format == ALL) addChoosableFileFilter(x3dFilterOpen);
        if (format == DXF || format == ALL) addChoosableFileFilter(dxfFilterOpen);
        if (format == ALL) addChoosableFileFilter(allFilterOpen);
    }
}
