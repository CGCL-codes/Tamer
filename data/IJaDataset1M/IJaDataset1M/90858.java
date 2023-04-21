package saheba.util.rsa.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import saheba.util.rsa.Keys;
import saheba.util.rsa.Logic;

/**
 * The Class RSACrypter is a simple development tool which can be used to decode
 * or encode Strings, e.g. to decode a property value for a coded property file.
 * 
 * @author Sarah Haselbauer
 * @creation 13.06.2008
 */
public class RSACrypter {

    /** The input. */
    private static JTextField input = null;

    /** The target. */
    private static JTextField target = null;

    /** The result. */
    private static JTextField result = new JTextField();

    /** The frame. */
    private static JFrame frame = new JFrame();

    /**
	 * The main method runs the smartGui.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) {
        getSmartGui().setVisible(true);
    }

    /**
	 * Gets the smart gui.
	 * 
	 * @return the smart gui
	 */
    public static JFrame getSmartGui() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("RSACrypter");
        frame.setLayout(new BorderLayout());
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(3, 1));
        JPanel serOptionPanel = new JPanel();
        serOptionPanel.setLayout(new GridLayout(2, 1));
        serOptionPanel.add(new JLabel("(existing) serialized key pair file:"));
        target = new JTextField("C:\\Programme\\PumBi\\conf" + System.getProperty("file.separator") + Keys.DEFAULT_RSA_KEY_FILE_NAME);
        target.setPreferredSize(new Dimension(700, 30));
        serOptionPanel.add(target);
        JPanel inputOptionPanel = new JPanel();
        inputOptionPanel.setLayout(new GridLayout(2, 1));
        inputOptionPanel.add(new JLabel("input"));
        input = new JTextField("");
        input.setPreferredSize(new Dimension(700, 30));
        inputOptionPanel.add(input);
        JPanel resultOptionPanel = new JPanel();
        resultOptionPanel.setLayout(new GridLayout(2, 1));
        resultOptionPanel.add(new JLabel("result"));
        result.setEditable(false);
        result.setPreferredSize(new Dimension(700, 30));
        resultOptionPanel.add(result);
        optionPanel.add(serOptionPanel);
        optionPanel.add(inputOptionPanel);
        optionPanel.add(resultOptionPanel);
        frame.add(optionPanel, BorderLayout.CENTER);
        JButton crypt = new JButton("crypt");
        crypt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doCrypt(null);
            }
        });
        JButton decode = new JButton("decode");
        decode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doDecode(null);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(crypt);
        buttonPanel.add(decode);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setResizable(false);
        frame.pack();
        return frame;
    }

    private static Logic logic;

    /**
	 * Do crypt.
	 * 
	 * @pre the text of targetTextField referrences a valid rsaKey.ser file
	 *      which has been generated with the {@link RSASerFileGenerator}
	 */
    private static void doCrypt(Class<?> baseInnerClass) {
        Keys keysInUse = new Keys(baseInnerClass, target.getText());
        logic = new Logic(keysInUse);
        result.setText(logic.crypt(input.getText()));
    }

    /**
	 * Do decode.
	 * 
	 * @pre the text of targetTextField referrences a valid rsaKey.ser file
	 *      which has been generated with the {@link RSASerFileGenerator}
	 */
    private static void doDecode(Class<?> baseInnerClass) {
        Keys keysInUse = new Keys(baseInnerClass, target.getText());
        logic = new Logic(keysInUse);
        result.setText(logic.decode(input.getText()));
    }
}
