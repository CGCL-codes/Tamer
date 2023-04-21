package ar.com.larreta.intercambio.client.comunes;

import ar.com.larreta.intercambio.client.PedidoDeLista;
import ar.com.larreta.intercambio.client.comunes.ProcesosConst;

public class ListaDeMensajes extends PedidoDeLista {

    public ListaDeMensajes() {
        setProceso(ProcesosConst.LISTAR_MENSAJES);
    }
}
