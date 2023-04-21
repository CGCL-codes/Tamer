package br.com.cqipac.service;

import java.util.Date;
import java.util.List;
import br.com.cqipac.dao.DaoFactory;
import br.com.cqipac.dao.RemoteDao;
import br.com.cqipac.to.Funcionario;
import br.com.cqipac.util.CQIpacUtil;

public class FuncionarioService {

    public List<Funcionario> listaFuncionario() throws Exception {
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        List<Funcionario> funcionarios = funcionarioDao.list();
        return funcionarios;
    }

    public List<Funcionario> listaFuncionario(Funcionario funcionario) throws Exception {
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        List<Funcionario> funcionarios = funcionarioDao.list(funcionario, 0);
        return funcionarios;
    }

    public Funcionario buscaFuncionario(int codFuncionario) throws Exception {
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        return (Funcionario) funcionarioDao.findById(codFuncionario);
    }

    public Funcionario buscaFuncionario(Funcionario funcionario) throws Exception {
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        funcionario = (Funcionario) funcionarioDao.find(funcionario, 0);
        return funcionario;
    }

    public void cadastraFuncionario(Funcionario funcionario, Funcionario funcionarioInclusao) throws Exception {
        funcionario.setDesSenha(CQIpacUtil.cryptografar(funcionario.getDesSenha()));
        funcionario.setFuncionario(funcionarioInclusao);
        funcionario.setDtInclusao(new Date(new Date().getTime()));
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        funcionarioDao.persist(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario, Funcionario funcionarioInclusao) throws Exception {
        funcionario.setDesSenha(CQIpacUtil.cryptografar(funcionario.getDesSenha()));
        funcionario.setFuncionario(funcionarioInclusao);
        funcionario.setDtInclusao(new Date(new Date().getTime()));
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        funcionarioDao.merge(funcionario);
    }

    public void excluiFuncionario(Funcionario funcionario) throws Exception {
        RemoteDao funcionarioDao;
        funcionarioDao = DaoFactory.getDao("FuncionarioDao");
        funcionarioDao.remove(funcionario);
    }
}
