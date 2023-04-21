package jade.gui;

import javax.swing.*;
import java.util.Properties;

/**
 * This class encapsulates some informations used by the program

 
   @author Francisco Regi, Andrea Soracchi - Universita' di Parma
   @version $Date: 2004-07-01 15:15:48 +0200 (gio, 01 lug 2004) $ $Revision: 5176 $

 */
public class GuiProperties {

    protected static UIDefaults MyDefaults;

    protected static GuiProperties foo = new GuiProperties();

    public static final String ImagePath = "";

    static {
        Object[] icons = { "RMAAction.AddAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/cervello.gif"), "RMAAction.CustomActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/custom.gif"), "RMAAction.KillActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/kill.gif"), "RMAAction.RemoveActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/ex.gif"), "RMAAction.ResumeActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/sveglia.gif"), "RMAAction.SnifferActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/sniffer.gif"), "RMAAction.StartActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/start.gif"), "RMAAction.StartNewAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/baby.gif"), "RMAAction.SuspendActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/suspend.gif"), "RMAAction.ChangeAgentOwnershipActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/owner.gif"), "RMAAction.MoveAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/move.gif"), "RMAAction.CloneAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/clone.gif"), "RMAAction.SaveAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/saveagent.gif"), "RMAAction.LoadAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/loadagent.gif"), "RMAAction.FreezeAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/freezeagent.gif"), "RMAAction.ThawAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/thawagent.gif"), "TreeData.SuspendedIcon", LookAndFeel.makeIcon(foo.getClass(), "images/stopTree.gif"), "TreeData.RunningIcon", LookAndFeel.makeIcon(foo.getClass(), "images/runtree.gif"), "TreeData.FolderIcon", LookAndFeel.makeIcon(foo.getClass(), "images/foldergreen.gif"), "TreeData.FolderIcon1", LookAndFeel.makeIcon(foo.getClass(), "images/folderyellow.gif"), "RMAAction.DummyAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/dummyagent.gif"), "RMAAction.LoggerAgentActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/logger.gif"), "RMAAction.DGGUIActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/dfsmall.gif"), "RMAAction.AddRemotePlatformActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/connect.gif"), "RMAAction.IntrospectorActionIcon", LookAndFeel.makeIcon(foo.getClass(), "images/bug.gif") };
        MyDefaults = new UIDefaults(icons);
    }

    public static final Icon getIcon(String key) {
        Icon i = MyDefaults.getIcon(key);
        if (i == null) {
            return null;
        } else return MyDefaults.getIcon(key);
    }
}
