package br.com.promove.view.table;

import java.io.Serializable;
import java.util.List;
import br.com.promove.entity.Transportadora;
import br.com.promove.exception.PromoveException;
import br.com.promove.service.CtrcService;
import br.com.promove.service.ServiceFactory;
import br.com.promove.view.TransportadoraView;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

public class TransportadoraTable extends Table {

    public static final Object[] NATURAL_COL_ORDER = new Object[] { "codigo", "cnpj", "descricao" };

    public static final String[] COL_HEADERS = new String[] { "Código", "CNPJ", "Descrição" };

    private TransportadoraView view;

    private CtrcService ctrcService;

    private TransportadoraContainer container;

    public TransportadoraTable() {
        ctrcService = ServiceFactory.getService(CtrcService.class);
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

    public void setView(TransportadoraView view) {
        this.view = view;
    }

    public BeanItemContainer<Transportadora> getContainer() {
        if (container == null) container = new TransportadoraContainer();
        return container;
    }

    class TransportadoraContainer extends BeanItemContainer<Transportadora> implements Serializable {

        public TransportadoraContainer() {
            super(Transportadora.class);
            populate();
        }

        private void populate() {
            try {
                List<Transportadora> list = ctrcService.buscarTodasTransportadoras();
                for (Transportadora Transportadora : list) {
                    addItem(Transportadora);
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
            BeanItem<Transportadora> item = (BeanItem<Transportadora>) getItem(getValue());
            view.getForm().createFormBody(item);
        }
    }
}
