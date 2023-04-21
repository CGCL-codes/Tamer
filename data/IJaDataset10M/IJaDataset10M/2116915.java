package jir;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jir.algorithms.ptd.*;
import jir.common.Application;
import jir.common.ApplicationAdapter;
import jir.common.JMultilineLabel;

public class ClassifierSelector extends JFrame {

    private static final long serialVersionUID = 2626709140510751030L;

    protected static Application[] applications = { new PTDApplication() };

    protected static JComboBox combo;

    /**
	 * Starts the classifier selector application.
	 * @param args Arguments list
	 */
    public static void main(String[] args) {
    }

    public static ClassifierSelector selector;

    static {
        selector = new ClassifierSelector();
    }

    /**
	 * Creates a new instance of the classifier selector.
	 */
    private ClassifierSelector() {
        super("jClassifier selector");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagConstraints;
        Insets insetsR = new Insets(5, 5, 0, 5);
        Insets insetsB = new Insets(5, 5, 5, 0);
        Insets insetsBR = new Insets(5, 5, 5, 5);
        this.setLayout(gridBag);
        JLabel label1 = new JLabel("Please select which algorithm to use:");
        gridBagConstraints = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsR, 0, 0);
        this.add(label1, gridBagConstraints);
        combo = new JComboBox(applications);
        gridBagConstraints = new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsR, 0, 0);
        this.add(combo, gridBagConstraints);
        JMultilineLabel label2 = new JMultilineLabel("To run the applications directly, simply execute the corresponding Application classes located in the respective packages.", 10, 2);
        label2.setMaxWidth(250);
        label2.setJustified(true);
        label2.setFont(new Font(label1.getFont().getName(), Font.PLAIN, 10));
        gridBagConstraints = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsR, 0, 0);
        this.add(label2, gridBagConstraints);
        JButton button1 = new JButton("OK");
        gridBagConstraints = new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsB, 0, 0);
        this.add(button1, gridBagConstraints);
        JButton button2 = new JButton("Cancel");
        gridBagConstraints = new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsBR, 0, 0);
        this.add(button2, gridBagConstraints);
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ClassifierSelector.start();
            }
        });
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ClassifierSelector.cancel();
            }
        });
        this.addWindowListener(new ApplicationAdapter(this, true, null));
        this.pack();
        this.setSize(this.getPreferredSize());
        this.setMinimumSize(this.getPreferredSize());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    protected static void start() {
        ((Application) combo.getSelectedItem()).initialize();
        selector.setVisible(false);
    }

    protected static void cancel() {
        selector.setVisible(false);
        selector.dispose();
        System.exit(0);
    }
}
