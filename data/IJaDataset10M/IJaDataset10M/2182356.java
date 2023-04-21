package es.ulpgc.dis.heuristicide.rcp.ui.metaheuristic;

import java.util.ArrayList;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import es.ulpgc.dis.heuriskein.model.solver.MetaHeuristic;
import es.ulpgc.dis.heuristicide.project.ConfigurationManager;

public class MetaheuristicPage extends WizardPage {

    protected Combo typesCombo;

    protected MetaHeuristic metaheuristic;

    protected String representation;

    protected MetaheuristicPage(String arg0) {
        super(arg0);
        setTitle("Metaheuristic's Configuration");
        setDescription("Select desired representation");
    }

    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        comp.setLayout(layout);
        Label label = new Label(comp, SWT.NONE);
        label.setText("Metaheuristic Type");
        typesCombo = new Combo(comp, SWT.NONE);
        if (metaheuristic.getRepresentation() == null) {
            ArrayList<String> list = ConfigurationManager.getRepresentationsList();
            for (String str : list) {
                typesCombo.add(str);
            }
            typesCombo.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    if (typesCombo.getSelectionIndex() != -1) {
                        MetaheuristicPage.this.setPageComplete(true);
                    } else {
                        MetaheuristicPage.this.setPageComplete(false);
                    }
                }
            });
            typesCombo.select(0);
        }
        setControl(comp);
    }
}
