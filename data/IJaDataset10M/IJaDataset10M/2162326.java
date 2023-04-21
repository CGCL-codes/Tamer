package dataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import manager.Manager;

/**
 * Classe che implementa la gestione del volume. Questa classe � stata definita
 * ma ancora non � stata implementata.
 */
@XStreamAlias("VolumeControl")
public class VolumeControl extends Control {

    private String type;

    /**
	 * Instantiates a new volume control.
	 * 
	 * @param id
	 *            Identificatore univoco dell'elemento.
	 * @param manager
	 *            Manager che controlla l'esecuzione.
	 * @param type
	 *            Il tipo definisce l'esecuzione, "+" se si vuole un elemento
	 *            per aumentare il volume, "-" per diminuire, "X" per la
	 *            funzione mute.
	 */
    public VolumeControl(int id, Manager manager, String type) {
        super(id, manager);
        this.type = type;
    }

    /**
	 * Non ancora implementata.
	 * 
	 * @see dataModel.Control#exec()
	 */
    public void exec() {
        PrintWriter out = null;
        try {
            out = super.openConnection();
            out.println("admin");
            out.flush();
            if (type.equalsIgnoreCase("-")) out.println(""); else if (type.equalsIgnoreCase("+")) out.println(""); else if (type.equalsIgnoreCase("X")) out.println("");
            out.flush();
        } catch (ConnectException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
