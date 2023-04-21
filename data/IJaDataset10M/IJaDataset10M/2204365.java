package com.lepidllama.packagegui;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.swixml.SwingEngine;
import com.lepidllama.packageeditor.dbpf.ResourceLookupPack;
import com.lepidllama.packageeditor.resources.Vpxy;
import com.lepidllama.packagegui.components.TgiTableModel;
import com.lepidllama.packagegui.components.TgiTableMouseEventListener;

public class PluginVpxy extends ResourcePlugin<Vpxy> implements PropertyChangeListener {

    private Container panel;

    JTable refsTable;

    JTable floatsTable;

    JTextField float0Text;

    JTextField float1Text;

    JTextField float2Text;

    JTextField float3Text;

    JTextField float4Text;

    JTextField float5Text;

    public PluginVpxy() throws Exception {
        try {
            panel = new SwingEngine(this).render("xml/pluginVpxy.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResource(ResourceLookupPack rlp, Vpxy resource) {
        super.setResource(rlp, resource);
        this.resource = resource;
        Vpxy vpxy = (Vpxy) resource;
        if (vpxy.getReferences() != null) {
            refsTable.setModel(new TgiTableModel(vpxy.getReferences()));
            refsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        }
        TgiTableMouseEventListener listener = new TgiTableMouseEventListener();
        refsTable.addMouseListener(listener);
        refsTable.setAutoCreateRowSorter(true);
        float[] floats = vpxy.getBoundingBox();
        float0Text.setText("" + floats[0]);
        float1Text.setText("" + floats[1]);
        float2Text.setText("" + floats[2]);
        float3Text.setText("" + floats[3]);
        float4Text.setText("" + floats[4]);
        float5Text.setText("" + floats[5]);
    }

    public Container getPanel() {
        return panel;
    }

    public String getName() {
        return "VPXY Header";
    }

    public Icon getIcon() {
        return Icons.getIcon("sitemap");
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("all")) {
            setResource(null, resource);
        }
    }
}
