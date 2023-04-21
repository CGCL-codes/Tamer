package br.ita.trucocearense.common.core.interfaces.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import br.ita.trucocearense.common.core.interfaces.remoteobjects.ObjUsuario;

public interface UsuarioRemoto extends Remote {

    public ObjUsuario fazerLogin(String login, String senha) throws RemoteException;

    public void fazerLogout(String login) throws RemoteException;

    public boolean cadastrarUsuario(String login, String senha, String apelido) throws RemoteException;

    public int obterScoreAtualizado(String login) throws RemoteException;
}
