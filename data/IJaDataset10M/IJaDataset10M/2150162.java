package com.naguisabet.facturacion.logic;

import com.naguisabet.facturacion.entity.Vendedor;
import com.naguisabet.facturacion.jpaController.JpaControllerFactory;
import com.naguisabet.facturacion.jpaController.VendedorJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ARQUITECTURA
 */
public class VendedorLogic implements IVendedorLogic {

    public void registrarVendedor(String cedulaNit, String nombreCompleto, String direccion, String telefono) throws Exception {
        if (cedulaNit == null) {
            throw new Exception("LA CEDULA / NIT ES OBLIGATORIA");
        }
        if (cedulaNit.length() == 0) {
            throw new Exception("LA CEDULA / NIT DEBE CONTENER SOLO NUMEROS");
        }
        if (nombreCompleto == null || nombreCompleto.equals("")) {
            throw new Exception("EL NOMBRE DEL VENDEDOR NO PUEDE IR VACIO");
        }
        Vendedor vendedor = new Vendedor();
        vendedor.setCedulaNit(cedulaNit);
        vendedor.setNombreCompleto(nombreCompleto);
        vendedor.setDireccion(direccion);
        vendedor.setTelefono(telefono);
        JpaControllerFactory.getInstace().getVendedorJpaController().create(vendedor);
    }

    public void modificarVendedor(String cedulaNit, String nombreCompleto, String direccion, String telefono) throws Exception {
        if (cedulaNit == null) {
            throw new Exception("LA CEDULA / NIT ES OBLIGATORIA");
        }
        if (cedulaNit.length() == 0) {
            throw new Exception("LA CEDULA / NIT DEBE CONTENER SOLO NUMEROS");
        }
        if (nombreCompleto == null || nombreCompleto.equals("")) {
            throw new Exception("EL NOMBRE DEL VENDEDOR NO PUEDE IR VACIO");
        }
        Vendedor vendedor = new Vendedor();
        vendedor.setCedulaNit(cedulaNit);
        vendedor.setNombreCompleto(nombreCompleto);
        vendedor.setDireccion(direccion);
        vendedor.setTelefono(telefono);
        JpaControllerFactory.getInstace().getVendedorJpaController().edit(vendedor);
    }

    public Vendedor buscarVendedor(String cedulaNit) throws Exception {
        Vendedor vendedor = null;
        String q = "select v.idVendedor,v.cedulaNit,v.nombreCompleto,v.direccion,v.telefono from Vendedor v where v.cedulaNit='" + cedulaNit + "'";
        List lista = (List) JpaControllerFactory.getInstace().getVendedorJpaController().getEntityManager().createQuery(q).getResultList();
        vendedor = (Vendedor) lista.get(0);
        return vendedor;
    }
}
