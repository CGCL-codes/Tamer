package com.interworldtransport.cladosviewer;

import com.interworldtransport.clados.*;
import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.SOpsMagnitudeEvents
 *  This class manages events relating to the answering of a simple question.
 *  What is the magnitude of this Monad?
 *
 * @version 0.80, $Date: 2005/07/25 01:44:25 $
 * @author Dr Alfred W Differ
 */
public class SOpsMagnitudeEvents implements ActionListener {

    protected ViewerMenu ParentGUIMenu;

    protected JMenuItem ControlIt;

    protected SOpsEvents Parent;

    /** This is the default constructor.
 */
    public SOpsMagnitudeEvents(ViewerMenu pGUIMenu, JMenuItem pmniControlled, SOpsEvents pParent) {
        this.ParentGUIMenu = pGUIMenu;
        this.ControlIt = pmniControlled;
        this.ControlIt.addActionListener(this);
        this.Parent = pParent;
    }

    /** This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt) {
        MonadPanel MP0 = ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(0).getMonadPanel(0);
        Monad Monad0 = MP0.getMonad();
        double scale = Monad0.MagnitudeOf();
        MP0.setBottomFields();
        ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad magnitude is: \n");
        ParentGUIMenu.ParentGUI.StatusLine.setWhere(scale);
    }
}
