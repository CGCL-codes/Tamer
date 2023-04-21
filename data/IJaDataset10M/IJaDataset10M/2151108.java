package hermes.browser;

import hermes.browser.dialog.AboutDialog;
import hermes.swing.SwingUtils;
import hermes.swing.actions.ActionRegistry;
import hermes.swing.actions.AddDurableTopicAction;
import hermes.swing.actions.AddQueueAction;
import hermes.swing.actions.AddTopicAction;
import hermes.swing.actions.CreateNewContextAction;
import hermes.swing.actions.CreateNewMessageStoreAction;
import hermes.swing.actions.CreateNewSessionAction;
import hermes.swing.actions.DeleteBrowserTreeNodeAction;
import hermes.swing.actions.DiscoverDestinationsAction;
import hermes.swing.actions.EditObjectAction;
import hermes.swing.actions.NewPreferencesAction;
import hermes.swing.actions.PreferencesAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.DockableBarContext;
import com.jidesoft.swing.JideButton;

public class ConfigurationToolBar extends CommandBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7106014299869433789L;

    public ConfigurationToolBar() {
        super("Configuration");
        setHidable(false);
        getContext().setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        getContext().setInitMode(DockableBarContext.STATE_HORI_DOCKED);
        final JideButton aboutButton = SwingUtils.createToolBarButton("toolbarButtonGraphics/general/About16.gif", "About");
        aboutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AboutDialog.showAboutDialog(HermesBrowser.getBrowser());
            }
        });
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(CreateNewSessionAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(CreateNewContextAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(CreateNewMessageStoreAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(EditObjectAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(DiscoverDestinationsAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(AddQueueAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(AddTopicAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(AddDurableTopicAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(DeleteBrowserTreeNodeAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(PreferencesAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(NewPreferencesAction.class)));
        add(aboutButton);
    }
}
