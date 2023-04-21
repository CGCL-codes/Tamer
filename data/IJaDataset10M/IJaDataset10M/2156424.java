package verinec.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import javax.swing.*;

/** 
 * Dialog to report warnings.
 * getResult tells which option has been choosen if proposeContinue was true.
 * Otherwise, a simple close will be available and no result value available.
 *
 * @author david.buchmann at unifr.ch 
 */
public class WarningDialog extends JDialog {

    /** The dialog was ended with cancel. */
    public static final int CANCEL = 1;

    /** The dialog was ended with continue. */
    public static final int CONTINUE = 2;

    private int result = 0;

    /** For the save button. */
    private String filecontent;

    /** Create a warning dialog with configuration and error messages.
	 * Provides the option to save the stuff into a text file.
	 * 
	 * @param parent The parent window for the modal dialog
	 * @param title Dialog title
	 * @param rawconfig The original configuration that has been imported
	 * @param messages The error messages.
	 * @param proposeContinue Whether the dialog should show the cancel / continue buttons
	 */
    public WarningDialog(Frame parent, String title, String rawconfig, String messages, boolean proposeContinue) {
        super(parent, title, true);
        filecontent = rawconfig + "\n\n*** Produced the warnings: ***\n\n" + messages;
        Container contentPane = getContentPane();
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(0.7);
        split.setTopComponent(createMessagePanel(rawconfig, null));
        split.setBottomComponent(createMessagePanel(messages, Color.ORANGE));
        contentPane.add(split, BorderLayout.CENTER);
        contentPane.add(createButtons(proposeContinue), BorderLayout.SOUTH);
        contentPane.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    /** Get the selection by the user. Only valid if proposeContinue has been true. 
	 * 
	 * @return One of the constants CANCEL or CONTINUE
	 */
    public int getResult() {
        return result;
    }

    private Component createMessagePanel(String txt, Color color) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JTextArea a = new JTextArea(txt);
        a.setEditable(false);
        if (color != null) a.setBackground(color);
        Dimension d = a.getPreferredSize();
        d.width -= 20;
        d.height += 16;
        if (d.height > screen.height / 2 - 60) d.height = screen.height / 2 - 60;
        JScrollPane p = new JScrollPane(a);
        p.setPreferredSize(d);
        return p;
    }

    private JPanel createButtons(boolean proposeContinue) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        JButton save = new JButton("Save output...");
        save.addActionListener(new ActionListener() {

            /** save dialog
			 * 
			 * @param event
			 */
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showDialog(WarningDialog.this, "Save configuration and messages") == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter fw = new FileWriter(chooser.getSelectedFile());
                        fw.write(filecontent);
                        fw.close();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
                WarningDialog.this.dispose();
            }
        });
        p.add(save);
        if (proposeContinue) {
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {

                /** cancel dialog
				 * 
				 * @param event
				 */
                public void actionPerformed(ActionEvent event) {
                    result = CANCEL;
                    WarningDialog.this.dispose();
                }
            });
            p.add(cancel);
            JButton cont = new JButton("Continue");
            cont.addActionListener(new ActionListener() {

                /** close dialog
				 * 
				 * @param event
				 */
                public void actionPerformed(ActionEvent event) {
                    result = CONTINUE;
                    WarningDialog.this.dispose();
                }
            });
            p.add(cont);
        } else {
            JButton close = new JButton("Close");
            close.addActionListener(new ActionListener() {

                /** close dialog
				 * 
				 * @param event
				 */
                public void actionPerformed(ActionEvent event) {
                    WarningDialog.this.dispose();
                }
            });
            p.add(close);
        }
        return p;
    }
}
