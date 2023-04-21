package Control;

import DAO.CiudadDAO;
import DAO.Registro_invDAO;
import DAO.UsuarioDAO;
import Entity.Canjes;
import Entity.Ciudad;
import Entity.Equipomasfull;
import Entity.Estaciones;
import Entity.Premios;
import Entity.Registro_Inv;
import Entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nico
 */
public class Test_CanjearPuntos {

    public Test_CanjearPuntos() {
    }

    private static Equipomasfull sistema = Login.sistema;

    private CanjearPuntos canje = new CanjearPuntos();

    private Canjes canjeactual = new Canjes();

    private String usuarionoexiste = "El usuario no existe";

    private String usuarioexiste = "";

    private String continu = "Continue";

    private String ciudadinvalida = "Ciudad no valida";

    private String cannodis = "La cantidad ingresada no esta disponible";

    private String puntosinsu = "Puntos Insuficientes";

    private String operacionvalida = "La accion se ha realizado satisfactoriamente";

    private static List<Premios> hacerlista(int can1, int can2, int can3, int can4, int can5, int can6, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8, boolean b9, boolean b10) {
        ArrayList<Premios> listapremios = new ArrayList();
        Premios cperro = new Premios();
        cperro.setNombre("Combo Perro");
        cperro.setCantidad(can1);
        cperro.setEstado(b1);
        cperro.setPuntos(50);
        Premios bonocombustible = new Premios();
        bonocombustible.setNombre("Bono Combustible");
        bonocombustible.setCantidad(can2);
        bonocombustible.setEstado(b2);
        bonocombustible.setPuntos(250);
        Premios bonocomsumo = new Premios();
        bonocomsumo.setNombre("Bono Consumo");
        bonocomsumo.setCantidad(can3);
        bonocomsumo.setEstado(b3);
        bonocomsumo.setPuntos(350);
        Premios descaceite = new Premios();
        descaceite.setNombre("Descuento Aceite");
        descaceite.setCantidad(can4);
        descaceite.setEstado(b4);
        descaceite.setPuntos(150);
        Premios descllantas = new Premios();
        descllantas.setNombre("Descuento Llantas");
        descllantas.setCantidad(can5);
        descllantas.setEstado(b5);
        descllantas.setPuntos(350);
        Premios recargacelular = new Premios();
        recargacelular.setNombre("Recarga Celular");
        recargacelular.setCantidad(can6);
        recargacelular.setEstado(b6);
        recargacelular.setPuntos(100);
        Premios lavadamoto = new Premios();
        lavadamoto.setNombre("Lavada Moto");
        lavadamoto.setCantidad(-99);
        lavadamoto.setEstado(b7);
        lavadamoto.setPuntos(35);
        Premios lavadacarro = new Premios();
        lavadacarro.setNombre("Lavada Carro");
        lavadacarro.setCantidad(-99);
        lavadacarro.setEstado(b8);
        lavadacarro.setPuntos(50);
        Premios lavadacamioneta = new Premios();
        lavadacamioneta.setNombre("Lavada Camioneta");
        lavadacamioneta.setCantidad(-99);
        lavadacamioneta.setEstado(b9);
        lavadacamioneta.setPuntos(80);
        Premios lavadabuseta = new Premios();
        lavadabuseta.setNombre("Lavada Buseta");
        lavadabuseta.setCantidad(-99);
        lavadabuseta.setEstado(b10);
        lavadabuseta.setPuntos(130);
        listapremios.add(cperro);
        listapremios.add(bonocombustible);
        listapremios.add(bonocomsumo);
        listapremios.add(descaceite);
        listapremios.add(descllantas);
        listapremios.add(recargacelular);
        listapremios.add(lavadamoto);
        listapremios.add(lavadacarro);
        listapremios.add(lavadacamioneta);
        listapremios.add(lavadabuseta);
        return listapremios;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Registro_invDAO regdao = Login.regdao;
        CiudadDAO ciudaddao = Login.ciudaddao;
        UsuarioDAO usuariodao = Login.usuariodao;
        List<Usuario> listausers = new ArrayList();
        List<Ciudad> listaciudad = new ArrayList();
        List<Estaciones> listaestacionesbogota = new ArrayList();
        List<Estaciones> listaestacionescali = new ArrayList();
        List<Premios> listapremios1 = new ArrayList();
        List<Premios> listapremios2 = new ArrayList();
        List<Premios> listapremios3 = new ArrayList();
        List<Premios> listapremios4 = new ArrayList();
        List<Premios> listapremios5 = new ArrayList();
        List<Premios> listapremios6 = new ArrayList();
        List<Premios> listapremios7 = new ArrayList();
        List<Premios> listapremios8 = new ArrayList();
        List<Premios> listapremios9 = new ArrayList();
        List<Premios> listapremios10 = new ArrayList();
        Estaciones bog1 = new Estaciones();
        bog1.setDireccion("Cra 7 No 51-28");
        bog1.setNomb_estacion("Texaco 8");
        bog1.setSigla("Tx8");
        listapremios1 = hacerlista(25, 98, 101, 76, 38, 83, true, true, true, true, true, true, false, false, false, false);
        bog1.setPremios((ArrayList<Premios>) listapremios1);
        System.out.print(bog1.getPremios().get(0).getNombre());
        listaestacionesbogota.add(bog1);
        Estaciones bog2 = new Estaciones();
        bog2.setDireccion("Autopista Norte km20");
        bog2.setNomb_estacion("Texaco 20");
        bog2.setSigla("Tx20");
        listapremios2 = hacerlista(0, 88, 67, 70, 91, 63, false, true, true, true, true, true, false, false, false, false);
        bog2.setPremios((ArrayList<Premios>) listapremios2);
        listaestacionesbogota.add(bog2);
        Estaciones bog3 = new Estaciones();
        bog3.setDireccion("Av 13 No 76-14");
        bog3.setNomb_estacion("Texaco 22");
        bog3.setSigla("Tx22");
        listapremios3 = hacerlista(0, 14, 78, 26, 100, 28, false, true, true, true, true, true, false, false, false, false);
        bog3.setPremios((ArrayList<Premios>) listapremios3);
        listaestacionesbogota.add(bog3);
        Estaciones bog4 = new Estaciones();
        bog4.setDireccion("Av El Dorado No 66-28");
        bog4.setNomb_estacion("Texaco 30");
        bog4.setSigla("Tx30");
        listapremios4 = hacerlista(26, 83, 98, 52, 68, 51, true, true, true, true, true, true, true, true, true, true);
        bog4.setPremios((ArrayList<Premios>) listapremios4);
        listaestacionesbogota.add(bog4);
        Estaciones bog5 = new Estaciones();
        bog5.setDireccion("Av 68 No 97-55");
        bog5.setNomb_estacion("Texaco 39");
        bog5.setSigla("Tx39");
        listapremios5 = hacerlista(27, 51, 39, 75, 58, 68, true, true, true, true, true, true, true, true, true, true);
        bog5.setPremios((ArrayList<Premios>) listapremios5);
        listaestacionesbogota.add(bog5);
        Estaciones bog6 = new Estaciones();
        bog6.setDireccion("Calle 134 No 54-11");
        bog6.setNomb_estacion("Texaco 41");
        bog6.setSigla("Tx41");
        listapremios6 = hacerlista(55, 77, 86, 49, 85, 96, true, true, true, true, true, true, false, false, false, false);
        bog6.setPremios((ArrayList<Premios>) listapremios6);
        listaestacionesbogota.add(bog6);
        Ciudad Bogota = new Ciudad();
        Bogota.setNombrecity("Bogota");
        Bogota.setNumestaciones(6);
        Bogota.setEstaciones((ArrayList<Estaciones>) listaestacionesbogota);
        Bogota.setId((long) 1);
        listaciudad.add(Bogota);
        Estaciones cal1 = new Estaciones();
        cal1.setDireccion("Av casañasgordas No 22-00");
        cal1.setNomb_estacion("Texaco 34");
        cal1.setSigla("Tx34");
        listapremios7 = hacerlista(31, 26, 44, 97, 87, 58, true, true, true, true, true, true, true, true, true, true);
        cal1.setPremios((ArrayList<Premios>) listapremios7);
        listaestacionescali.add(cal1);
        Estaciones cal2 = new Estaciones();
        cal2.setDireccion("Calle 5 No 55-00");
        cal2.setNomb_estacion("Texaco 10");
        cal2.setSigla("Tx10");
        listapremios8 = hacerlista(15, 97, 20, 25, 54, 94, true, true, true, true, true, true, false, false, false, false);
        cal2.setPremios((ArrayList<Premios>) listapremios8);
        listaestacionescali.add(cal2);
        Estaciones cal3 = new Estaciones();
        cal3.setDireccion("Av Roosevelt No 31-00");
        cal3.setNomb_estacion("Texaco 03");
        cal3.setSigla("Tx3");
        listapremios9 = hacerlista(85, 17, 70, 27, 31, 53, true, true, true, true, true, true, false, false, false, false);
        cal3.setPremios((ArrayList<Premios>) listapremios9);
        listaestacionescali.add(cal3);
        Estaciones cal4 = new Estaciones();
        cal4.setDireccion("Calle 5 No 25-20");
        cal4.setNomb_estacion("Texaco 25");
        cal4.setSigla("Tx25");
        listapremios10 = hacerlista(0, 52, 63, 72, 23, 25, false, true, true, true, true, true, true, true, true, true);
        cal4.setPremios((ArrayList<Premios>) listapremios10);
        listaestacionescali.add(cal4);
        Ciudad Cali = new Ciudad();
        Cali.setNombrecity("Cali");
        Cali.setNumestaciones(4);
        Cali.setEstaciones((ArrayList<Estaciones>) listaestacionescali);
        Cali.setId((long) 2);
        listaciudad.add(Cali);
        Usuario user = new Usuario();
        user.setNombre("Carlos Alberto ");
        user.setApellidos("Perez Ruiz");
        user.setCodigo(1L);
        user.setIdentificacion(1L);
        user.setPuntos(35000);
        user.setCanjes(new ArrayList<Canjes>());
        listausers.add(user);
        List<Registro_Inv> listaderegistro = new ArrayList();
        Registro_Inv registro = new Registro_Inv();
        Registro_Inv registro2 = new Registro_Inv();
        registro.setOperacion("Inicialización");
        registro.setNombreciudad("Cali");
        registro.setNombreestacion("Todas");
        registro.setId((long) 1);
        registro2.setOperacion("Inicializacion");
        registro2.setNombreciudad("Bogota");
        registro2.setNombreestacion("Todas");
        registro2.setId((long) 2);
        listaderegistro.add(registro);
        listaderegistro.add(registro2);
        regdao.crear(registro);
        regdao.crear(registro2);
        ciudaddao.crear(Bogota);
        ciudaddao.crear(Cali);
        usuariodao.crear(user);
        sistema.setmRegistro_Inv(listaderegistro);
        sistema.setmUsuario(listausers);
        sistema.setmCiudad(listaciudad);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void Prueba1usuarioexisteCodigo() {
        Usuario u = canje.Usuarioexiste(1L, 0);
        String b = canje.id(u);
        assertEquals(b, usuarioexiste);
    }

    @Test
    public void Prueba2usuarioexisteCedula() {
        Usuario u = canje.Usuarioexiste(1L, 1);
        String b = canje.id(u);
        assertEquals(b, usuarioexiste);
    }

    @Test
    public void Prueba3usuarionoexisteCodigo() {
        Usuario u = canje.Usuarioexiste(7L, 0);
        String b = canje.id(u);
        assertEquals(b, usuarionoexiste);
    }

    @Test
    public void Prueba4usuarionoexisteCedula() {
        Usuario u = canje.Usuarioexiste(7L, 1);
        String b = canje.id(u);
        assertEquals(b, usuarionoexiste);
    }

    @Test
    public void Prueba5premiodisponible() {
        String b = canje.premiosDispd(0, 0, 25, "Bono Combustible", "Bogota");
        assertEquals(b, continu);
    }

    @Test
    public void Prueba6ciudadnovalida() {
        String b = canje.premiosDispd(0, 0, 25, "Bono Combustible", "Bog");
        assertEquals(b, ciudadinvalida);
    }

    @Test
    public void Prueba7cantidadnodisponible() {
        String b = canje.premiosDispd(0, 0, 99, "Bono Combustible", "Bogota");
        assertEquals(b, cannodis);
    }

    @Test
    public void Prueba8puntosinsuficientes() {
        int tipoid = 1;
        long identificacion = 1L;
        Usuario u = canje.Usuarioexiste(identificacion, tipoid);
        Usuario user = new Usuario();
        if (tipoid == 1) {
            user.setIdentificacion(identificacion);
        } else {
            user.setCodigo(identificacion);
        }
        String b = canje.hacercanje(canjeactual, u, 101, canje, tipoid, user, "Bono Consumo", "Texaco 8", "Bogota", "Bono Consumo", 0, 0);
        assertEquals(b, puntosinsu);
    }

    @Test
    public void Prueba9operacionvalida() {
        int tipoid = 1;
        long identificacion = 1L;
        Usuario u = canje.Usuarioexiste(identificacion, tipoid);
        Usuario user = new Usuario();
        if (tipoid == 1) {
            user.setIdentificacion(identificacion);
        } else {
            user.setCodigo(identificacion);
        }
        String b = canje.hacercanje(canjeactual, u, 100, canje, tipoid, user, "Bono Consumo", "Texaco 8", "Bogota", "Bono Consumo", 0, 0);
        assertEquals(b, operacionvalida);
    }
}
