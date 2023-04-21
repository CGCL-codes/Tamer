package mathgame.login;

import mathgame.common.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ToolsTab extends JPanel {

    public static final int LEFT_INSETS = 10;

    public static final int RIGHT_INSETS = 10;

    public static final int TOP_INSETS = 5;

    public static final int BOTTOM_INSETS = 5;

    public static final int PREFERRED_WIDTH = 510;

    private ViewResultsPanel viewResultsPanel;

    private StartAnimationTestPanel startAnimationTestPanel;

    private StartQuestionReaderPanel startQuestionReaderPanel = null;

    private mathgame.devutils.animationtest.Main animationTest = null;

    private mathgame.question.reader.QuestionReader questionReader = null;

    public ToolsTab() {
        viewResultsPanel = new ViewResultsPanel();
        startAnimationTestPanel = new StartAnimationTestPanel();
        startQuestionReaderPanel = new StartQuestionReaderPanel();
        setLayout(null);
        viewResultsPanel.setSize(viewResultsPanel.getPreferredSize());
        startAnimationTestPanel.setSize(startAnimationTestPanel.getPreferredSize());
        startQuestionReaderPanel.setSize(startQuestionReaderPanel.getPreferredSize());
        viewResultsPanel.setLocation(LEFT_INSETS + getInsets().left, TOP_INSETS + getInsets().top);
        startAnimationTestPanel.setLocation(viewResultsPanel.getLocation().x, viewResultsPanel.getLocation().y + viewResultsPanel.getSize().height);
        startQuestionReaderPanel.setLocation(startAnimationTestPanel.getLocation().x, startAnimationTestPanel.getLocation().y + startAnimationTestPanel.getSize().height);
        add(viewResultsPanel);
        add(startAnimationTestPanel);
        add(startQuestionReaderPanel);
        Component limitingX = viewResultsPanel;
        Component limitingY = startQuestionReaderPanel;
        int minSizeX = LEFT_INSETS + getInsets().left + limitingX.getSize().width + RIGHT_INSETS + getInsets().right;
        int minSizeY = TOP_INSETS + getInsets().top + limitingY.getLocation().y + limitingY.getSize().height + BOTTOM_INSETS + getInsets().bottom;
        Dimension preferredSize = new Dimension(getPreferredSize().width + minSizeX, getPreferredSize().height + minSizeY);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
    }

    public void startAnimationTestButtonActionPerformed(ActionEvent evt) {
        if (animationTest != null && animationTest.isRunning()) JOptionPane.showMessageDialog(this, "Du har redan ett f�nster �ppet!", "Fel", JOptionPane.ERROR_MESSAGE); else animationTest = new mathgame.devutils.animationtest.Main(this);
    }

    private void startQuestionReaderButtonActionPerformed(ActionEvent evt) {
        if (questionReader == null || !questionReader.isVisible()) {
            synchronized (this) {
                questionReader = new mathgame.question.reader.QuestionReader(false);
                questionReader.setVisible(true);
            }
        } else JOptionPane.showMessageDialog(this, "Du har redan ett fr�gel�sar-f�nster �ppet", "Fel", JOptionPane.ERROR_MESSAGE);
    }

    private class ViewResultsPanel extends JPanel {

        public static final int LEFT_INSETS = 5;

        public static final int RIGHT_INSETS = 5;

        public static final int TOP_INSETS = 0;

        public static final int BOTTOM_INSETS = 0;

        private FixedWidthTextArea viewResultsDescriptionArea;

        private JLabel usernameLabel;

        private JTextField usernameField;

        private JButton writeGameResultsButton;

        private JButton writeDiagnosisResultsButton;

        public ViewResultsPanel() {
            int width = ToolsTab.PREFERRED_WIDTH - ToolsTab.LEFT_INSETS - ToolsTab.RIGHT_INSETS;
            setLayout(null);
            setBorder(new TitledBorder("Se resultat"));
            viewResultsDescriptionArea = new FixedWidthTextArea(width - getInsets().left - getInsets().right - LEFT_INSETS - RIGHT_INSETS);
            usernameLabel = new JLabel();
            usernameField = new JTextField(new TextLengthChecker(Database.SIZE_PERSON_ANVANDARNAMN), "", 10);
            writeGameResultsButton = new JButton();
            writeDiagnosisResultsButton = new JButton();
            viewResultsDescriptionArea.setText("H�r kan du f� utskrifter spel- och diagnosresultat p� fil. Du kan bara skriva in elevers anv�ndarnamn, eftersom att resultaten bara lagras f�r elever.");
            viewResultsDescriptionArea.setOpaque(false);
            usernameLabel.setText("Anv�ndarnamn");
            writeGameResultsButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    writeGameResultsButtonActionPerformed(evt);
                }
            });
            writeGameResultsButton.setText("Skriv spelresultat till fil");
            writeDiagnosisResultsButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    writeDiagnosisResultsButtonActionPerformed(evt);
                }
            });
            writeDiagnosisResultsButton.setText("Skriv diagnosresultat till fil");
            viewResultsDescriptionArea.setSize(viewResultsDescriptionArea.getPreferredSize());
            usernameLabel.setSize(usernameLabel.getPreferredSize());
            usernameField.setSize(usernameField.getPreferredSize());
            writeGameResultsButton.setSize(writeGameResultsButton.getPreferredSize());
            writeDiagnosisResultsButton.setSize(writeDiagnosisResultsButton.getPreferredSize());
            viewResultsDescriptionArea.setLocation(getInsets().left + LEFT_INSETS, getInsets().top + TOP_INSETS);
            usernameLabel.setLocation(viewResultsDescriptionArea.getLocation().x, (viewResultsDescriptionArea.getLocation().y + viewResultsDescriptionArea.getSize().height + 5 + (usernameLabel.getSize().height < usernameField.getSize().height ? usernameField.getSize().height - usernameLabel.getSize().height : 0)));
            usernameField.setLocation(usernameLabel.getLocation().x + usernameLabel.getSize().width + 5, (usernameLabel.getLocation().y + usernameLabel.getSize().height / 2 - usernameField.getSize().height / 2));
            writeGameResultsButton.setLocation(usernameLabel.getLocation().x, Math.max(usernameLabel.getLocation().y + usernameLabel.getSize().height, usernameField.getLocation().y + usernameField.getSize().height) + 10);
            writeDiagnosisResultsButton.setLocation((writeGameResultsButton.getLocation().x + writeGameResultsButton.getSize().width), writeGameResultsButton.getLocation().y);
            add(viewResultsDescriptionArea);
            add(usernameLabel);
            add(usernameField);
            add(writeGameResultsButton);
            add(writeDiagnosisResultsButton);
            Dimension preferredSize = new Dimension(width, writeDiagnosisResultsButton.getLocation().y + writeDiagnosisResultsButton.getSize().height + BOTTOM_INSETS + getInsets().bottom);
            setPreferredSize(preferredSize);
            setMinimumSize(preferredSize);
        }

        public void writeGameResultsButtonActionPerformed(ActionEvent evt) {
            String username = usernameField.getText();
            if (username.trim().equals("")) JOptionPane.showMessageDialog(this, "Du har inte fyllt i ett anv�ndarnamn!", "Fel", JOptionPane.ERROR_MESSAGE); else {
                if (!Database.getInstance().writeResultsGame(username)) JOptionPane.showMessageDialog(this, "Kunde inte spara spelresultat! (Ok�nt fel)", "Fel", JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, "Spelresultat sparade till filen \"Spelresultat_f�r_" + username + ".txt\"", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void writeDiagnosisResultsButtonActionPerformed(ActionEvent evt) {
            String username = usernameField.getText();
            if (username.trim().equals("")) JOptionPane.showMessageDialog(this, "Du har inte fyllt i ett anv�ndarnamn!", "Fel", JOptionPane.ERROR_MESSAGE); else {
                if (!Database.getInstance().writeResultsDiagnosis(username)) JOptionPane.showMessageDialog(this, "Kunde inte spara diagnosresultat! (Ok�nt fel)", "Fel", JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, "Diagnosresultat sparade till filen \"Diagnosresultat_f�r_" + username + ".txt\"", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class StartAnimationTestPanel extends JPanel {

        public static final int LEFT_INSETS = 5;

        public static final int RIGHT_INSETS = 5;

        public static final int TOP_INSETS = 0;

        public static final int BOTTOM_INSETS = 0;

        private FixedWidthTextArea descriptionArea;

        private JButton startButton;

        public StartAnimationTestPanel() {
            int width = ToolsTab.PREFERRED_WIDTH - ToolsTab.LEFT_INSETS - ToolsTab.RIGHT_INSETS;
            setLayout(null);
            setBorder(new TitledBorder("Testprogram f�r animationer"));
            descriptionArea = new FixedWidthTextArea(width - getInsets().left - getInsets().right - LEFT_INSETS - RIGHT_INSETS);
            startButton = new JButton();
            descriptionArea.setText("Om du h�ller p� och ritar r�rliga figurer till Matematikspelet och vill se hur se ser ut i sekvens s� finns h�r ett verktyg.");
            descriptionArea.setOpaque(false);
            add(descriptionArea);
            descriptionArea.setSize(descriptionArea.getPreferredSize());
            descriptionArea.setLocation(getInsets().left + LEFT_INSETS, getInsets().top + TOP_INSETS);
            startButton.setText("Starta testprogrammet f�r animationer");
            startButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    startAnimationTestButtonActionPerformed(evt);
                }
            });
            add(startButton);
            startButton.setSize(startButton.getPreferredSize());
            startButton.setLocation(descriptionArea.getLocation().x, descriptionArea.getLocation().y + descriptionArea.getSize().height + 10);
            setPreferredSize(new Dimension(width, startButton.getLocation().y + startButton.getSize().height + BOTTOM_INSETS + getInsets().bottom));
        }
    }

    private class StartQuestionReaderPanel extends JPanel {

        public static final int LEFT_INSETS = 5;

        public static final int RIGHT_INSETS = 5;

        public static final int TOP_INSETS = 0;

        public static final int BOTTOM_INSETS = 0;

        private FixedWidthTextArea descriptionArea;

        private JButton startButton;

        public StartQuestionReaderPanel() {
            int width = ToolsTab.PREFERRED_WIDTH - ToolsTab.LEFT_INSETS - ToolsTab.RIGHT_INSETS;
            setLayout(null);
            setBorder(new TitledBorder("Frågel�sare"));
            descriptionArea = new FixedWidthTextArea(width - getInsets().left - getInsets().right - LEFT_INSETS - RIGHT_INSETS);
            startButton = new JButton();
            descriptionArea.setText("H�r kan du skapa fr�gor fr�n en fil. Mer om denna funktionalitet finns beskrivet i anv�ndarmanualen.");
            descriptionArea.setOpaque(false);
            add(descriptionArea);
            descriptionArea.setSize(descriptionArea.getPreferredSize());
            descriptionArea.setLocation(getInsets().left + LEFT_INSETS, getInsets().top + TOP_INSETS);
            startButton.setText("Starta Fr�gel�saren");
            startButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    startQuestionReaderButtonActionPerformed(evt);
                }
            });
            add(startButton);
            startButton.setSize(startButton.getPreferredSize());
            startButton.setLocation(descriptionArea.getLocation().x, descriptionArea.getLocation().y + descriptionArea.getSize().height + 10);
            setPreferredSize(new Dimension(width, startButton.getLocation().y + startButton.getSize().height + BOTTOM_INSETS + getInsets().bottom));
        }
    }
}
