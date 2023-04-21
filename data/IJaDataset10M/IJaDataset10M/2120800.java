package be.vds.jtbdive.client.view.core.importer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.xml.DiveSiteParser;
import be.vds.jtbdive.core.xml.DiverParser;
import be.vds.jtbdive.core.xml.LogBookParser;

public class ImportLogBookDialog extends JDialog {

    private static final long serialVersionUID = 8421741325033895055L;

    private LogBookManagerFacade logBookManagerFacade;

    private FileSelector selector;

    private DiverManagerFacade diverManagerFacade;

    private DiveSiteManagerFacade diveLocationManagerFacade;

    public ImportLogBookDialog(LogBookManagerFacade logBookManagerFacade, DiverManagerFacade diverManagerFacade, DiveSiteManagerFacade diveLocationManagerFacade) {
        this.logBookManagerFacade = logBookManagerFacade;
        this.diverManagerFacade = diverManagerFacade;
        this.diveLocationManagerFacade = diveLocationManagerFacade;
        init();
    }

    private void init() {
        this.getContentPane().add(createContentPane());
        this.setSize(200, 120);
    }

    private Component createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(createMainPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);
        return contentPane;
    }

    private Component createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayoutManager.addComponent(mainPanel, createFileSelectionComponent(), c, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        return mainPanel;
    }

    private Component createFileSelectionComponent() {
        selector = new FileSelector();
        return selector;
    }

    private Component createButtonsPanel() {
        JButton cancelButton = new I18nButton(new AbstractAction("cancel") {

            private static final long serialVersionUID = -6351067332929216970L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        JButton importButton = new I18nButton(new AbstractAction("import") {

            private static final long serialVersionUID = 9076743329291015264L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
                saveAllData();
            }
        });
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(importButton);
        p.add(cancelButton);
        return p;
    }

    private void saveAllData() {
        File f = selector.getSelectedFile();
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new FileInputStream(f));
            Map<Long, DiveSite> diveLocationMap = new HashMap<Long, DiveSite>();
            DiveSiteParser dlp = new DiveSiteParser();
            for (Iterator iterator = doc.getRootElement().getChild("divesites").getChildren("divesite").iterator(); iterator.hasNext(); ) {
                Element dlEl = (Element) iterator.next();
                DiveSite dl = dlp.readDiveLocationElement(dlEl);
                diveLocationMap.put(dl.getId(), dl);
            }
            Map<Long, Diver> diverMap = new HashMap<Long, Diver>();
            DiverParser dp = new DiverParser();
            for (Iterator iterator = doc.getRootElement().getChild("divers").getChildren("diver").iterator(); iterator.hasNext(); ) {
                Element dEl = (Element) iterator.next();
                Diver d = dp.readDiver(dEl);
                diverMap.put(d.getId(), d);
            }
            LogBookParser p = new LogBookParser();
            LogBook lb = p.readLogBook(doc.getRootElement().getChild("logbook"), diveLocationMap, diverMap);
            for (Diver diver : diverMap.values()) {
                diver.setId(-1);
                diver = diverManagerFacade.saveDiver(diver);
            }
            for (DiveSite divelocation : diveLocationMap.values()) {
                divelocation.setId(-1);
                divelocation = diveLocationManagerFacade.saveDiveLocation(divelocation);
            }
            logBookManagerFacade.setCurrentLogBook(lb);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataStoreException e) {
            e.printStackTrace();
        }
    }
}
