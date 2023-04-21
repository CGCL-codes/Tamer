package org.regilo.content.editor.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.regilo.content.RegiloContentImages;
import org.regilo.content.model.Node;
import org.regilo.content.tabs.AbstractTab;
import org.regilo.core.ui.RegiloCoreImages;
import org.regilo.core.ui.editors.RegiloEditor;
import org.regilo.core.ui.editors.pages.StandardDetailsPage;

public class ContentsPageDetail extends StandardDetailsPage {

    private class TabsSort implements Comparator<AbstractTab> {

        public int compare(AbstractTab o1, AbstractTab o2) {
            if (o1.getWeight() < o2.getWeight()) {
                return -1;
            } else if (o1.getWeight() > o2.getWeight()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private Node node;

    private RegiloEditor editor;

    private ContentsPageMaster masterPage;

    private static final String TAB_EXTENSION_ID = "org.regilo.content.tabs";

    private static final Logger log = Logger.getLogger(ContentsPageDetail.class);

    public ContentsPageDetail(Node node, RegiloEditor editor, ContentsPageMaster masterPage) {
        this.node = node;
        this.editor = editor;
        this.masterPage = masterPage;
    }

    public void createContents(Composite parent) {
        GridLayout layout = new GridLayout(1, false);
        parent.setLayout(layout);
        FormToolkit toolkit = getMform().getToolkit();
        Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        section.setText("Edit content");
        section.setDescription("View and edit details about this content.");
        GridData gd = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(gd);
        final Composite client = toolkit.createComposite(section);
        client.setLayout(layout);
        createSectionToolbar(section, toolkit);
        CTabFolder folder = new CTabFolder(client, SWT.TOP | SWT.BORDER);
        folder.setLayoutData(gd);
        toolkit.adapt(folder);
        folder.setBackground(new Color(PlatformUI.getWorkbench().getDisplay(), new RGB(200, 200, 200)));
        IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(TAB_EXTENSION_ID).getExtensions();
        List<AbstractTab> tabs = new ArrayList<AbstractTab>();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] elements = extensions[i].getConfigurationElements();
            for (int j = 0; j < elements.length; j++) {
                IConfigurationElement element = elements[j];
                try {
                    AbstractTab tab = (AbstractTab) element.createExecutableExtension("tab");
                    String name = element.getAttribute("name");
                    int weight = 0;
                    try {
                        weight = Integer.parseInt(element.getAttribute("weight"));
                    } catch (NumberFormatException e) {
                        log.error(e.getMessage());
                    } catch (InvalidRegistryObjectException e) {
                        log.error(e.getMessage());
                    }
                    tab.setName(name);
                    tab.setWeight(weight);
                    tabs.add(tab);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(tabs, new TabsSort());
        for (AbstractTab tab : tabs) {
            CTabItem item = new CTabItem(folder, SWT.NONE);
            item.setText(tab.getName());
            Composite tabContent = toolkit.createComposite(folder);
            tabContent.setLayout(layout);
            tab.createTabContens(tabContent, toolkit, editor, masterPage, node);
            item.setControl(tabContent);
        }
        folder.setSelection(0);
        toolkit.paintBordersFor(client);
        section.setClient(client);
    }

    private void createSectionToolbar(Section section, FormToolkit toolkit) {
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(section);
        final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        toolbar.setCursor(handCursor);
        toolbar.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                if ((handCursor != null) && (handCursor.isDisposed() == false)) {
                    handCursor.dispose();
                }
            }
        });
        CommandContributionItemParameter addNodeContributionParameter = new CommandContributionItemParameter(editor.getSite(), null, "org.regilo.content.commands.saveNode", CommandContributionItem.STYLE_PUSH);
        addNodeContributionParameter.icon = RegiloCoreImages.getInstance().DESC_UPDATE;
        CommandContributionItem addNodeItem = new CommandContributionItem(addNodeContributionParameter);
        toolBarManager.add(addNodeItem);
        CommandContributionItemParameter previewNodeContributionParameter = new CommandContributionItemParameter(editor.getSite(), null, "org.regilo.content.commands.previewNode", CommandContributionItem.STYLE_PUSH);
        previewNodeContributionParameter.icon = RegiloContentImages.getInstance().DESC_PREVIEW;
        CommandContributionItem previewNodeItem = new CommandContributionItem(previewNodeContributionParameter);
        toolBarManager.add(previewNodeItem);
        toolBarManager.update(true);
        section.setTextClient(toolbar);
    }
}
