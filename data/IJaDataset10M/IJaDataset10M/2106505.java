package com.mebigfatguy.patchanim.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.mebigfatguy.patchanim.gui.events.ExportEvent;
import com.mebigfatguy.patchanim.gui.events.ExportListener;
import com.mebigfatguy.patchanim.main.PatchAnimBundle;

/**
 * a progress dialog for exporting images
 */
public class ExportFrame extends JDialog implements ExportListener {

    private static final long serialVersionUID = 3111097499092146056L;

    private JProgressBar bar;

    /**
	 * constructs the export progress dialog
	 */
    public ExportFrame() {
        initComponents();
    }

    /**
	 * initialize the gui components
	 */
    private void initComponents() {
        ResourceBundle rb = PatchAnimBundle.getBundle();
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(4, 4));
        bar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        cp.add(bar, BorderLayout.CENTER);
        pack();
        setTitle(rb.getString(PatchAnimBundle.EXPORTINGFILE));
    }

    /**
	 * implements the ExportListener to update the progress bar
	 * 
	 * @param ee the export event describing what file number is being exported
	 */
    public void imageExported(final ExportEvent ee) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                bar.setMaximum(ee.getTotalImages());
                bar.setValue(ee.getCurrentImage());
            }
        });
    }
}
