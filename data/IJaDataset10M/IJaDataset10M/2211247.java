package br.com.webcrm.service;

import java.util.List;
import br.com.webcrm.model.Questionario;

public interface QuestionarioService extends BaseService {

    public Questionario persistir(Questionario questionario) throws Exception;

    public Questionario gravar(Questionario questionario) throws Exception;

    public Questionario alterar(Questionario questionario) throws Exception;

    public void excluir(Questionario questionario) throws Exception;

    public List<Questionario> consultarTodos() throws Exception;
}
