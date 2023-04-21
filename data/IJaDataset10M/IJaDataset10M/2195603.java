package com.digdia.app.gwt.client.view.layout;

import com.digdia.app.gwt.client.constanst.FormConstant;
import com.digdia.app.gwt.client.constanst.RoleConstant;
import com.digdia.app.gwt.client.model.datasource.FactoryDataSource;
import com.digdia.app.gwt.client.model.util.RoleDictionary;
import com.digdia.app.gwt.client.view.modal.PenyakitDetailModal;
import com.digdia.app.gwt.client.view.util.paging.PagingLayoutListGrid;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 *
 * @author vita
 */
public class PenyakitLayoutMaximize extends PagingLayoutListGrid {

    private Menu contextMenuPenyakit;

    private MenuItem addNewMenuItem;

    private MenuItem refreshMenuItem;

    private MenuItem deleteMenuItem;

    private MenuItem viewDetailMenuItem;

    public PenyakitLayoutMaximize() {
        super("PenyakitLayoutMaximize", true);
        initPenyakitLayout();
        initMenu();
        initPaging();
        initController();
    }

    private void initPenyakitLayout() {
        setID("penyakitLayout2Max");
    }

    private void initMenu() {
        contextMenuPenyakit = new Menu();
        contextMenuPenyakit.setShowShadow(true);
        contextMenuPenyakit.setShadowDepth(4);
        MenuItemSeparator separator = new MenuItemSeparator();
        MenuItem header = new MenuItem("Menu Penyakit", "icons/16/application.png");
        header.setEnabled(false);
        addNewMenuItem = new MenuItem("Tambah Baru", "icons/16/application_add.png");
        refreshMenuItem = new MenuItem("Refresh", "icons/16/rss.png");
        viewDetailMenuItem = new MenuItem("Lihat Detail", "icons/16/application_edit.png");
        if (RoleDictionary.getRoleID() == RoleConstant.PAKAR) {
            deleteMenuItem = new MenuItem("Hapus", "icons/16/application_remove.png");
            contextMenuPenyakit.setItems(header, separator, refreshMenuItem, addNewMenuItem, viewDetailMenuItem, deleteMenuItem, separator);
        } else if (RoleDictionary.getRoleID() == RoleConstant.ADMINISTRATOR) {
            contextMenuPenyakit.setItems(header, separator, refreshMenuItem, addNewMenuItem, viewDetailMenuItem, separator);
        }
    }

    private void initPaging() {
        ListGridField namaPenyakit = new ListGridField(FormConstant.PENYAKIT_NAME, "Penyakit");
        namaPenyakit.setAlign(Alignment.CENTER);
        namaPenyakit.setSortDirection(SortDirection.ASCENDING);
        setField(namaPenyakit);
        setSortField(FormConstant.GEJALA_NAME);
        setSelectionStyle(SelectionStyle.SINGLE);
        setContextMenuListGrid(contextMenuPenyakit);
        setDataSource(FactoryDataSource.getPenyakitDataSource());
        Label label = new Label("Daftar Penyakit");
        label.setStyleName("headerList");
        label.setWidth(120);
        label.setIcon("icons/16/applications.png");
        label.setIconAlign("right");
        setTitleLabel(label);
        setShowSearchItem("Penyakit");
        init();
    }

    private void initController() {
        refreshMenuItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                deactiveSearchProgram();
                refreshData();
            }
        });
        viewDetailMenuItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                ListGridRecord record = getListGrid().getSelectedRecord();
                if (record != null) {
                    new PenyakitDetailModal(new Integer(record.getAttribute(FormConstant.PENYAKIT_ID))) {

                        @Override
                        protected void onCloseRefreshData() {
                            refreshData();
                            updateRow = true;
                        }
                    };
                }
            }
        });
        addNewMenuItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new PenyakitDetailModal() {

                    @Override
                    protected void onCloseRefreshData() {
                        refreshData();
                        updateRow = true;
                    }
                };
            }
        });
        if (RoleDictionary.getRoleID() == RoleConstant.PAKAR) {
            deleteMenuItem.addClickHandler(new ClickHandler() {

                public void onClick(MenuItemClickEvent event) {
                    ListGridRecord record = getListGrid().getSelectedRecord();
                    if (record != null) {
                        String namaPenyakit = record.getAttribute(FormConstant.PENYAKIT_NAME);
                        String message = "";
                        if (namaPenyakit != null && !namaPenyakit.equals("")) {
                            message = namaPenyakit.length() > 20 ? namaPenyakit.substring(0, 19) : namaPenyakit;
                        }
                        SC.confirm("Konfirmasi penghapusan!", "Apakah anda akan menghapus [ " + message + " ] dari daftar penyakit?", new BooleanCallback() {

                            public void execute(Boolean value) {
                                if (value) {
                                    getListGrid().removeSelectedData();
                                    updateRow = true;
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private boolean updateRow = false;

    public boolean isUpdateRow() {
        return updateRow;
    }
}
