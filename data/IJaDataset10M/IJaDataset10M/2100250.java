package org.mersys.sagi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.mersys.sagi.Filtro;
import org.mersys.sagi.FiltroDeBusqueda;
import org.mersys.sagi.TipoOrdenacion;
import org.mersys.sagi.modelo.Contribuyente;
import org.mersys.sagi.modelo.TipoContribuyente;
import org.mersys.sagi.modelo.UsoVehiculo;
import org.mersys.sagi.modelo.Vehiculo;
import org.mersys.utilidades.Iniciador;

/*****************************************************************************************
 * @author Franky Villadiego
 * @version 1.0.0
 *****************************************************************************************/
public class DaoVehiculo {

    private static final Logger log = Logger.getLogger(DaoVehiculo.class.getName());

    private Session ss;

    private Transaction tx;

    public DaoVehiculo() {
    }

    /** Permite obtener la Session actual o la crea si no existe alguna, tambien
     *  crea una Transaccion.
     *
     * @throws org.hibernate.HibernateException
     */
    private void Iniciar() throws HibernateException {
        ss = Iniciador.getSessionFactory().getCurrentSession();
        tx = ss.beginTransaction();
    }

    /** Realiza un rollback en caso de que ocurra una execpcion
     * en la capa de accedo a datos o JDBC.
     *
     */
    private void admExcepcion(HibernateException ex) {
        if (tx != null) {
            tx.rollback();
        }
        log.info("HAP: Error de Hibernate, detalle:" + ex);
    }

