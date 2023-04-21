package net.sourceforge.processdash.tool.export.ui.wizard;

import net.sourceforge.processdash.tool.export.mgr.AbstractInstruction;
import net.sourceforge.processdash.tool.export.mgr.ExportInstructionDispatcher;
import net.sourceforge.processdash.tool.export.mgr.ExportManager;
import net.sourceforge.processdash.tool.export.mgr.ExportMetricsFileInstruction;

public class ManageExportsPanel extends ManagePanel {

    public ManageExportsPanel(Wizard wizard) {
        super(wizard, ExportManager.getInstance(), "Export.Manage");
    }

    protected WizardPanel getAddPanel() {
        return new EditExportMetricsFilePanel(wizard, null, true);
    }

    protected WizardPanel getEditPanel(AbstractInstruction instr) {
        return (WizardPanel) instr.dispatch(editPanelGenerator);
    }

    private class EditPanelGenerator implements ExportInstructionDispatcher {

        public Object dispatch(ExportMetricsFileInstruction instr) {
            return new EditExportMetricsFilePanel(wizard, instr, true);
        }
    }

    private EditPanelGenerator editPanelGenerator = new EditPanelGenerator();
}
