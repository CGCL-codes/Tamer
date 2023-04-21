package queens.facades;

import java.util.List;
import queens.beans.BusquedaClientesBean;
import queens.beans.ClienteBean;

public interface IClienteFacade {

    public void insertarCliente(ClienteBean clienteBean);

    public void editarCliente(ClienteBean clienteBean);

    public void borrarCliente(String dni);

    public ClienteBean obtenerCliente(String dni);

    public boolean existeCliente(String dni, String activo);

    @SuppressWarnings("unchecked")
    public List buscarClientes(BusquedaClientesBean busquedaClientesBean);

    @SuppressWarnings("unchecked")
    public List listadoClientes(String ordenacionListado);
}
