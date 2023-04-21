package org.hyperimage.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import org.hyperimage.client.util.MetadataHelper;
import org.hyperimage.client.ws.HiInscription;

/**
 * @author Jens-Martin Loebel
 */
public class InscriptionViewerControl extends JPanel {

    private static final long serialVersionUID = -1037643810535292908L;

    private HiInscription inscription = null;

    private JTextPane insPane;

    private String defLang;

    public InscriptionViewerControl(String defLang) {
        this.defLang = defLang;
        this.setLayout(new BorderLayout());
        insPane = new JTextPane();
        insPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        insPane.setBackground(Color.WHITE);
        insPane.setEditable(false);
        insPane.setContentType("text/html");
        this.add(new JScrollPane(insPane), BorderLayout.CENTER);
    }

    public void setInscription(HiInscription inscription) {
        this.inscription = inscription;
        if (this.inscription != null) {
            String html = MetadataHelper.richTextToHTML(MetadataHelper.findValue("HIBase", "content", MetadataHelper.getDefaultMetadataRecord(inscription, defLang)));
            insPane.setText(html);
        }
    }
}
