package com.interworldtransport.cladosviewer;

import com.interworldtransport.clados.*;
import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.COpsDotEvents
 *  This class manages events relating to a complex operation.
 *  Dot this Monad with another Monad.
 *
 * @version 0.80, $Date: 2005/07/25 01:44:25 $
 * @author Dr Alfred W Differ
 */
public class COpsDotEvents implements ActionListener {

    protected ViewerMenu ParentGUIMenu;

    protected JMenuItem ControlIt;

    protected COpsEvents Parent;

    /** This is the default constructor.
 */
    public COpsDotEvents(ViewerMenu pGUIMenu, JMenuItem pmniControlled, COpsEvents pParent) {
        this.ParentGUIMenu = pGUIMenu;
        this.ControlIt = pmniControlled;
        this.ControlIt.addActionListener(this);
        this.Parent = pParent;
    }

    /** This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt) {
        MonadPanel temp0 = ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(0).getMonadPanel(0);
        MonadPanel temp1 = ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(1).getMonadPanel(0);
        Monad Monad0 = null;
        Monad Monad1 = null;
        if (temp0 != null) {
            Monad0 = temp0.getMonad();
        }
        if (temp1 != null) {
            Monad1 = temp1.getMonad();
        }
        if (Monad0 != null || Monad1 != null) {
            try {
                Monad0.Dot(Monad1);
                temp0.setBottomFields();
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Second Monad dotted against the first.\n");
            } catch (NoReferenceMatchException e) {
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Reference Match error between second and first monads.\n");
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Second Monad not dotted against the first.\n");
            } catch (DotDefinitionException d) {
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Dot Definition error between second and first monads.\n");
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Second Monad not dotted against the first.\n");
            } catch (CladosException ec) {
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("General Clados error between second and first monads.\n");
                ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Second Monad not dotted against the first.\n");
            }
        } else {
            ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("Second Monad not dotted against the first.\n");
        }
    }
}
