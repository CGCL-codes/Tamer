package trabalho.comum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface IEmprestimo extends Remote {

    public Date getData_fim() throws RemoteException;

    public Date getData_inicio() throws RemoteException;

    public Date getDevolvido() throws RemoteException;

    public IMembro getMembro() throws RemoteException;

    public void Terminar() throws RemoteException;

    public boolean EstaEmprestado() throws RemoteException;

    public void DefinirDuracao(int duracao) throws RemoteException;

    public String toStr() throws RemoteException;
}
