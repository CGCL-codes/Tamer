package ar.edu.unlp.info.diseyappweb.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import ar.edu.unlp.info.diseyappweb.controller.LicitacionController;
import ar.edu.unlp.info.diseyappweb.controller.LlamadoLicitacionController;
import ar.edu.unlp.info.diseyappweb.controller.MateriaPrimaController;
import ar.edu.unlp.info.diseyappweb.controller.PedidoController;
import ar.edu.unlp.info.diseyappweb.controller.UsuarioController;
import ar.edu.unlp.info.diseyappweb.controller.exception.ArgumentoInvalido;
import ar.edu.unlp.info.diseyappweb.controller.exception.ErrorAplicacion;
import ar.edu.unlp.info.diseyappweb.data.HibernateDAOFactory;
import ar.edu.unlp.info.diseyappweb.data.HibernateUtil;
import ar.edu.unlp.info.diseyappweb.data.dao.CompaniaDAO;
import ar.edu.unlp.info.diseyappweb.model.Compania;
import ar.edu.unlp.info.diseyappweb.model.Empresa;
import ar.edu.unlp.info.diseyappweb.model.MateriaPrima;
import ar.edu.unlp.info.diseyappweb.model.LlamadoLicitacion;
import ar.edu.unlp.info.diseyappweb.model.MateriaPrimaEntidad;
import ar.edu.unlp.info.diseyappweb.model.Pedido;
import ar.edu.unlp.info.diseyappweb.model.Proveedor;
import ar.edu.unlp.info.diseyappweb.model.Usuario;
import ar.edu.unlp.info.diseyappweb.model.exception.EntidadExistente;
import ar.edu.unlp.info.diseyappweb.model.exception.EstadoPedidoInvalido;
import ar.edu.unlp.info.diseyappweb.model.exception.ExisteLicitacionConMateriaPrima;
import ar.edu.unlp.info.diseyappweb.model.exception.ExisteLlamadoLicitacionConMateriaPrima;
import ar.edu.unlp.info.diseyappweb.model.exception.MateriaPrimaExistente;
import ar.edu.unlp.info.diseyappweb.model.exception.NickExistente;
import ar.edu.unlp.info.poo.rules.RuleConfig;
import ar.edu.unlp.info.poo.rules.excpetion.ActionAlreadyExist;
import ar.edu.unlp.info.poo.rules.excpetion.AssessorAlreadyExist;
import ar.edu.unlp.info.poo.rules.excpetion.RuleObjectAlreadyExist;
import ar.edu.unlp.info.poo.rules.excpetion.RuleException;
import junit.framework.TestCase;

public class PedidoTestCase extends TestCase {

    private PedidoController controller;

    private Compania compania;

    @Override
    protected void setUp() {
        this.config();
        this.createCompania();
        this.setController(new PedidoController());
    }

    @Override
    protected void tearDown() throws Exception {
        this.deleteCompania();
    }

