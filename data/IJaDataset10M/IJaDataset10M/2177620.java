package ar.com.larreta.grilla.client;

import java.util.HashMap;
import java.util.Map;
import ar.com.larreta.controlador.client.Invocador;
import ar.com.larreta.intercambio.client.ListaRespuesta;
import ar.com.larreta.intercambio.client.PedidoDeLista;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Proxy extends RpcProxy {

    private PedidoDeLista pedido;

    private String nombreLista;

    private Grilla grilla;

    public Proxy(PedidoDeLista pedido, String nombreLista) {
        super();
        this.pedido = pedido;
        this.nombreLista = nombreLista;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    @Override
    protected void load(Object loadConfig, AsyncCallback callback) {
        BasePagingLoadConfig config = (BasePagingLoadConfig) loadConfig;
        RetornoGrilla retornoGrilla = new RetornoGrilla(grilla);
        retornoGrilla.agregar(callback);
        if (pedido != null) {
            pedido.setProperties(config.getProperties());
            pedido.setLimit(config.getLimit());
            pedido.setOffset(config.getOffset());
            pedido.setSortDir(config.getSortDir().name());
            pedido.setSortField(config.getSortField());
            Invocador.invocar(pedido, retornoGrilla);
        } else {
            Map result = new HashMap();
            ListaRespuesta listaRespuesta = new ListaRespuesta();
            if (config.getOffset() + config.getLimit() > grilla.getDatos().size()) {
                listaRespuesta.setLista(grilla.getDatos().subList(config.getOffset(), grilla.getDatos().size()));
            } else {
                listaRespuesta.setLista(grilla.getDatos().subList(config.getOffset(), config.getOffset() + config.getLimit()));
            }
            listaRespuesta.setTotalLength(grilla.getDatos().size());
            listaRespuesta.setOffset(config.getOffset());
            result.put(nombreLista, listaRespuesta);
            retornoGrilla.onSuccess(result);
        }
    }

    @Override
    public void load(final DataReader reader, final Object loadConfig, final AsyncCallback callback) {
        load(loadConfig, new AsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @SuppressWarnings("unchecked")
            public void onSuccess(Object result) {
                try {
                    Map data = null;
                    if (reader != null) {
                        data = (Map) reader.read(loadConfig, result);
                    } else {
                        data = (Map) result;
                    }
                    callback.onSuccess(data.get(nombreLista));
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        });
    }
}
