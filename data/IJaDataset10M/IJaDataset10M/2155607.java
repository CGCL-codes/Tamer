package camadaRN;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RNEliteSquadCliente {

    private static RNEliteSquadCliente rnCliente;

    private camadaSO.ClienteRMI cliente;

    private static camadaSO.IClienteServidorRMI servidor;

    private List<RNUsuario> usuariosConectados;

    private RNUsuario usuario;

    private RNEliteSquadCliente() {
    }

    public static RNEliteSquadCliente getRNCliente() {
        if (rnCliente == null) {
            rnCliente = new RNEliteSquadCliente();
        }
        return rnCliente;
    }

    public static boolean conectarServidor(String ipServ) {
        try {
            setServidor((camadaSO.IClienteServidorRMI) java.rmi.Naming.lookup("//" + ipServ + "//Servidor"));
            javax.swing.JOptionPane.showMessageDialog(null, "Conexao realizada com sucesso.");
            return true;
        } catch (MalformedURLException e) {
            apresentarMsgNaoConectou();
            e.printStackTrace();
        } catch (RemoteException e) {
            apresentarMsgNaoConectou();
            e.printStackTrace();
        } catch (NotBoundException e) {
            apresentarMsgNaoConectou();
            e.printStackTrace();
        }
        return false;
    }

    private static void apresentarMsgNaoConectou() {
        javax.swing.JOptionPane.showMessageDialog(null, "Nao foi possivel realizar a conexao.");
    }

    public boolean iniciarClienteRMI() {
        try {
            setCliente(new camadaSO.ClienteRMI());
            return getCliente().iniciarClienteRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean finalizarCliente() {
        getUsuario().setStatus(false);
        getUsuario().enviarNovoStatus();
        desconectarCliente();
        boolean error = getCliente().finalizarClienteRMI();
        setCliente(null);
        return error;
    }

    private void desconectarCliente() {
        try {
            RNEliteSquadCliente.getServidor().finalizarCliente(getUsuario().getNome());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<RNUsuario> getUsuariosConectados() {
        if (usuariosConectados == null) {
            usuariosConectados = new ArrayList<RNUsuario>();
        }
        return usuariosConectados;
    }

    public void setUsuariosConectados(List<RNUsuario> usuariosConectados) {
        this.usuariosConectados = usuariosConectados;
    }

    public camadaSO.ClienteRMI getCliente() {
        return cliente;
    }

    public void setCliente(camadaSO.ClienteRMI cliente) {
        this.cliente = cliente;
    }

    public static camadaSO.IClienteServidorRMI getServidor() {
        return servidor;
    }

    public static void setServidor(camadaSO.IClienteServidorRMI umServidor) {
        servidor = umServidor;
    }

    public RNUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(RNUsuario usuario) {
        this.usuario = usuario;
    }
}
