package com.interworldtransport.cladosviewer;

import java.awt.event.*;

/** com.interworldtransport.cladosviewer.HelpEvents
 * This class groups the event listeners associated with the Help menu.
 * It may be used in the future to act on events associated with the entire Help menu
 * by having it register as a Listeners with all of its controlled listeners.  The controlled
 * listeners will create an event or call their parent.  It could also register all the
 * components to which its listeners register....maybe....
 *
 * @version 0.80, $Date: 2005/07/25 01:44:25 $
 * @author Dr Alfred W Differ
 */
public class HelpEvents implements ActionListener {

    protected HelpSupportEvents sp;

    protected HelpAboutEvents ab;

    protected ViewerMenu ParentGUIMenu;

    /** This is the default constructor.  The event structure of the Help
 *  menu starts here and finishes with the child menu items.
 */
    public HelpEvents(ViewerMenu pTheGUIMenu) {
        this.ParentGUIMenu = pTheGUIMenu;
        this.sp = new HelpSupportEvents(ParentGUIMenu, ParentGUIMenu.mniSupport, this);
        this.ab = new HelpAboutEvents(ParentGUIMenu, ParentGUIMenu.mniAbout, this);
    }

    /** This is the default action to be performed by all members of the Help menu.
 *  It will be overridden by specific members of the menu.
 */
    public void actionPerformed(ActionEvent evt) {
        ;
    }
}
