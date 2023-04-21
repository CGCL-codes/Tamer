package br.gov.sample.demoiselle.escola.persistence.dao.implementation;

import java.util.List;
import br.gov.component.demoiselle.hibernate.filter.dao.HibernateFilterGenericDAO;
import br.gov.framework.demoiselle.persistence.hibernate.HibernateUtil;
import br.gov.sample.demoiselle.escola.bean.Funcionario;
import br.gov.sample.demoiselle.escola.persistence.dao.IFuncionarioDAO;
import br.gov.sample.demoiselle.escola.persistence.dao.filter.FiltroFuncionario;

/**
 * Implementação do DAO Professor, por meio do Hibernate
 * @author CETEC/CTJEE
 */
public class FuncionarioDAOHibernate extends HibernateFilterGenericDAO<Funcionario> implements IFuncionarioDAO {

    /**
	 * Procurar um funcionário a partir do campo ID
	 */
    public Funcionario buscar(Funcionario funcionario) {
        HibernateUtil.getInstance().getSession().flush();
        FiltroFuncionario f = new FiltroFuncionario();
        f.addEquals(FiltroFuncionario.ID, funcionario.getId());
        List<Funcionario> retorno = find(f);
        if (retorno != null && retorno.size() > 0) return retorno.get(0);
        return null;
    }

    /**
	 * Filtrar a lista de funcionários a partir de um POJO
	 */
    public List<Funcionario> filtrar(Funcionario pojo) {
        HibernateUtil.getInstance().getSession().flush();
        return findByExample(pojo);
    }

    /**
	 * Lista todos os funcionários
	 */
    public List<Funcionario> listar() {
        HibernateUtil.getInstance().getSession().flush();
        return find(new FiltroFuncionario());
    }
}
