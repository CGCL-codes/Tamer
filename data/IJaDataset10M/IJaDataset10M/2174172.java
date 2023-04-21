package de.lamasep.gui.addresses;

import de.lamasep.gui.KeyboardEvent;
import de.lamasep.gui.Keyboard;
import de.lamasep.gui.KeyboardListener;
import de.lamasep.gui.util.GuiStyle;
import de.lamasep.gui.Menu;
import de.lamasep.gui.MenuButton;
import de.lamasep.gui.SimpleButton;
import de.lamasep.map.addresses.Address;
import de.lamasep.navigation.CurrentMap;
import de.lamasep.gui.navigation.RouteCalculationRunner;
import de.lamasep.navigation.CurrentPosition;
import de.lamasep.navigation.PositionUnit;
import de.lamasep.system.Registry;
import de.lamasep.system.Threading;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

/**
 * Anzeigekomponente für das Adressbuch, Adress-Suche und Adress-Menü.
 *
 * @author sniechzial
 */
public class AddressManager extends Menu implements Observer {

    /**
     * Logger Initialisieren.
     */
    private static Logger logger;

    static {
        logger = Logger.getLogger(AddressManager.class);
    }

    /**
     * Zuletzt angefahrene Ziele Anzeige.
     */
    private LruAddressView lruAddressView;

    /**
     * Adressbuch Anzeige.
     */
    private AddressbookView addressbookView;

    /**
     * Observable Wrapper um Adressbuch.
     */
    private ObservableAddressBook oAddressBook;

    /**
     * Container für die aktuelle Karte.
     */
    private CurrentMap currentMap;

    /**
     * Konstruktor - Initialisiert mit dem Container für die altuelle Karte,
     * dem Adressbuch und der Eltern-Menükomponente.
     * @param oAddressBook
     *      Observable Wrapper um Adressbuch das angezeigt wird,
     *      <code>oAddressBook != null</code>
     * @param currentMap
     *      Aktuelle Karte, <code>store != null</code>
     * @param previous
     *      Eltern-Knoten in der Menüstruktur, <code>previous != null</code>
     * @throws IllegalArgumentException falls <code>oAddressBook == null
     *          || currentMap == null || previous == null</code>
     */
    public AddressManager(CurrentMap currentMap, ObservableAddressBook oAddressBook, Menu previous) {
        super(previous, new VerticalLayout());
        this.currentMap = currentMap;
        this.oAddressBook = oAddressBook;
        logger.debug("INIT AddressManager with map=" + ((currentMap.getMap() == null) ? null : currentMap.getMap().getTitle()));
        init();
    }

    /**
     * Initialisiert die GUI Komponenten und registriert die Menüstruktur.
     */
    private void init() {
        MenuButton btnAdrBook = new MenuButton("Adressbuch");
        MenuButton btnAdrSearch = new MenuButton("Adress-Suche");
        MenuButton btnLastDest = new MenuButton("letzte Ziele");
        addressbookView = new AddressbookView(this, oAddressBook);
        this.registerNext(btnAdrBook, addressbookView);
        this.add(btnAdrBook);
        ObservableAddress found = new ObservableAddress();
        found.addObserver(this);
        this.registerNext(btnAdrSearch, new CitySearchView(currentMap, found, this, true));
        this.add(btnAdrSearch);
        lruAddressView = new LruAddressView(this, oAddressBook);
        this.registerNext(btnLastDest, lruAddressView);
        this.add(btnLastDest);
        MenuButton btnBack = new MenuButton("Zurück");
        this.registerPrevious(btnBack);
        this.add(btnBack, BorderLayout.NORTH);
    }

    /**
     * Wird von ObservableAddress benachrichtigt, wenn eine Adresse
     * gefunden wurde.
     * @param o beobachtete Instanz
     * @param arg Argument
     */
    @Override
    public void update(final Observable o, final Object arg) {
        if (o instanceof ObservableAddress) {
            if (arg instanceof Address) {
                Address ad = (Address) arg;
                showNext(new AddressFoundView(ad, this));
            }
        }
    }

    /**
     * Anzeigekomponente für das Speichern einer Adresse in einem Adressbuch.
     *
     * @author sniechzial
     */
    private class AddressSaveView extends Menu implements ActionListener, KeyboardListener {

        /**
         * Panel für Adress-Informationen.
         */
        private JPanel dataPanel;

        /**
         * Bildschirmtastatur.
         */
        private Keyboard keyboard;

        /**
         * Panel für Aktions-Buttons.
         */
        private JPanel buttonPanel;

        /**
         * Zu speichernde Adressse.
         */
        private Address address;

        /**
         * Textfeld für den Adresstitel.
         */
        private JTextField fieldTitle;

        /**
         * Button für Aktion "Speichern".
         */
        private SimpleButton btnSave;

        /**
         * Button für Aktion "Zurück".
         */
        private SimpleButton btnBack;

        /**
         * Adressbuch-Instanz für Speicherung.
         */
        private ObservableAddressBook book;

