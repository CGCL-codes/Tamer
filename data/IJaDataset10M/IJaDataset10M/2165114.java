package br.com.boierado.generic.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DaoAbstratoImpl implements DaoGenerico<Object> {

    private JdbcTemplate jdbc;

    public DaoAbstratoImpl() {
    }

    public DaoAbstratoImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean inserir(Object objeto) {
        return false;
    }

    @Override
    public boolean atualizar(Object objeto) {
        return false;
    }

    @Override
    public boolean excluir(int id) {
        return false;
    }

    @Override
    public List<Object> listarTodos() {
        return null;
    }

    @Override
    public Object listarPorId(int id) {
        return null;
    }

    @Override
    public Object listaPorNome(String nome) {
        return null;
    }

    /**
	 * @return the jdbc
	 */
    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    /**
	 * @param jdbc the jdbc to set
	 */
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
}
