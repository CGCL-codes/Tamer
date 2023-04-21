package servicos.impl.pessoa;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import org.jboss.annotation.ejb.LocalBinding;
import constantes.EjbNames;
import dao.pessoa.PessoaDao;
import entidades.pessoa.Pessoa;
import servicos.interfaces.pessoa.IPessoa;

@Stateless
@LocalBinding(jndiBinding = EjbNames.PESSOA)
public class PessoaImpl implements IPessoa {

    public Pessoa buscarPessoa(Long id) throws PersistenceException {
        return null;
    }

    public boolean pessoaExisteCpf(String cpf) throws PersistenceException {
        PessoaDao dao = new PessoaDao();
        Pessoa pessoa = dao.buscarPorCpf(cpf);
        return (pessoa != null);
    }

    public Long buscarIdPessoaPorCpf(String cpf) throws PersistenceException {
        PessoaDao dao = new PessoaDao();
        Pessoa pessoa = dao.buscarPorCpf(cpf);
        if (pessoa == null) {
            return null;
        } else {
            return pessoa.getId();
        }
    }
}
