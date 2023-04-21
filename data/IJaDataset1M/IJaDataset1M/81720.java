package org.commonmap.cmarender.gui;

import org.commonmap.gui.Logbox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.commonmap.cmarender.Cmarender;
import org.commonmap.cmarender.CmarenderEventListener;
import static org.commonmap.util.Translator.tr;

/**
 *
 * @author nazo
 * @version $Id: CmarenderPanel.java 48 2010-02-24 00:29:31Z nazotoko $
 */
public class CmarenderPanel extends JPanel implements ActionListener, CmarenderEventListener {

    /** gui */
    protected JButton buttonStart;

    protected JButton buttonStop;

    protected Logbox logbox;

    protected Cmarender cmarender;

    /**
     * 
     * @param cmr Cmarender object
     */
    public CmarenderPanel(Cmarender cmr) {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        cmarender = cmr;
        logbox = new Logbox();
        cmarender.addLogHandler(logbox.getLogHandler());
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        add(logbox, c);
        buttonStart = new JButton(tr("start"));
        buttonStart.setEnabled(false);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        add(buttonStart, c);
        cmr.addEventListener(this);
        buttonStop = new JButton(tr("stop"));
        buttonStop.setEnabled(false);
        c.gridx = 1;
        c.gridy = 2;
        add(buttonStop, c);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(buttonStart)) {
            cmarender.start();
        } else if (src.equals(buttonStop)) {
            cmarender.stop();
            buttonStop.setEnabled(false);
            buttonStop.removeActionListener(this);
        }
    }

    public void cmarenderStatusChanged(int s) {
        switch(s) {
            case Cmarender.STATUS_READY:
                buttonStart.setEnabled(true);
                buttonStart.addActionListener(this);
                buttonStop.setEnabled(false);
                buttonStop.removeActionListener(this);
                break;
            case Cmarender.STATUS_RUN:
                buttonStart.setEnabled(false);
                buttonStart.removeActionListener(this);
                buttonStop.addActionListener(this);
                buttonStop.setEnabled(true);
                break;
            case Cmarender.STATUS_NOTREADY:
                buttonStart.setEnabled(false);
                buttonStart.removeActionListener(this);
                buttonStop.setEnabled(false);
                buttonStop.removeActionListener(this);
                break;
        }
    }
}
