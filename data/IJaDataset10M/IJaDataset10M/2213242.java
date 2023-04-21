package uniriotec.pm.empresa.dao.postgresql;

import java.sql.PreparedStatement;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uniriotec.pm.empresa.dao.EmpregadoDao;
import uniriotec.pm.empresa.model.Empregado;
import uniriotec.pm.empresa.util.Validador;

/**
 *
 * @author Alberto
 */
public class PostgresqlEmpregadoDaoTest {

    public PostgresqlEmpregadoDaoTest() {
    }

    private PostgresqlEmpregadoDao dao;

    String url = "jdbc:postgresql://localhost:5432/empresa";

    String user = "postgres";

    String password = "labccet";

    String driver = "org.postgresql.Driver";

    @Before
    public void setUp() throws Exception {
        dao = new PostgresqlEmpregadoDao(url, user, password, driver);
        String sql = "INSERT INTO empregado (cpf, nome, sexo,data_nascimento,data_admissao,salario,data_desligamento)" + " VALUES ('10065320727', 'Alberto Paulino Rodrigues', 'Masculino','14/06/1983', '09/12/2009', 4500,null)";
        dao.criaConexao(true);
        PreparedStatement stmt = null;
        stmt = dao.getConnection().prepareStatement(sql);
        stmt.execute();
        dao.getConnection().commit();
    }

    @Test
    public void testCreate() {
        try {
            Empregado e1 = new Empregado("07106979708", "Helena", "Masculino", Validador.formataData("14/06/1983"), Validador.formataData("09/12/2009"), Double.parseDouble("3500.00"), Validador.formataData("21/12/2011"));
            dao.create(e1);
            EmpregadoDao outroDao = new PostgresqlEmpregadoDao(url, user, password, driver);
            Empregado e2 = outroDao.searchByCpf("10065320727");
            assertAtributosEmpregado(e2, "10065320727", "Alberto Paulino Rodrigues", "Masculino", Validador.formataData("14/06/1983"), Validador.formataData("09/12/2009"), Double.parseDouble("3500.00"), Validador.formataData("21/12/2011"));
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public void tearDown() throws Exception {
        dao = new PostgresqlEmpregadoDao(url, user, password, driver);
        String sql = "DELETE FROM empregado WHERE cpf ='10065320727' ";
        dao.criaConexao(true);
        PreparedStatement stmt = null;
        stmt = dao.getConnection().prepareStatement(sql);
        stmt.execute();
        dao.getConnection().commit();
        dao.getConnection().close();
    }

    private void assertAtributosEmpregado(Empregado e, String cpf, String nome, String sexo, java.sql.Date dataNascimento, java.sql.Date dataAdmissao, double salario, java.sql.Date dataDesligamento) {
        assertNotNull(e);
        assertEquals(cpf, e.getCpf());
        assertEquals(nome, e.getNome());
        assertEquals(sexo, e.getSexo());
        assertEquals(dataNascimento, e.getDataNascimento());
        assertEquals(dataAdmissao, e.getDataAdmissao());
        assertEquals(salario, e.getSalario());
        assertEquals(dataDesligamento, e.getDataDesligamento());
    }
}
