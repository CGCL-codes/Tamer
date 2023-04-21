package laboratorios;

import excecoes.RepositorioException;
import laboratorios.Laboratorio;

public interface IRepositorioLaboratorio {

    public void inserir(Laboratorio laboratorio) throws RepositorioException;

    public void atualizar(Laboratorio laboratorio) throws RepositorioException;

    public void remover(Laboratorio laboratorio) throws RepositorioException;

    public boolean existePorCodigo(int codigoLaboratorio) throws RepositorioException;

    public boolean existePorDescricaoCnpj(String descricao, String cnpj) throws RepositorioException;

    public Laboratorio procurarPorDescricaoCnpj(String cnpj, String descricao) throws RepositorioException;

    public Laboratorio procurarPorCodigo(int codigoLaboratorio) throws RepositorioException;

    public IIteratorLaboratorio getIterator() throws RepositorioException;

    public Laboratorio[] getLaboratorios() throws RepositorioException;
}
