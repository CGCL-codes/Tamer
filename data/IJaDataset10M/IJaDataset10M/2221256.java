package no.ugland.utransprod.gui;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import no.ugland.utransprod.gui.handlers.PaidViewHandler;
import no.ugland.utransprod.model.PaidV;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Vindu for � sette forh�ndsbetaling
 * 
 * @author atle.brekka
 * 
 */
public class PaidView extends ApplyListView<PaidV> {

    /**
	 * 
	 */
    private JRadioButton radioButtonAll;

    /**
	 * 
	 */
    private JRadioButton radioButtonAssembly;

    /**
	 * 
	 */
    private JRadioButton radioButtonNotAssembly;

    /**
	 * @param aViewHandler
	 * @param printButton
	 */
    @Inject
    public PaidView(PaidViewHandler aViewHandler, @Assisted boolean printButton) {
        super(aViewHandler, printButton);
    }

    /**
	 * @see no.ugland.utransprod.gui.ApplyListView#initComponents(no.ugland.utransprod.gui.WindowInterface)
	 */
    @Override
    protected void initComponents(WindowInterface window) {
        super.initComponents(window);
        radioButtonAll = ((PaidViewHandler) viewHandler).getRadioButtonAll();
        radioButtonAssembly = ((PaidViewHandler) viewHandler).getRadioButtonAssembly();
        radioButtonNotAssembly = ((PaidViewHandler) viewHandler).getRadioButtonNotAssembly();
    }

    /**
	 * @see no.ugland.utransprod.gui.ApplyListView#buildPanel(no.ugland.utransprod.gui.WindowInterface)
	 */
    @Override
    public JComponent buildPanel(WindowInterface window) {
        initComponents(window);
        FormLayout layout = new FormLayout("10dlu," + viewHandler.getTableWidth() + ":grow,3dlu,p,10dlu", "10dlu,p,3dlu,top:p,3dlu,top:p,top:3dlu,top:p,3dlu,top:p,120dlu:grow,5dlu,p,3dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildFilterPanel(), cc.xy(2, 2));
        builder.add(checkBoxFilter, cc.xy(4, 4));
        builder.add(buildButtons(), cc.xywh(4, 6, 1, 6));
        builder.add(new JScrollPane(tableAppList), cc.xywh(2, 4, 1, 8));
        builder.add(ButtonBarFactory.buildCenteredBar(buttonRefresh, buttonCancel), cc.xyw(2, 13, 4));
        return builder.getPanel();
    }

    /**
	 * Lager panel med filterknapper
	 * 
	 * @return panel
	 */
    private JPanel buildFilterPanel() {
        FormLayout layout = new FormLayout("p,3dlu,p,3dlu,p,3dlu,p,3dlu,p", "p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Produktomr�de:", cc.xy(1, 1));
        builder.add(comboBoxProductAreaGroup, cc.xy(3, 1));
        builder.add(radioButtonAll, cc.xy(5, 1));
        builder.add(radioButtonAssembly, cc.xy(7, 1));
        builder.add(radioButtonNotAssembly, cc.xy(9, 1));
        return builder.getPanel();
    }

    public Dimension getWindowSize() {
        return viewHandler.getWindowSize();
    }
}
