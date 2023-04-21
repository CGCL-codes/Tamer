package com.oat.explorer.domains.cfo.gui.panels;

import javax.swing.JPanel;
import com.oat.Domain;
import com.oat.domains.cfo.CFODomain;
import com.oat.explorer.domains.cfo.gui.plots.CFOPlot;
import com.oat.explorer.gui.panels.MasterPanel;

/**
 * Type: FuncOptMasterPanel<br/>
 * Date: 24/11/2006<br/>
 * <br/>
 * Description:
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 10/07/2007   JBrownlee   Modified to use a standardised plot panel
 * </pre>
 */
public class CFOMasterPanel extends MasterPanel {

    protected CFOPlot plot;

    public CFOMasterPanel(Domain domain) {
        super(domain);
    }

    @Override
    protected JPanel[] prepareAdditionalCentralPanels() {
        plot = new CFOPlot();
        return new JPanel[] { plot };
    }

    @Override
    protected void prepareAdditionalListeners() {
        controlPanel.registerClearableListener(plot);
        problemPanel.registerProblemChangedListener(plot);
        problemPanel.registerNewSolutionListener(plot);
    }
}
