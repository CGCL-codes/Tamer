package ar.com.larreta.procesos.pasos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ar.com.larreta.comunes.Transformador;
import ar.com.larreta.excepciones.Excepcion;
import ar.com.larreta.intercambio.client.PedidoDeBajas;
import ar.com.larreta.procesos.Paso;

public class ObtenerBajas extends Paso {

    private PedidoDeBajas getPedidoDeBajas() {
        return (PedidoDeBajas) this.proceso.getPedido();
    }

    @Override
    protected void accion() {
        List convertidos = new ArrayList();
        List list = getPedidoDeBajas().getLista();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            try {
                convertidos.add(Transformador.getInstancia().getObjetoNuevo(it.next()));
            } catch (InstantiationException e) {
                logger.error(Excepcion.getStackTrace(e));
            } catch (IllegalAccessException e) {
                logger.error(Excepcion.getStackTrace(e));
            }
        }
        salidas.put(ENTIDADES, convertidos);
    }
}
