package com.gustozzi.distribucion.negocio.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.gustozzi.distribucion.action.util.Constantes;
import com.gustozzi.distribucion.dao.CobranzaDAO;
import com.gustozzi.distribucion.dao.impl.CobranzaDAOImpl;
import com.gustozzi.distribucion.dao.util.DataAccess;
import com.gustozzi.distribucion.dao.util.DataAccessExceptions;
import com.gustozzi.distribucion.negocio.CobranzaBO;
import com.gustozzi.domain.Cliente;
import com.gustozzi.domain.Cobranza;
import com.gustozzi.domain.OrdenCobranza;
import com.gustozzi.domain.Pago;
import com.gustozzi.domain.SolicitudCobranza;

public class CobranzaBOImpl implements CobranzaBO {

    DataAccess transaccionCobranza = new DataAccess("java:comp/env/jdbc/Distribucion");

    CobranzaDAO cobranzaDAO = new CobranzaDAOImpl(transaccionCobranza);

    SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));

    @Override
    public List<Cobranza> obtenerListaCobranza(String rucCliente, String dni, String razonSocial, String codigoCentroDistribucion, String codigoCobrador, String fechaDesde, String fechaHasta) throws DataAccessExceptions {
        List<Cobranza> listaDespacho = null;
        int codigoCentro = 0;
        int cobrador = 0;
        Date desde = null;
        Date hasta = null;
        try {
            if (!fechaDesde.equalsIgnoreCase("")) {
                desde = new Date(sdf.parse(fechaDesde).getTime());
                hasta = new Date(sdf.parse(fechaHasta).getTime());
            }
            if (!codigoCentroDistribucion.equalsIgnoreCase("")) {
                codigoCentro = Integer.parseInt(codigoCentroDistribucion);
            }
            if (!codigoCobrador.equalsIgnoreCase("")) {
                cobrador = Integer.parseInt(codigoCobrador);
            }
            listaDespacho = cobranzaDAO.obtenerListaCobranza(rucCliente, "", razonSocial, codigoCentro, cobrador, desde, hasta);
        } catch (Exception e) {
            throw new DataAccessExceptions();
        }
        return listaDespacho;
    }

    @Override
    public List<SolicitudCobranza> obtenerListaSolicitudesCobranza(String codigo, String zona, String fechaDesde, String fechaHasta) throws DataAccessExceptions {
        List<SolicitudCobranza> listaSolicitudCobranza = null;
        Date desde = null;
        Date hasta = null;
        int codigoZona = 0;
        int codigoSolicitud = 0;
        try {
            if (!fechaDesde.equalsIgnoreCase("")) {
                desde = new Date(sdf.parse(fechaDesde).getTime());
                hasta = new Date(sdf.parse(fechaHasta).getTime());
            }
            if (!codigo.equalsIgnoreCase("")) {
                codigoSolicitud = Integer.parseInt(codigo);
            }
            if (!zona.equalsIgnoreCase("")) {
                codigoZona = Integer.parseInt(zona);
            }
            listaSolicitudCobranza = cobranzaDAO.obtenerListaSolicitudesCobranza(codigoSolicitud, codigoZona, desde, hasta);
        } catch (Exception e) {
            throw new DataAccessExceptions();
        }
        return listaSolicitudCobranza;
    }

    @Override
    public List<OrdenCobranza> obtenerListaOrdenesCobranza(String codigo, String numeroDocumento, String fechaDesde, String fechaHasta) throws DataAccessExceptions {
        List<OrdenCobranza> listaOrdenCobranza = null;
        Date desde = null;
        Date hasta = null;
        int codigoSolicitud = 0;
        try {
            if (!fechaDesde.equalsIgnoreCase(Constantes.CADENA_VACIA)) {
                desde = new Date(sdf.parse(fechaDesde).getTime());
                hasta = new Date(sdf.parse(fechaHasta).getTime());
            }
            if (!codigo.equalsIgnoreCase(Constantes.CADENA_VACIA)) {
                codigoSolicitud = Integer.parseInt(codigo);
            }
            listaOrdenCobranza = cobranzaDAO.obtenerListaOrdenesCobranza(codigoSolicitud, numeroDocumento, desde, hasta);
        } catch (Exception e) {
            throw new DataAccessExceptions();
        }
        return listaOrdenCobranza;
    }

    @Override
    public List<Pago> obtenerListaPagos(String codigo, String documentoCliente, String fechaDesde, String fechaHasta) throws DataAccessExceptions {
        List<Pago> listaPagos = null;
        Date desde = null;
        Date hasta = null;
        int codigoOrdenCobranza = 0;
        try {
            if (!fechaDesde.equalsIgnoreCase("")) {
                desde = new Date(sdf.parse(fechaDesde).getTime());
                hasta = new Date(sdf.parse(fechaHasta).getTime());
            }
            if (!codigo.equalsIgnoreCase("")) {
                codigoOrdenCobranza = Integer.parseInt(codigo);
            }
            listaPagos = cobranzaDAO.obtenerListaPagos(codigoOrdenCobranza, documentoCliente, desde, hasta);
        } catch (Exception e) {
            throw new DataAccessExceptions();
        }
        return listaPagos;
    }

    @Override
    public String eliminarSolicitudCobranza(String codigo) throws DataAccessExceptions {
        String respuesta;
        try {
            respuesta = cobranzaDAO.eliminarSolicitudCobranza(Integer.parseInt(codigo));
        } catch (Exception e) {
            throw new DataAccessExceptions();
        }
        return respuesta;
    }

    public String guardarSolicitudCobranza(String codigoSolicitud, String codigoCobrador, String codigoEstado, List<Cliente> listaClientes, String fechaSolicitud, boolean indicadorRegistro) throws DataAccessExceptions {
        String respuesta = Constantes.CADENA_VACIA;
        int codSolicitud = 0;
        int codCobrador = 0;
        int codEstado = 0;
        int listClientes[] = null;
        Cliente beanCliente = null;
        Date fecSolicitud = null;
        try {
            if (!codigoSolicitud.equalsIgnoreCase("")) {
                codSolicitud = Integer.parseInt(codigoSolicitud);
            }
            codCobrador = Integer.parseInt(codigoCobrador);
            if (codigoEstado.equalsIgnoreCase("ACTIVO")) {
                codEstado = 1;
            }
            listClientes = new int[listaClientes.size()];
            for (int i = 0; i < listaClientes.size(); i++) {
                beanCliente = (Cliente) listaClientes.get(i);
                listClientes[i] = Integer.parseInt(beanCliente.getCodigo());
            }
            if (!fechaSolicitud.equalsIgnoreCase("")) {
                fecSolicitud = new Date(sdf.parse(fechaSolicitud).getTime());
            }
            respuesta = cobranzaDAO.guardarSolicitudCobranza(codSolicitud, codCobrador, codEstado, listClientes, fecSolicitud, indicadorRegistro);
        } catch (Exception e) {
            throw new DataAccessExceptions(e);
        }
        return respuesta;
    }

    @Override
    public List<Cliente> obtenerListaClientesXSolicitud(String codigoSolicitud) throws DataAccessExceptions {
        List<Cliente> listaCliente = null;
        int codSolicitud = 0;
        try {
            if (!codigoSolicitud.equalsIgnoreCase("")) {
                codSolicitud = Integer.parseInt(codigoSolicitud);
            }
            listaCliente = cobranzaDAO.obtenerListaClientesXSolicitud(codSolicitud);
        } catch (Exception e) {
            throw new DataAccessExceptions(e);
        }
        return listaCliente;
    }
}
