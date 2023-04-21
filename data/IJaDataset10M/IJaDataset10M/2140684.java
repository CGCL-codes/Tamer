package org.dcma.client.grids;

import java.util.ArrayList;
import java.util.List;
import org.dcma.model.*;
import org.dcma.tests.testdata.TestData;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;

public class GridProcesos extends LayoutContainer {

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new ColumnLayout());
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig();
        column.setId("numero");
        column.setHeader("Numero");
        column.setAlignment(HorizontalAlignment.LEFT);
        column.setWidth(200);
        configs.add(column);
        column = new ColumnConfig("fechaInicio", "Last Updated", 100);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);
        column = new ColumnConfig("solicitante", "Solicitantes", 200);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);
        column = new ColumnConfig("invitado", "Invitados", 200);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);
        column = new ColumnConfig("estado", "Estado", 150);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        configs.add(column);
        ListStore<ProcedimientoModel> listStore = new ListStore<ProcedimientoModel>();
        listStore.add(TestData.getProcedimientos());
        ColumnModel cm = new ColumnModel(configs);
        ContentPanel cp = new ContentPanel();
        cp.setBodyBorder(false);
        cp.setHeading("Lista de Procesos");
        cp.setLayout(new FitLayout());
        cp.setSize(940, 300);
        Grid<ProcedimientoModel> grid = new Grid<ProcedimientoModel>(listStore, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setAutoExpandColumn("numero");
        grid.setBorders(true);
        grid.setStripeRows(true);
        cp.add(grid);
        add(cp);
    }
}
