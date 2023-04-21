package br.ita.trucocearense.cliente.core;

import java.rmi.RemoteException;
import br.ita.trucocearense.common.core.interfaces.remote.JogoRemoto;
import br.ita.trucocearense.common.core.interfaces.remoteobjects.ObjCarta;

public class Partida {

    private JogoRemoto jogoRemoto;

    private Rodada rodada;

    private Usuario user;

    public Partida(JogoRemoto jogoRemoto, Usuario user) {
        this.jogoRemoto = jogoRemoto;
        this.user = user;
        this.rodada = new Rodada(jogoRemoto, user);
    }

    public Rodada getRodada() {
        return rodada;
    }

    public Carta getManilha() throws RemoteException {
        return new Carta(this.getServer().getManilha());
    }

    public Carta[] getCartasJogador(int posicao) throws RemoteException {
        Carta[] cartasCliente = new Carta[3];
        ObjCarta[] cartasServidor = this.getServer().getCartasJogador(posicao);
        for (int i = 0; i < 3; i++) cartasCliente[i] = new Carta(cartasServidor[i]);
        return cartasCliente;
    }

    public JogoRemoto getServer() {
        return jogoRemoto;
    }

    public int getEquipeVencedoraPartida() throws RemoteException {
        return this.getServer().getEquipeVencedoraPartida();
    }
}
