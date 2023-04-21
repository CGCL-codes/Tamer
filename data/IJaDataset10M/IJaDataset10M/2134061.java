package br.ita.trucocearense.cliente.core;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import br.ita.trucocearense.common.core.interfaces.remote.JogoRemoto;
import br.ita.trucocearense.common.core.interfaces.remoteobjects.ObjCarta;

public class Carta implements ObjCarta, Serializable {

    private String codigo;

    private int valorInteiro;

    private boolean zap = false;

    private boolean espiao = false;

    public Carta(ObjCarta carta) {
        this.setCodigo(carta.getCodigo());
        this.setValorInteiro(carta.getValorInteiro());
        this.setEspiao(carta.isEspiao());
        this.setZap(carta.isZap());
    }

    public Carta(String codigo) {
        this.setCodigo(codigo);
    }

    @Override
    public String getCodigo() {
        return this.codigo;
    }

    @Override
    public int getValorInteiro() {
        return this.valorInteiro;
    }

    private void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    private void setValorInteiro(int valorInteiro) {
        this.valorInteiro = valorInteiro;
    }

    @Override
    public boolean isEspiao() {
        return this.espiao;
    }

    @Override
    public boolean isZap() {
        return this.zap;
    }

    public void setZap(boolean zap) {
        this.zap = zap;
    }

    public void setEspiao(boolean espiao) {
        this.espiao = espiao;
    }
}
