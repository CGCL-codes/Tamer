package junit.test;

import static org.junit.Assert.*;
import java.util.Date;
import negocio.cv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.DBManager;
import persistencia.cv.DAOPersona;

public class DAOPersonaTest {

    DAOPersona dao;

    @Before
    public void setUp() throws Exception {
        dao = new DAOPersona(new DBManager());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDAOPersona() {
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsert() {
        dao.insert(new Persona("Rodrigo", "Perez", new TipoDocumento(1, "DNI", null), 32685145, new Date(1981, 8, 5), new Nacionalidad(2, "Argentina"), new Long("3514587458"), 351, 4565585, "rodrg@hotmail.com", new EstadoCivil(2, "Soltero"), "Masculino", 2032685145, 0, 0, 0, false, "Considerar", new Domicilio(new Calle(3, "Velez"), 455, "San Jos�", "Oro y Parana", 13, "C", "5000", new Provincia(2, "Cordoba", null), new Localidad(2, "Capital", null), new Barrio(4, "Nueva Cordoba"))));
    }
}
