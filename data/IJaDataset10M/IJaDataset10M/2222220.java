package farmacia.repositorio;

import excecoes.RepositorioException;
import farmacia.entidades.Farmacia;

public interface IRepositoroFarmacia {

    public void inserir(Farmacia farmacia) throws RepositorioException;

    public void remover(Farmacia farmacia) throws RepositorioException;

    public Farmacia procurar(String cnpj) throws RepositorioException;

    public void atualizar(Farmacia farmacia) throws RepositorioException;

    public boolean existe(Farmacia farmacia) throws RepositorioException;
}
