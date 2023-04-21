package ar.com.larreta.intercambio.client.comunes;

import ar.com.larreta.intercambio.client.PedidoDeLista;

public class ListaDeTiposDeEmails extends PedidoDeLista {

    public ListaDeTiposDeEmails() {
        setProceso(ProcesosConst.LISTAR_TIPOS_DE_EMAILS);
    }
}
