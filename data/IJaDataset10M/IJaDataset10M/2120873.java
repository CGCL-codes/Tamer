package com.losalpes.catalogo.servicios;

import com.losalpes.catalogo.entities.ItemCarrito;
import com.losalpes.catalogo.entities.Usuario;
import java.util.ArrayList;
import javax.ejb.Remote;

/**
 * Contrato funcional de los servicios ofrecidos para el manejo de los carritos de compras.
 * Estos métodos son expuestos de manera remota para efectuar pruebas sobre su implementación
 * @author German Sotelo
 */
@Remote
public interface ICarritoServicesRemote {

    /**
     * Agrega un producto al carrito
     * @param id Referencia del producto a agregar.
     * @param cantidad Cantidad del producto a agregar
     */
    public void agregarAlCarrito(String id, int cantidad);

    /**
     * Retorna todos los items que actualmente se encuentran en el carrito de compras.
     * @return
     */
    public ArrayList<ItemCarrito> getItemsCarrito();

    /**
     * Remueve del carrito el producto que tiene la referencia dada como parámetro.
     * @param id identificador del producto a remover
     */
    public void remove(String id);

    /**
     * Remueve todos los items de carrito de compras.
     */
    public void removeAll();

    /**
     * Paga los items de carrito de compras y genera los correspondientes registros de venta.
     * @param user usuario que realizò la compra.
     */
    public void pagarCarrito(Usuario user);
}
