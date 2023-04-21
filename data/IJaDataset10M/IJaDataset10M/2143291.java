package projektdiary.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import javax.swing.JButton;

public class HelpDialog extends JDialog {

    private JPanel jContentPane = null;

    private JTextArea jTextArea = null;

    private JEditorPane jEditorPane = null;

    private JButton jButton = null;

    private JScrollPane jScrollPane1 = null;

    /**
	 * This is the default constructor
	 */
    public HelpDialog() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(400, 500);
        this.setTitle("Help Dialog");
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJScrollPane1(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJButton(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jEditorPane
	 * 
	 * @return javax.swing.JEditorPane
	 */
    private JEditorPane getJEditorPane() {
        if (jEditorPane == null) {
            jEditorPane = new JEditorPane();
            jEditorPane.setContentType("text/html");
            jEditorPane.setEditable(false);
            jEditorPane.setText("<html>\n  <head>\n    <title>Help for Project Diary</title>\n  </head>\n  <body>\n  <h2>What is Project Diary</h2><br>\n  \n  Project Diary is a program that helps you keep trak of your work with a project.\n  \n  <h2>How to use</h2><br>\n  Every time you have made changes to your project open your project diary program and write down what you have done and whatever you wish and how much time you have spent on it.<br><br>\n  \n  The program keeps track of the total time the project has taken. If you want a report on what you have done it's just to export your project diary to a html file frome the file menu.<br>\n  \n    <h2>The graphical user interface(gui)</h2><br>\n    The gui is build up by two main components. One is a list of events and the second is an editor for the current selected event.<br>\n      <h3>The event list</h3><br>\n       You can add and delete events by using the corresponding buttons in the bottom of the list. Select an event by clicking on it. A selected event is set to the editor as the current event.<br>\n      <h3>The event editor</h3><br>\n      The event editor have feelds for editing a comment of the event, the date of the event and the length of the event. To save after editing a event you can press the save button or select an other event in the event list or add a new event.<br>\n      <h3>The menu</h3><br>\n      In the file menu you can save current diary, create a new one or open a saved diary. You can also export the current diary to an html file witch is useful if you want to create a report of your work with a project.<br><br>\n      \n      In the properties menu you can change the orientation of the components and the look of them.\n      <br>\n  \n    \n  <h2>Bug reporting</h2><br>\n  If you have found a bug or other error in the program the creator of it is very thankful if you send a bug report to the following mail adress:<br>\n  kjellw@cs.umu.se<br>\n  Describe the error and what caused it.\n  </body>\n</html>");
            jEditorPane.setCaretPosition(0);
        }
        return jEditorPane;
    }

    /**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Close");
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane(getJEditorPane());
            jScrollPane1.setViewportView(getJEditorPane());
        }
        return jScrollPane1;
    }
}
