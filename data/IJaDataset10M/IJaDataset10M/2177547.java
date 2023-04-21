package com.googlecode.harapeko.examples.components;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXBusyLabel;
import com.googlecode.harapeko.annotation.JCheckBoxResource;
import com.googlecode.harapeko.annotation.JPanelResource;
import com.googlecode.harapeko.annotation.JXBusyLabelResource;
import com.googlecode.harapeko.annotation.layout.BorderLayout;
import com.googlecode.harapeko.annotation.layout.FlowLayout;

@BorderLayout(center = "busy", south = "control")
@JPanelResource
public class Busy extends JPanel {

    @JXBusyLabelResource(busy$bind = "${isBusy.selected}")
    JXBusyLabel busy;

    @FlowLayout(layout = { "isBusy" })
    @JPanelResource
    JPanel control;

    @JCheckBoxResource(text = "isBusy?")
    JCheckBox isBusy;
}
