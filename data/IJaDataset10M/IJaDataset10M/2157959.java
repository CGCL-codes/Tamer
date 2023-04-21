package trabalho.odonto.dao;

import java.util.ArrayList;
import trabalho.odonto.classesbasicas.Funcionario;
import trabalho.odonto.exception.FuncionarioException;
import trabalho.odonto.idao.FuncionarioIDAO;
import trabalho.odonto.util.HibernateUtil;

public class FuncionarioDAO implements FuncionarioIDAO {

    @Override
    public void cadastrarFuncionario(Funcionario f) throws FuncionarioException {
        HibernateUtil.getSession().beginTransaction();
        HibernateUtil.getSession().save(f);
        HibernateUtil.getSession().getTransaction().commit();
    }

    @Override
    public void removerFuncionario(Funcionario f) throws FuncionarioException {
        HibernateUtil.getSession().delete(f);
    }

    @Override
    public void atualizarFuncionario(Funcionario f) throws FuncionarioException {
        HibernateUtil.getSession().beginTransaction();
        HibernateUtil.getSession().saveOrUpdate(f);
        HibernateUtil.getSession().getTransaction().commit();
    }

    @Override
    public ArrayList consultarFuncionario() {
        return null;
    }
}
