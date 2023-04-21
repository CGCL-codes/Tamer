package com.uk.ui.windows;

import com.uk.data.containers.TarifaContainer;
import com.uk.data.ejbs.IFaturaBean;
import com.uk.data.entities.StatusEnum;
import com.uk.data.entities.Tarifa;
import com.uk.interfaces.PopupWindowListener;
import com.uk.ui.ColumnGeneratorUtils;
import com.uk.ui.PopupWindow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Table;

public class TarifaSelectWindow extends PopupWindow implements ValueChangeListener {

    private TarifaContainer tarifaContainer;

    private PopupWindowListener listener;

    private Tarifa selectedTarifa;

    public TarifaSelectWindow(PopupWindowListener listener, IFaturaBean faturaBean) {
        super("Lista e tarifave aktive", "600px", "300px");
        this.center();
        this.listener = listener;
        this.tarifaContainer = TarifaContainer.createFromEjb(faturaBean, StatusEnum.AKTIV);
        Table tarifaListTable = new Table();
        tarifaListTable.setContainerDataSource(this.tarifaContainer);
        tarifaListTable.setVisibleColumns(TarifaContainer.COL_FOR_FATURA);
        tarifaListTable.setColumnHeaders(TarifaContainer.COL_FOR_FATURA_HEADER);
        tarifaListTable.setPageLength(4);
        tarifaListTable.setSelectable(true);
        tarifaListTable.setImmediate(true);
        tarifaListTable.setSortDisabled(true);
        tarifaListTable.setWidth("550px");
        tarifaListTable.setSelectable(true);
        tarifaListTable.setImmediate(true);
        tarifaListTable.addListener(this);
        this.addComponent(tarifaListTable);
        tarifaListTable.addGeneratedColumn("aplikoTVSH", ColumnGeneratorUtils.PoJoColumnGenerator());
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        this.selectedTarifa = (Tarifa) event.getProperty().getValue();
        this.listener.returnAction(this);
        this.close();
    }

    public Tarifa getSelectedTarifa() {
        return selectedTarifa;
    }
}
