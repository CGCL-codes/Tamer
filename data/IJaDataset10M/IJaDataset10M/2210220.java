package org.weras.portal.clientes.client.comum;

import com.google.gwt.user.client.Element;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.XTemplate;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.DataView;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.DataViewListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class BarraFerramentas extends Panel {

    public static final String PREFIXO_ID_ABA = "ABA_";

    public static final String PREFIXO_BOTAO_ABRIR_MODULO = "BOTAO_ABRIR_MODULO_";

    private static ArrayReader reader = new ArrayReader(new RecordDef(new FieldDef[] { new StringFieldDef("nome"), new StringFieldDef("titulo"), new StringFieldDef("url") }));

    private static XTemplate template = new XTemplate(new String[] { "<tpl for='.'>", "<div class='thumb-wrap'>", "<div class='thumb'><img id='BOTAO_MODULO_{nome}' src='{url}' ext:qtip='{titulo}'></div>", "<span class='x-editable' ext:qtip='{titulo}'>{nome}</span></div>", "</tpl>", "<div class='x-clear'></div>" });

    private DataView dataView;

    public BarraFerramentas(final PaginaPadrao paginaPadrao) {
        setLayout(new FitLayout());
        setId("toolbar-view");
        setAutoHeight(true);
        dataView = new DataView("div.thumb-wrap") {

            public void prepareData(Data data) {
                data.setProperty("nome", Format.ellipsis(data.getProperty("nome"), 15));
            }
        };
        dataView.setAutoWidth(true);
        dataView.addListener(new DataViewListenerAdapter() {

            public void onClick(DataView source, int index, Element node, EventObject e) {
                super.onClick(source, index, node, e);
                Modulo<?> selecionado = paginaPadrao.getModulo(index);
                String idAba = PREFIXO_ID_ABA + selecionado.getEntidadeSingular();
                paginaPadrao.mostrarAba(idAba, "Lista de " + selecionado.getEntidadePlural(), selecionado.criarConteudoModulo());
                paginaPadrao.doLayout();
                selecionado.atualizar();
            }
        });
        dataView.setTpl(template);
        dataView.setAutoHeight(true);
        dataView.setMultiSelect(true);
        dataView.setOverCls("x-view-over");
        add(dataView);
    }

    public void atualiza(Object[][] data) {
        MemoryProxy dataProxy = new MemoryProxy(data);
        Store store = new Store(dataProxy, reader, true);
        store.load();
        dataView.setStore(store);
        dataView.refresh();
    }
}
