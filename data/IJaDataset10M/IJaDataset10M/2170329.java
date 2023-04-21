package camadaRN;

import java.rmi.RemoteException;
import camadaIU.BotaoPeca;

public class RNPartida {

    private RNTabuleiro tabuleiro;

    private RNUsuario usuario;

    private int tipo;

    public RNPartida(RNUsuario usuario, int tipoTabuleiro) {
        setTabuleiro(new RNTabuleiro(this));
        setTipo(tipoTabuleiro);
        setUsuario(usuario);
    }

    public void criaPeca(int countY, int countX, int forca) {
        RNPeca pecaCriada = new RNPeca(forca);
        if (countY > 5) pecaCriada.setDono(this.getUsuario());
        this.getTabuleiro().setPeca(countY, countX, pecaCriada);
        this.getTabuleiro().setForca(countY, countX, forca);
    }

    public void gravarPosicaoPecas(BotaoPeca casas[][]) {
        for (int countY = 0; countY < 10; countY++) {
            for (int countX = 0; countX < 10; countX++) {
                criaPeca(countY, countX, casas[countY][countX].getForca());
            }
        }
        try {
            RNEliteSquadCliente.getServidor().gravarPecasRemoto(this.getUsuario().getNome(), getTabuleiro().getForcas());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void atualizarTabuleiroRemoto(int xpeca1, int ypeca1, int xpeca2, int ypeca2) {
        try {
            RNEliteSquadCliente.getServidor().atualizarTabuleiroRemoto(this.getUsuario().getNome(), xpeca1, ypeca1, xpeca2, ypeca2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void desistirPartida() {
        try {
            RNEliteSquadCliente.getServidor().desistirPartida(this.getUsuario().getNome());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void realizarCombate(int xpeca1, int ypeca1, int xpeca2, int ypeca2) throws RemoteException {
        RNEliteSquadCliente.getServidor().realizarCombate(this.getUsuario().getNome(), xpeca1, ypeca1, xpeca2, ypeca2);
    }

    public RNUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(RNUsuario usuario) {
        this.usuario = usuario;
    }

    public RNTabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(RNTabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
