package br.gov.demoiselle.escola.persistence.dao.filter;

import br.gov.component.demoiselle.hibernate.filter.Filter;
import br.gov.demoiselle.escola.bean.Funcionario;

/**
 * Classe de Filtro para os DAO de Funcionario
 * @author CETEC/CTJEE
 *
 */
public class FiltroFuncionario extends Filter {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String NOME = "nome";

    /**
	 * Construtor
	 */
    public FiltroFuncionario() {
        setClazz(Funcionario.class);
        addOrder(NOME, ASC);
    }
}
