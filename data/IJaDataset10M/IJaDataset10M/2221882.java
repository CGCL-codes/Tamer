package projeto.controller;

import java.util.List;
import projeto.model.Estabelecimento;

/**
 * Interface de filtros
 * @author Jeymisson
 *
 */
public interface Filtro {

    /**
	 * Metódo que filtra as lista de Estabelecimento
	 * @param filtro filtro desejado
	 * @param ests  lista de Estabelecimento a ser filtrada
	 * @return Lista de Estabelecimento filtrada
	 */
    public List<Estabelecimento> filtraEstabelecimentos(String filtro, List<Estabelecimento> ests);
}
