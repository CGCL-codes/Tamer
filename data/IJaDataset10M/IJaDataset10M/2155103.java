package org.gjt.sp.jedit.gui;

import javax.swing.*;
import java.io.File;
import org.gjt.sp.jedit.*;

public class CurrentDirectoryMenu extends JMenu {

    public CurrentDirectoryMenu(View view) {
        String label = jEdit.getProperty("current-directory.label");
        int index = label.indexOf('$');
        char mnemonic = '\0';
        if (index != -1) {
            mnemonic = Character.toUpperCase(label.charAt(index + 1));
            label = label.substring(0, index) + label.substring(index + 1);
        }
        setText(label);
        setMnemonic(mnemonic);
        this.view = view;
    }

    public void setPopupMenuVisible(boolean b) {
        if (b) {
            if (getMenuComponentCount() != 0) removeAll();
            File file = view.getBuffer().getFile();
            if (file == null) {
                JMenuItem mi = new JMenuItem(jEdit.getProperty("current-directory.not-local"));
                mi.setEnabled(false);
                add(mi);
                super.setPopupMenuVisible(b);
                return;
            }
            File dir = new File(file.getParent());
            JMenuItem mi = new JMenuItem(dir.getPath());
            mi.setEnabled(false);
            add(mi);
            addSeparator();
            JMenu current = this;
            EditAction action = jEdit.getAction("open-file");
            String[] list = dir.list();
            if (list != null) {
                MiscUtilities.quicksort(list, new MiscUtilities.StringICaseCompare());
                for (int i = 0; i < list.length; i++) {
                    String name = list[i];
                    file = new File(dir, name);
                    if (file.isDirectory()) continue;
                    mi = new EnhancedMenuItem(name, action, file.getPath());
                    if (current.getItemCount() >= 20) {
                        current.addSeparator();
                        JMenu newCurrent = new JMenu(jEdit.getProperty("common.more"));
                        current.add(newCurrent);
                        current = newCurrent;
                    }
                    current.add(mi);
                }
            }
        }
        super.setPopupMenuVisible(b);
    }

    private View view;
}
