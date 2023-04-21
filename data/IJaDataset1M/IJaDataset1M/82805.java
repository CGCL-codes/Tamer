package brainlink.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import brainlink.core.model.FontStyle;
import brainlink.core.model.NTEColour;
import brainlink.core.model.TypeFaceDescriptor;

/**
 * Combo box which can be used to select a colour for text.
 * @author iain
 */
public class ColourSelector extends JComboBox implements ItemListener {

    private Brainlink brainlink;

    public ColourSelector(Brainlink brainlink) {
        super(NTEColour.COLOUR_PALETTE);
        this.brainlink = brainlink;
        setEditable(false);
        ColourCellRenderer colourRenderer = new ColourCellRenderer();
        setRenderer(colourRenderer);
        setSelectedIndex(0);
        Dimension maxSize = new Dimension((int) getPreferredSize().getWidth(), Integer.MAX_VALUE);
        setMaximumSize(maxSize);
        addItemListener(this);
    }

    public NTEColour getSelectedColour() {
        return (NTEColour) getSelectedItem();
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            FontStyle currentFontStyle = brainlink.currentTypeFace.getFont();
            NTEColour selectedColour = (NTEColour) e.getItem();
            brainlink.currentTypeFace = new TypeFaceDescriptor(selectedColour, currentFontStyle);
            brainlink.networkSession.changeLocalUserColour(selectedColour);
            brainlink.documentView.changeSelectedBlockTypeFace();
        }
    }

    private class ColourCellRenderer extends JLabel implements ListCellRenderer {

        private ColourTile colourTile;

        public ColourCellRenderer() {
            colourTile = new ColourTile();
            setIcon(colourTile);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            NTEColour colour = (NTEColour) value;
            colourTile.setColour(colour.getJavaColour());
            setText("" + colour);
            return this;
        }
    }
}
