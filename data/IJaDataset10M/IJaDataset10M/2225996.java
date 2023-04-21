package com.interworldtransport.cladosviewer;

import com.interworldtransport.clados.*;
import java.awt.event.*;
import javax.swing.*;

/** com.interworldtransport.cladosviewer.BOpsGradeEvents
 *  This class manages events relating to the answering of a boolean question.
 *  Is the Monad a particular grade?
 *
 * @version 0.80, $Date: 2010/09/07 04:22:49 $
 * @author Dr Alfred W Differ
 */
public class BOpsGradeEvents implements ActionListener {

    protected ViewerMenu ParentGUIMenu;

    protected JMenuItem ControlIt;

    protected BOpsEvents Parent;

    /** This is the default constructor.
 */
    public BOpsGradeEvents(ViewerMenu pGUIMenu, JMenuItem pmniControlled, BOpsEvents pParent) {
        this.ParentGUIMenu = pGUIMenu;
        this.ControlIt = pmniControlled;
        this.ControlIt.addActionListener(this);
        this.Parent = pParent;
    }

    /** This is the actual action to be performed by this member of the menu.
 */
    public void actionPerformed(ActionEvent evt) {
        Monad Monad0 = ParentGUIMenu.ParentGUI.CenterAll.getNyadPanel(0).getMonadPanel(0).getMonad();
        boolean test = false;
        try {
            test = Monad0.isGrade(new Integer(ParentGUIMenu.ParentGUI.StatusLine.stview.getText()).intValue());
        } catch (NoDefinedGradeException e) {
            test = false;
        } catch (BladeOutOfRangeException er) {
            test = false;
        } catch (NumberFormatException ef) {
            test = false;
        }
        if (test) {
            ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad is judged as pure grade match for grade: {" + ParentGUIMenu.ParentGUI.StatusLine.stview.getText() + "}\n");
        } else {
            ParentGUIMenu.ParentGUI.StatusLine.setStatusMsg("First Monad is judged not a pure grade match for grade :{ " + ParentGUIMenu.ParentGUI.StatusLine.stview.getText() + "}\n");
        }
    }
}
