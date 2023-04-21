package edu.ucla.stat.SOCR.chart.demo;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import edu.ucla.stat.SOCR.chart.j3d.Chart3D;

/** 
 * create a empty chart
 * 
 * @author jenny
 *
 */
public class NoChart3D extends Chart3D {

    /**
	 * @uml.property  name="toolBar"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
    protected JToolBar toolBar;

    public void init() {
        super.init();
        graphPanel = new JPanel();
    }
}
