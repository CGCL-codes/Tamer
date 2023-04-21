package trabalho.comum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IBiblioteca extends Remote {

    public Vector<ICatalogo> getCatalogos() throws RemoteException;

    public Vector<ICriterio> getCriterios() throws RemoteException;

    public int getDias_emprestimo() throws RemoteException;

    public Vector<IMembro> getMembros() throws RemoteException;

    public String getNome() throws RemoteException;

    public Vector<ITitulo> getTitulos() throws RemoteException;

    public ITitulo AdicionarTitulo(String descricao, ICatalogo catalogo) throws RemoteException;

    public Vector<ITitulo> PesquisarTitulos(String inquerito) throws RemoteException;

    public Vector<ITitulo> PesquisarTitulosAgrupamento(String inquerito) throws RemoteException;

    public boolean ApagarTitulo(ITitulo titulo) throws RemoteException;

    public IMembro AdicionarMembro(String nome, String morada, int telefone) throws RemoteException;

    public boolean ApagarMembro(IMembro membro) throws RemoteException;

    public Vector<IMembro> PesquisarMembros(String nome) throws RemoteException;

    public Vector<IMembro> PesquisarMembrosAgrupamento(String nome) throws RemoteException;

    public ICatalogo AdicionarCatalogo(String nome) throws RemoteException;

    public boolean ApagarCatalogo(ICatalogo catalogo) throws RemoteException;

    public ICriterio AdicionarCriterio(String nome) throws RemoteException;

    public boolean ApagarCriterio(ICriterio criterio) throws RemoteException;

    public void ActualizarNotificacoes(IMembro membro) throws RemoteException;

    public void AdicionarDadosTeste1() throws RemoteException;

    public void AdicionarDadosTeste2() throws RemoteException;

    public boolean equals(IBiblioteca biblioteca) throws RemoteException;
}
