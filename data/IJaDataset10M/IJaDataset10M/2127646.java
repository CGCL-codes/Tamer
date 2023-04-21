package com.dosideas.dao;

import com.dosideas.domain.Pais;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 * Esta clase representa un test de componente. Los tests de componentes
 * se encargan de testear una clase en particular, y todas las dependencias
 * que la misma tiene. 
 * 
 * Esta clase hereda de una clase de test de Spring, que provee varias
 * facilidades para el testeo de componentes con Spring.
 * 
 * Este test comprueba el funcionamiento de la clase PaisBo. Esta clase
 * utiliza un Dao, el cual accede a la base de datos. Es necesario tener
 * entonces la base de datos iniciada, con el modelo de datos del workshop
 * cargado. Para iniciar la base de datos: 
 *   Ir al menu Window > Services > Databases > Java DB > Click derecho > Start server
 * 
 * Para ejecutar este test en NetBeans: 
 *    click derecho > "Run PaisDaoComponenteTest.java" (SHIFT + F6)
 * 
 * @author ldeseta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-db.xml", "/application-dao.xml", "/application-hibernate.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class PaisDaoComponenteTest {

    /** La instancia bajo test. 
     *  La anotación @Autowired hará que Spring la inyecte automáticamente 
     */
    @Autowired
    private PaisDao instance;

    /**
     * Test de buscarPaisPorId method con un id existente. 
     * El metodo debe encontrar un Pais con el id buscado.
     */
    @Test
    public void buscarPaisPorIdConIdExistente() {
        Long id = 1L;
        Pais pais = instance.buscarPaisPorId(id);
        assertNotNull(pais);
        assertEquals(id, pais.getId());
        assertEquals("Argentina", pais.getNombre());
    }

    /**
     * Test de buscarPaisPorId method con un id inexistente. 
     * El metodo debe devolver null al buscar un id que no existe.
     */
    @Test
    public void buscarPaisPorIdConIdInexistente() {
        Long id = 21L;
        Pais pais = instance.buscarPaisPorId(id);
        assertNull(pais);
    }

    /**
     * Test de buscarPaisPorId method con un id null. 
     * El metodo debe tirar una IllegalArgumentException al intengar invocar
     * al metodo con un null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void buscarPaisPorIdConIdNull() {
        Pais pais = instance.buscarPaisPorId(null);
    }
}
