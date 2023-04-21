package com.jdiv.samples.truco;

import java.util.ArrayList;
import com.jdiv.JProcess;

/**
 * @author  Joyal
 */
public class Jugador extends JProcess {

    public static final int JUGADOR_1 = 1;

    public static final int JUGADOR_2 = 2;

    public static final int JUGADOR_3 = 3;

    public static final int JUGADOR_4 = 4;

    /**
	 * @uml.property  name="username"
	 */
    private String username;

    /**
	 * @uml.property  name="fichas"
	 */
    private int fichas;

    /**
	 * @uml.property  name="nivel"
	 */
    private int nivel;

    /**
	 * @uml.property  name="sexo"
	 */
    private int sexo;

    /**
	 * @uml.property  name="edad"
	 */
    private int edad;

    /**
	 * @uml.property  name="amigos"
	 */
    private ArrayList<Amigos> amigos = new ArrayList<Amigos>();

    /**
	 * @uml.property  name="msgprivados"
	 */
    private ArrayList<Mensaje> msgprivados = new ArrayList<Mensaje>();

    /**
	 * @uml.property  name="cartas"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    private Carta cartas[] = new Carta[3];

    /**
	 * @uml.property  name="posicion"
	 */
    private int posicion;

    public Jugador(String username, int fichas, int nivel, int sexo, int edad, ArrayList<Amigos> amigos, ArrayList<Mensaje> msgprivados) {
        super();
        this.username = username;
        this.fichas = fichas;
        this.nivel = nivel;
        this.sexo = sexo;
        this.edad = edad;
        this.amigos = amigos;
        this.msgprivados = msgprivados;
        this.graph = Main.nombres;
    }

    @Override
    public void begin() {
        int str = username.length() * 6;
        write(0, x - (str / 2), y - 5, 0, username);
    }

    public void loop() {
    }

    /**
	 * @return
	 * @uml.property  name="username"
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username
	 * @uml.property  name="username"
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return
	 * @uml.property  name="fichas"
	 */
    public int getFichas() {
        return fichas;
    }

    /**
	 * @param fichas
	 * @uml.property  name="fichas"
	 */
    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    /**
	 * @return
	 * @uml.property  name="nivel"
	 */
    public int getNivel() {
        return nivel;
    }

    /**
	 * @param nivel
	 * @uml.property  name="nivel"
	 */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
	 * @return
	 * @uml.property  name="sexo"
	 */
    public int getSexo() {
        return sexo;
    }

    /**
	 * @param sexo
	 * @uml.property  name="sexo"
	 */
    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    /**
	 * @return
	 * @uml.property  name="edad"
	 */
    public int getEdad() {
        return edad;
    }

    /**
	 * @param edad
	 * @uml.property  name="edad"
	 */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
	 * @return
	 * @uml.property  name="amigos"
	 */
    public ArrayList<Amigos> getAmigos() {
        return amigos;
    }

    /**
	 * @param amigos
	 * @uml.property  name="amigos"
	 */
    public void setAmigos(ArrayList<Amigos> amigos) {
        this.amigos = amigos;
    }

    /**
	 * @return
	 * @uml.property  name="msgprivados"
	 */
    public ArrayList<Mensaje> getMsgprivados() {
        return msgprivados;
    }

    /**
	 * @param msgprivados
	 * @uml.property  name="msgprivados"
	 */
    public void setMsgprivados(ArrayList<Mensaje> msgprivados) {
        this.msgprivados = msgprivados;
    }

    public void addCartas(int c1, int c2, int c3) {
        cartas[0] = new Carta(c1);
        cartas[1] = new Carta(c2);
        cartas[2] = new Carta(c3);
    }

    /**
	 * @return
	 * @uml.property  name="cartas"
	 */
    public Carta[] getCartas() {
        return cartas;
    }

    /**
	 * @param posicion
	 * @uml.property  name="posicion"
	 */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    /**
	 * @return
	 * @uml.property  name="posicion"
	 */
    public int getPosicion() {
        return this.posicion;
    }
}
