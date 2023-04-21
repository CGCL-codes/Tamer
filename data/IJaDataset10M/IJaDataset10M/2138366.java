package gui;

import gui.ComponentSwitcher.ComponentChoice;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Klasse StartScherm
 * Erft van JPanel
 * Bevat alle methoden om het 'aanmeldscherm' weer te geven.
 * @author Maarten Minnebo, David Covemaeker, Tim Van Thuyne, Toon Kint
 */
public class StartScherm extends JPanel {

    private static final long serialVersionUID = 6157975574629558678L;

    private GUIController guiController;

    private MainFrame mainFrame;

    private JLabel ircServerLabel;

    private JLabel ircChannelLabel;

    private JLabel ircPortLabel;

    private JLabel nickLabel;

    private JLabel bannerLabel;

    private JTextField ircServerTextField;

    private JTextField ircChannelTextField;

    private JTextField ircPortTextField;

    private JTextField nickTextField;

    private JButton connectButton;

    private JButton cancelButton;

    private final String DEFAULT_SERVER = "irc.quakenet.org";

    private final String DEFAULT_PORT = "6667";

    private final String DEFAULT_NICK = "DMTT_Guest";

    private final String DEFAULT_CHANNEL = "#DMTT";

    private Rectangle r;

    private Color bgc;

    private JProgressBar progressBar;

    private JLabel progressLabel;

    private ImageIcon myImage;

    private JLabel imageLabel;

    private boolean validServer = true, validPort = true, validChannel = true, validNick = true;

    /**
	 * Constructor van het StartScherm.
	 * Roep de initialisatie van het startscherm op.
	 * @param mf MainFrame waarvan het startscherm onderdeel uitmaakt
	 * @param guiController de GUIController
	 */
    public StartScherm(MainFrame mf, GUIController guiController) {
        this.guiController = guiController;
        mainFrame = mf;
        r = new Rectangle(0, 0, 0, 0);
        imageLabel = new JLabel();
        myImage = new ImageIcon(getClass().getClassLoader().getResource("ircsettingslogo.png"));
        initGUI();
    }

