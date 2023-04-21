package org.mitre.rt.client.ui.cchecks;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.apache.log4j.Logger;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.rtclient.FileType;

/**
 *
 * @author JWINSTON
 */
public class CCheckFilesListCellRenderer extends DefaultListCellRenderer {

    private static Logger logger = Logger.getLogger(CCheckFilesListCellRenderer.class.getPackage().getName());

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String text = "";
        if (value instanceof FileType) {
            FileType file = (FileType) value;
            if (file != null) text = file.getOrigFileName();
        }
        this.setText(text);
        GlobalUITools.setupListCellRendererUI(this, list, index, isSelected);
        return this;
    }
}
