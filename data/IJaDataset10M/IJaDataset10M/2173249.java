package org.pachyderm.migrationtool;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Controller for Migration Tool. This manages all the other classes, and it is the main frame
 * for the application.
 *
 * @author David Risner
 * @version $Revision: 516 $ $Date: 2008-10-10 16:57:54 -0400 (Fri, 10 Oct 2008) $
 */
public class MigrationToolController extends JFrame {

    private static final long serialVersionUID = -8035004599291406011L;

    public static final boolean DEBUG = true;

    private MigrationProperties mp = null;

    private CardLayout centerCardLayout = null;

    private JPanel centerCardPanel = null;

    private JButton continueButton = null;

    private JButton quitButton = null;

    private InitialScreen initialScreen = null;

    private ParametersScreen parametersScreen = null;

    private ProgressScreen progressScreen = null;

    private ContinueListener continueListener = null;

    public static void main(String[] args) {
        if (DEBUG) System.out.println("Running Pachyderm Migration Tool.");
        MigrationProperties mp = new MigrationProperties();
        if (DEBUG) System.out.println(mp.getString("propsloaded"));
        MigrationToolController mtc = new MigrationToolController(mp);
        mtc.centerFrameOnScreen();
        mtc.setVisible(true);
    }

    public MigrationToolController(MigrationProperties mp) {
        super(mp.getString("frametitle"));
        this.mp = mp;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            if (DEBUG) System.out.println("Unable to load look and feel");
        }
        setSize(680, 430);
        setupComponents();
        addCards();
    }

    private void setupComponents() {
        setLayout(new BorderLayout(5, 5));
        JLabel titleLabel = new JLabel(mp.getString("screen.title"), JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(titleLabel, BorderLayout.NORTH);
        centerCardLayout = new CardLayout();
        centerCardPanel = new JPanel();
        centerCardPanel.setLayout(centerCardLayout);
        add(centerCardPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 30, 4));
        quitButton = new JButton(mp.getString("button.quit"));
        continueButton = new JButton(mp.getString("button.continue"));
        buttonPanel.add(new JPanel());
        buttonPanel.add(continueButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(new JPanel());
        add(buttonPanel, BorderLayout.SOUTH);
        continueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                continueButtonClicked(e.getActionCommand());
            }
        });
        quitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quitButtonClicked();
            }
        });
    }

    private void addCards() {
        initialScreen = new InitialScreen(mp);
        parametersScreen = new ParametersScreen(mp);
        progressScreen = new ProgressScreen(mp, this);
        centerCardPanel.add(initialScreen, initialScreen.getCardName());
        centerCardPanel.add(parametersScreen, parametersScreen.getCardName());
        centerCardPanel.add(progressScreen, progressScreen.getCardName());
        displayInitialCard();
    }

    private void displayInitialCard() {
        continueButton.setActionCommand(parametersScreen.getCardName());
        continueListener = null;
        centerCardLayout.show(centerCardPanel, initialScreen.getCardName());
    }

    private void displayParametersCard() {
        continueButton.setActionCommand(progressScreen.getCardName());
        continueListener = parametersScreen;
        centerCardLayout.show(centerCardPanel, parametersScreen.getCardName());
    }

    private void displayProgressCard() {
        continueButton.setActionCommand("done");
        continueListener = null;
        centerCardLayout.show(centerCardPanel, progressScreen.getCardName());
        progressScreen.startMigrationTasks();
    }

    protected void quitButtonClicked() {
        if (DEBUG) System.out.println("quitButtonClicked()");
        System.exit(0);
    }

    protected void continueButtonClicked(String command) {
        if (DEBUG) System.out.println("continueButtonClicked(), command = \"" + command + "\"");
        if (continueListener != null) {
            if (!continueListener.continueButtonClicked()) {
                return;
            }
        }
        if (command.equals(parametersScreen.getCardName())) {
            if (DEBUG) System.out.println("Going to displayParametersCard");
            displayParametersCard();
        } else if (command.equals(progressScreen.getCardName())) {
            if (DEBUG) System.out.println("Going to progressCard");
            displayProgressCard();
        }
    }

    /**
   * Center this JFrame on the screen
   */
    public void centerFrameOnScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Point p = new Point();
        double centerX = d.getWidth() / 2;
        double centerY = d.getHeight() / 2;
        this.getSize(d);
        p.setLocation(centerX - (d.getWidth() / 2), centerY - (d.getHeight() / 2));
        if (p.getX() < 0) {
            p.setLocation(0, p.getY());
        }
        if (p.getY() < 0) {
            p.setLocation(p.getX(), 0);
        }
        this.setLocation(p);
    }

    public Params getSourceParameters() {
        return parametersScreen.getSourceParams();
    }

    public Params getDestinationParameters() {
        return parametersScreen.getDestinationParams();
    }

    public void disableContinueButton() {
        continueButton.setEnabled(false);
    }
}
