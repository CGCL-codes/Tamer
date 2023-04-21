package br.gov.sample.demoiselle.escola.business;

import java.util.List;
import br.gov.framework.demoiselle.core.layer.IBusinessController;
import br.gov.sample.demoiselle.escola.bean.Funcionario;

public interface IFuncionarioBC extends IBusinessController {

    public void inserir(Funcionario funcionario);

    public void remover(Funcionario funcionario);

    public void alterar(Funcionario funcionario);

    public List<Funcionario> listar();

    public Funcionario buscar(Funcionario funcionario);

    public List<Funcionario> filtrar(Funcionario funcionario);
}
