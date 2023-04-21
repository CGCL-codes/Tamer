package org.plog4u.wiki.actions.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.plog4u.wiki.actions.mediawiki.config.ISystemConfiguration;
import org.plog4u.wiki.editor.WikiEditorPlugin;
import org.plog4u.wiki.internal.UserConfiguration;
import org.plog4u.wiki.internal.UserConfigurationManager;
import org.plog4u.wiki.internal.IUserConfiguration;
import org.plog4u.wiki.viewsupport.ListContentProvider;

public abstract class AbstractWikiSQLAction implements IObjectActionDelegate {

    private IWorkbenchPart workbenchPart;

    protected UserConfiguration getConfigurationType(final String type) {
        final List allConfigsList = UserConfigurationManager.getInstance().getConfigurations();
        final ArrayList configsList = new ArrayList();
        for (int i = 0; i < allConfigsList.size(); i++) {
            final IUserConfiguration temp = (IUserConfiguration) allConfigsList.get(i);
            if (temp.getType().equals(type)) {
                configsList.add(temp);
            }
        }
        if (configsList.size() == 1) {
            return (UserConfiguration) configsList.get(0);
        }
        Collections.sort(configsList);
        UserConfiguration configuration = null;
        final ListSelectionDialog listSelectionDialog = new ListSelectionDialog(WikiEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), configsList, new ListContentProvider(), new LabelProvider(), "Select the SQL database URL.");
        listSelectionDialog.setTitle("Multiple active configuration found");
        if (listSelectionDialog.open() == Window.OK) {
            final Object[] locations = listSelectionDialog.getResult();
            if (locations != null) {
                for (int i = 0; i < locations.length; i++) {
                    configuration = (UserConfiguration) locations[i];
                    break;
                }
            }
        }
        return configuration;
    }

    protected UserConfiguration getConfiguration() {
        return getConfigurationType(WikiEditorPlugin.WIKIPEDIA_SQL);
    }

    /**
   *  
   */
    public AbstractWikiSQLAction() {
        super();
    }

    /**
   * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
   */
    public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
        workbenchPart = targetPart;
    }

    public void run(final IAction action) {
        try {
            ISelectionProvider selectionProvider = null;
            selectionProvider = workbenchPart.getSite().getSelectionProvider();
            StructuredSelection selection = null;
            selection = (StructuredSelection) selectionProvider.getSelection();
            Iterator iterator = null;
            iterator = selection.iterator();
            final UserConfiguration configuration = getConfiguration();
            final String wikiLocale = configuration.getType().substring(WikiEditorPlugin.PREFIX_LOAD.length());
            final ISystemConfiguration wikipediaProperties = WikiEditorPlugin.getWikiInstance(wikiLocale);
            final HashSet set = new HashSet();
            final IResourceVisitor visitor = new IResourceVisitor() {

                public boolean visit(IResource resource) {
                    switch(resource.getType()) {
                        case IResource.FILE:
                            if (resource.getFileExtension().equalsIgnoreCase(WikiEditorPlugin.WP_EXTENSION)) {
                                set.add(resource);
                            }
                            break;
                    }
                    return true;
                }
            };
            while (iterator.hasNext()) {
                final Object obj = iterator.next();
                if (obj instanceof IResource) {
                    final IResource resource = (IResource) obj;
                    switch(resource.getType()) {
                        case IResource.FOLDER:
                        case IResource.FILE:
                            resource.accept(visitor);
                            break;
                    }
                }
            }
            if (set.size() > 0) {
                final IFile[] files = new IFile[set.size()];
                set.toArray(files);
                createAndRunJob(configuration, wikipediaProperties, files);
            }
        } catch (final CoreException e) {
            e.printStackTrace();
        }
    }

    /**
   * @param configuration
   * @param wikipediaProperties
   * @param files
   */
    protected abstract void createAndRunJob(UserConfiguration configuration, ISystemConfiguration wikipediaProperties, IFile[] files);

    public ISchedulingRule modifyRule(final IFile[] files) {
        ISchedulingRule combinedRule = null;
        final IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace().getRuleFactory();
        ISchedulingRule rule;
        for (int i = 0; i < files.length; i++) {
            rule = ruleFactory.modifyRule(files[i]);
            combinedRule = MultiRule.combine(rule, combinedRule);
        }
        return combinedRule;
    }

    /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
    public void selectionChanged(final IAction action, final ISelection selection) {
    }
}
