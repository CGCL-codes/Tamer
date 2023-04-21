package com.timenes.clips.platform.jse.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.timenes.clips.platform.model.ModelObject;
import com.timenes.clips.platform.view.View;

public abstract class IconView extends View {

    private JPanel component;

    private IconPanel icon;

    private JLabel label;

    public IconView(View parent, ModelObject modelObject) {
        super(parent, modelObject);
        component = new JPanel();
        component.setLayout(new GridBagLayout());
        icon = new IconPanel("");
        label = new JLabel();
        GridBagConstraints linebreak = new GridBagConstraints();
        linebreak.gridwidth = GridBagConstraints.REMAINDER;
        component.add(icon, linebreak);
        component.add(label);
    }

    @Override
    public void refresh() {
        label.setText(getTitle());
    }

    public Component getComponent() {
        return component;
    }

    protected abstract String getTitle();
}
