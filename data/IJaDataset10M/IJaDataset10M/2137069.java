package trivia;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import questionary.MultMultAnsQ;
import questionary.MultOneAnsQ;
import questionary.NumberQ;
import questionary.Question;
import questionary.SimpleQ;
import questionary.TrueFalseQ;
import myExceptions.MyFileException;

public class ChooseQtypeFrame {

    private int y;

    private int offset = 35;

    private JFrame jChooseQtypeFrame = null;

    private JPanel jChooseQtypeContentPane = null;

    private final String[] typeQtitle = { "Pregunta con respuesta de texto simple", "Pregunta con respuesta numerica", "Pregunta de opciones multiples con unica respuesta", "Pregunta de opciones multiples con varias respuestas", "Preguntas de verdader o falso" };

    private final Object[][] typeOptions = { { "1" }, { "1" }, { "1", "2", "3", "4", "5" }, { "1", "2", "3", "4", "5" }, { "2" } };

    private JRadioButton[] jTypeRadioButton = new JRadioButton[5];

    private ButtonGroup typeGroup = new ButtonGroup();

    private JButton jOkButton = null;

    @SuppressWarnings("unused")
    private JButton jSaveButton = null;

    @SuppressWarnings("unused")
    private JButton jCancelButton = null;

    private Question newQ;

    private int type = 1;

    private int optionCount;

    private int answersCount;

    private JComboBox jOptionsCountComboBox = null;

    private JComboBox jAnswersComboBox = null;

    private JLabel jOptionCountLabel = null;

    private JLabel jAnswerLabel = null;

    public ChooseQtypeFrame() {
        jChooseQtypeFrame = getJChooseQtypeFrame();
    }

    /**
	 * This method initializes jChooseQtypeFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
    public JFrame getJChooseQtypeFrame() {
        if (jChooseQtypeFrame == null) {
            jChooseQtypeFrame = new JFrame();
            jChooseQtypeFrame.setSize(new Dimension(550, 522));
            jChooseQtypeFrame.setTitle("Choose question type ...");
            jChooseQtypeFrame.setContentPane(getJChooseQtypeContentPane());
        }
        return jChooseQtypeFrame;
    }

    /**
	 * This method initializes jChooseQtypeContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJChooseQtypeContentPane() {
        y = 20;
        if (jChooseQtypeContentPane == null) {
            jChooseQtypeContentPane = new JPanel();
            jChooseQtypeContentPane.setLayout(null);
            getJTypeRadioButton();
            for (int i = 0; i < 5; i++) {
                jChooseQtypeContentPane.add(jTypeRadioButton[i]);
                typeGroup.add(jTypeRadioButton[i]);
            }
            jTypeRadioButton[0].setSelected(true);
            jChooseQtypeContentPane.add(jOptionsCountComboBox = getJComboBoxes((Object[]) typeOptions[0], 220, y), null);
            jChooseQtypeContentPane.add(jAnswersComboBox = getJComboBoxes((Object[]) typeOptions[0], 220, y + offset), null);
            jOptionsCountComboBox.setSelectedIndex(0);
            jAnswersComboBox.setSelectedIndex(0);
            jChooseQtypeContentPane.add(getJOkButton(), null);
            jChooseQtypeContentPane.add(getJOptionCountLabel(), null);
            jChooseQtypeContentPane.add(getJAnswersLabel(), null);
        }
        return jChooseQtypeContentPane;
    }

    private void getJTypeRadioButton() {
        for (int i = 0; i < 5; i++) {
            jTypeRadioButton[i] = new JRadioButton();
            jTypeRadioButton[i].setText(typeQtitle[i]);
            jTypeRadioButton[i].setBounds(new Rectangle(15, y, 325, 25));
            jTypeRadioButton[0].setSelected(true);
            jTypeRadioButton[i].addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    for (int i = 0; i < 5; i++) {
                        if (jTypeRadioButton[i].isSelected()) {
                            type = i + 1;
                            jChooseQtypeContentPane.remove(jOptionsCountComboBox);
                            jChooseQtypeContentPane.remove(jAnswersComboBox);
                            jChooseQtypeContentPane.add(jOptionsCountComboBox = getJComboBoxes((Object[]) typeOptions[i], 220, y), null);
                            jChooseQtypeContentPane.add(jAnswersComboBox = getJComboBoxes((Object[]) typeOptions[i], 220, y + offset), null);
                            jChooseQtypeFrame.setContentPane(jChooseQtypeContentPane);
                            break;
                        }
                    }
                }
            });
            y += offset;
        }
    }

    /**
	 * This method initializes jOkButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJOkButton() {
        if (jOkButton == null) {
            jOkButton = new JButton();
            jOkButton.setBounds(new Rectangle(363, 429, 166, 52));
            jOkButton.setText("Ok");
            jOkButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    optionCount = Integer.parseInt((String) jOptionsCountComboBox.getSelectedItem());
                    answersCount = Integer.parseInt((String) jAnswersComboBox.getSelectedItem());
                    try {
                        setQuestion();
                    } catch (MyFileException e1) {
                        e1.printStackTrace();
                    }
                    jChooseQtypeFrame.setVisible(false);
                }
            });
        }
        return jOkButton;
    }

    /**
	 * This method initializes jOptionsCountComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJComboBoxes(Object[] op, int posX, int posY) {
        JComboBox cbox = new JComboBox(op);
        cbox.setBounds(new Rectangle(posX, posY, 75, 20));
        return cbox;
    }

    /**
	 * This method initializes jOptionCountLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
    private JLabel getJOptionCountLabel() {
        if (jOptionCountLabel == null) {
            jOptionCountLabel = new JLabel();
            jOptionCountLabel.setText("Options numbers (Max 5):  ");
            jOptionCountLabel.setBounds(new Rectangle(20, y, 150, 20));
        }
        return jOptionCountLabel;
    }

    private JLabel getJAnswersLabel() {
        if (jAnswerLabel == null) {
            jAnswerLabel = new JLabel();
            jAnswerLabel.setText("Answers numbers: ");
            jAnswerLabel.setBounds(new Rectangle(20, y + 30, 150, 20));
        }
        return jAnswerLabel;
    }

    public void setQuestion() throws MyFileException {
        String[] options;
        String[] answers;
        String question = "<Write here your question>";
        options = new String[optionCount];
        for (int i = 0; i < optionCount; i++) {
            options[i] = new String("");
        }
        answers = new String[answersCount];
        for (int i = 0; i < answersCount; i++) {
            answers[i] = new String("");
        }
        switch(type) {
            case 1:
                newQ = new SimpleQ(question, "", 0, 0);
                break;
            case 2:
                newQ = new NumberQ(question, 0, 0, 0);
                break;
            case 3:
                newQ = new MultOneAnsQ(question, answers[0], options, 0, 0);
                break;
            case 4:
                newQ = new MultMultAnsQ(question, answers, options, 0, 0);
                break;
            case 5:
                newQ = new TrueFalseQ(question, answers[0], 0, 0);
                break;
        }
    }

    public Question getQuestion() {
        return newQ;
    }

    public void setOptionsCount(int value) {
        optionCount = value;
    }

    public void setAnswerCount(int value) {
        answersCount = value;
    }

    public String getOptionsComboBoxSelected() {
        System.out.println("opcion combobox, item: " + jOptionsCountComboBox.getSelectedItem());
        return (String) jOptionsCountComboBox.getSelectedItem();
    }

    public String getAnswerComboBoxSelected() {
        System.out.println("answerop combobox, item: " + jAnswersComboBox.getSelectedItem());
        return (String) jAnswersComboBox.getSelectedItem();
    }
}
