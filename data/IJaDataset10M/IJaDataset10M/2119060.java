package vf2.so;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import vf1.so.VF1_SO_IClienteRMI;
import vf2.rn.*;
import vf2.ip.*;

/**
 * Class VF2_SO_BFTServidor
 */
public class VF2_SO_BFTServidor {

    private static HashMap<String, VF2_RN_Jogador> jogadores = new HashMap<String, VF2_RN_Jogador>();

    private static final VF2_SO_BFTServidor servidor = new VF2_SO_BFTServidor();

    private VF2_SO_ServidorRMI servidorRMI;

    private String ip;

    private VF2_RN_Espera espera = new VF2_RN_Espera();

    private VF2_SO_BFTServidor() {
        try {
            setServidorRMI(new VF2_SO_ServidorRMI());
        } catch (java.rmi.RemoteException ex) {
        }
    }

    ;

    private void setIp(String newVar) {
        ip = newVar;
    }

    private String getIp() {
        return ip;
    }

    public void conectarJogador(String usuario, String ip) {
        VF2_RN_Jogador jogador = new VF2_RN_Jogador();
        jogador.setNome(usuario);
        jogador.setIp(ip);
        getJogadores().put(ip, jogador);
        getEspera().inserirNaListaEspera(jogador);
        getEspera().enviarLista();
    }

    public void desconectarJogador(String usuario) {
    }

    public boolean iniciarServidor() throws UnknownHostException, MalformedURLException, RemoteException {
        return getServidorRMI().iniciarServidorRMI();
    }

    public boolean encerrarServidor() {
        boolean error = getServidorRMI().finalizarServidorRMI();
        setServidorRMI(null);
        return error;
    }

    public static VF2_SO_BFTServidor getServidor() {
        return servidor;
    }

    public VF2_SO_ServidorRMI getServidorRMI() {
        return servidorRMI;
    }

    public void setServidorRMI(VF2_SO_ServidorRMI servidorRMI) {
        this.servidorRMI = servidorRMI;
    }

    public VF2_RN_Espera getEspera() {
        return espera;
    }

    public void setEspera(VF2_RN_Espera espera) {
        this.espera = espera;
    }

    protected static HashMap<String, VF2_RN_Jogador> getJogadores() {
        return jogadores;
    }

    protected static void setJogadores(HashMap<String, VF2_RN_Jogador> jogadores) {
        VF2_SO_BFTServidor.jogadores = jogadores;
    }
}
