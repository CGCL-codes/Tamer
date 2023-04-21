package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**Die Klasse ApplicationToolbar erzeugt eine {@link JToolBar} unterhalb des Menues. */
public class ApplicationToolbar extends JToolBar {

    private URL iconErstellenURL = Adressverwaltung.class.getResource("add.png");

    private static URL iconAnzeigenURL = Adressverwaltung.class.getResource("view.png");

    private static URL iconSuchenURL = Adressverwaltung.class.getResource("search.png");

    private static URL iconExportURL = Adressverwaltung.class.getResource("vcardExport.png");

    private static URL iconLoeschenURL = Adressverwaltung.class.getResource("remove.png");

    private static URL iconRefreshURL = Adressverwaltung.class.getResource("refresh.png");

    private static URL iconImportURL = Adressverwaltung.class.getResource("vcardImport.png");

    private static URL iconErstellen3URL = Adressverwaltung.class.getResource("add2.png");

    private static URL iconExport2URL = Adressverwaltung.class.getResource("vcardExport2.png");

    private static URL iconImport2URL = Adressverwaltung.class.getResource("vcardImport2.png");

    private static URL iconAnzeigen2URL = Adressverwaltung.class.getResource("view2.png");

    private static URL iconLoeschen2URL = Adressverwaltung.class.getResource("remove2.png");

    private static URL iconRefresh2URL = Adressverwaltung.class.getResource("refresh2.png");

    private static URL iconSearch2URL = Adressverwaltung.class.getResource("search2.png");

    private ImageIcon add2 = new ImageIcon(iconErstellen3URL);

    private ImageIcon export2 = new ImageIcon(iconExport2URL);

    private ImageIcon import2 = new ImageIcon(iconImport2URL);

    private ImageIcon view2 = new ImageIcon(iconAnzeigen2URL);

    private ImageIcon remove2 = new ImageIcon(iconLoeschen2URL);

    private ImageIcon search2 = new ImageIcon(iconSearch2URL);

    private ImageIcon refresh2 = new ImageIcon(iconRefresh2URL);

    private JButton addContactBtn = new JButton(new ImageIcon(iconErstellenURL));

    private JButton viewContactBtn = new JButton(new ImageIcon(iconAnzeigenURL));

    private JButton deleteContactBtn = new JButton(new ImageIcon(iconLoeschenURL));

    private JButton exportContactBtn = new JButton(new ImageIcon(iconExportURL));

    private JButton searchContactBtn = new JButton(new ImageIcon(iconSuchenURL));

    private JButton refreshContactBtn = new JButton(new ImageIcon(iconRefreshURL));

    private JButton importContactBtn = new JButton(new ImageIcon(iconImportURL));

    private JPanel pMain = new JPanel();

    private JPanel pButtons = new JPanel();

    private JPanel pSearch = new JPanel();

    private JTextField tSearch = new JTextField("Suche...", 10);

    /**Instantiates a new application toolbar. 
	 * @param adressverw Objekt der Adressverwaltung */
    public ApplicationToolbar(final Adressverwaltung adressverw) {
        addContactBtn.setToolTipText("Neuen Buddy erstellen");
        viewContactBtn.setToolTipText("Buddy bearbeiten");
        deleteContactBtn.setToolTipText("Buddy loeschen");
        exportContactBtn.setToolTipText("'Buddy to vCard'");
        searchContactBtn.setToolTipText("Suchen");
        refreshContactBtn.setToolTipText("Filter zuruecksetzen");
        importContactBtn.setToolTipText("'vCard to Buddy'");
        addContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        viewContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        deleteContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        exportContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        searchContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        refreshContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        importContactBtn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        addContactBtn.setRolloverIcon(add2);
        exportContactBtn.setRolloverIcon(export2);
        viewContactBtn.setRolloverIcon(view2);
        searchContactBtn.setRolloverIcon(search2);
        refreshContactBtn.setRolloverIcon(refresh2);
        deleteContactBtn.setRolloverIcon(remove2);
        importContactBtn.setRolloverIcon(import2);
        setRollover(true);
        setFloatable(false);
        pMain.setLayout(new BorderLayout());
        pButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        pSearch.setLayout(new FlowLayout(FlowLayout.RIGHT));
        add(pMain);
        pMain.add(pButtons, BorderLayout.WEST);
        pMain.add(pSearch, BorderLayout.EAST);
        pButtons.add(addContactBtn);
        pButtons.add(viewContactBtn);
        pButtons.add(deleteContactBtn);
        pButtons.add(exportContactBtn);
        pButtons.add(importContactBtn);
        pSearch.add(refreshContactBtn);
        pSearch.add(tSearch);
        pSearch.add(searchContactBtn);
        refreshContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                mrefreshContactBtn(adressverw);
            }
        });
        tSearch.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ((JTextField) evt.getComponent()).setText("");
            }

            public void focusLost(java.awt.event.FocusEvent evt2) {
                if (tSearch.getText().length() > 1) {
                } else {
                    ((JTextField) evt2.getComponent()).setText("Suche...");
                }
            }
        });
        tSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                mtSearchaction(adressverw);
            }
        });
        searchContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                mtSearchaction(adressverw);
            }
        });
        addContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                maddContactBtn(adressverw);
            }
        });
        viewContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                mviewContactBtn(adressverw);
            }
        });
        deleteContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                mdeleteContactBtn(adressverw);
            }
        });
        exportContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                adressverw.exportcontact();
            }
        });
        importContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                adressverw.importcontact();
            }
        });
        searchContactBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                adressverw.sucheperson("");
            }
        });
    }

    /** Enable buttons. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enableButtons(boolean bool) {
        addContactBtn.setEnabled(bool);
        viewContactBtn.setEnabled(bool);
        deleteContactBtn.setEnabled(bool);
        searchContactBtn.setEnabled(bool);
        importContactBtn.setEnabled(bool);
        exportContactBtn.setEnabled(bool);
        refreshContactBtn.setEnabled(bool);
    }

    /** setze Enable add-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enableadd(boolean bool) {
        addContactBtn.setEnabled(bool);
    }

    /** setze Enable view-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enableview(boolean bool) {
        viewContactBtn.setEnabled(bool);
    }

    /** setze Enable delete-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enabledelete(boolean bool) {
        deleteContactBtn.setEnabled(bool);
    }

    /** setze Enable search-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enablesearch(boolean bool) {
        searchContactBtn.setEnabled(bool);
    }

    /** setze Enable export-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enableexport(boolean bool) {
        exportContactBtn.setEnabled(bool);
    }

    /** setze Enable import-Button. 
	 * @param bool zum aktivieren/deaktivieren */
    public void enableimport(boolean bool) {
        importContactBtn.setEnabled(bool);
    }

    public void mrefreshContactBtn(Adressverwaltung adressverw) {
        adressverw.sucheperson("");
        adressverw.changeInfo("Filter zurueckgesetzt");
    }

    public void mtSearchaction(Adressverwaltung adressverw) {
        if (tSearch.getText().equals("Suche...") || tSearch.getText().equals("")) {
            adressverw.sucheperson("");
        } else {
            adressverw.sucheperson(tSearch.getText());
        }
    }

    public void maddContactBtn(Adressverwaltung adressverw) {
        enableButtons(false);
        adressverw.personAdd();
        adressverw.disableMenuItems();
    }

    public void mviewContactBtn(Adressverwaltung adressverw) {
        enableButtons(false);
        adressverw.personView();
        adressverw.disableMenuItems();
    }

    public void mdeleteContactBtn(Adressverwaltung adressverw) {
        adressverw.personRemove();
        adressverw.disableMenuItems();
    }
}
