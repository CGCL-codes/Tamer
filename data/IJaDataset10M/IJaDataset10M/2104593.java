package com.quickwcm.admin.ui.client.pages;

import java.util.List;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.quickwcm.admin.ui.client.AdminUIHelper;
import com.quickwcm.admin.ui.client.beans.ContentTO;
import com.quickwcm.admin.ui.client.beans.PageBundleTO;
import com.quickwcm.admin.ui.client.beans.PageTO;
import com.quickwcm.admin.ui.client.beans.RichTextTO;
import com.quickwcm.admin.ui.client.rpc.AsyncCallbackAdaptor;
import com.quickwcm.admin.ui.client.rpc.RPCHelper;
import com.quickwcm.admin.ui.client.utils.LazyTab;
import com.quickwcm.admin.ui.client.utils.LazyTabInitializerListener;
import com.quickwcm.admin.ui.client.utils.UIUtils;

public class PageEditor extends Composite {

    private PageTO page;

    private PagePropertiesEditor pagePropertiesEditor;

    private PageContentsEditor contentsEditor;

    private SimplePanel topContainer;

    public PageEditor(PageTO page) {
        this.page = page;
        topContainer = new SimplePanel();
        initWidget(topContainer);
        topContainer.setWidget(UIUtils.getLoadingMessage("Loading Page Editor..."));
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                redraw();
            }
        });
    }

    private void redraw() {
        contentsEditor = null;
        TabPanel tabPanel = new TabPanel();
        tabPanel.addTabListener(new LazyTabInitializerListener(tabPanel));
        tabPanel.setWidth("100%");
        tabPanel.setHeight("100%");
        tabPanel.add(new LazyTab() {

            protected Widget lazyInit() {
                HorizontalPanel beforeTabs = new HorizontalPanel();
                beforeTabs.setWidth("100%");
                pagePropertiesEditor = new PagePropertiesEditor(page);
                beforeTabs.add(pagePropertiesEditor);
                beforeTabs.add(new PagePermissionsEditor(page));
                return beforeTabs;
            }
        }, "Properties");
        tabPanel.add(new LazyTab() {

            protected Widget lazyInit() {
                final SimplePanel holder = new SimplePanel();
                holder.setWidget(UIUtils.getLoadingMessage("Loading Contents..."));
                RPCHelper.getPagesRPC().getPageContents(page, new AsyncCallbackAdaptor<List<ContentTO>>() {

                    @Override
                    public void success(List<ContentTO> contents) {
                        contentsEditor = new PageContentsEditor(page, contents);
                        holder.setWidget(contentsEditor);
                    }
                });
                return holder;
            }
        }, "Content");
        tabPanel.selectTab(0);
        VerticalPanel topPanel = new VerticalPanel();
        topPanel.setWidth("100%");
        topPanel.setHeight("100%");
        topPanel.setSpacing(10);
        FlowPanel contentHolder = new FlowPanel();
        String strTitle = page.getTitle();
        if (page.isNew()) {
            strTitle += " (New)";
        } else if (page.isDeleted()) {
            strTitle += " (Deleted)";
        } else if (page.isEdited()) {
            strTitle += " (Edited)";
        }
        Widget title = new Label(strTitle);
        title.setStyleName("pageTitle");
        contentHolder.add(title);
        contentHolder.add(tabPanel);
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        Button saveButton = new Button("Save", new ClickListener() {

            public void onClick(Widget arg0) {
                savePage(false);
            }
        });
        saveButton.setEnabled(!page.isDeleted());
        buttonsPanel.add(saveButton);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        Button saveAndCloseButton = new Button("Save & Close", new ClickListener() {

            public void onClick(Widget arg0) {
                savePage(true);
            }
        });
        saveAndCloseButton.setEnabled(!page.isDeleted());
        buttonsPanel.add(saveAndCloseButton);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        buttonsPanel.add(new Button(page.isDeleted() ? "Undelete" : "Delete", new ClickListener() {

            public void onClick(Widget arg0) {
                if (page.hasChildren()) {
                    UIUtils.alert("Can't delete this page. Delete or move all children first.");
                    return;
                }
                topContainer.setWidget(UIUtils.getLoadingMessage((page.isDeleted() ? "Undeleting" : "Deleting") + " Page..."));
                RPCHelper.getPagesRPC().deletePage(page, new AsyncCallbackAdaptor<Void>() {

                    public void success(Void result) {
                        if (page.isNew()) {
                            AdminUIHelper.instance().closeActivePageEditor();
                        } else {
                            reloadPageAndRedraw();
                        }
                        AdminUIHelper.instance().reloadPagesTree();
                    }
                });
            }
        }));
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        Button publishButton = new Button("Publish", new ClickListener() {

            public void onClick(Widget arg0) {
                UIUtils.alert("not implemented O_o");
            }
        });
        publishButton.setEnabled(page.isDeleted() || page.isNew() || page.isEdited());
        buttonsPanel.add(publishButton);
        buttonsPanel.add(new HTML("&nbsp;&nbsp;"));
        buttonsPanel.add(new Button("Close w/o Saving", new ClickListener() {

            public void onClick(Widget arg0) {
                AdminUIHelper.instance().closeActivePageEditor();
            }
        }));
        topPanel.add(buttonsPanel);
        topPanel.setCellHeight(buttonsPanel, "30px");
        topPanel.add(contentHolder);
        topContainer.setWidget(topPanel);
    }

    private void savePage(final boolean close) {
        page.setTitle(pagePropertiesEditor.getPageTitle());
        System.out.println(" - pagetitle=" + page.getTitle());
        page.setKeywords(pagePropertiesEditor.getPageKeywords());
        page.setParent(pagePropertiesEditor.getParentPage());
        PageBundleTO bundle = new PageBundleTO();
        bundle.setPageTO(page);
        if (contentsEditor != null) {
            List<ContentTO> editedContent = contentsEditor.getEditedContent();
            for (ContentTO contentTO : editedContent) {
                System.out.println(" - title=" + contentTO.getTitleText());
                if (contentTO instanceof RichTextTO) {
                    System.out.println(" - html=" + ((RichTextTO) contentTO).getHtml());
                }
            }
            bundle.setContent(editedContent);
        }
        topContainer.setWidget(UIUtils.getLoadingMessage("Saving Page..."));
        RPCHelper.getPagesRPC().savePage(bundle, new AsyncCallbackAdaptor<Void>() {

            public void success(Void result) {
                AdminUIHelper.instance().reloadPagesTree();
                if (close) {
                    AdminUIHelper.instance().closeActivePageEditor();
                } else {
                    reloadPageAndRedraw();
                }
            }
        });
    }

    private void reloadPageAndRedraw() {
        RPCHelper.getPagesRPC().getPage(page.getId(), new AsyncCallbackAdaptor<PageTO>() {

            public void success(PageTO _page) {
                page = _page;
                redraw();
            }

            ;
        });
    }
}
