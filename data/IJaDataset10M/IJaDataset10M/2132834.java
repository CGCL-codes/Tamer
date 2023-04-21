package dao;

import java.util.List;
import exceptions.BuscarObjetoException;
import bean.DescuentoCompraBean;

public interface DescuentoCompraDAO {

    public DescuentoCompraBean findByID(Integer id) throws BuscarObjetoException;

    public List<DescuentoCompraBean> getAll();

    public void grabarDescuentoCompra(DescuentoCompraBean descuentoCompra);

    public List<DescuentoCompraBean> getDescuentosXProveedor(int idProveedor);
}
