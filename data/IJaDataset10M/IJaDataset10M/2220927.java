package pearls;

import pearls.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/***************************************************************************

    <p><i>PFrame:</i> Provides the base class behavior for stand alone
    windows in the PEARLS system.</p>


    <p><i>Development Environment</i></p>

      <li>Compiled under JDK 1.30</li>

    <p><i>History</i></p>

    * <pre>
    *    $Log: PFrame.java,v $
    *    Revision 1.5  2001/09/02 03:51:23  noelenete
    *    Renamed PLinkViewer PNoteViewer.
    *
    *    Revision 1.4  2001/08/19 01:45:05  noelenete
    *    Link support #6: Hebrew input in PNoteViewer.
    *
    *    Revision 1.3  2001/01/14 00:13:37  noelenete
    *    Created a new Revision class.
    *
    *    Revision 1.2  2000/11/23 00:19:21  noelenete
    *    Source code format.
    *
    *    Revision 1.1.1.1  2000/11/22 23:22:47  noelenete
    *    Initial import of PEARLS code into SourceForge.
    *
    *    Revision 1.0  2000/11/21 09:21:00  NoelEnete
    *    Created the class.
    * </pre>


 ***************************************************************************/
public class PFrame extends JFrame implements WindowListener, ActionListener {

    public static final String msVer = "@(#) $Id: PFrame.java,v 1.5 2001/09/02 03:51:23 noelenete Exp $";

    Container contentPane;

    private static int miNumInstances = 0;

    /**
 */
    public PFrame() {
        init();
    }

    /**   Instantiate this frame with any of Pearls's panel subclasses.  
 *    Each of these classes register ActionListeners and send the
 *    ActionEvent with cmd="Done" when the panel can be removed.
 *
 */
    public PFrame(PPanel ppIn) {
        init();
        ppIn.addActionListener(this);
        contentPane.add(ppIn, BorderLayout.CENTER);
    }

    /**   Add the ability to specify the title on the frame.
 *
 */
    public PFrame(String sTitleIn, PPanel ppIn) {
        super(sTitleIn);
        init();
        ppIn.addActionListener(this);
        contentPane.add(ppIn, BorderLayout.CENTER);
    }

    /**
 */
    private void init() {
        miNumInstances++;
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        addWindowListener(this);
    }

    /**
 */
    public static void main(String[] args) {
        PFrame pf;
        pf = new PFrame();
        pf.setBounds(350, 10, 300, 200);
        pf.getContentPane().add("Center", new Button("Hi"));
        pf.show();
    }

    /**   This frame listens to the PPanel that provides the user interface.
 *    When that panel issues an ActionEvent that uses the "Done" command,
 *    this panel should dispose of itself.
 *
 */
    public void actionPerformed(ActionEvent evtIn) {
        String sCommand;
        sCommand = evtIn.getActionCommand();
        if (sCommand.equals(PPanel.DONE)) {
            dispose();
        }
    }

    /**
 */
    public void windowActivated(WindowEvent e) {
    }

    /**
 */
    public void windowClosed(WindowEvent e) {
    }

    /**
 */
    public void windowClosing(WindowEvent e) {
        miNumInstances--;
        if (miNumInstances == 0) {
            System.exit(0);
        } else {
            dispose();
        }
    }

    /**
 */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
 */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
 */
    public void windowIconified(WindowEvent e) {
    }

    /**
 */
    public void windowOpened(WindowEvent e) {
    }
}
