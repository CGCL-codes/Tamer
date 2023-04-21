package br.cefetrn.datinf.estoque.persistencia;

import java.sql.SQLException;
import java.util.Collection;
import br.cefetrn.datinf.estoque.dominio.Categoria;

public interface CategoriaDao {

    Categoria obterPorId(int id) throws SQLException;

    int inserir(Categoria categoria) throws SQLException;

    void deletar(Categoria categoria) throws SQLException;

    Collection<Categoria> recuperarCategorias() throws SQLException;
}