        /**
         * Konstruktor - Initialisierung mit zu speichernder Adresse,
         * Adressbuch und Eltern Menü-Komponente.
         * @param address zu speichernde Adresse
         * @param book Adressbuch
         * @param previous Eltern Menü-Komponente
         */
        public AddressSaveView(final Address address, final ObservableAddressBook book, final Menu previous) {
            super(previous, new BorderLayout());
            this.address = address;
            this.book = book;
            dataPanel = new JPanel(new HorizontalLayout());
            dataPanel.setOpaque(false);
            JLabel labelTitle = new JLabel("Titel: ");
            labelTitle.setFont(GuiStyle.FONT_LARGE);
            dataPanel.add(labelTitle);
            fieldTitle = new JTextField(20);
            fieldTitle.setFont(GuiStyle.FONT_LARGE);
            fieldTitle.setText(address.getName());
            dataPanel.add(fieldTitle);
            this.add(dataPanel, BorderLayout.NORTH);
            keyboard = new Keyboard(new AcceptingKeyboardModel());
            keyboard.addListener(this);
            JPanel down = new JPanel(new BorderLayout());
            down.setOpaque(false);
            down.add(keyboard, BorderLayout.SOUTH);
            this.add(down, BorderLayout.CENTER);
            buttonPanel = new JPanel(new HorizontalLayout(GuiStyle.ACTION_BTN_SPACING));
            buttonPanel.setOpaque(false);
            btnBack = new SimpleButton("Zurück");
            btnSave = new SimpleButton("Speichern");
            btnSave.addActionListener(this);
            registerPrevious(btnBack);
            buttonPanel.add(btnBack);
            buttonPanel.add(btnSave);
            this.add(buttonPanel, BorderLayout.SOUTH);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btnSave) {
                String newName = fieldTitle.getText();
                if (!newName.isEmpty()) {
                    CurrentMap currentMap = Registry.instance().getActiveMap();
                    if (currentMap.getMap() == null) {
                        getMaster().showError("Keine Karte geladen", "Adresse speichern", "Kann Adresse nicht speichern, weil keine" + " Karte geladen ist um dise zuzuordnen.");
                    } else {
                        address.setMapId(currentMap.getMap().getMapdirectory());
                        book.remove(address);
                        address.setName(newName);
                        book.add(address);
                        showPrevious();
                    }
                }
            }
        }

        @Override
        public void typingPerformed(KeyboardEvent event) {
            fieldTitle.setText(event.eventOnString(fieldTitle.getText()));
        }
    }

    /**
     * Anzeigekomponente für eine gefundene Adresse mit Funktionalität zum
     * Navigieren und Speichern.
     *
     * @author sniechzial
     */
    private class AddressFoundView extends Menu implements ActionListener, Observer {

        /**
         * Panel für Adress-Informationen.
         */
        private JPanel addressPanel;

        /**
         * Panel für Aktions-Buttons.
         */
        private JPanel buttonPanel;

        /**
         * Button für Aktion "Goto".
         */
        private SimpleButton btnGoto;

        /**
         * Button für Aktion "Speichern".
         */
        private SimpleButton btnSave;

        /**
         * Button für Aktion "Zurück".
         */
        private SimpleButton btnBack;

        /**
         * Anzuzeigende Adresse.
         */
        private Address address;

        /**
         * Eltern Menü-Komponente.
         */
        private Menu previous;

        /**
         * Container für aktuelle Position.
         */
        private CurrentPosition currentPosition;

        /**
         * Konstruktor - Initialisierung mit anzuzeigender Adresse und
         * Eltern Menü-Komponente.
         * @param address anzuzeigende Adresse
         * @param previous Eltern Menü-Komponente
         */
        public AddressFoundView(Address address, Menu previous) {
            super(previous, new BorderLayout());
            this.previous = previous;
            addressPanel = new JPanel(new VerticalLayout());
            addressPanel.setOpaque(false);
            this.address = address;
            JLabel labelAddress = new JLabel(address.getName());
            labelAddress.setFont(GuiStyle.FONT_LARGE);
            labelAddress.setHorizontalAlignment(CENTER);
            labelAddress.setPreferredSize(GuiStyle.TABLE_DIMENSION_LARGE);
            labelAddress.setBorder(new BevelBorder(BevelBorder.RAISED));
            addressPanel.add(labelAddress);
            this.add(addressPanel, BorderLayout.NORTH);
            buttonPanel = new JPanel(new HorizontalLayout(GuiStyle.ACTION_BTN_SPACING));
            buttonPanel.setOpaque(false);
            btnGoto = new SimpleButton("Goto");
            btnGoto.addActionListener(this);
            PositionUnit unit = Registry.instance().getPositionUnit();
            if (unit != null) {
                currentPosition = unit.getPosition();
                if (currentPosition != null) {
                    currentPosition.addObserver(this);
                }
            }
            toggleSignalState();
            btnSave = new SimpleButton("Speichern");
            btnSave.addActionListener(this);
            btnBack = new SimpleButton("Zurück");
            registerPrevious(btnBack);
            buttonPanel.add(btnBack);
            buttonPanel.add(btnGoto);
            buttonPanel.add(btnSave);
            this.add(buttonPanel, BorderLayout.SOUTH);
        }

        /**
         * Passt den enabled-Zustand der Buttons an die Verfügbarkeit einer
         * Positions-Information an.
         */
        private void toggleSignalState() {
            if (btnGoto != null) {
                if (currentPosition != null && currentPosition.getPosition() != null) {
                    btnGoto.setEnabled(true);
                    btnGoto.setToolTipText("Navigation starten");
                } else {
                    btnGoto.setEnabled(false);
                    btnGoto.setToolTipText("Kein GPS-Signal verfügbar");
                }
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o == currentPosition) {
                toggleSignalState();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btnSave) {
                AddressSaveView addressSaveView = new AddressSaveView(address, oAddressBook, previous);
                showNext(addressSaveView);
            } else if (source == btnGoto) {
                oAddressBook.addDestination(address);
                Threading.submit(new RouteCalculationRunner(getMaster(), address));
                showMaster();
            }
        }
    }
}
