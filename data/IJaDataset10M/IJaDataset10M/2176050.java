package org.jlense.uiworks.internal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.jlense.uiworks.internal.registry.ICategory;
import org.jlense.uiworks.internal.registry.IViewDescriptor;
import org.jlense.uiworks.internal.registry.OutlookShortcutExtensionReader;
import org.jlense.uiworks.internal.registry.ViewRegistry;
import org.jlense.uiworks.internal.registry.ViewRegistryReader;
import org.jlense.uiworks.part.ViewPart;
import org.jlense.uiworks.widget.PtRolloverButton;
import org.jlense.uiworks.widget.WidgetUtils;
import org.jlense.uiworks.widget.outlook.JLOutlookBar;
import org.jlense.uiworks.widget.outlook.JLOutlookList;
import org.jlense.uiworks.workbench.IWorkbenchPage;
import org.jlense.uiworks.workbench.PartInitException;

/**
 * A view containing Outlook Workbench shortcuts.
 * Similar to the shortcuts bar in MS Outlook
 * 
 * @author ted stockwell [emorning@sourceforge.net]
 */
public class OutlookShortcutsView extends ViewPart {

    private static WorkbenchPlugin PLUGIN = WorkbenchPlugin.getDefault();

    JLOutlookBar m_bar = new JLOutlookBar();

    Map m_models = new HashMap();

    /**
     * Creates a new ShortcutsView .
     */
    public OutlookShortcutsView() {
    }

    public JComponent createPartControl() {
        final JPanel control = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.fill = gbc.BOTH;
        gbc.weightx = gbc.weighty = 1.0;
        control.add(m_bar, gbc);
        try {
            IPluginRegistry registry = Platform.getPluginRegistry();
            OutlookShortcutExtensionReader shortcutReader = new OutlookShortcutExtensionReader();
            List viewIds = shortcutReader.getAllShortcutViews("org.jlense.uiworks.OutlookPerspective");
            ViewRegistry viewRegistry = new ViewRegistry();
            (new ViewRegistryReader()).readViews(registry, viewRegistry);
            HashMap tabLabels = new HashMap();
            for (Iterator iview = viewIds.iterator(); iview.hasNext(); ) {
                String viewId = (String) iview.next();
                PLUGIN.logDebug("Attempting to add shortcut to org.jlense.uiworks.OutlookPerspective for view:" + viewId);
                IViewDescriptor viewDescriptor = viewRegistry.find(viewId);
                if (viewDescriptor == null) {
                    PLUGIN.logError("Unrecognised view id:" + viewId);
                    continue;
                }
                IConfigurationElement viewElement = viewDescriptor.getConfigurationElement();
                if (viewElement == null) continue;
                String tabId = viewElement.getAttribute("category");
                if (tabId == null || tabId.length() <= 0) tabId = "org.jlense.uiworks";
                ICategory category = viewRegistry.findCategory(tabId);
                if (category == null) {
                    PLUGIN.logError("Unrecognised category id:" + tabId);
                    continue;
                }
                IConfigurationElement categoryElement = viewRegistry.findCategory(tabId).getConfigurationElement();
                if (categoryElement == null) continue;
                String categoryLabel = categoryElement.getAttribute("name");
                for (; ; ) {
                    boolean found = false;
                    int iamp = categoryLabel.indexOf('&');
                    if (0 <= iamp) {
                        categoryLabel = categoryLabel.substring(0, iamp) + categoryLabel.substring(iamp + 1);
                        found = true;
                    }
                    iamp = categoryLabel.indexOf("&amp;");
                    if (0 <= iamp) {
                        categoryLabel = categoryLabel.substring(0, iamp) + categoryLabel.substring(iamp + 5);
                        found = true;
                    }
                    if (!found) break;
                }
                if (tabLabels.get(categoryLabel) == null) {
                    String tooltip = categoryElement.getAttribute("tooltip");
                    DefaultListModel model = (DefaultListModel) m_models.get(categoryLabel);
                    if (model == null) {
                        model = new DefaultListModel();
                        m_models.put(tabId, model);
                    }
                    m_bar.addTab(categoryLabel, null, new JLOutlookList(model), tooltip);
                    tabLabels.put(categoryLabel, viewId);
                }
                {
                    final String id = viewElement.getAttribute("id");
                    String label = viewElement.getAttribute("name");
                    String iconName = viewElement.getAttribute("icon32");
                    if (iconName == null || iconName.length() <= 0) iconName = viewElement.getAttribute("icon");
                    if (iconName == null || iconName.length() <= 0) iconName = PLUGIN.bind("org.jlense.uiworks.internal.OutlookShortcutsView.default.icon");
                    IPluginDescriptor viewPluginDescriptor = viewElement.getDeclaringExtension().getDeclaringPluginDescriptor();
                    ClassLoader classLoader = viewPluginDescriptor.getPluginClassLoader();
                    ImageIcon icon = WidgetUtils.getImageIconResource(classLoader, iconName);
                    if (icon == null) icon = WidgetUtils.getImageIconResource(PLUGIN.getDescriptor().getPluginClassLoader(), iconName);
                    PtRolloverButton btn = new PtRolloverButton(label, icon);
                    btn.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            final JFrame shell = getViewSite().getWorkbenchWindow().getShell();
                            shell.getToolkit().getSystemEventQueue().invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        IWorkbenchPage page = getViewSite().getPage();
                                        page.showView(id);
                                    } catch (PartInitException e) {
                                        String msg = PLUGIN.bind("msg.errorDisplayingContent");
                                        PLUGIN.logError(msg, e);
                                        PLUGIN.displayError(shell, msg, e);
                                    }
                                }
                            });
                        }
                    });
                    DefaultListModel model = (DefaultListModel) m_models.get(tabId);
                    model.addElement(btn);
                    PLUGIN.logDebug("Shortcut added to OutlookShortcutView for view:" + viewId);
                }
            }
        } catch (Exception e) {
            String msg = PLUGIN.bind("msg.errorDisplayingContent");
            PLUGIN.logError(msg, e);
        }
        control.revalidate();
        return control;
    }

    public void requestFocus() {
        m_bar.requestFocus();
    }
}