    /** Permite agregar un nuevo Vehiculo a la base de datos.
     * La Placa es transformada automaticamente a mayusculas y
     * se limpian los espacios en los bordes si los hay.
     *
     * @param obj El Objeto Vehiculo a guardar
     */
    public void Guardar(Vehiculo obj) {
        obj.ValidarAtributos();
        try {
            Iniciar();
            ss.save(obj);
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
    }

    /**
     * Permite actualizar un Vehiculo existente en la base de datos.
     * 
     * @param obj El Objeto Vehiculo a actualizar.
     */
    public void Actualizar(Vehiculo obj) {
        obj.ValidarAtributos();
        try {
            Iniciar();
            ss.update(obj);
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
    }

    /**
    * Permite eliminar un Vehiculo de la base de datos.
    * @param obj El Objeto Vehiculo a eliminar.
    */
    public void Eliminar(Vehiculo obj) {
        obj.ValidarAtributos();
        try {
            Iniciar();
            ss.delete(obj);
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
    }

    /**
     * Retorna un Objeto Vehiculo correspondiente al id del parametro, si no existe
     * retorna una null.
     * 
     * @param id
     * @return Un objeto Vehiculo
     */
    public Vehiculo getVehiculo(long id) {
        Vehiculo obj = null;
        try {
            Iniciar();
            obj = (Vehiculo) ss.get(Vehiculo.class, id);
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return obj;
    }

    /**
     * Retorna un objeto Vehiculo correspondiente al parametro pasado. La
     * Placa del vehiculo sera exactamente igual al parametro.
     *
     * @param placa
     * @return Un objeto Vehiculo.
     */
    public Vehiculo getVehiculo(String placa) {
        Vehiculo obj = new Vehiculo();
        try {
            Iniciar();
            Criteria crt = ss.createCriteria(Vehiculo.class);
            crt.add(Restrictions.like(Vehiculo.VEHICULO_PLACA, placa.toUpperCase(), MatchMode.EXACT));
            obj = (Vehiculo) crt.uniqueResult();
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return obj;
    }

    /**
     * Retorna una Lista de Vehiculos. La cantidad de Vehiculos a retornar sera la
     * establecida en el parametro cantidad, en dado caso que el parametro
     * sea cero, se retornaran todos los Vehiculos existentes.
     * @param cantidad
     * @return Un List de Vehiculos.
     */
    public List<Vehiculo> getVehiculos(int cantidad) {
        List<Vehiculo> lst = new ArrayList<Vehiculo>();
        try {
            Iniciar();
            String hql = "from Vehiculo order by id desc";
            Query q_imp = ss.createQuery(hql);
            if (cantidad >= 1) {
                q_imp.setMaxResults(cantidad);
            }
            lst = q_imp.list();
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return lst;
    }

    /**
     * <p>Retorna una Lista de Contribuyentes que van a depender de los valores que esten
     * establecidos en el Filtro.</p>
     * <p>Los valores del Filtro son: </p>
     * <ul>
     * <li>El campo de busqueda</li>
     * <li>El texto a buscar</li>
     * <li>El tipo de coincidencia</li>
     * <li>El campo a ordenar</li>
     * <li>El tipo de ordenación</li>
     * </ul>
     * 
     * @param filtro
     * @return Un Objeto List de Contribuyentes
     */
    public List<Contribuyente> getContribuyentes(Filtro filtro) {
        log.info("HAP: Llamando admContribuyentes -> getContribuyentes(filtro)");
        String campo = filtro.getCampoDeBusqueda();
        String textoBuscar = filtro.getTextoDeBusqueda().toUpperCase().trim();
        log.info("HAP: Texto a Buscar: " + textoBuscar);
        List<Contribuyente> lst = new ArrayList<Contribuyente>();
        try {
            Iniciar();
            log.info("HAP: Crando objeto Criteria");
            Criteria crt = ss.createCriteria(Contribuyente.class);
            log.info("HAP: Aplicando criterios de consulta segun el campo");
            if (campo.equals(Contribuyente.CONTRIBUYENTE_ID)) {
                Long id = Long.parseLong(textoBuscar);
                log.info("Consultando por ID");
                Contribuyente obj = (Contribuyente) ss.get(Contribuyente.class, id);
                if (obj != null) {
                    lst.add(obj);
                }
            } else {
                if (campo.equals(Contribuyente.CONTRIBUYENTE_NOMBRE)) {
                    log.info("Consultando por NOMBRE");
                    if (filtro.getCoincidencia() == null || filtro.getCoincidencia().equals("0")) {
                        log.info("Coincidencia vacia, aplicando por defecto EMPIEZA POR");
                        crt.add(Restrictions.like(Contribuyente.CONTRIBUYENTE_NOMBRE, textoBuscar, MatchMode.START));
                    } else {
                        if (filtro.getCoincidencia().equals(FiltroDeBusqueda.EMPIEZA)) {
                            log.info("Coincidencia EMPIEZA POR");
                            crt.add(Restrictions.like(Contribuyente.CONTRIBUYENTE_NOMBRE, textoBuscar, MatchMode.START));
                        } else {
                            if (filtro.getCoincidencia().equals(FiltroDeBusqueda.CONTIENE)) {
                                log.info("Coincidencia CONTIENE");
                                crt.add(Restrictions.like(Contribuyente.CONTRIBUYENTE_NOMBRE, textoBuscar, MatchMode.ANYWHERE));
                            }
                        }
                    }
                    if (filtro.getCampoOrdenado() == null || filtro.getCampoOrdenado().equals("0")) {
                        log.info("Ordenacion vacia, aplicando por defecto NOMBRE ASC");
                        crt.addOrder(Order.asc(Contribuyente.CONTRIBUYENTE_NOMBRE));
                    }
                }
                log.info("HAP: Aplicando criterios de ordenacion segun el campo y el modo");
                if (filtro.getCampoOrdenado() != null && !filtro.getCampoOrdenado().equals("0")) {
                    if (filtro.getCampoOrdenado().equals(Contribuyente.CONTRIBUYENTE_ID)) {
                        if (filtro.getTipoOrdenamiento() == TipoOrdenacion.ASCENDENTE) {
                            crt.addOrder(Order.asc(Contribuyente.CONTRIBUYENTE_ID));
                        } else {
                            crt.addOrder(Order.desc(Contribuyente.CONTRIBUYENTE_ID));
                        }
                    } else {
                        if (filtro.getCampoOrdenado().equals(Contribuyente.CONTRIBUYENTE_NOMBRE)) {
                            if (filtro.getTipoOrdenamiento() == TipoOrdenacion.ASCENDENTE) {
                                crt.addOrder(Order.asc(Contribuyente.CONTRIBUYENTE_NOMBRE));
                            } else {
                                crt.addOrder(Order.desc(Contribuyente.CONTRIBUYENTE_NOMBRE));
                            }
                        }
                    }
                }
            }
            lst = crt.list();
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        log.info("HAP: Finalizando admContribuyentes -> getContribuyentes");
        return lst;
    }

    /**
     * Retorna una Lista de Vehiculos que coincidan en cualquier parte de la placa
     * con el texto del parametro.
     *
     * @param textoDeBusqueda
     * @return List de Vehiculos
     */
    public List<Vehiculo> getVehiculos(String textoDeBusqueda) {
        List<Vehiculo> lst = new ArrayList<Vehiculo>();
        try {
            Iniciar();
            Criteria crt = ss.createCriteria(Vehiculo.class);
            crt.add(Restrictions.like(Vehiculo.VEHICULO_PLACA, textoDeBusqueda.toUpperCase(), MatchMode.ANYWHERE));
            crt.addOrder(Order.asc(Vehiculo.VEHICULO_PLACA));
            lst = crt.list();
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return lst;
    }

    /**
     * <p>Devuelve una lista de Usos de Vehiculos, estos tipos de Usos estan comprendido por los
     * que se agreguen a la base de datos, basicamente siempre son:</p>
     *
     * <ul>
     *  <li>Particular</li>
     *  <li>Colectivo</li>
     *  <li>Carga</li>
     *  <li>Taxi</li>
     * </ul>
     *
     * @return Un objeto List<UsoVehiculo>
     */
    public List<UsoVehiculo> getUsos() {
        List<UsoVehiculo> lst = new ArrayList<UsoVehiculo>();
        try {
            Iniciar();
            String hql = "from UsoVehiculo";
            Query q = ss.createQuery(hql);
            lst = q.list();
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return lst;
    }

    /**
     * Devuelve un objeto UsoVehiculo correspondiente al parametro.
     * 
     * @param id
     * @return Un objeto UsoVehiculo
     */
    public UsoVehiculo getUsoVehiculo(long id) {
        UsoVehiculo uso = new UsoVehiculo();
        try {
            Iniciar();
            uso = (UsoVehiculo) ss.get(UsoVehiculo.class, id);
            tx.commit();
        } catch (HibernateException ex) {
            admExcepcion(ex);
        }
        return uso;
    }
}
