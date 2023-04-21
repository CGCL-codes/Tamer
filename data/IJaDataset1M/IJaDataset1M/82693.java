package br.ita.trucocearense.server.core.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import br.ita.trucocearense.common.core.interfaces.remote.JogoRemoto;
import br.ita.trucocearense.common.core.interfaces.remote.MesaRemota;
import br.ita.trucocearense.common.core.interfaces.remote.padraoobserver.RemoteObserver;
import br.ita.trucocearense.common.core.interfaces.remote.padraoobserver.RemoteSubject;
import br.ita.trucocearense.common.core.interfaces.remoteobjects.ObjUsuario;
import br.ita.trucocearense.server.core.Equipe;
import br.ita.trucocearense.server.core.Jogo;
import br.ita.trucocearense.server.core.Mesa;
import br.ita.trucocearense.server.core.remote.padraoobserver.RemoteSubjectImpl;
import br.ita.trucocearense.server.domain.Usuario;

public class MesaRemotaImpl extends RemoteSubjectImpl implements MesaRemota {

    private Mesa mesa = new Mesa();

    private JogoRemotoImpl jogoremoto;

    public MesaRemotaImpl(JogoRemotoImpl jogoremoto) throws RemoteException {
        this.jogoremoto = jogoremoto;
    }

    public String[] getUsuariosMesa() throws RemoteException {
        return this.mesa.getUsuariosMesa();
    }

    public void insereUsuarioMesa(int pos, String login, String senha, String apelido, int score) throws RemoteException {
        Usuario user = new Usuario(login, senha, apelido, score);
        this.mesa.insereUsuarioMesa(pos, user);
        notifyObservers("atualizarSalaDeJogo");
        if (this.mesa.getTotalUsuarioMesa() == 4) {
            jogoremoto.removeAllObservers();
            this.mesa.getEquipe()[0].zeraPontuacaoJogo();
            this.mesa.getEquipe()[1].zeraPontuacaoJogo();
            notifyObservers("iniciarJogo");
            jogoremoto.iniciarJogo(this.mesa);
            System.out.println("iniciarJogo");
        }
        System.out.println("Usuario " + login + "entrou na mesa");
    }

    public void removeUsuarioMesa(int pos) throws RemoteException {
        this.mesa.removeUsuarioMesa(pos);
        notifyObservers("atualizarSalaDeJogo");
    }
}
