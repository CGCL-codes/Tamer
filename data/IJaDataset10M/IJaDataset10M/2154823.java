package com.tesisutn.restsoft.dominio.pedido.estadosDetalle;

import com.tesisutn.restsoft.dominio.pedido.DetalleDePedido;

public class DetalleEntregado implements IEstadoDetalleDePedido {

    public void preparar(DetalleDePedido detalle) {
        ;
    }

    public void terminar(DetalleDePedido detalle) {
        ;
    }

    public void entregar(DetalleDePedido detalle) {
        ;
    }

    public void anular(DetalleDePedido detalle) {
        ;
    }

    public void cancelar(DetalleDePedido detalle) {
        ;
    }

    @Override
    public String toString() {
        return "Entregado";
    }
}
