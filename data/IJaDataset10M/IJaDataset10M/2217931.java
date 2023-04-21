package br.com.dimag.safetycar.business.cadastro;

import java.util.List;
import br.com.dimag.safetycar.data.RepositoryUF;
import br.com.dimag.safetycar.exception.DadosInsuficientesException;
import br.com.dimag.safetycar.exception.DataException;
import br.com.dimag.safetycar.model.UF;

public class CadastroUF {

    private RepositoryUF repository;

    public CadastroUF() throws Exception {
        repository = RepositoryUF.getInstance();
    }

    public void inserir(UF uf) throws DadosInsuficientesException, DataException {
        repository.insert(uf);
    }

    public void atualizar(UF uf) throws DadosInsuficientesException {
        if (uf.getId() == null) {
            throw new DadosInsuficientesException("O Objeto " + uf.getClass().getSimpleName() + " n�o possui ID.");
        }
        repository.update(uf);
    }

    public void delete(UF uf) throws DadosInsuficientesException {
        if (uf.getId() == null) {
            throw new DadosInsuficientesException("O Objeto " + uf.getClass().getSimpleName() + " n�o possui ID.");
        }
        repository.delete(uf);
    }

    public List<UF> list() {
        return repository.list();
    }
}
