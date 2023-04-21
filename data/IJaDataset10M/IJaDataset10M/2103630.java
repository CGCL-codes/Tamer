package gruntspud.ui.commandoptions;

import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.ui.AbstractTab;
import gruntspud.ui.GruntspudCheckBox;
import gruntspud.ui.Tabber;
import gruntspud.ui.UIUtil;
import gruntspud.ui.WatchList;
import gruntspud.ui.preferences.GlobalOptionsTab;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.Watch;
import org.netbeans.lib.cvsclient.command.unedit.UneditCommand;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public class UneditOptionsPane extends Tabber {

    private UneditGeneralTab generalTab;

    private GlobalOptionsTab globalOptionsTab;

    private WatchList watch;

    private GruntspudContext context;

    private GruntspudCheckBox doNotRecurse;

    /**
     *  Constructor for the EditOptionsPane object
     *
     *@param  host  Description of the Parameter
     */
    public UneditOptionsPane(GruntspudContext context) {
        super();
        this.context = context;
        generalTab = new UneditGeneralTab();
        globalOptionsTab = new GlobalOptionsTab();
        globalOptionsTab.init(context);
        addTab(generalTab);
        addTab(globalOptionsTab);
    }

    /**
     *  Gets the commandsForSettings attribute of the CommitInfoPane object
     *
     *@return    The commandsForSettings value
     */
    public Command[] getCommandsForSettings() {
        UneditCommand cmd = new UneditCommand();
        if (watch.getSelectedWatch() != null) {
            cmd.setTemporaryWatch(watch.getSelectedWatch());
        }
        cmd.setRecursive(!doNotRecurse.isSelected());
        return new Command[] { cmd };
    }

    class UneditGeneralTab extends AbstractTab {

        /**
         *  Constructor for the CommitGeneralTab object
         */
        UneditGeneralTab() {
            super("General", UIUtil.getCachedIcon(Constants.ICON_TOOL_UNEDIT));
            setTabToolTipText("General unedit options");
            setLayout(new GridBagLayout());
            setTabMnemonic('g');
            setTabLargeIcon(UIUtil.getCachedIcon(Constants.ICON_TOOL_LARGE_UNEDIT));
            GridBagConstraints gbc = new GridBagConstraints();
            Insets i1 = new Insets(3, 3, 3, 3);
            gbc.insets = i1;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 2.0;
            doNotRecurse = new GruntspudCheckBox("Do not recurse");
            doNotRecurse.setSelected(context.getHost().getBooleanProperty(Constants.UNEDIT_GENERAL_DO_NOT_RECURSE, false));
            UIUtil.jGridBagAdd(this, doNotRecurse, gbc, GridBagConstraints.REMAINDER);
            gbc.weightx = 2.0;
            gbc.weighty = 1.0;
            watch = new WatchList();
            watch.setSelectedWatchName(context.getHost().getProperty(Constants.UNEDIT_GENERAL_TEMP_WATCH, Watch.NONE.toString()));
            JScrollPane scroller = new JScrollPane(watch);
            JPanel p = new JPanel(new GridLayout(1, 1));
            p.setBorder(BorderFactory.createTitledBorder("Temporary watch"));
            p.add(scroller);
            p.setOpaque(false);
            UIUtil.jGridBagAdd(this, p, gbc, GridBagConstraints.REMAINDER);
        }

        public boolean validateTab() {
            return true;
        }

        public void applyTab() {
            context.getHost().setBooleanProperty(Constants.UNEDIT_GENERAL_DO_NOT_RECURSE, doNotRecurse.isSelected());
            String n = watch.getSelectedWatchName();
            context.getHost().setProperty(Constants.UNEDIT_GENERAL_TEMP_WATCH, (n == null) ? "" : n);
        }

        public void tabSelected() {
        }
    }
}
