package org.jcvi.platetools.swing.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import org.jcvi.glk.TrimSequence;
import org.jcvi.glk.TrimSequenceAttribute;
import org.jcvi.glk.TrimSequenceAttributeType;
import org.jcvi.platetools.swing.GlobalTheme;
import org.jcvi.platetools.swing.ThemedJPanel;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class PrimerInfoPanel extends ThemedJPanel {

    /** The Serial Version UID */
    private static final long serialVersionUID = -8436686942623664918L;

    private final JLabel primerName;

    private final JLabel primerRegion;

    private final JLabel primerCoord;

    private final JLabel primerSeqCode;

    private final JTextPane primerSeq;

    /**
     * Creates a new <code>PrimerInfoPanel</code>.
     */
    public PrimerInfoPanel() {
        super();
        final Font valueFont = GlobalTheme.frameTheme().getBaseFont().deriveFont(Font.BOLD, 10.0F);
        this.primerName = this.buildLabel("-", SwingConstants.LEFT, valueFont);
        this.primerRegion = this.buildLabel("-", SwingConstants.LEFT, valueFont);
        this.primerCoord = this.buildLabel("-", SwingConstants.LEFT, valueFont);
        this.primerSeqCode = this.buildLabel("-", SwingConstants.LEFT, valueFont);
        this.primerSeq = new JTextPane();
        this.init();
    }

    private void init() {
        this.setLayout(new MigLayout("gap 3px 8px"));
        this.primerSeq.setFont(GlobalTheme.frameTheme().getBaseFont().deriveFont(9.0F));
        this.primerSeq.setBackground(null);
        this.primerSeq.setBorder(null);
        this.primerSeq.setEditable(false);
        this.primerSeq.setOpaque(false);
        this.primerSeq.setMargin(new Insets(0, 0, 0, 0));
        this.add(this.buildHeaderLabel("Name:"), "align right");
        this.add(this.primerName, "pushx, spanx, wrap");
        this.add(this.buildHeaderLabel("Region:"), "align right");
        this.add(this.primerRegion, "pushx 30");
        this.add(this.buildHeaderLabel("Coordinate:"), "gap 6, align right");
        this.add(this.primerCoord, "pushx 40");
        this.add(this.buildHeaderLabel("Naming Tag:"), "pushx, align right");
        this.add(this.primerSeqCode, "pushx 50, wrap");
        this.add(this.buildHeaderLabel("Bases:"), "align right, aligny top");
        this.add(this.primerSeq, "growx, pushx, spanx, wrap");
    }

    public void clear() {
        this.primerName.setText("---");
        this.primerRegion.setText("---");
        this.primerCoord.setText("---");
        this.primerSeq.setText("---");
    }

    public void display(TrimSequence primer) {
        this.primerName.setText(primer.getName());
        final TrimSequenceAttribute regionAttr = primer.getAttribute(new TrimSequenceAttributeType("region"));
        final TrimSequenceAttribute coordAttr = primer.getAttribute(new TrimSequenceAttributeType("coordinate"));
        final TrimSequenceAttribute seqCodeAttr = primer.getAttribute(new TrimSequenceAttributeType("seq_code"));
        if (regionAttr == null) {
            this.primerRegion.setText("< n/a >");
        } else {
            this.primerRegion.setText(regionAttr.getValue());
        }
        if (coordAttr == null) {
            this.primerCoord.setText("< n/a >");
        } else {
            this.primerCoord.setText(coordAttr.getValue());
        }
        if (seqCodeAttr == null) {
            this.primerSeqCode.setText("< n/a >");
        } else {
            this.primerSeqCode.setText(seqCodeAttr.getValue());
        }
        this.primerSeq.setText(primer.getSequence());
    }

    public JScrollPane buildSequenceScrollPane(JTextArea view) {
        view.setOpaque(false);
        final JScrollPane scroll = new JScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    @Override
    public JLabel buildHeaderLabel(String text) {
        final JLabel header = super.buildHeaderLabel(text);
        header.setForeground(new Color(0x80, 0x80, 0xC0));
        return header;
    }

    @Override
    public Font getHeaderFont() {
        return GlobalTheme.frameTheme().getBaseFont().deriveFont(Font.PLAIN, 9.0F);
    }
}
