package com.iver.cit.gvsig.geoprocess.core.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.gvsig.gui.beans.AcceptCancelPanel;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

public class SpatialJoinNumericFieldSelection extends JDialog implements SpatialJoinNumFieldIF {

    private static final long serialVersionUID = -9031708821779458450L;

    private FLyrVect inputLayer;

    private JPanel jContentPane;

    private JPanel acceptCancelPanel;

    private boolean ok = false;

    /**
	 * This is the default constructor
	 */
    public SpatialJoinNumericFieldSelection(FLyrVect inputLayer) {
        super((JFrame) PluginServices.getMainFrame(), true);
        this.inputLayer = inputLayer;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setTitle(PluginServices.getText(this, "Funciones_Sumarizacion"));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getJContentPane(), BorderLayout.CENTER);
        getContentPane().add(getAcceptCancelPanel(), BorderLayout.SOUTH);
    }

    private JPanel getAcceptCancelPanel() {
        if (acceptCancelPanel == null) {
            acceptCancelPanel = new AcceptCancelPanel();
            ((AcceptCancelPanel) acceptCancelPanel).setOkButtonActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    ok();
                }
            });
            ((AcceptCancelPanel) acceptCancelPanel).setCancelButtonActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    setVisible(false);
                }
            });
        }
        return acceptCancelPanel;
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new NumericFieldFunctionsControl(inputLayer);
        }
        return jContentPane;
    }

    public void ok() {
        ok = true;
        setVisible(false);
    }

    public Map getSumarizationFunctions() {
        return ((NumericFieldFunctionsControl) jContentPane).getFieldFunctionMap();
    }

    public boolean isOk() {
        return ok;
    }
}
