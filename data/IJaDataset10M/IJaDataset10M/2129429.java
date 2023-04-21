package org.ros.worldwind.ui.elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.ros.worldwind.Adapter;
import org.ros.worldwind.AdapterProperties;
import org.ros.worldwind.marker.RenderableMarker;
import org.ros.worldwind.ui.Displays;

public class MarkerElement extends AbstractElement {

    private static final long serialVersionUID = -8930968317355985851L;

    public static final String TOPIC_KEY = ".topic";

    public static final String GFX_KEY = ".gfx";

    public static final String TOOLTIP_KEY = ".tooltip";

    public static final String SELECT_MARKER_KEY = ".selectmarker";

    private JFormattedTextField topic;

    private JFormattedTextField gfx;

    private JCheckBox showTooltip;

    private JCheckBox selectMarker;

    private String topicName;

    private RenderableMarker renderableMarker;

    public MarkerElement(Adapter adapter, String persistenceID) {
        super(adapter, persistenceID);
        TextFieldActionListener textFieldActionListener = new TextFieldActionListener();
        topicName = AdapterProperties.getInstance().getProperty(persistenceID + TOPIC_KEY);
        if (topicName != null && !topicName.equalsIgnoreCase("")) addMarker(topicName);
        topic = new JFormattedTextField();
        topic.setText(topicName);
        topic.addActionListener(textFieldActionListener);
        addRow(new JLabel("topic"), topic);
        gfx = new JFormattedTextField();
        gfx.setText(AdapterProperties.getInstance().getProperty(persistenceID + GFX_KEY));
        gfx.addActionListener(textFieldActionListener);
        addRow(new JLabel("gfx path"), gfx);
        checkGFX();
        JPanel controls = new JPanel(new MigLayout("hidemode 3, novisualpadding, ins 2", "[][][][][fill, grow]", "[]"));
        controls.setOpaque(false);
        showTooltip = new JCheckBox();
        showTooltip.setToolTipText("show object tooltips");
        String showTooltipString = AdapterProperties.getInstance().getProperty(persistenceID + TOOLTIP_KEY);
        if (showTooltipString == null) {
            showTooltipString = "true";
            AdapterProperties.getInstance().setProperty(persistenceID + TOOLTIP_KEY, showTooltipString);
        }
        showTooltip.setSelected(Boolean.parseBoolean(showTooltipString));
        if (renderableMarker != null) renderableMarker.setShowTooltip(showTooltip.isSelected());
        showTooltip.setOpaque(false);
        showTooltip.addItemListener(new TooltipItemListener());
        controls.add(new JLabel("tooltip"));
        controls.add(showTooltip);
        selectMarker = new JCheckBox();
        selectMarker.setToolTipText("select marker");
        setSelection(Boolean.parseBoolean(AdapterProperties.getInstance().getProperty(persistenceID + SELECT_MARKER_KEY)));
        selectMarker.setOpaque(false);
        selectMarker.addItemListener(new SelectMarkerItemListener());
        controls.add(new JLabel("select"));
        controls.add(selectMarker);
        JButton flyToMarker = new JButton("fly to");
        flyToMarker.setToolTipText("fly to marker");
        flyToMarker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (renderableMarker != null) flyTo(renderableMarker.getPosition(), renderableMarker.getScale() * 15);
            }
        });
        controls.add(flyToMarker, Displays.LAYOUT_BUTTON + ", gap 20 ");
        addRow(controls, "");
    }

    private void addMarker(String topicName) {
        renderableMarker = new RenderableMarker(topicName, adapter, this);
        adapter.getRenderableManager().addMarker(renderableMarker);
    }

    public boolean isSelection() {
        return selectMarker.isSelected();
    }

    public void setSelection(boolean selection) {
        selectMarker.setSelected(selection);
        updateRenderableMarker();
        storeProperties();
    }

    private void checkGFX() {
        String gfxPath = AdapterProperties.getInstance().getProperty(persistenceID + GFX_KEY);
        if (gfxPath != null && renderableMarker != null) if (!gfxPath.equalsIgnoreCase("")) renderableMarker.setSurfaceImagePath(gfxPath);
    }

    private void deleteRenderable() {
        if (renderableMarker != null) {
            renderableMarker.setParent(null);
            adapter.getRenderableManager().removeMarker(renderableMarker);
        }
        renderableMarker = null;
    }

    public void updateRenderableMarker() {
        if (renderableMarker != null) {
            renderableMarker.setSelected(selectMarker.isSelected());
            if (adapter.getSelection() != null) {
                if (selectMarker.isSelected()) {
                    adapter.getSelection().addSelection(topicName, renderableMarker.getMarker(), new SelectionActionListener());
                } else {
                    adapter.getSelection().removeSelection(topicName);
                }
            }
        }
    }

    public void updateSelectionPanel() {
        if (selectMarker.isSelected()) adapter.getSelection().updateSelection(topicName, renderableMarker.getMarker());
    }

    @Override
    public void updateOrigin() {
        if (renderableMarker != null) renderableMarker.update();
    }

    private class TooltipItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e != null) {
                if (renderableMarker != null) {
                    renderableMarker.setShowTooltip(showTooltip.isSelected());
                }
            }
            storeProperties();
        }
    }

    private class SelectMarkerItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e != null) setSelection(selectMarker.isSelected());
        }
    }

    private void updateDisplay() {
        if (!topic.getText().equalsIgnoreCase("")) {
            if (renderableMarker == null) {
                addMarker(topic.getText());
                checkGFX();
            }
            if (topicName != null) adapter.getSelection().removeSelection(topicName);
            topicName = topic.getText();
            renderableMarker.subscribe(topic.getText());
            updateRenderableMarker();
        } else {
            deleteRenderable();
        }
        storeProperties();
        checkGFX();
    }

    private void storeProperties() {
        AdapterProperties.getInstance().setProperty(persistenceID + TOPIC_KEY, topic.getText());
        AdapterProperties.getInstance().setProperty(persistenceID + GFX_KEY, gfx.getText());
        AdapterProperties.getInstance().setProperty(persistenceID + TOOLTIP_KEY, showTooltip.isSelected() + "");
        AdapterProperties.getInstance().setProperty(persistenceID + SELECT_MARKER_KEY, selectMarker.isSelected() + "");
    }

    private class TextFieldActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateDisplay();
        }
    }

    public ActionListener getDeleteActionListener() {
        return deleteActionListener;
    }

    public static final String ACTION_COMMAND_DESELECT = "deselect";

    public static final String ACTION_COMMAND_SELECT = "select";

    private class SelectionActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setSelection(e.getActionCommand().equalsIgnoreCase(ACTION_COMMAND_SELECT));
        }
    }

    private DeleteActionListener deleteActionListener = new DeleteActionListener();

    private class DeleteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRenderable();
            AdapterProperties.getInstance().removeProperty(persistenceID + TOPIC_KEY);
            AdapterProperties.getInstance().removeProperty(persistenceID + GFX_KEY);
            AdapterProperties.getInstance().removeProperty(persistenceID + TOOLTIP_KEY);
            AdapterProperties.getInstance().removeProperty(persistenceID + SELECT_MARKER_KEY);
        }
    }
}
