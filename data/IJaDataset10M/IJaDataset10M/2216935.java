package megamek.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import megamek.client.util.AdvancedLabel;

/**
 * Every about dialog in MegaMek should have an identical look-and-feel.
 */
public class CommonHelpDialog extends Dialog {

    /**
     * The help text that should be displayed to the user.
     */
    private String helpText = null;

    /**
     * Create a help dialog for the given parent <code>Frame</code> by
     * reading from the indicated <code>File</code>.
     *
     * @param   frame - the parent <code>Frame</code> for this dialog.
     *          This value should <b>not</b> be <code>null</code>.
     * @param   helpfile - the <code>File</code> containing the help text.
     *          This value should <b>not</b> be <code>null</code>.
     */
    public CommonHelpDialog(Frame frame, File helpfile) {
        super(frame);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        StringBuffer buff = new StringBuffer();
        if (helpfile == null) {
            this.setTitle("No Help Available");
            buff.append("Help is currently unavailable.");
        } else {
            this.setTitle("Help File: " + helpfile.getName());
            boolean firstLine = true;
            try {
                BufferedReader input = new BufferedReader(new FileReader(helpfile));
                String line = input.readLine();
                while (line != null) {
                    if (firstLine) {
                        firstLine = false;
                    } else {
                        buff.append("\n");
                    }
                    buff.append(line);
                    line = input.readLine();
                }
            } catch (IOException exp) {
                if (!firstLine) {
                    buff.append("\n \n");
                }
                buff.append("Error reading help file: ").append(exp.getMessage());
                exp.printStackTrace();
            }
        }
        this.setLayout(new BorderLayout());
        AdvancedLabel lblHelp = new AdvancedLabel(buff.toString());
        ScrollPane scroll = new ScrollPane();
        scroll.add(lblHelp);
        this.add(scroll, BorderLayout.CENTER);
        Button butClose = new Button("Close");
        butClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                quit();
            }
        });
        this.add(butClose, BorderLayout.SOUTH);
        Dimension screenSize = frame.getToolkit().getScreenSize();
        Dimension windowSize = new Dimension(screenSize.width / 2, screenSize.height / 2);
        this.pack();
        this.setSize(windowSize);
        this.setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
    }

    void quit() {
        this.hide();
    }
}
