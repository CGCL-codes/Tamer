package macaw.plugins;

import macaw.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;
import macaw.util.HTMLGenerationUtility;
import macaw.util.OKClosePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DummyMacawPlugin extends AbstractMacawPlugin implements ActionListener {

    private JDialog dialog;

    private JButton close;

    public DummyMacawPlugin() {
    }

    private String generateHTMLReport() {
        HTMLGenerationUtility htmlUtility = new HTMLGenerationUtility();
        htmlUtility.beginDocument();
        String backgroundInformation = MacawMessages.getMessage("futureEnhancementPlugin.backgroundInformation");
        htmlUtility.addParagraph(backgroundInformation);
        htmlUtility.addBlankParagraphLines(2);
        htmlUtility.beginTable();
        htmlUtility.beginRow();
        String pluginNameLabelText = MacawMessages.getMessage("futureEnhancementPlugin.name");
        htmlUtility.addBoldColumnValue(pluginNameLabelText);
        htmlUtility.addColumnValue(getDisplayName());
        htmlUtility.endRow();
        htmlUtility.beginRow();
        String pluginDescriptionLabelText = MacawMessages.getMessage("futureEnhancementPlugin.featureDescription");
        htmlUtility.addBoldColumnValue(pluginDescriptionLabelText);
        htmlUtility.addColumnValue(getDescription());
        htmlUtility.endRow();
        htmlUtility.endTable();
        htmlUtility.endDocument();
        return htmlUtility.getHTML();
    }

    private void close() {
        dialog.setVisible(false);
    }

    public void run() {
        SessionProperties sessionProperties = new SessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        String futureEnhancementTitle = MacawMessages.getMessage("futureEnhancementPlugin.title");
        dialog = userInterfaceFactory.createDialog();
        dialog.setTitle(futureEnhancementTitle);
        GridBagConstraints panelGC = userInterfaceFactory.createGridBagConstraints();
        JPanel panel = userInterfaceFactory.createPanel();
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weightx = 1.0;
        panelGC.weighty = 1.0;
        JEditorPane editorPane = userInterfaceFactory.createEditorPane();
        editorPane.setText(generateHTMLReport());
        JScrollPane scrollPane = userInterfaceFactory.createScrollPane(editorPane);
        panel.add(scrollPane, panelGC);
        OKClosePanel okClosePanel = new OKClosePanel(userInterfaceFactory, this);
        panelGC.gridy++;
        panelGC.anchor = GridBagConstraints.SOUTHEAST;
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.weighty = 0;
        okClosePanel.useOnlyClose();
        okClosePanel.buildUI();
        panel.add(okClosePanel.getPanel(), panelGC);
        close = okClosePanel.getCloseButton();
        dialog.getContentPane().add(panel);
        dialog.setSize(300, 300);
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        Object button = event.getSource();
        if (button == close) {
            close();
        }
    }
}
