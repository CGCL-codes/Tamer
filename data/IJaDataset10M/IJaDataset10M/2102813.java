package hr.fer.zemris.java.tecaj_6.vjezba.gui;

import hr.fer.zemris.java.tecaj_6.vjezba.Kalkulator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.EmptyStackException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Graficko sucelje jednostavnog kalkulatora koji funkcionira na principu stoga.
 * Ako tijekom izvodjenja neke operacije na stogu nema dovoljno elemenata, 
 * kalkulator javlja gresku. 
 * Ako zadnji broj nije unesen na stog, odnosno traje unos broja, kad se 
 * pritisne neki operator trenutni broj se sprema na stog i tada se izvrsi
 * operacija
 * 
 * @author Hrvoje Torbasinovic
 *
 */
public class KalkulatorGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panel;

    private JTextField textfield;

    /**
	 * varijabla koja oznacava da li je u tijeku unos broja u kalkulator
	 */
    private boolean unosBroja = false;

    /**
	 * kalkulator koji racuna sve potrebne operacije
	 */
    private Kalkulator calc;

    public KalkulatorGUI() {
        calc = new Kalkulator();
        initGUI();
    }

    /**
	 * stvara graficko sucelje kalkulatora
	 */
    private void initGUI() {
        setTitle("Kalkulator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        textfield = new JTextField("0");
        textfield.setHorizontalAlignment(JTextField.RIGHT);
        textfield.setEditable(false);
        textfield.setPreferredSize(new Dimension(0, 50));
        textfield.setFont(new Font(null, Font.PLAIN, 35));
        getContentPane().add(textfield, BorderLayout.NORTH);
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 5));
        addButtons();
        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
	 * dodaje gumbe u graficko sucelje kalkulatora
	 */
    private void addButtons() {
        JButton gumb;
        gumb = new JButton("7");
        gumb.setActionCommand("7");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("8");
        gumb.setActionCommand("8");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("9");
        gumb.setActionCommand("9");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("DEL");
        panel.add(gumb);
        gumb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!unosBroja) return;
                String s = textfield.getText();
                if (s.length() > 1) textfield.setText(s.substring(0, s.length() - 1)); else {
                    textfield.setText("0");
                    unosBroja = false;
                }
            }
        });
        gumb = new JButton("/");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("divide");
        panel.add(gumb);
        gumb = new JButton("4");
        gumb.setActionCommand("4");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("5");
        gumb.setActionCommand("5");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("6");
        gumb.setActionCommand("6");
        panel.add(gumb);
        gumb.addActionListener(new numbersActionListener());
        gumb = new JButton("RESET");
        panel.add(gumb);
        gumb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                calc.reset();
                textfield.setText("0");
                unosBroja = false;
            }
        });
        gumb = new JButton("*");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("multiply");
        panel.add(gumb);
        gumb = new JButton("1");
        gumb.setActionCommand("1");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("2");
        gumb.setActionCommand("2");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("3");
        gumb.setActionCommand("3");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton("sin");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("sin");
        panel.add(gumb);
        gumb = new JButton("-");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("subtract");
        panel.add(gumb);
        gumb = new JButton("0");
        gumb.setActionCommand("0");
        gumb.addActionListener(new numbersActionListener());
        panel.add(gumb);
        gumb = new JButton(".");
        panel.add(gumb);
        gumb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!textfield.getText().contains(".")) {
                    textfield.setText(textfield.getText() + ".");
                    unosBroja = true;
                }
            }
        });
        gumb = new JButton("ENTER");
        panel.add(gumb);
        gumb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                calc.push(Double.parseDouble(textfield.getText()));
                unosBroja = false;
            }
        });
        gumb = new JButton("cos");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("cos");
        panel.add(gumb);
        gumb = new JButton("+");
        gumb.addActionListener(new operatorsActionListener());
        gumb.setActionCommand("add");
        panel.add(gumb);
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    new KalkulatorGUI();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Pomocna klasa koja implementira ActionListener koji slusa kada je pritisnuta 
	 * neka znamenka na kalkulatoru. Znamenka se dodaje u text field.
	 * 
	 * @author Hrvoje Torbasinovic
	 *
	 */
    private class numbersActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (unosBroja) {
                textfield.setText(textfield.getText() + e.getActionCommand());
            } else {
                textfield.setText(e.getActionCommand());
                unosBroja = true;
            }
        }
    }

    /**
	 * Pomocna klasa koja implementira ActionListener koji slusa kada je pritisnuda neki
	 * operator na kalkulatoru. Pritisnuta operacija se izracuna i ispisuje u text field
	 * 
	 * @author Hrvoje Torbasinovic
	 *
	 */
    private class operatorsActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (unosBroja) {
                calc.push(Double.parseDouble(textfield.getText()));
                unosBroja = false;
            }
            try {
                if (e.getActionCommand().equals("add")) textfield.setText(String.valueOf(calc.add())); else if (e.getActionCommand().equals("subtract")) textfield.setText(String.valueOf(calc.subtract())); else if (e.getActionCommand().equals("multiply")) textfield.setText(String.valueOf(calc.multiply())); else if (e.getActionCommand().equals("divide")) textfield.setText(String.valueOf(calc.divide())); else if (e.getActionCommand().equals("sin")) textfield.setText(String.valueOf(calc.sin())); else if (e.getActionCommand().equals("cos")) textfield.setText(String.valueOf(calc.cos()));
            } catch (EmptyStackException ex) {
                textfield.setText("ERROR");
                unosBroja = false;
            }
        }
    }
}
