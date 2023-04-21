package uniriotec.pm.empresa.dao.xml;

import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uniriotec.pm.empresa.dao.EmpregadoDAO;
import uniriotec.pm.empresa.modelo.Empregado;

/**
 *
 * @author Alberto
 */
public class XmlEmpregadoDAOTest {

    public XmlEmpregadoDAOTest() {
    }

    private XmlEmpregadoDAO EmpregadoDAO;

    private static final String NOME_ARQUIVO_Empregados = "empregados.xml";

    public void setUp() throws Exception {
        EmpregadoDAO = new XmlEmpregadoDAO(NOME_ARQUIVO_Empregados);
        EmpregadoDAO.clear();
        assertEquals(0, EmpregadoDAO.size());
    }

    public void testCreate() {
        try {
            Empregado e1 = new Empregado("10065320727", "Alberto Paulino Rodrigues");
            EmpregadoDAO.create(e1);
            EmpregadoDAO outroDAO = new XmlEmpregadoDAO(NOME_ARQUIVO_Empregados);
            Empregado e2 = outroDAO.searchByCpf("10065320727");
            assertAtributosEmpregado(e2, "10065320727", "Alberto Paulino Rodrigues");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public void testUpdate() {
        try {
            Empregado e1 = new Empregado("10065320727", "Alberto Paulino Rodrigues");
            EmpregadoDAO.update(e1);
            e1.setCpf("10065320727");
            e1.setNome("Gilberto Paulino Rodrigues");
            EmpregadoDAO.update(e1);
            EmpregadoDAO outroDAO = new XmlEmpregadoDAO(NOME_ARQUIVO_Empregados);
            Empregado p5 = outroDAO.searchByCpf("10065320727");
            assertAtributosEmpregado(p5, "10065320727", "Gilberto Paulino Rodrigues");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public void testRemove() {
        try {
            Empregado p1 = new Empregado(1, "Prod 1", 10.55, 200);
            EmpregadoDAO.inserirEmpregado(p1);
            EmpregadoDAO.removerEmpregado(p1);
            EmpregadoDAO outroDAO = new EmpregadoDAO(NOME_ARQUIVO_Empregados);
            Empregado p5 = outroDAO.obterEmpregado(1);
            assertNull(p5);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }

    public void testInserirVarios() {
        try {
            Empregado p1 = new Empregado(1, "Prod 1", 10.55, 200);
            EmpregadoDAO.inserirEmpregado(p1);
            Empregado p2 = new Empregado(2, "Prod 2", 20.55, 400);
            EmpregadoDAO.inserirEmpregado(p2);
            EmpregadoDAO outroDAO = new EmpregadoDAO(NOME_ARQUIVO_EmpregadoS);
            Empregado p5 = outroDAO.obterEmpregado(1);
            assertAtributosEmpregado(p5, 1, "Prod 1", 10.55, 200);
            p5 = outroDAO.obterEmpregado(2);
            assertAtributosEmpregado(p5, 2, "Prod 2", 20.55, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }

    public void testInserirDuplicado() {
        try {
            Empregado p1 = new Empregado(1, "Prod 1", 10.55, 200);
            EmpregadoDAO.inserirEmpregado(p1);
            EmpregadoDAO.inserirEmpregado(p1);
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void testAlterarInexistente() {
        try {
            Empregado p1 = new Empregado(1, "Prod 1", 10.55, 200);
            EmpregadoDAO.atualizarEmpregado(p1);
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void testExcluirInexistente() {
        try {
            Empregado p1 = new Empregado(1, "Prod 1", 10.55, 200);
            EmpregadoDAO.removerEmpregado(p1);
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void testImportacaoEmpregados() {
        try {
            Empregado p;
            EmpregadoDAO.importarEmpregados("Importacao.txt");
            EmpregadoDAO outroDAO = new EmpregadoDAO(NOME_ARQUIVO_EmpregadoS);
            p = outroDAO.obterEmpregado(100);
            assertAtributosEmpregado(p, 100, "Arroz Xpto", 20.00, 100);
            p = outroDAO.obterEmpregado(150);
            assertAtributosEmpregado(p, 150, "Feijao Xpto", 10.00, 120);
            p = outroDAO.obterEmpregado(300);
            assertAtributosEmpregado(p, 300, "Acucar", 20.00, 100);
            p = outroDAO.obterEmpregado(400);
            assertAtributosEmpregado(p, 400, "Carne Moida", 10.00, 120);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void assertAtributosEmpregado(Empregado e, String cpf, String nome) {
        assertNotNull(e);
        assertEquals(nome, e.getNome());
        assertEquals(cpf, e.getCpf());
    }
}
