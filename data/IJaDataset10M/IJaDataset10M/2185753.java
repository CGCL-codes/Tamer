package Classes;

import java.io.Serializable;
import javax.swing.ImageIcon;

public class Usuarios implements Serializable {

    private static final long serialVersionUID = 2L;

    private String nome = null;

    private String pass = null;

    private boolean status = false;

    private int UDPport = 0;

    private String IP = null;

    public String getIP() {
        return IP;
    }

    public void setIP(String ip) {
        IP = ip;
    }

    public Usuarios(String nome, String pass, boolean status, int pport) {
        this.nome = nome;
        this.status = status;
        this.UDPport = pport;
        this.pass = pass;
    }

    public Usuarios(String nome, String pass, boolean status) {
        this.nome = nome;
        this.pass = pass;
        this.status = status;
    }

    public Usuarios(String nome, String pass) {
        this.nome = nome;
        this.pass = pass;
        this.status = false;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getUDPport() {
        return UDPport;
    }

    public void setUDPport(int pport) {
        UDPport = pport;
    }
}
