package trabalho.comum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

public interface IMembro extends Remote {

    public String getNome() throws RemoteException;

    public int getTelefone() throws RemoteException;

    public Date getData_termino_mensalidade() throws RemoteException;

    public String getMorada() throws RemoteException;

    public Vector<INotificacao> getNotificacoes() throws RemoteException;

    public IBiblioteca getBiblioteca() throws RemoteException;

    public void PagarMensalidade() throws RemoteException;

    public boolean MensalidadePaga() throws RemoteException;

    public void AdicionarNotificacao(int tipo, String descricao) throws RemoteException;

    public void ApagarNotificacao(INotificacao notificacao) throws RemoteException;

    public void ApagarNotificacao(int tipo) throws RemoteException;

    public void ApagarNotificacao(int tipo, String descricao) throws RemoteException;

    public String toStr() throws RemoteException;

    public boolean equals(IMembro membro) throws RemoteException;
}
