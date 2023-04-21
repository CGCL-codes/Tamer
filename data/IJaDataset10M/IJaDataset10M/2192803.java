package com.siasal.documentos.business;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.common.exception.BusinessException;
import com.common.exception.InternalApplicationException;
import com.common.persistence.domainstore.IPersistenceManager;
import com.siasal.documentos.commons.EjemplarTO;
import com.siasal.prestamos.business.Prestamo;
import com.siasal.prestamos.business.Reservacion;

/**
 * @hibernate.class table="doc_ejemplar"
 * 
 * @hibernate.query name="getByCodigo" query="from Ejemplar as e where e.codigo=
 *                  :codigo "
 * @hibernate.query name="ejemplaresPrestados" query="select count(*) from Ejemplar as e where e.estado=200"
 * @hibernate.query name="ejemplaresPerdidos" query="select count(*) from Ejemplar as e where e.estado=201"
 * @hibernate.query name="ejemplaresReparandose" query="select count(*) from Ejemplar as e where e.estado=202"
 * @hibernate.query name="ejemplaresExistentes" query="select count(*) from Ejemplar as e where e.fecha>=
 *                  :fechaini and e.fecha<= :fechafin "
 *                  
 */
public class Ejemplar implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5958243355875659331L;

    public static final int DISPONIBLE = 0;

    public static final int PRESTADO = 200;

    public static final int PERDIDO = 201;

    public static final int EN_REPARACION = 202;

    public static final int RESERVADO = 203;

    public static final int NO_DISPONIBLE = 210;

    public static final int ELIMINADO = 220;

    private Documento documento;

    /** @modelguid {3B5C036B-39EA-431F-AB94-8DFA8B440720} */
    private Integer id;

    /** @modelguid {5E60AAD3-404D-49A6-A808-5A4BB35CB0F9} */
    private Date fecha;

    /** @modelguid {65BC15F9-4D87-4302-A990-4F93B9BC5B63} */
    private float costo;

    private Procedencia procedencia;

    /** @modelguid {A7598A54-1C6B-4BAB-A7F5-7E26126D05CB} */
    private boolean original;

    /** @modelguid {BD21B29C-0B91-4B85-A059-B8171E88AEDA} */
    private Categoria categoriaFija;

    private Categoria categoriaVariable;

    private int numero;

    private String codigo;

    /**
	 * Estado del ejemplar
	 */
    private int estado;

    private static Map catalogoEstadoPresentacion;

    static {
        catalogoEstadoPresentacion = new HashMap();
        catalogoEstadoPresentacion.put("DISPONIBLE", 0);
        catalogoEstadoPresentacion.put("PRESTADO", 200);
        catalogoEstadoPresentacion.put("PERDIDO", 201);
        catalogoEstadoPresentacion.put("EN REPARACION", 202);
        catalogoEstadoPresentacion.put("RESERVADO", 203);
        catalogoEstadoPresentacion.put("NO DISPONIBLE", 210);
        catalogoEstadoPresentacion.put("ELIMINADO", 220);
    }

    /** @modelguid {C286A55D-FCF5-4135-BE8F-B1DAE61080A3} */
    public void adquisicion() {
    }

    /** @modelguid {0E6A3D91-5155-4DDF-AAD7-75D8ADC71EA8} */
    public void prestamo() {
    }

    /** @modelguid {018F81DC-3B3F-4725-B4DC-210502A82228} */
    public void devolucion() {
    }

    /** @modelguid {B70C6DF0-E9DB-4C38-BB49-4930A37468A3} */
    public void repuesto() {
    }

    /** @modelguid {BA5776BA-F8BC-47C4-A81E-A45F65263333} */
    public void encontrado() {
    }

    /** @modelguid {8E051D5B-133A-44C8-BA77-70CB854128E6} */
    public void darDeBaja() {
    }

    /** @modelguid {FD8C8233-7487-4CF1-9DAC-9F4C305DFFE9} */
    public void perdida() {
    }

    /** @modelguid {055786A0-CCAC-49EE-8212-86D4E6ABF5C0} */
    public void reparado() {
    }

    /**
	 * @return
	 * @hibernate.many-to-one column="cat_fija_id"
	 *                        class="com.siasal.documentos.business.Categoria"
	 */
    public Categoria getCategoriaFija() {
        return categoriaFija;
    }

    public void setCategoriaFija(Categoria categoriaFija) {
        this.categoriaFija = categoriaFija;
    }

    /**
	 * @return
	 * @hibernate.many-to-one column="cat_variable_id"
	 *                        class="com.siasal.documentos.business.Categoria"
	 */
    public Categoria getCategoriaVariable() {
        return categoriaVariable;
    }

    public void setCategoriaVariable(Categoria categoriaVariable) {
        this.categoriaVariable = categoriaVariable;
    }

    /**
	 * @hibernate.property column="ejem_costo" type="float"
	 * @return
	 */
    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    /**
	 * @hibernate.property column="ejem_fecha" type="date"
	 * @return
	 */
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
	 * @hibernate.id column="ejem_id" type="int"
	 *               generator-class="com.common.persistence.secuenciales.MaxIdentifierGenerator"
	 * @hibernate.generator-param name="table" value="doc_ejemplar"
	 * @hibernate.generator-param name="column" value="ejem_id"
	 * @return
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @hibernate.property column="ejem_original" type="true_false"
	 * @return
	 */
    public boolean getOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public Ejemplar() {
        super();
    }

    public Ejemplar(Categoria fija, Categoria variable, float costo, Date fecha, Integer id, boolean original, Procedencia procedencia) {
        super();
        categoriaFija = fija;
        categoriaVariable = variable;
        this.costo = costo;
        this.fecha = fecha;
        this.id = id;
        this.original = original;
        this.procedencia = procedencia;
    }

    /**
	 * @hibernate.property column="ejem_numero" type="int"
	 * @return
	 */
    public int getNumero() {
        return numero;
    }

    public void setNumero(int aNumero) {
        this.numero = aNumero;
    }

    /**
	 * Solo construimos el objeto a partir de los datos propios del objeto no de
	 * las relaciones
	 * 
	 * @param ejemplarTO
	 */
    public Ejemplar(EjemplarTO ejemplarTO) {
        setCosto(ejemplarTO.getCosto());
        setFecha(ejemplarTO.getFecha());
        setId(ejemplarTO.getId());
        setOriginal(ejemplarTO.getOriginal());
        setNumero(ejemplarTO.getNumero());
    }

    public EjemplarTO getTO() {
        EjemplarTO ejemplarTmp = new EjemplarTO(getCategoriaFija().getTO(), getCategoriaVariable().getTO(), getCosto(), getFecha(), getId(), getOriginal(), getProcedencia().getProcedenciaTO(), getNumero());
        ejemplarTmp.setEstadoPresentacion(getEstadoPresentacion());
        ejemplarTmp.setCodigo(getCodigo());
        ejemplarTmp.setEstado(getEstado());
        if (getEstado() == PRESTADO) {
            ejemplarTmp.setReservar(true);
        } else {
            ejemplarTmp.setReservar(false);
        }
        return ejemplarTmp;
    }

    /**
	 * @hibernate.many-to-one column="proc_id"
	 *                        class="com.siasal.documentos.business.Procedencia"
	 */
    public Procedencia getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(Procedencia procedencia) {
        this.procedencia = procedencia;
    }

    /**
	 * @hibernate.property column="ejem_codigo"
	 */
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void generarCodigo(String codigoTipoDocumento, String codigoDocumento) {
        int numeroTmp = 1000;
        if (getOriginal() == true) {
            numeroTmp += 100;
        }
        numeroTmp += getNumero();
        codigo = codigoTipoDocumento + codigoDocumento + Integer.toString(numeroTmp).substring(1);
    }

    /**
	 * 
	 * @hibernate.many-to-one column="DOCUM_ID"
	 *                        class="com.siasal.documentos.business.Documento"
	 *                        cascade="all"
	 * 
	 */
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    /**
	 * @hibernate.property column="ejem_estado" type="int"
	 */
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void evaluarEjemplarParaPrestamo() throws BusinessException {
        if (getEstado() == PRESTADO) {
            throw new BusinessException("Ejemplar prestado");
        } else if (getEstado() == PERDIDO) {
            throw new BusinessException("Ejemplar perdido");
        } else if (getEstado() == EN_REPARACION) {
            throw new BusinessException("Ejemplar en reparacion");
        }
    }

    /**
	 * Cadena que devuelve la presentacion del estado del ejemplar para el
	 * cliente
	 * 
	 * @return
	 */
    public String getEstadoPresentacion() {
        if (getEstado() == PERDIDO) {
            return "PERDIDO";
        }
        if (getEstado() == RESERVADO) {
            return "RESERVADO";
        } else if (getEstado() == PRESTADO) {
            return "PRESTADO";
        } else if (getEstado() == DISPONIBLE) {
            return "DISPONIBLE";
        } else if (getEstado() == NO_DISPONIBLE) {
            return "NO DISPONIBLE";
        } else if (getEstado() == EN_REPARACION) {
            return "EN REPARACION";
        } else {
            throw new InternalApplicationException("Representacion de estado de ejemplar no tiene presentacion:" + getEstado());
        }
    }

    /**
	 * para verificar si un ejemplar esta reservado
	 * @param pm
	 * @param codigoEjemplar
	 * @return
	 */
    public static boolean reservado(IPersistenceManager pm, String codigoEjemplar) {
        Map parametros = new HashMap();
        parametros.put("codigoEjemplar", codigoEjemplar);
        Reservacion reservacion = (Reservacion) pm.getUniqueNamedQuery("getReservacion", parametros);
        if (reservacion == null) {
            return false;
        }
        return true;
    }

    /**
	 * Para identificar si esta reservado para un usuario
	 * @param pm
	 * @param codigoEjemplar
	 * @param idUsuario
	 * @return
	 */
    public static boolean reservadoParaUsuario(IPersistenceManager pm, String codigoEjemplar, Integer idUsuario) {
        Map parametros = new HashMap();
        parametros.put("codigoEjemplar", codigoEjemplar);
        parametros.put("usuarioId", idUsuario);
        Reservacion reservacion = (Reservacion) pm.getUniqueNamedQuery("getReservacionPorUsuario", parametros);
        if (reservacion == null) {
            return false;
        }
        return true;
    }

    /**
	 * Metodo que nos retorma el prestamo actual del ejemplar
	 * @return
	 */
    public Prestamo buscarPrestamoActual(IPersistenceManager pm) {
        Map parametros = new HashMap();
        parametros.put("codigoEjemplar", getCodigo());
        Prestamo prestamoTmp = (Prestamo) pm.getUniqueNamedQuery("getPrestamoPorEjemplar", parametros);
        return prestamoTmp;
    }

    public Map getCatalogoEstadoPresentacion() {
        return catalogoEstadoPresentacion;
    }
}
