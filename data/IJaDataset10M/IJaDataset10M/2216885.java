package pearls;

import pearls.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;

/***************************************************************************

    <p><i>PassageBrowser:</i> The main application which coordinates the
    presentation of multiple scripture versions and translation 
    explanations</p>

    <p>The user interface is an application of a web browser's user 
    interface toward the needs of one studying the Bible. </p>


    <p><i>Copyright</i> (C) 2000 Noel Enete. All rights reserved.</p>
                
    <p>This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version. </p>
                                
    <p>This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details. </p>
                        
    <p>You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA. </p>


    <p><i>Development Environment</i></p>

      <li>Compiled under JDK 1.30</li>

    <p><i>History</i></p>

    * <pre>
    *    $Log: PassageBrowser.java,v $
    *    Revision 1.21  2001/09/22 00:51:48  noelenete
    *    Got tiling to work correctly.
    *
    *    Revision 1.20  2001/09/20 05:01:25  noelenete
    *    Started on the tile window service.
    *
    *    Revision 1.19  2001/09/19 00:38:53  noelenete
    *    Got launch to setBounds without shift and got a Mac Icon.
    *
    *    Revision 1.18  2001/09/15 00:57:41  noelenete
    *    Added the WEB version.
    *
    *    Revision 1.17  2001/09/02 04:01:34  noelenete
    *    Renamed GreekScriptureViewerView to OriginalScriptureViewerView.
    *
    *    Revision 1.16  2001/09/02 03:51:23  noelenete
    *    Renamed PLinkViewer PNoteViewer.
    *
    *    Revision 1.15  2001/08/25 00:53:35  noelenete
    *    Added the OS X .app directory tree.
    *
    *    Revision 1.14  2001/08/19 01:45:05  noelenete
    *    Link support #6: Hebrew input in PNoteViewer.
    *
    *    Revision 1.13  2001/08/12 23:53:03  noelenete
    *    Session 3 building link support.
    *
    *    Revision 1.12  2001/08/12 03:04:50  noelenete
    *    Continued building link system; used prop file for initial windows to open.
    *
    *    Revision 1.11  2001/08/05 20:26:33  noelenete
    *    Began to code an application defaults property file.
    *
    *    Revision 1.10  2001/08/01 08:14:07  noelenete
    *    Cosmetic fixes.
    *
    *    Revision 1.9  2001/07/30 17:44:20  noelenete
    *    Added 1st pass at Dynamically loaded Greek/Hebrew font.
    *
    *    Revision 1.8  2001/07/21 01:44:21  noelenete
    *    Finished integrating James's RSV and DBY texts and fixed a number of bugs in Pearls.
    *
    *    Revision 1.7  2001/07/19 17:56:59  noelenete
    *    Added code to integrate RSV & DBY
    *
    *    Revision 1.6  2001/07/16 01:02:45  noelenete
    *    James added keyboard accelerators.
    *
    *    Revision 1.5  2001/01/17 04:15:43  noelenete
    *    Changed abbr from UBS to GKHB.
    *
    *    Revision 1.4  2001/01/14 00:13:37  noelenete
    *    Created a new Revision class.
    *
    *    Revision 1.3  2000/12/30 01:36:56  noelenete
    *    First display of Greek characters.
    *
    *    Revision 1.2  2000/11/23 00:19:22  noelenete
    *    Source code format.
    *
    *    Revision 1.1.1.1  2000/11/22 23:22:46  noelenete
    *    Initial import of PEARLS code into SourceForge.
    *
    *    Revision 1.0  2000/09/10 21:07:06  ndenete
    *    Created the class.
    * </pre>

 ***************************************************************************/
public class PassageBrowser extends PFrame {

    public static final String msVer = "@(#) $Id: PassageBrowser.java,v 1.21 2001/09/22 00:51:48 noelenete Exp $";

    public static Properties mprDefaults;

    public static Font mfCustom;

    private Container mContentPane;

    private JDesktopPane mDesktopPane;

    private Vector mvScriptureViewers;

    private JMenu mVersionsMenu;

    protected JMenu mLinksMenu;

    protected JSplitPane mSplitPane;

    protected PNoteViewer mNoteViewer;

    protected int miVersionsLaunched = 2;