    /**
	 * De initialisatie van de GUI. Wordt opgeroepen uit de constructor.
	 */
    private void initGUI() {
        bgc = new Color(239, 228, 176);
        mainFrame.setJMenuBar(null);
        this.add(getBannerLabel());
        this.add(getIrcServerLabel());
        this.add(getIrcPortLabel());
        this.add(getIrcChannelLabel());
        this.add(getNickLabel());
        this.add(getIrcServerTextField());
        this.add(getIrcPortTextField());
        this.add(getIrcChannelTextField());
        this.add(getNickTextField());
        this.add(getConnectButton());
        this.add(getCancelButton());
        this.add(getProgressBar());
        this.add(getProgressLabel());
        this.add(imageLabel);
        imageLabel.setBounds(360, 55, 405, 430);
        imageLabel.setSize(405, 430);
        imageLabel.setLocation(360, 55);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setIcon(myImage);
        this.setSize(765, 490);
        setLayout(new BorderLayout());
        setBackground(bgc);
        this.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ircServerTextField.requestFocusInWindow();
            }
        });
    }

    /**
	 * Functie ge�rfd van JPanel.
	 * Tekent het Panel naar de GUI.
	 */
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(15, 55, 345, 430);
        g.setColor(Color.GRAY);
        g.drawLine(25, 300, 350, 300);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    /**
	 * Functie die de BannerLabel returned ('getter').
	 * Initialiseert het label wanneer dit nog niet gebeurd is.
	 * @return bannerLabel (JLabel)
	 */
    private JLabel getBannerLabel() {
        if (bannerLabel == null) {
            bannerLabel = new JLabel();
            bannerLabel.setText("Geef uw voorkeuren in om verbinding te maken met een server.");
            bannerLabel.setBounds(0, 0, 765, 40);
            bannerLabel.setFont(new Font("Tahoma", 1, 14));
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bannerLabel.setOpaque(true);
            bannerLabel.setBackground(Color.black);
            bannerLabel.setForeground(Color.white);
        }
        return bannerLabel;
    }

    /**
	 * Functie die het IRCChannelLabel returned ('getter').
	 * Initialiseert het label wanneer dit nog niet gebeurd is.
	 * @return ircChannelLabel (JLabel)
	 */
    private JLabel getIrcChannelLabel() {
        if (ircChannelLabel == null) {
            ircChannelLabel = new JLabel("Kanaal:");
            ircChannelLabel.setBounds(35, 160, 65, 20);
            ircChannelLabel.setBackground(Color.WHITE);
            ircChannelLabel.setForeground(Color.BLACK);
            ircChannelLabel.setHorizontalAlignment(SwingConstants.LEFT);
            ircChannelLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            ircChannelLabel.setVisible(true);
        }
        return ircChannelLabel;
    }

    /**
	 * Functie die het IRCChannelLabel returned ('getter').
	 * Initialiseert het TextField wanneer dit nog niet gedaan is.
	 * @return ircChannelTextField (JTextField)
	 */
    private JTextField getIrcChannelTextField() {
        if (ircChannelTextField == null) {
            TextFieldGetsFocus focusListener = new TextFieldGetsFocus();
            ircChannelTextField = new JTextField();
            ircChannelTextField.setBounds(115, 155, 120, 30);
            ircChannelTextField.setSize(120, 30);
            ircChannelTextField.setLocation(new Point(115, 155));
            ircChannelTextField.setOpaque(true);
            ircChannelTextField.setBackground(Color.WHITE);
            ircChannelTextField.setForeground(Color.BLACK);
            ircChannelTextField.setText(DEFAULT_CHANNEL);
            ircChannelTextField.setToolTipText("Geef hier het kanaal in voorafgegaan door #.");
            ircChannelTextField.addFocusListener(focusListener);
        }
        return ircChannelTextField;
    }

    /**
	 * Functie die het IRCServerLabel returned ('getter').
	 * Initialiseert het Label wanneer dit nog niet gedaan is.
	 * @return ircServerLabel (JLabel).
	 */
    private JLabel getIrcServerLabel() {
        if (ircServerLabel == null) {
            ircServerLabel = new JLabel("Server:");
            ircServerLabel.setBounds(35, 80, 65, 20);
            ircServerLabel.setBackground(Color.WHITE);
            ircServerLabel.setForeground(Color.BLACK);
            ircServerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            ircServerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            ircServerLabel.setVisible(true);
        }
        return ircServerLabel;
    }

    /**
	 * Functie het IRCServerTextField returned ('getter').
	 * Initialiseert het TextField wanneer dit nog niet gedaan is.
	 * @return ircServerTextField (JTextField)
	 */
    private JTextField getIrcServerTextField() {
        if (ircServerTextField == null) {
            TextFieldGetsFocus focusListener = new TextFieldGetsFocus();
            ircServerTextField = new JTextField();
            ircServerTextField.setBounds(115, 75, 200, 30);
            ircServerTextField.setSize(200, 30);
            ircServerTextField.setLocation(new Point(115, 75));
            ircServerTextField.setOpaque(true);
            ircServerTextField.setBackground(Color.WHITE);
            ircServerTextField.setForeground(Color.BLACK);
            ircServerTextField.setText(DEFAULT_SERVER);
            ircServerTextField.setToolTipText("Geef hier de IRC Server op waarmee u wilt verbinden.");
            ircServerTextField.addFocusListener(focusListener);
        }
        return ircServerTextField;
    }

    /**
	 * Functie die het IRCPortLabel returned ('getter').
	 * Initialiseert het Label wanneer dit nog niet gedaan is.
	 * @return ircPortLabel (JLabel).
	 */
    private JLabel getIrcPortLabel() {
        if (ircPortLabel == null) {
            ircPortLabel = new JLabel("Poort:");
            ircPortLabel.setBounds(35, 120, 65, 20);
            ircPortLabel.setBackground(Color.WHITE);
            ircPortLabel.setForeground(Color.BLACK);
            ircPortLabel.setHorizontalAlignment(SwingConstants.LEFT);
            ircPortLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            ircPortLabel.setVisible(true);
        }
        return ircPortLabel;
    }

    /**
	 * Functie die het IRCPortTextField returned ('getter').
	 * Initialiseert het TextField wanneer dit nog niet gedaan is.
	 * @return ircPortTextField (JTextField)
	 */
    private JTextField getIrcPortTextField() {
        if (ircPortTextField == null) {
            TextFieldGetsFocus focusListener = new TextFieldGetsFocus();
            ircPortTextField = new JTextField();
            ircPortTextField.setBounds(115, 115, 50, 30);
            ircPortTextField.setSize(50, 30);
            ircPortTextField.setLocation(new Point(115, 115));
            ircPortTextField.setOpaque(true);
            ircPortTextField.setBackground(Color.WHITE);
            ircPortTextField.setForeground(Color.BLACK);
            ircPortTextField.setText(DEFAULT_PORT);
            ircPortTextField.setToolTipText("Geef hier het poortnummer voor de server in.");
            ircPortTextField.addFocusListener(focusListener);
        }
        return ircPortTextField;
    }

    /**
	 * Functie die het NickLabel returned ('getter').
	 * Initialiseert het Label waneer dit nog niet gedaan is.
	 * @return nickLabel (JLabel).
	 */
    private JLabel getNickLabel() {
        if (nickLabel == null) {
            nickLabel = new JLabel("Nickname:");
            nickLabel.setBounds(35, 200, 65, 20);
            nickLabel.setBackground(Color.WHITE);
            nickLabel.setForeground(Color.BLACK);
            nickLabel.setHorizontalAlignment(SwingConstants.LEFT);
            nickLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            nickLabel.setVisible(true);
        }
        return nickLabel;
    }

    /**
	 * Functie die het NickTextField returned ('getter').
	 * Initialiseert het TextField wanneer dit nog niet gebeurd is.
	 * @return nickTextField (JTextField).
	 */
    private JTextField getNickTextField() {
        if (nickTextField == null) {
            TextFieldGetsFocus focusListener = new TextFieldGetsFocus();
            nickTextField = new JTextField();
            nickTextField.setBounds(115, 195, 120, 30);
            nickTextField.setSize(120, 30);
            nickTextField.setLocation(new Point(115, 195));
            nickTextField.setOpaque(true);
            nickTextField.setBackground(Color.WHITE);
            nickTextField.setForeground(Color.BLACK);
            nickTextField.setText(DEFAULT_NICK);
            nickTextField.setToolTipText("Geef hier uw gewenste nickname in.");
            nickTextField.addFocusListener(focusListener);
        }
        return nickTextField;
    }

    /**
	 * Functie die het ConnectButton returned ('getter').
	 * Initialiseert de Button wanneer dit nog niet gebeurd is.
	 * @return connectButton (JButton).
	 */
    private JButton getConnectButton() {
        if (connectButton == null) {
            connectButtonClicked actionListener = new connectButtonClicked();
            connectButton = new JButton();
            connectButton.setText("Verbinden");
            connectButton.setBounds(75, 255, 100, 30);
            connectButton.setLocation(75, 255);
            connectButton.setSize(100, 30);
            connectButton.setEnabled(true);
            connectButton.addActionListener(actionListener);
            connectButton.setMnemonic(KeyEvent.VK_ENTER);
            connectButton.registerKeyboardAction(connectButton.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
            connectButton.registerKeyboardAction(connectButton.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        return connectButton;
    }

    /**
	 * Functie die de CancelButton returned ('getter').
	 * Initialiseert de Button wanneer dit nog niet gebeurd is.
	 * @return cancelButton (JButton).
	 */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButtonClicked actionListener = new cancelButtonClicked();
            cancelButton = new JButton();
            cancelButton.setText("Annuleren");
            cancelButton.setBounds(200, 255, 100, 30);
            cancelButton.setLocation(200, 255);
            cancelButton.setSize(100, 30);
            cancelButton.setEnabled(false);
            cancelButton.addActionListener(actionListener);
        }
        return cancelButton;
    }

    /**
	 * Functie die de ProgressLabel returned ('getter').
	 * Initialiseert het Label wanneer dit nog niet gebeurd is.
	 * @return progressLabel (JLabel).
	 */
    private JLabel getProgressLabel() {
        if (progressLabel == null) {
            progressLabel = new JLabel("Er wordt verbinding gemaakt met de server.");
            progressLabel.setBounds(35, 350, 305, 20);
            progressLabel.setBackground(Color.WHITE);
            progressLabel.setForeground(Color.BLACK);
            progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
            progressLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            progressLabel.setVisible(false);
        }
        return progressLabel;
    }

    /**
	 * Functie die de ProgressBar returned ('getter').
	 * Initialiseert de ProgressBar wanneer dit nog niet gebeurd is.
	 * @return progressbar (JProgressBar).
	 */
    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setBounds(110, 390, 150, 25);
            progressBar.setVisible(false);
        }
        return progressBar;
    }

    /**
	 * Private Klasse die een FocusListener implementeert.
	 * Zal de GUI van extra informatie voorzien naargelang de focus van de gebruiker.
	 * @author Maarten Minnebo, David Covemaeker, Tim Van Thuyne, Toon Kint
	 */
    private class TextFieldGetsFocus implements FocusListener {

        private String output = "";

        private String title = "";

        /**
		 * Override-functie van de FocusListener.
		 * Wanneer een JTextField zijn focus verliest (= gebruiker selecteert een TextField of Button) 
		 * dan controleert deze functie over welke textField het gaat en delegeert naar de juist check()-functie.
		 * Deze check-functie controleert de inhoud van het textfield op zijn juistheid.
		 */
        @Override
        public void focusLost(FocusEvent fe) {
            JTextField jtf = (JTextField) fe.getComponent();
            if (jtf.equals(ircServerTextField)) checkIrcServerTextField(jtf);
            if (jtf.equals(ircChannelTextField)) checkIrcChannelTextField(jtf);
            if (jtf.equals(ircPortTextField)) checkIrcPortTextField(jtf);
            if (jtf.equals(nickTextField)) checkNickTextField(jtf);
            if (!output.equals("")) {
                JOptionPane.showMessageDialog(mainFrame.getContentPane(), output, title, JOptionPane.ERROR_MESSAGE);
                jtf.requestFocusInWindow();
            }
        }

        /**
		 * Wordt aangeroepen wanneer ircServerTextField zijn focus verliest.
		 * Controleert de inhoud op correctheid.
		 * Geeft foutmelding wanneer fout en plaatst focus terug. 
		 * @param jtf JTextField (ircServerTextField)
		 * @return String output
		 */
        private String checkIrcServerTextField(JTextField jtf) {
            String server = jtf.getText();
            output = "";
            title = "Foutieve Server";
            validServer = true;
            if (!server.matches("[a-zA-Z0-9.]+")) {
                output = "Enkel letters, cijfers en punten zijn toegelaten.";
                jtf.setText(DEFAULT_SERVER);
                validServer = false;
            }
            return output;
        }

        /**
		 * Wordt aangeroepen wanneer ircChannelTextField zijn focus verliest.
		 * Controleert de inhoud op correctheid.
		 * Geeft foutmelding wanneer fout en plaatst focus terug. 
		 * @param jtf TextField (ircChannelTextField)
		 * @return String output
		 */
        private String checkIrcChannelTextField(JTextField jtf) {
            String channel = jtf.getText();
            output = "";
            title = "Foutief Channel";
            validChannel = true;
            if (!channel.matches("#[a-zA-Z0-9./_/-]+")) {
                if (!channel.startsWith("#")) output = "Het eerste teken moet '#' zijn."; else output = "Enkel letters, cijfers, _ en . zijn toegelaten.";
                jtf.setText(DEFAULT_CHANNEL);
                validChannel = false;
            }
            return output;
        }

        /**
		 * Wordt aangeroepen wanneer ircPortTextField zijn focus verliest.
		 * Controleert de inhoud op correctheid.
		 * Geeft foutmelding wanneer fout en plaatst focus terug. 
		 * @param jtf JTextField (ircPortTextField)
		 * @return String output
		 */
        private String checkIrcPortTextField(JTextField jtf) {
            String poort = jtf.getText();
            output = "";
            title = "Foutieve Poort";
            validPort = true;
            if (!poort.matches("[0-9]*")) {
                output = "Enkel cijfers zijn toegelaten.";
                jtf.setText(DEFAULT_PORT);
                validPort = false;
            } else {
                int poortnummer = Integer.parseInt(poort);
                if (poortnummer < 1024 || poortnummer > 65535) {
                    output = "Geef een poortnummer in tussen 1024 en 65535.";
                    jtf.setText(DEFAULT_PORT);
                    validPort = false;
                }
            }
            return output;
        }

        /**
		 * Wordt aangeroepen wanneer nickTextField zijn focus verliest.
		 * Controleert de inhoud op correctheid.
		 * Geeft foutmelding wanneer fout en plaatst focus terug. 
		 * @param jtf JTextField (nickTextField)
		 * @return String output
		 */
        private String checkNickTextField(JTextField jtf) {
            String nick = jtf.getText();
            output = "";
            title = "Foutief Channel";
            validNick = true;
            if (!nick.matches("[a-zA-Z0-9./_/-]{3,}")) {
                output = "Enkel letters, cijfers, _ en . zijn toegelaten.";
                jtf.setText(DEFAULT_NICK);
                validNick = false;
            }
            return output;
        }

        /**
		 * Override-functie van de FocusListener.
		 * Zal de tekst in het JTextField selecteren wanneer de gebruiker de focus legt op die JTextField.
		 * Wanneer het JTextField ircChannelTextField aanduidt, zal alle tekst geselecteerd worden behalve de hashtag (het eerste karakter).
		 */
        @Override
        public void focusGained(FocusEvent fe) {
            JTextField jtf = (JTextField) fe.getComponent();
            if (jtf.equals(ircChannelTextField)) jtf.select(1, jtf.getText().length()); else jtf.selectAll();
        }
    }

    /**
	 * Private klasse die de ActionListener implementeert.
	 * Zal aangeroepen worden wanneer op de ConnectButton geklikt werd.
	 * @author Maarten Minnebo, David Covemaeker, Tim Van Thuyne, Toon Kint
	 */
    private class connectButtonClicked implements ActionListener {

        /**
		 * Overridefunctie van de ActionListener.
		 * Zal na het klikken op de ConnectButton de GUI aanpassen en de gegevens naar de IRCController sturen.
		 * Uiteraard gebeurt dit enkel wanneer de gegevens correct zijn.
		 * @param ae ActionEvent
		 */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String server, nick, channel;
            int port;
            if (validServer && validPort && validChannel && validNick) {
                server = ircServerTextField.getText();
                nick = nickTextField.getText();
                channel = ircChannelTextField.getText();
                port = Integer.parseInt(ircPortTextField.getText());
                ircServerTextField.setEnabled(false);
                ircPortTextField.setEnabled(false);
                ircChannelTextField.setEnabled(false);
                nickTextField.setEnabled(false);
                connectButton.setEnabled(false);
                cancelButton.setEnabled(true);
                progressBar.setVisible(true);
                progressLabel.setVisible(true);
                boolean connected = guiController.submitData(server, port, channel, nick);
                if (!connected) {
                    mainFrame.switchToComponent(guiController, ComponentChoice.StartScherm);
                }
            }
        }
    }

    /**
	 * Private Klasse die de ActionListener implementeert.
	 * Zal aangeroepen worden wanneer op de CancelButton geklikt werd.
	 * @author Maarten Minnebo, David Covemaeker, Tim Van Thuyne, Toon Kint	 *
	 */
    private class cancelButtonClicked implements ActionListener {

        /**
		 * Zal het AanmeldScherm (StartScherm) resetten wanneer hierop geklikt wordt.
		 * @param arg0 ActionEvent
		 */
        @Override
        public void actionPerformed(ActionEvent arg0) {
            guiController.disconnect();
            mainFrame.switchToComponent(guiController, ComponentChoice.StartScherm);
        }
    }
}
