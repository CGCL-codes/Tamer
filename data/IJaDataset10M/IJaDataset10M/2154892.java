package net.sourceforge.neonzip.threads;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import gnu.regexp.*;
import org.apache.log4j.Category;
import net.sourceforge.neonzip.*;
import net.sourceforge.neonzip.plugins.*;

public final class DeleteRunnable implements Runnable {

    private static final Category log = Category.getInstance(DeleteRunnable.class);

    private Main fMain = null;

    public DeleteRunnable(Main aMain) {
        fMain = aMain;
    }

    public void run() {
        PluginInterface plugin = fMain.getPlugin();
        if (!plugin.supportDelete()) {
            Utils.sayInformation("Currently selected plugin don't support Delete operation.", fMain);
            return;
        }
        ArchiveTableModel tableModel = fMain.getTableModel();
        DeleteDialog dialog = new DeleteDialog(fMain, true);
        dialog.setEntireArchiveSelected(!tableModel.isSelectedRow());
        dialog.setSelectedFilesSelected(tableModel.isSelectedRow());
        dialog.setSelectedFilesEnabled(tableModel.isSelectedRow());
        dialog.show();
        if (dialog.isApproved()) {
            fMain.lockGUI();
            ArchiveTable table = fMain.getArchiveTable();
            if (dialog.isFilteredFilesSelected()) {
                table.clearSelection();
                RE filter = Utils.getFileFilter(dialog.getFileFilter());
                ArrayList filenames = fMain.getAllFilenames();
                for (int i = 0; i < filenames.size(); i++) {
                    String name = (String) filenames.get(i);
                    if (filter.isMatch(name)) table.addRowSelectionInterval(i, i);
                }
                try {
                    plugin.delete(table.getSelectedRows());
                } catch (DeleteException e) {
                    log.fatal("Delete operation error", e);
                    Utils.sayError("Delete operation error: " + e.getMessage(), fMain);
                } finally {
                    fMain.unlockGUI();
                }
            }
            if (dialog.isEntireArchiveSelected()) {
                int count = table.getRowCount();
                int[] indexes = new int[count];
                for (int i = 0; i < count; i++) {
                    indexes[i] = i;
                }
                try {
                    plugin.delete(indexes);
                } catch (DeleteException e) {
                    log.fatal("Delete operation error", e);
                    Utils.sayError("Delete operation error: " + e.getMessage(), fMain);
                } finally {
                    fMain.unlockGUI();
                }
            }
            if (dialog.isSelectedFilesSelected()) {
                try {
                    plugin.delete(table.getSelectedRows());
                } catch (DeleteException e) {
                    log.fatal("Delete operation error", e);
                    Utils.sayError("Delete operation error: " + e.getMessage(), fMain);
                } finally {
                    fMain.unlockGUI();
                }
            }
            fMain.unlockGUI();
        }
    }
}
