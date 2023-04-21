package br.com.promove.view.table;

import java.io.Serializable;
import java.util.List;
import br.com.promove.entity.Fabricante;
import br.com.promove.exception.PromoveException;
import br.com.promove.service.CadastroService;
import br.com.promove.service.ServiceFactory;
import br.com.promove.view.FabricanteView;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

public class FabricanteTable extends Table {

    public static final Object[] NATURAL_COL_ORDER = new Object[] { "codigo", "nome" };

    public static final String[] COL_HEADERS = new String[] { "Código", "Nome" };

    private FabricanteView view;

    private CadastroService cadastroService;

    private FabricanteTableContainer container;

    public FabricanteTable() {
        cadastroService = ServiceFactory.getService(CadastroService.class);
        buildTable();
    }

    private void buildTable() {
        setSizeFull();
        setColumnCollapsingAllowed(true);
        setSelectable(true);
        setImmediate(true);
        setNullSelectionAllowed(false);
        setContainerDataSource(getContainer());
        setVisibleColumns(NATURAL_COL_ORDER);
        setColumnHeaders(COL_HEADERS);
        addListener(new RowSelectedListener());
    }

    public void setView(FabricanteView view) {
        this.view = view;
    }

    public BeanItemContainer<Fabricante> getContainer() {
        if (container == null) container = new FabricanteTableContainer();
        return container;
    }

    class FabricanteTableContainer extends BeanItemContainer<Fabricante> implements Serializable {

        public FabricanteTableContainer() {
            super(Fabricante.class);
            populate();
        }

        private void populate() {
            try {
                List<Fabricante> list = cadastroService.buscarTodosFabricantes();
                for (Fabricante fab : list) {
                    addItem(fab);
                }
            } catch (PromoveException e) {
                e.printStackTrace();
            }
        }
    }

    class RowSelectedListener implements ValueChangeListener {

        @Override
        public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
            Property property = event.getProperty();
            BeanItem<Fabricante> item = (BeanItem<Fabricante>) getItem(getValue());
            view.getForm().createFormBody(item);
        }
    }
}
