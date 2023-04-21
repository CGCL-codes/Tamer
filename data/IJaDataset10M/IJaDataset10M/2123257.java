package biblioteca.persistencia.pojo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import biblioteca.persistencia.entidade.IEntidade;

public class Exemplar implements IEntidade<Exemplar> {

    private Long id;

    private Livro livro;

    private Integer numero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String getSqlIncluir() {
        return "INSERT INTO EXEMPLAR " + "(LIVRO, NUMERO) " + "VALUES " + "(:livro, :numero, :titulo, :ano, :editora)";
    }

    @Override
    public String getSqlAlterar() {
        return "UPDATE EXEMPLAR SET " + " LIVRO = :livro" + " NUMERO = :numero" + " WHERE ID = :id ";
    }

    @Override
    public String getSqlExcluir() {
        return "DELETE FROM EXEMPLAR WHERE ID = :id";
    }

    @Override
    public String getSqlObter() {
        return "SELECT * FROM EXEMPLAR WHERE ID = :id";
    }

    @Override
    public String getSqlListarTodos() {
        return "SELECT * FROM EXEMPLAR";
    }

    @Override
    public String getSqlListarPorCriterios() {
        return null;
    }

    @Override
    public Object[] getAtributos() {
        return new Object[] { this.getLivro().getId(), this.getNumero() };
    }

    @Override
    public void setarParametrosSQL(PreparedStatement ps, Object[] parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            if (parametros[i] instanceof Integer) {
                ps.setInt(i + 1, (Integer) parametros[i]);
            } else if (parametros[i] instanceof Long) {
                ps.setLong(i + 1, (Long) parametros[i]);
            } else if (parametros[i] instanceof String) {
                ps.setString(i + 1, (String) parametros[i]);
            } else if (parametros[i] == null) {
                ps.setNull(i + 1, Types.NULL);
            }
        }
    }

    @Override
    public Exemplar carregarObjeto(ResultSet resultSet) throws SQLException {
        Exemplar exemplar = new Exemplar();
        exemplar.setId(resultSet.getLong("ID"));
        Livro livro = new Livro();
        livro.setId(resultSet.getLong("LIVRO"));
        exemplar.setLivro(livro);
        exemplar.setNumero(resultSet.getInt("NUMERO"));
        return exemplar;
    }

    @Override
    public List<Exemplar> carregarLista(ResultSet resultSet) throws SQLException {
        List<Exemplar> retorno = new ArrayList<Exemplar>();
        while (resultSet.next()) {
            retorno.add(this.carregarObjeto(resultSet));
        }
        return retorno;
    }
}