    public void testRealizarPedidoMateriaPrima() {
        MateriaPrima mp1 = this.getMateriaPrima1();
        MateriaPrima mp2 = this.getMateriaPrima2();
        Calendar fecha;
        HashMap<MateriaPrima, Float> pedidos;
        this.grabarMateriasPrimas(mp1, mp2);
        this.crearLicitacion(mp1, mp2);
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 20);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        pedidos = new HashMap<MateriaPrima, Float>();
        pedidos.put(mp1, 1f);
        pedidos.put(mp2, 2f);
        try {
            this.getController().realizarPedidoMateriaPrima(null, fecha);
            fail("No se detectó el error ocurrido en el paramétro pedidos");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, null);
            fail("No se detectó el error ocurrido en el paramétro fecha");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, fecha);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        assertTrue(this.getCompania().getPedidos().size() >= 2);
    }

    public void testSolicitarPedidoMateriaPrima() {
        MateriaPrima mp1 = this.getMateriaPrima1();
        MateriaPrima mp2 = this.getMateriaPrima2();
        Calendar fecha;
        HashMap<MateriaPrima, Float> pedidos;
        Pedido pedido = null;
        Calendar fechaSolicitud;
        this.grabarMateriasPrimas(mp1, mp2);
        this.crearLicitacion(mp1, mp2);
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 20);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        pedidos = new HashMap<MateriaPrima, Float>();
        pedidos.put(mp1, 1f);
        pedidos.put(mp2, 2f);
        fechaSolicitud = Calendar.getInstance();
        fechaSolicitud.set(Calendar.DATE, 21);
        fechaSolicitud.set(Calendar.MONTH, 5);
        fechaSolicitud.set(Calendar.YEAR, 2008);
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, fecha);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        Iterator<Pedido> iter = this.getCompania().getPedidos().iterator();
        if (iter.hasNext()) pedido = iter.next();
        try {
            this.getController().solicitarPedidoMateriaPrima(null, fechaSolicitud);
            fail("No se detectó el error ocurrido en el parámetro pedido");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        }
        try {
            this.getController().solicitarPedidoMateriaPrima(pedido, null);
            fail("No se detectó el error ocurrido en el parámetro fechaCancelacion");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        }
        try {
            this.getController().solicitarPedidoMateriaPrima(pedido, fechaSolicitud);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        }
        try {
            this.getController().solicitarPedidoMateriaPrima(pedido, fechaSolicitud);
            fail("No se detectó el error en el estado del pedido");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        } catch (EstadoPedidoInvalido e) {
            assertTrue(true);
        }
    }

    public void testCancelarPedidoMateriaPrima() {
        MateriaPrima mp1 = this.getMateriaPrima1();
        MateriaPrima mp2 = this.getMateriaPrima2();
        Calendar fecha;
        HashMap<MateriaPrima, Float> pedidos;
        Pedido pedido = null;
        Calendar fechaCancelacion;
        this.grabarMateriasPrimas(mp1, mp2);
        this.crearLicitacion(mp1, mp2);
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 20);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        pedidos = new HashMap<MateriaPrima, Float>();
        pedidos.put(mp1, 1f);
        pedidos.put(mp2, 2f);
        fechaCancelacion = Calendar.getInstance();
        fechaCancelacion.set(Calendar.DATE, 21);
        fechaCancelacion.set(Calendar.MONTH, 5);
        fechaCancelacion.set(Calendar.YEAR, 2008);
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, fecha);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        Iterator<Pedido> iter = this.getCompania().getPedidos().iterator();
        if (iter.hasNext()) pedido = iter.next();
        try {
            this.getController().cancelarPedidoMateriaPrima(null, fechaCancelacion);
            fail("No se detectó el error ocurrido en el parámetro pedido");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al cancelar un pedido");
        }
        try {
            this.getController().cancelarPedidoMateriaPrima(pedido, null);
            fail("No se detectó el error ocurrido en el parámetro fechaCancelacion");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al cancelar un pedido");
        }
        try {
            this.getController().cancelarPedidoMateriaPrima(pedido, fechaCancelacion);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al cancelar un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al cancelar un pedido");
        }
        try {
            this.getController().cancelarPedidoMateriaPrima(pedido, fechaCancelacion);
            fail("No se detectó el error sobre el estado del pedido");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al cancelar un pedido");
        } catch (EstadoPedidoInvalido e) {
            assertTrue(true);
        }
    }

    public void testEmitirPedidoMateriaPrima() {
        MateriaPrima mp1 = this.getMateriaPrima1();
        MateriaPrima mp2 = this.getMateriaPrima2();
        Calendar fecha;
        HashMap<MateriaPrima, Float> pedidos;
        Pedido pedido = null;
        Calendar fechaSolicitud;
        Calendar fechaEmicion;
        this.grabarMateriasPrimas(mp1, mp2);
        this.crearLicitacion(mp1, mp2);
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 20);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        pedidos = new HashMap<MateriaPrima, Float>();
        pedidos.put(mp1, 1f);
        pedidos.put(mp2, 2f);
        fechaSolicitud = Calendar.getInstance();
        fechaSolicitud.set(Calendar.DATE, 21);
        fechaSolicitud.set(Calendar.MONTH, 5);
        fechaSolicitud.set(Calendar.YEAR, 2008);
        fechaEmicion = Calendar.getInstance();
        fechaEmicion.set(Calendar.DATE, 22);
        fechaEmicion.set(Calendar.MONTH, 5);
        fechaEmicion.set(Calendar.YEAR, 2008);
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, fecha);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        Iterator<Pedido> iter = this.getCompania().getPedidos().iterator();
        if (iter.hasNext()) pedido = iter.next();
        try {
            this.getController().solicitarPedidoMateriaPrima(pedido, fechaSolicitud);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(null, fechaSolicitud);
            fail("No se detectó el error en el estado del pedido");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, null);
            fail("No se detectó el error en el estado del fechaEmicion");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, null);
            fail("No se detectó el error en el estado del fechaEmicion");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, null);
            fail("No se detectó el error en el estado del fechaEmicion");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, fechaEmicion);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, fechaEmicion);
            fail("No se detectó el error en el estado del pedido");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        } catch (EstadoPedidoInvalido e) {
            assertTrue(true);
        }
    }

    public void testRecibirPedidoMateriaPrima() {
        MateriaPrima mp1 = this.getMateriaPrima1();
        MateriaPrima mp2 = this.getMateriaPrima2();
        Calendar fecha;
        HashMap<MateriaPrima, Float> pedidos;
        Pedido pedido = null;
        Calendar fechaSolicitud;
        Calendar fechaEmicion;
        Calendar fechaRecibir;
        this.grabarMateriasPrimas(mp1, mp2);
        this.crearLicitacion(mp1, mp2);
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 20);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        pedidos = new HashMap<MateriaPrima, Float>();
        pedidos.put(mp1, 1f);
        pedidos.put(mp2, 2f);
        fechaSolicitud = Calendar.getInstance();
        fechaSolicitud.set(Calendar.DATE, 21);
        fechaSolicitud.set(Calendar.MONTH, 5);
        fechaSolicitud.set(Calendar.YEAR, 2008);
        fechaEmicion = Calendar.getInstance();
        fechaEmicion.set(Calendar.DATE, 22);
        fechaEmicion.set(Calendar.MONTH, 5);
        fechaEmicion.set(Calendar.YEAR, 2008);
        fechaRecibir = Calendar.getInstance();
        fechaRecibir.set(Calendar.DATE, 23);
        fechaRecibir.set(Calendar.MONTH, 5);
        fechaRecibir.set(Calendar.YEAR, 2008);
        try {
            this.getController().realizarPedidoMateriaPrima(pedidos, fecha);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al realizar un pedido");
        }
        Iterator<Pedido> iter = this.getCompania().getPedidos().iterator();
        if (iter.hasNext()) pedido = iter.next();
        try {
            this.getController().solicitarPedidoMateriaPrima(pedido, fechaSolicitud);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al solicitar un pedido");
        }
        try {
            this.getController().emitirPedidoMateriaPrima(pedido, fechaEmicion);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al emitir un pedido");
        }
        try {
            this.getController().recibirPedidoMateriaPrima(null, fechaRecibir);
            fail("No se detectó un error sobre el parámetro pedido");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        }
        try {
            this.getController().recibirPedidoMateriaPrima(pedido, null);
            fail("No se detectó un error sobre el parámetro fecechaRecibir");
        } catch (ArgumentoInvalido e) {
            assertTrue(true);
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        }
        try {
            this.getController().recibirPedidoMateriaPrima(pedido, fechaRecibir);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        } catch (EstadoPedidoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al recibir un pedido");
        }
    }

    private void crearLicitacion(MateriaPrima mp1, MateriaPrima mp2) {
        LlamadoLicitacionController ctrlLlamado = new LlamadoLicitacionController();
        LicitacionController ctrlLicitacion = new LicitacionController();
        LlamadoLicitacion llamado = null;
        Calendar fecha;
        Calendar fechaFinLlamado;
        Calendar fechaInicioLicitacion;
        Calendar fechaFinLicitacion;
        HashSet<MateriaPrima> materiasPrimas;
        Proveedor prov;
        fecha = Calendar.getInstance();
        fecha.set(Calendar.DATE, 1);
        fecha.set(Calendar.MONTH, 5);
        fecha.set(Calendar.YEAR, 2008);
        fechaFinLlamado = Calendar.getInstance();
        fechaFinLlamado.set(Calendar.DATE, 2);
        fechaFinLlamado.set(Calendar.MONTH, 5);
        fechaFinLlamado.set(Calendar.YEAR, 2008);
        fechaInicioLicitacion = Calendar.getInstance();
        fechaInicioLicitacion.set(Calendar.DATE, 3);
        fechaInicioLicitacion.set(Calendar.MONTH, 5);
        fechaInicioLicitacion.set(Calendar.YEAR, 2008);
        fechaFinLicitacion = Calendar.getInstance();
        fechaFinLicitacion.set(Calendar.DATE, 24);
        fechaFinLicitacion.set(Calendar.MONTH, 5);
        fechaFinLicitacion.set(Calendar.YEAR, 2008);
        materiasPrimas = new HashSet<MateriaPrima>();
        materiasPrimas.add(mp1);
        materiasPrimas.add(mp2);
        prov = this.getProveedor();
        try {
            llamado = ctrlLlamado.nuevoLLamadoLicitacion(materiasPrimas, fecha, fechaFinLlamado, fechaInicioLicitacion, fechaFinLicitacion);
            assertTrue(true);
        } catch (ExisteLlamadoLicitacionConMateriaPrima e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo llamado a licitación");
        } catch (ExisteLicitacionConMateriaPrima e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo llamado a licitación");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo llamado a licitación");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo llamado a licitación");
        }
        try {
            ctrlLicitacion.crearLicitacion(llamado, prov);
            assertTrue(true);
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al crear una licitación");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al crear una licitación");
        }
    }

    private MateriaPrima getMateriaPrima1() {
        MateriaPrimaEntidad mp = new MateriaPrimaEntidad();
        mp.setNombre("Nombre Materia Prima 1");
        mp.setDetalle("Detalle Materia Prima 1");
        mp.setMonto(3f);
        return mp;
    }

    private MateriaPrima getMateriaPrima2() {
        MateriaPrimaEntidad mp = new MateriaPrimaEntidad();
        mp.setNombre("Nombre Materia Prima 2");
        mp.setDetalle("Detalle Materia Prima 2");
        mp.setMonto(3f);
        return mp;
    }

    private void grabarMateriasPrimas(MateriaPrima mp1, MateriaPrima mp2) {
        MateriaPrimaController ctrlMateriaPrima = new MateriaPrimaController();
        try {
            ctrlMateriaPrima.agregarMateriaPrima(mp1, 1f);
            ctrlMateriaPrima.agregarMateriaPrima(mp2, 2f);
        } catch (MateriaPrimaExistente e) {
            fail("Se detectó un error que no debió ocurrir al agregar materias primas");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al agregar materias primas");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al agregar materias primas");
        }
    }

    private Proveedor getProveedor() {
        Proveedor prov;
        Usuario us;
        Empresa emp = new Empresa();
        UsuarioController ctrlUsuario = new UsuarioController();
        emp.setRazonSocial("Razon Social");
        emp.setCuit("Cuit");
        us = new Usuario();
        us.setNick("Nick");
        us.setPassword("Pass");
        us.setEntidad(emp);
        prov = us.setRolProveedor();
        try {
            ctrlUsuario.agregarUsuario(us);
        } catch (NickExistente e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo usuario");
        } catch (EntidadExistente e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo usuario");
        } catch (ErrorAplicacion e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo usuario");
        } catch (ArgumentoInvalido e) {
            fail("Se detectó un error que no debió ocurrir al crear un nuevo usuario");
        }
        return prov;
    }

    private void createCompania() {
        Compania compania = new Compania();
        HibernateDAOFactory factory = new HibernateDAOFactory();
        CompaniaDAO dao = factory.getCompaniaDAO();
        dao.makePersistent(compania);
        this.setCompania(compania);
    }

    private void deleteCompania() {
        HibernateDAOFactory factory = new HibernateDAOFactory();
        CompaniaDAO dao = factory.getCompaniaDAO();
        dao.makeTransient(this.getCompania());
        dao.flush();
    }

    private void config() {
        Configuration config = HibernateUtil.createConfiguration();
        config = RuleConfig.config(config);
        HibernateUtil.createSessionFactory(config);
        Session session = HibernateDAOFactory.getCurrentSession();
        try {
            RuleConfig.initialize(session);
        } catch (RuleException e) {
            fail("Error inesperado: " + e.getMessage());
        } catch (AssessorAlreadyExist e) {
            fail("Error inesperado: " + e.getMessage());
        } catch (ActionAlreadyExist e) {
            fail("Error inesperado: " + e.getMessage());
        } catch (RuleObjectAlreadyExist e) {
            fail("Error inesperado: " + e.getMessage());
        }
    }

    public PedidoController getController() {
        return controller;
    }

    public void setController(PedidoController controller) {
        this.controller = controller;
    }

    public Compania getCompania() {
        return compania;
    }

    public void setCompania(Compania compania) {
        this.compania = compania;
    }
}
