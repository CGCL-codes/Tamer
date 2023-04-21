package jsynoptic.plugins.java3d.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.media.j3d.Light;
import javax.swing.JPanel;
import jsynoptic.plugins.java3d.edit.PointLightEdit;
import jsynoptic.plugins.java3d.edit.PropertyEdit;

/**
 */
public class PointLightPanel extends LightPanel {

    JPanel _center;

    public PointLightPanel(Frame owner, PointLightEdit editor) {
        super(owner, editor);
        _center = new JPanel(new FlowLayout());
        PropertyEdit<Light, ?> e = editor.getPropertyEdit(PointLightEdit.LightPosition);
        _center.add(new PropertiesPanel<Light>(_owner, e));
        PropertyEdit<Light, ?> e2 = editor.getPropertyEdit(PointLightEdit.LightAttenuation);
        _center.add(new PropertiesPanel<Light>(_owner, e2));
        add(BorderLayout.CENTER, _center);
    }
}
