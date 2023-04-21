package br.ita.trucocearense.server.core;

import java.rmi.RemoteException;
import br.ita.trucocearense.server.domain.Usuario;

public class Mesa {

    private Equipe[] equipe = new Equipe[2];

    private int totalUsuarioMesa = 0;

    public Mesa() {
        equipe[0] = new Equipe();
        equipe[1] = new Equipe();
    }

    public int getTotalUsuarioMesa() {
        return totalUsuarioMesa;
    }

    public void setTotalUsuarioMesa(int totalUsuarioMesa) {
        this.totalUsuarioMesa = totalUsuarioMesa;
    }

    public Equipe[] getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe[] equipe) {
        this.equipe = equipe;
    }

    public void incTotalUsuarioMesa() {
        this.totalUsuarioMesa++;
    }

    public void decTotalUsuarioMesa() {
        this.totalUsuarioMesa--;
    }

    public String[] getUsuariosMesa() {
        String[] usuarios = new String[4];
        if (this.equipe[0].getMembro()[0] != null) usuarios[0] = this.equipe[0].getMembro()[0].getApelido();
        if (this.equipe[0].getMembro()[1] != null) usuarios[2] = this.equipe[0].getMembro()[1].getApelido();
        if (this.equipe[1].getMembro()[0] != null) usuarios[1] = this.equipe[1].getMembro()[0].getApelido();
        if (this.equipe[1].getMembro()[1] != null) usuarios[3] = this.equipe[1].getMembro()[1].getApelido();
        return usuarios;
    }

    public void insereUsuarioMesa(int posicao, Usuario user) {
        this.setUserPosicao(posicao, user);
        this.incTotalUsuarioMesa();
    }

    public void removeUsuarioMesa(int posicao) {
        this.setUserPosicao(posicao, null);
        this.decTotalUsuarioMesa();
    }

    public void removeUsuariosMesa() {
        for (int i = 0; i < 4; i++) this.removeUsuarioMesa(i);
    }

    public Usuario getUserPosicao(int posicao) {
        return this.equipe[posicao % 2].getMembro()[(posicao > 1 ? 1 : 0)];
    }

    public void setUserPosicao(int posicao, Usuario user) {
        this.equipe[posicao % 2].getMembro()[(posicao > 1 ? 1 : 0)] = user;
    }
}
