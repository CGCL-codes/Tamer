package Pericles_Laboratorio.src;

public interface IRepositorioLabotario {

    public void inserir(Laboratorio laboratorio) throws RepositorioLaboratorioException;

    public void atualizar(Laboratorio laboratorio) throws RepositorioLaboratorioException;

    public void remover(Laboratorio laboratorio) throws RepositorioLaboratorioException;

    public boolean existe(int codigoLaboratorio) throws RepositorioLaboratorioException;

    public Laboratorio procurar(int codigoLaboratorio) throws RepositorioLaboratorioException;

    public IteratorLaboratorio getIterator();
}
