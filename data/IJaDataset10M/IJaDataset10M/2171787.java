package gui.mainmenupanel;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenuPanelView extends JPanel {

    private static final long serialVersionUID = 1L;

    MainMenuPanelModel model = new MainMenuPanelModel(this);

    JLabel wordsToAskCount;

    JLabel totalWordCount;

    public MainMenuPanelView() {
        setLayout(null);
        initComponents();
        addActions();
        addComponents();
        model.getDataFromDatabase();
        setVisible(true);
    }

    public void initComponents() {
        wordsToAskCount = new JLabel("Number of words to ask: ");
        wordsToAskCount.setBounds(50, 50, 250, 25);
        totalWordCount = new JLabel("Total number of words: ");
        totalWordCount.setBounds(50, 100, 250, 25);
    }

    public void addActions() {
    }

    public void addComponents() {
        add(wordsToAskCount);
        add(totalWordCount);
    }
}
