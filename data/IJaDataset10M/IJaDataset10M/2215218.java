package net.sourceforge.jlatin.modules;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import net.sourceforge.jlatin.constants.*;
import net.sourceforge.jlatin.gui.*;

class ModuleMath extends JPanelReview {

    protected static String repeat(String s, int times) {
        String output = "";
        for (int i = 0; i < times; i++) {
            output += s;
        }
        return output;
    }

    int a, b, c;

    char operator;

    JLabel MathQuestion;

    JTextField userResponse;

    final int minimumValue = 1;

    final int maximumValue = 123;

    public static String convertArabic(int arabic) {
        switch(arabic) {
            case 0:
                return "";
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
        if (arabic < 40) {
            int repeats;
            repeats = (arabic / 10);
            return repeat("X", repeats) + convertArabic(arabic - 10 * repeats);
        } else if (arabic < 50) {
            return "XL" + convertArabic(arabic - 40);
        } else if (arabic < 90) {
            return "L" + convertArabic(arabic - 50);
        } else if (arabic < 100) {
            return "XC" + convertArabic(arabic - 90);
        } else if (arabic < 400) {
            int repeats;
            repeats = (arabic / 100);
            return repeat("C", repeats) + convertArabic(arabic - 100 * repeats);
        } else if (arabic < 500) {
            return "CD" + convertArabic(arabic - 400);
        } else if (arabic < 1000) {
            int repeats;
            repeats = (arabic / 500);
            return repeat("D", repeats) + convertArabic(arabic - 500 * repeats);
        } else if (arabic < 5001) {
            int repeats;
            repeats = (arabic / 1000);
            return repeat("M", repeats) + convertArabic(arabic - 1000 * repeats);
        }
        return "U_N_K_O_W_N";
    }

    public void createNewQuestion() {
        Random theRand = new Random();
        switch(theRand.nextInt(4)) {
            case (0):
                {
                    operator = '+';
                    c = theRand.nextInt(maximumValue - minimumValue) + minimumValue + 1;
                    b = theRand.nextInt(c - 1) + minimumValue;
                    a = c - b;
                }
                break;
            case (1):
                {
                    operator = '-';
                    a = theRand.nextInt(maximumValue - minimumValue) + minimumValue + 1;
                    b = theRand.nextInt(a - 1) + minimumValue;
                    c = a - b;
                }
                break;
            case (2):
                {
                    operator = '*';
                    c = theRand.nextInt(maximumValue - minimumValue) + minimumValue;
                    Vector factors = getFactors(c);
                    b = ((Integer) (factors.elementAt(theRand.nextInt(factors.size())))).intValue();
                    a = c / b;
                }
                break;
            case (3):
                {
                    operator = '/';
                    a = theRand.nextInt(maximumValue - minimumValue) + minimumValue;
                    Vector factors = getFactors(a);
                    b = ((Integer) (factors.elementAt(theRand.nextInt(factors.size())))).intValue();
                    c = a / b;
                }
                break;
        }
        updateDisplayedQuestion();
    }

    public void updateDisplayedQuestion() {
        MathQuestion.setText(convertArabic(a) + " " + operator + " " + convertArabic(b) + " = ");
    }

    protected Vector getFactors(int input) {
        Vector v = new Vector();
        for (int i = 1; i <= input; i++) {
            if (input % i == 0) {
                v.add(new Integer(i));
            }
        }
        return v;
    }

    ModuleMath() {
        final String FontName = new String("Helvetica Bold");
        final int FontStyleName = Font.BOLD;
        final int FontSize = 40;
        MathQuestion = new JLabel();
        JPanel MathQuestionPanel = new JPanel();
        userResponse = new JTextField(10);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 5));
        a = 1;
        b = 2;
        c = 3;
        createNewQuestion();
        final JButton newQuestion = new JButton("New Question");
        newQuestion.setMnemonic(KeyEvent.VK_N);
        final JButton romanAnswer = new JButton("Roman answer");
        final JButton arabicAnswer = new JButton("Arabic answer");
        final JButton arabicQuestion = new JButton("Arabic question");
        newQuestion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createNewQuestion();
                userResponse.requestFocus();
            }
        });
        arabicQuestion.addMouseListener(new MouseAdapter() {

            String oldWord = new String();

            public void mousePressed(MouseEvent e) {
                oldWord = MathQuestion.getText();
                MathQuestion.setForeground(constants.hintColor);
                MathQuestion.setText(a + " " + operator + " " + b + " = ");
            }

            public void mouseReleased(MouseEvent e) {
                MathQuestion.setForeground(Color.black);
                MathQuestion.setText(oldWord);
                userResponse.grabFocus();
            }
        });
        romanAnswer.addMouseListener(new MouseAdapter() {

            String oldWord = new String();

            public void mousePressed(MouseEvent e) {
                oldWord = userResponse.getText();
                userResponse.setForeground(constants.hintColor);
                userResponse.setText(convertArabic(c));
            }

            public void mouseReleased(MouseEvent e) {
                userResponse.setForeground(Color.black);
                userResponse.setText(oldWord);
                userResponse.grabFocus();
            }
        });
        arabicAnswer.addMouseListener(new MouseAdapter() {

            String oldWord = new String();

            public void mousePressed(MouseEvent e) {
                oldWord = userResponse.getText();
                userResponse.setForeground(constants.hintColor);
                userResponse.setText("" + c);
            }

            public void mouseReleased(MouseEvent e) {
                userResponse.setForeground(Color.black);
                userResponse.setText(oldWord);
                userResponse.grabFocus();
            }
        });
        userResponse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if ((userResponse.getText()).trim().equalsIgnoreCase("" + c) || (userResponse.getText()).trim().equalsIgnoreCase(convertArabic(c))) {
                    userResponse.setText("");
                    createNewQuestion();
                }
            }
        });
        MathQuestionPanel.setLayout(new GridLayout(1, 2));
        MathQuestionPanel.add(MathQuestion);
        controlPanel.add(newQuestion);
        controlPanel.add(romanAnswer);
        controlPanel.add(arabicAnswer);
        controlPanel.add(arabicQuestion);
        controlPanel.add(exitButton);
        controlPanel.setBorder(new TitledBorder("The Control Panel"));
        userResponse.setFont(new Font(FontName, FontStyleName, FontSize));
        userResponse.setHorizontalAlignment(SwingConstants.LEFT);
        MathQuestion.setFont(new Font(FontName, FontStyleName, FontSize));
        MathQuestion.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setLayout(new GridLayout(4, 1));
        this.add(new JPanel());
        this.add(MathQuestionPanel);
        this.add(new JPanel());
        this.add(controlPanel);
        MathQuestionPanel.add(userResponse);
    }
}
