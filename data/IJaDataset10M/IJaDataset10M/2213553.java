package com.memomics.cytoscape_plugin.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.alitora.asapi.publicModel.ASAPI_Entity;
import com.alitora.asapi.publicModel.ASAPI_Response;
import com.alitora.asapi.publicModel.ASAPI_Status;
import com.memomics.cytoscape_plugin.MemomicsCytoscapePlugin;
import com.memomics.cytoscape_plugin.model.Application;
import com.memomics.cytoscape_plugin.tree.Branch;

public class ClipboardPanelV2 extends AbstractTreePanel {

    private static final long serialVersionUID = 1L;

    public ClipboardPanelV2() {
        super(Application.get().getClipboardRoot(), false);
    }

    @Override
    protected Branch getRootNode() {
        return Application.get().getClipboardRoot();
    }

    @Override
    protected Branch[] getRootNodes() {
        return null;
    }

    @Override
    protected String getTitle() {
        return "Clipboard";
    }

    @Override
    protected JPopupMenu createPopupMenu(final ASAPI_Entity entity) {
        JPopupMenu menu = super.createPopupMenu(entity);
        JMenuItem removeFromClipboard = new JMenuItem("Remove from clipboard");
        removeFromClipboard.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ASAPI_Response removeFromClipboard = Application.get().removeFromClipboard(entity.getUMIS());
                if (removeFromClipboard.getStatusCode().equals(ASAPI_Status.STATUS_OK)) {
                    System.out.println("Successfully removed from clipboard.");
                    MemomicsCytoscapePlugin.connectionTab.refreshClipboard();
                } else {
                    System.out.println("ERROR: " + removeFromClipboard.getStatusCode());
                }
            }
        });
        menu.add(removeFromClipboard);
        return menu;
    }
}
