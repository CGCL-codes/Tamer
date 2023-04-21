package br.gov.demoiselle.rh.persistence.dao;

import java.util.List;
import br.gov.demoiselle.rh.bean.Funcionario;
import br.gov.framework.demoiselle.core.layer.IDAO;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 * 
 */
public interface IFuncionarioDAO extends IDAO<Funcionario> {

    public List<Funcionario> list();

    ;

    public Funcionario find(Funcionario arg0);

    ;

    public Funcionario buscarFuncionario(Funcionario arg0);

    public List<Funcionario> listarFuncionarios();
}