    /**   Constructs the class.
 *
 */
    public PassageBrowser() {
        super();
        int iCount;
        int iVersion;
        String s;
        String sWindow;
        StringTokenizer st;
        int x, y, width, height;
        init();
        PUtils.setLookAndFeel();
        createVisualComponents();
        s = (String) mprDefaults.get("versionsmenu_def");
        s = (String) mprDefaults.get(s);
        st = new StringTokenizer(s, ",");
        iCount = 0;
        while (st.hasMoreTokens()) {
            sWindow = st.nextToken();
            s = st.nextToken();
            x = PUtils.getIntFromString(s);
            s = st.nextToken();
            y = PUtils.getIntFromString(s);
            s = st.nextToken();
            width = PUtils.getIntFromString(s);
            s = st.nextToken();
            height = PUtils.getIntFromString(s);
            if ("frame".equals(sWindow)) {
                setVisible(true);
                setBounds(x, y, width, height);
                validate();
                continue;
            }
            iVersion = PLocation.getVersionNumber(sWindow);
            launchScriptureViewer(sWindow.toUpperCase() + ++iCount, iVersion, x, y, width, height);
        }
        miVersionsLaunched = iCount;
    }

    /**   Initializes the class.
 *
 */
    private void init() {
        Font font;
        FileInputStream fis;
        mvScriptureViewers = new Vector();
        mprDefaults = PUtils.getPropertiesFromString(PUtils.getPropertyStringFromFile("pearls.prop"));
        try {
            fis = new FileInputStream("pearls.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fis);
            mfCustom = font.deriveFont((float) 12.0);
        } catch (Exception e) {
            System.out.println("PassageBrowser.init(): unable to create the " + "font: " + e);
            System.exit(1);
        }
    }

    /**   Test harness for the class.
 *
 */
    public static void main(String[] asArgsIn) {
        PassageBrowser passageBrowser = new PassageBrowser();
    }

    /**   Invoked when the user selects "close link" from the plink menu and
 *    it collapses the plink viewer and removes the hilights in each of 
 *    the open scripture views.
 *
 */
    protected void closePLink() {
        int i;
        int iSize;
        ScriptureViewerView view;
        iSize = mvScriptureViewers.size();
        for (i = 0; i < iSize; i++) {
            view = (ScriptureViewerView) mvScriptureViewers.elementAt(i);
            view.cancelPLink();
        }
        mNoteViewer.cancelPLink();
        hideLinkViewer();
    }

    /**
 */
    protected void createVisualComponents() {
        int i;
        String s;
        Action action;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu passagesMenu;
        JMenuBar menuBar;
        PassageChooser mPassageChooser;
        mContentPane = getContentPane();
        mContentPane.setLayout(new BorderLayout());
        mDesktopPane = new JDesktopPane();
        mDesktopPane.setOpaque(true);
        mNoteViewer = new PNoteViewer();
        mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mDesktopPane, mNoteViewer);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setResizeWeight(1.0);
        hideLinkViewer();
        mContentPane.add(BorderLayout.CENTER, mSplitPane);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        action = new LaunchPBrowserAction("New Browser");
        fileMenu.add(action);
        fileMenu.getItem(fileMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.ALT_MASK));
        fileMenu.addSeparator();
        action = new AbstractAction() {

            public void actionPerformed(ActionEvent evtIn) {
                System.exit(0);
            }
        };
        action.putValue(Action.NAME, "Exit");
        fileMenu.add(action);
        fileMenu.getItem(fileMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.ALT_MASK));
        menuBar.add(fileMenu);
        editMenu = new JMenu("Edit");
        action = new TestAction("Cut ...");
        editMenu.add(action);
        editMenu.getItem(editMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK));
        action = new TestAction("Copy ...");
        editMenu.add(action);
        editMenu.getItem(editMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.ALT_MASK));
        action = new TestAction("Paste ...");
        editMenu.add(action);
        editMenu.getItem(editMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.ALT_MASK));
        menuBar.add(editMenu);
        passagesMenu = new JMenu("Passages");
        action = new TestAction("Add Passage");
        passagesMenu.add(action);
        action = new TestAction("Add Passage to...");
        passagesMenu.add(action);
        action = new TestAction("Edit Passages");
        passagesMenu.add(action);
        passagesMenu.addSeparator();
        action = new TestAction("God so loved the world (Jn 3:16)");
        passagesMenu.add(action);
        action = new TestAction("No condemnation (Rom 8:1)");
        passagesMenu.add(action);
        action = new TestAction("Wages of sin (Rom 6:23)");
        passagesMenu.add(action);
        action = new TestAction("God demonstrated His love (Rom 5:8)");
        passagesMenu.add(action);
        menuBar.add(passagesMenu);
        mVersionsMenu = new JMenu("Versions");
        action = new LaunchVersionAction("KJV - King James", PLocation.VERSION_KJV);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, Event.CTRL_MASK + Event.SHIFT_MASK));
        action = new LaunchVersionAction("WEB - World English Bible", PLocation.VERSION_WEB);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK + Event.SHIFT_MASK));
        action = new LaunchVersionAction("DBY - Darby Translation", PLocation.VERSION_DBY);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK + Event.SHIFT_MASK));
        action = new LaunchVersionAction("RSV - Revised Standard Version", PLocation.VERSION_RSV);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK + Event.SHIFT_MASK));
        action = new LaunchVersionAction("ORIG - Greek and Hebrew Original", PLocation.VERSION_ORIG);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK + Event.SHIFT_MASK));
        mVersionsMenu.addSeparator();
        action = new LaunchVersionAction("Tile Windows", -1);
        mVersionsMenu.add(action);
        mVersionsMenu.getItem(mVersionsMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK + Event.SHIFT_MASK));
        action = new TestAction("Save window positions...");
        mVersionsMenu.add(action);
        mVersionsMenu.addSeparator();
        for (i = 1; true; i++) {
            s = (String) mprDefaults.get("versionsmenu_" + i);
            if (s == null) break;
            s = (String) mprDefaults.get("versionsmenu_" + i + "_desc");
            if (s == null) s = "<no desc>";
            action = new TestAction(s);
            mVersionsMenu.add(action);
        }
        menuBar.add(mVersionsMenu);
        mLinksMenu = new JMenu("PLinks");
        action = new PLinkAction("New PLink", PLinkAction.NEW);
        mLinksMenu.add(action);
        mLinksMenu.getItem(mLinksMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.ALT_MASK + Event.SHIFT_MASK));
        action = new PLinkAction("Edit PLink", PLinkAction.EDIT);
        mLinksMenu.add(action);
        mLinksMenu.getItem(mLinksMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.ALT_MASK + Event.SHIFT_MASK));
        mLinksMenu.addSeparator();
        action = new PLinkAction("Save PLink", PLinkAction.SAVE);
        mLinksMenu.add(action);
        mLinksMenu.getItem(mLinksMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.ALT_MASK));
        action = new PLinkAction("Close PLink", PLinkAction.CLOSE);
        mLinksMenu.add(action);
        mLinksMenu.getItem(mLinksMenu.getItemCount() - 1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.ALT_MASK + Event.SHIFT_MASK));
        menuBar.add(mLinksMenu);
        mLinksMenu.addSeparator();
        action = new TestAction("Show All PLinks");
        mLinksMenu.add(action);
        menuBar.add(mLinksMenu);
        setJMenuBar(menuBar);
    }

    /**
 */
    public void displayLinkViewer() {
        mSplitPane.setDividerLocation(.7);
    }

    /**   Cycles through the open scripture version views and returns the handle
 *    to the ORIG (Greek and Hebrew) version.  If more than one ORIG version
 *    is encountered or if no ORIG version is encountered an error is issued
 *    to the GUI and to the stdout and null is returned.
 *
 */
    protected ScriptureViewerView getOrigVersion() {
        int i;
        int iSize;
        String s;
        ScriptureViewerView view;
        ScriptureViewerView origView = null;
        iSize = mvScriptureViewers.size();
        for (i = 0; i < iSize; i++) {
            view = (ScriptureViewerView) mvScriptureViewers.elementAt(i);
            if (view.isOrigVersion()) {
                if (origView != null) {
                    s = "Links can not be entered when more than one ORIG\n" + "version is open in this browser.  Close the other\n" + "ORIG versions and try again.";
                    JOptionPane.showMessageDialog(mContentPane, s, "New Link Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("PassageBrowser.getOrigVersion():\n" + s);
                    return (null);
                } else {
                    origView = view;
                }
            }
        }
        if (origView == null) {
            s = "Links can not be entered unless one ORIG version\n" + "is open in this browser.  Open one ORIG version\n" + "then try again.";
            JOptionPane.showMessageDialog(mContentPane, s, "New Link Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("PassageBrowser.getOrigVersion():\n" + s);
            return (null);
        }
        return (origView);
    }

    /**
 */
    public Vector getScriptureViewers() {
        return (mvScriptureViewers);
    }

    /**
 */
    public void hideLinkViewer() {
        mSplitPane.setDividerLocation(900);
    }

    /**   Launches a ScriptureViewerView in an InternalFrame and addes it to
 *    the browser's collection of scripture viewers.
 *
 */
    public void launchScriptureViewer(String sTitleIn, int iVersionIn, int xIn, int yIn, int iWidthIn, int iHeightIn) {
        PInternalFrame internalFrame;
        ScriptureViewerView scriptureViewerView;
        if (iVersionIn < PLocation.NUM_VERSIONS) {
            switch(iVersionIn) {
                case PLocation.VERSION_ORIG:
                    scriptureViewerView = new OriginalScriptureViewerView(iVersionIn, this);
                    break;
                default:
                    scriptureViewerView = new ScriptureViewerView(iVersionIn, this);
                    break;
            }
        } else {
            System.err.println("Unknown version to launch, version=" + iVersionIn);
            return;
        }
        mvScriptureViewers.add(scriptureViewerView);
        internalFrame = new PInternalFrame(sTitleIn, true, true, true, true);
        internalFrame.addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameClosing(InternalFrameEvent evtIn) {
                Component[] aComponents;
                PInternalFrame internalFrame2;
                ScriptureViewerView scriptureViewerView2;
                internalFrame2 = (PInternalFrame) evtIn.getSource();
                aComponents = internalFrame2.getRootPane().getContentPane().getComponents();
                if (aComponents.length != 1) {
                    System.out.println("Should be just one component in this " + "internal frame, instead it has " + aComponents.length + " component[s]");
                    return;
                }
                scriptureViewerView2 = (ScriptureViewerView) aComponents[0];
                mvScriptureViewers.remove(scriptureViewerView2);
            }
        });
        internalFrame.getContentPane().add(scriptureViewerView);
        internalFrame.setBounds(xIn, yIn, iWidthIn, iHeightIn);
        internalFrame.setVisible(true);
        mDesktopPane.add(internalFrame, JLayeredPane.DEFAULT_LAYER, 0);
        try {
            internalFrame.setSelected(true);
        } catch (Exception e) {
            System.out.println("Error selecting frame: " + e);
        }
    }

    /**   The "add link" action invokes then when the user is about to add a new
 *    scripture link to the system.  This method enumerates the collection of 
 *    scripture viewers and sends newPLink() to each of them.
 *
 */
    protected void newPLink() {
        int i;
        int iSize;
        ScriptureViewerView view;
        displayLinkViewer();
        getOrigVersion();
        iSize = mvScriptureViewers.size();
        for (i = 0; i < iSize; i++) {
            view = (ScriptureViewerView) mvScriptureViewers.elementAt(i);
            view.newPLink();
        }
        mNoteViewer.newPLink();
    }

    /**   The "save link" action invokes then when the user is about to add a new
 *    scripture link to the system.  This method enumerates the collection of 
 *    scripture viewers and sends savePLink() to each of them.
 *
 */
    protected void savePLink() {
        int i;
        int iSize;
        String sLinkNote;
        String[] asLocations;
        String[] asOrigLocations;
        ScriptureViewerView view;
        ScriptureViewerView origView;
        PLinkCollection linkCollection;
        origView = getOrigVersion();
        if (origView == null) return;
        asOrigLocations = origView.savePLink();
        linkCollection = new PLinkCollection();
        iSize = mvScriptureViewers.size();
        for (i = 0; i < iSize; i++) {
            view = (ScriptureViewerView) mvScriptureViewers.elementAt(i);
            if (!view.isOrigVersion()) {
                asLocations = view.savePLink();
                linkCollection.addPLink(asOrigLocations, asLocations);
            }
        }
        sLinkNote = mNoteViewer.savePLink();
        System.out.println("Links: ");
        int j;
        String[] as;
        as = linkCollection.getPLinks();
        for (j = 0; j < as.length; j++) {
            System.out.println("  " + as[j]);
        }
        System.out.println("Note:\n  " + sLinkNote);
    }

    /**   Iterates throught the PInternalFrames and resizes them so they fit
 *    side by side in one row within the parent frame size.  This method
 *    was done in haste by pasting code from a couple of different 
 *    examples.  It could stand to be tightened up, especially the code
 *    that lays out the frames is written for multiple row layout.  It
 *    should be reduced to simple one row logic.
 *
 */
    protected void tileViewers() {
        int i;
        int j;
        int w;
        int h;
        int x;
        int y;
        int iSize;
        int rows;
        int cols;
        int count;
        LinkedList llFrames;
        Dimension size;
        Container container;
        JInternalFrame frame;
        JInternalFrame[] aFrames;
        ScriptureViewerView view;
        llFrames = new LinkedList();
        iSize = mvScriptureViewers.size();
        for (i = 0; i < iSize; i++) {
            view = (ScriptureViewerView) mvScriptureViewers.elementAt(i);
            container = view;
            while (!((container = container.getParent()) instanceof PInternalFrame)) {
                container = container.getParent();
                if (container instanceof PInternalFrame) {
                    frame = (PInternalFrame) container;
                    if ((frame.isClosed() == false) && (frame.isIcon() == false)) {
                        llFrames.add(frame);
                    }
                    break;
                }
            }
        }
        Collections.sort(llFrames, new Comparator() {

            public int compare(Object o1, Object o2) {
                int x1, x2;
                PInternalFrame frame1, frame2;
                frame1 = (PInternalFrame) o1;
                frame2 = (PInternalFrame) o2;
                x1 = frame1.getX();
                x2 = frame2.getX();
                if (x1 < x2) return (-1); else if (x1 == x2) return (0);
                return (1);
            }

            public boolean equals(Object obj) {
                return (super.equals(obj));
            }
        });
        count = llFrames.size();
        if (count <= 0) {
            return;
        }
        aFrames = new JInternalFrame[count];
        llFrames.toArray(aFrames);
        rows = 1;
        cols = count;
        if (rows * cols < count) {
            cols++;
            if (rows * cols < count) {
                rows++;
            }
        }
        size = mDesktopPane.getSize();
        w = size.width / cols;
        h = size.height / rows;
        x = 0;
        y = 0;
        for (i = 0; i < rows; i++) {
            for (j = 0; (j < cols) && (((i * cols) + j) < count); j++) {
                frame = aFrames[(i * cols) + j];
                mDesktopPane.getDesktopManager().resizeFrame(frame, x, y, w, h);
                x += w;
            }
            y += h;
            x = 0;
        }
    }

    /***************************************************************************

    <p><i>LaunchPBrowserAction:</i> Launches a new PassageBrowser in a new
    frame. </p>

 ***************************************************************************/
    class LaunchPBrowserAction extends AbstractAction {

        /**
 */
        public LaunchPBrowserAction(String sIn) {
            super(sIn);
        }

        /**
 */
        public void actionPerformed(ActionEvent evtIn) {
            PassageBrowser passageBrowser;
            passageBrowser = new PassageBrowser();
            System.out.println("LaunchPBrowserAction [" + evtIn.getActionCommand() + "] performed!");
        }
    }

    /***************************************************************************

    <p><i>LaunchVersionAction:</i> Launches a new ScriptureViewerView in
    an internal frame. </p>

 ***************************************************************************/
    class LaunchVersionAction extends AbstractAction {

        int miVersion;

        /**
 */
        public LaunchVersionAction(String sIn, int iVersionIn) {
            super(sIn);
            miVersion = iVersionIn;
        }

        /**
 */
        public void actionPerformed(ActionEvent evtIn) {
            int x;
            int y;
            String s;
            if (miVersion < 0) {
                tileViewers();
                return;
            }
            miVersionsLaunched++;
            x = (miVersionsLaunched * 15) + 30;
            y = (miVersionsLaunched * 10) + 10;
            if (miVersion < PLocation.NUM_VERSIONS) {
                s = PLocation.asVersions[miVersion].toUpperCase();
            } else {
                s = "UNKNOWN";
            }
            launchScriptureViewer(s + miVersionsLaunched, miVersion, x, y, 310, 300);
            System.out.println("LaunchVersionAction [" + evtIn.getActionCommand() + "] performed!");
        }
    }

    /***************************************************************************

    <p><i>PLinkAction:</i> Processes PLink Menu Selections. </p>

 ***************************************************************************/
    class PLinkAction extends AbstractAction {

        static final int NEW = 1;

        static final int EDIT = 2;

        static final int SAVE = 3;

        static final int CLOSE = 4;

        int miType;

        /**
 */
        public PLinkAction(String sIn, int iTypeIn) {
            super(sIn);
            miType = iTypeIn;
        }

        /**
 */
        public void actionPerformed(ActionEvent evtIn) {
            switch(miType) {
                case NEW:
                    newPLink();
                    break;
                case EDIT:
                    break;
                case SAVE:
                    savePLink();
                    break;
                case CLOSE:
                    closePLink();
                    break;
                default:
                    break;
            }
            System.out.println("PLinkAction [" + evtIn.getActionCommand() + "] performed!");
        }
    }

    /***************************************************************************

    <p><i>TestAction:</i> Used in debugging. </p>

 ***************************************************************************/
    class TestAction extends AbstractAction {

        /**
 */
        public TestAction(String sIn) {
            super(sIn);
        }

        /**
 */
        public void actionPerformed(ActionEvent evtIn) {
            JOptionPane.showMessageDialog(mContentPane, "Not implemented yet.  Keep trying, some of them are.", "Ooops!", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Action [" + evtIn.getActionCommand() + "] performed!");
        }
    }
}
