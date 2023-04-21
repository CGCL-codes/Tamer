package com.dosideas.business.impl;

import com.dosideas.dao.PaisDao;
import com.dosideas.domain.Pais;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Esta clase representa un test de unitario. Los tests de unitarios
 * se encargan de testear una clase en forma aislada al resto de sus
 * dependencias. Para lograr esto se "simulan" las dependencias con
 * objetos falsos que funcionan según nuestras necesidades (Mock Objects). 
 * 
 * Los tests unitarios funcionan en forma aislada sin ningún otro 
 * requerimiento de entorno, ya que todas las dependencias y entorno
 * son simulados. 
 * 
 * Para ejecutar este test en NetBeans: 
 *    click derecho > "Run PaisBoImplTest.java" (SHIFT + F6)
 * 
 * @author ldeseta
 */
public class PaisBoImplTest {

    /** La instancia bajo test */
    private PaisBoImpl instance;

    /** El mock del Dao, que inyectaremos a la instancia bajo test */
    private PaisDao paisDaoMock;

    /** 
     * Este método se ejecuta antes de iniciar la ejecución de cada
     * método de test. Aquí se realizan preparaciones generales para
     * la ejecución del test. 
     * En este caso, se crea un nuevo mock y una nueva instancia de 
     * PaisBo, y se inyecta este Dao a PaisBo. De esta manera, 
     * nos aseguramos que empezamos cada test con un mock "limpio". 
     */
    @Before
    public void setUp() {
        paisDaoMock = mock(PaisDao.class);
        instance = new PaisBoImpl();
        instance.setPaisDao(paisDaoMock);
    }

    /**
     * Este método se ejecuta luego de finalizar la ejecucación
     * de cada método de test.
     * Cualquier tarea de limpieza se ubica aquí.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of buscarPaisPorId method, of class PaisBoImpl.
     * 
     * Este test se encargará de preparar el Mock del Dao para
     * esperar que se lo invoque con el mismo ID que se invoca a PaisBo,
     * y luego devuelva un objeto de Pais.
     * 
     * Por último, se verifica que al invocar a paisBo se devuelva la
     * misma instancia que devolvió el Dao.
     * 
     */
    @Test
    public void testBuscarPaisPorIdConIdExistente() {
        Long id = 1L;
        Pais pais = new Pais();
        pais.setId(id);
        pais.setNombre("Pais Mock");
        doReturn(pais).when(paisDaoMock).buscarPaisPorId(id);
        Pais result = instance.buscarPaisPorId(id);
        assertEquals(pais, result);
    }

    /**
     * Test de buscarPaisPorId method con un id existente. 
     * El metodo debe encontrar un Pais con el id buscado.
     */
    @Test
    public void buscarPaisPorIdConIdInexistente() {
        Long id = 1L;
        doReturn(null).when(paisDaoMock).buscarPaisPorId(id);
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
        doThrow(new IllegalArgumentException()).when(paisDaoMock).buscarPaisPorId(null);
        Pais pais = instance.buscarPaisPorId(null);
    }
}
