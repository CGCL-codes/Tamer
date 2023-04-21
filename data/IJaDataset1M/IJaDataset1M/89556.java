package com.jguigen.standard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
	 * Implements a Metal look and feel that uses an inverse mono
	 *
	 * @author Jashua Marinacci
	 * @author Chris Adamson
	 * from the OReilly book Swing Hacks
	 */
public class InverseMetalLookAndFeel extends MetalLookAndFeel {

    public InverseMetalLookAndFeel() {
        super();
        MetalTheme theme = new InverseTheme();
        setCurrentTheme(theme);
    }
}
