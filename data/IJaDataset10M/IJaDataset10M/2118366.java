package com.iver.cit.gvsig;

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import com.hardcode.driverManager.Driver;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.exceptions.table.StartEditingTableException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IWriteable;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableEditStartExtension extends Extension {

    /**
     * @see com.iver.andami.plugins.IExtension#initialize()
     */
    public void initialize() {
    }

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
        if ("STARTEDIT".equals(actionCommand)) {
            IWindow v = PluginServices.getMDIManager().getActiveWindow();
            try {
                Table table = (Table) v;
                Driver drv = table.getModel().getModelo().getOriginalDriver();
                if (drv instanceof IWriteable) {
                    IWriter writer = ((IWriteable) drv).getWriter();
                    if (!writer.canSaveEdits()) {
                        JOptionPane.showMessageDialog((Component) PluginServices.getMDIManager().getActiveWindow(), PluginServices.getText(this, "this_table_is_not_self_editable"), PluginServices.getText(this, "warning"), JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog((Component) PluginServices.getMDIManager().getActiveWindow(), PluginServices.getText(this, "this_table_is_not_self_editable"), PluginServices.getText(this, "warning"), JOptionPane.WARNING_MESSAGE);
                    return;
                }
                table.startEditing();
            } catch (StartEditingTableException e) {
                e.printStackTrace();
            } catch (HeadlessException e) {
                e.printStackTrace();
            } catch (VisitorException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v == null) {
            return false;
        }
        if (v instanceof Table) {
            Table t = (Table) v;
            IEditableSource ies = t.getModel().getModelo();
            if (t.getModel().getLinkTable() != null) return false;
            if (ies.getOriginalDriver() instanceof IWriteable) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v == null) {
            return false;
        }
        if (v instanceof Table && !((Table) v).isEditing() && ((Table) v).getModel().getAssociatedTable() == null) {
            return true;
        }
        return false;
    }
}
