package br.com.gerpro.dao;

import java.util.List;
import br.com.gerpro.model.Cronograma;
import br.com.gerpro.model.ListaFuncao;

/**
 * @author Manoel
 *
 */
public interface FacadeCronograma {

    public void salvar(Cronograma cronograma);

    public List<Cronograma> procurarPorId(int idProposta, int idItem);

    public void remover(Cronograma cronograma);

    public List<Cronograma> listar();

    public List<Cronograma> listarPorNome(String nomeCronograma);

    public Cronograma procurarPorNome(String Nome);
}
