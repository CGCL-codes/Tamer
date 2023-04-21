package com.mindbox.viajes.dto;

import java.util.ArrayList;

/**
 * Esta clase identifica a la entidad Ciudad
 * MindBox 2009
 * @author Julian
 *
 */
public class Ciudad implements Comparable<Ciudad> {

    /**
	 * Identificaci�n de la ciudad.
	 */
    private String id;

    /**
	 * Nombre de la ciudad
	 */
    private String nombre;

    /**
	 * Pais al que pertenece la ciudad
	 */
    private Pais pais;

    /**
	 * Coordenada donde est� ubicada la ciudad
	 */
    private String coordenada;

    /**
	 * Eventos que tiene asociado la ciudad.
	 */
    private ArrayList<Evento> eventos;

    /**
	 * Constructor por Omision
	 */
    public Ciudad() {
        this.eventos = new ArrayList<Evento>();
        this.coordenada = "";
        this.id = "";
        this.nombre = "";
        this.pais = new Pais();
    }

    /**
	 * @param id
	 * @param nombre
	 * @param pais
	 * @param coordenada
	 */
    public Ciudad(String id, String nombre, Pais pais, String coordenada) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.coordenada = coordenada;
        this.eventos = new ArrayList<Evento>();
    }

    /**
	 * Obtiene el valor del atributo id
	 * @return Valor de id
	 */
    public String getId() {
        return id;
    }

    /**
	 * Asigna el valor al atributo id
	 * @param id Valor de id para asignar
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Obtiene el valor del atributo nombre
	 * @return Valor de nombre
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * Asigna el valor al atributo nombre
	 * @param nombre Valor de nombre para asignar
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * Obtiene el valor del atributo pais
	 * @return Valor de pais
	 */
    public Pais getPais() {
        return pais;
    }

    /**
	 * Asigna el valor al atributo pais
	 * @param pais Valor de pais para asignar
	 */
    public void setPais(Pais pais) {
        this.pais = pais;
    }

    /**
	 * Obtiene el valor del atributo coordenada
	 * @return Valor de coordenada
	 */
    public String getCoordenada() {
        return coordenada;
    }

    /**
	 * Asigna el valor al atributo coordenada
	 * @param coordenada Valor de coordenada para asignar
	 */
    public void setCoordenada(String coordenada) {
        this.coordenada = coordenada;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    /**
	 * Obtiene el valor del atributo eventos
	 * @return Valor de eventos
	 */
    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    /**
	 * Asigna el valor al atributo eventos
	 * @param eventos Valor de eventos para asignar
	 */
    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    public int compareTo(Ciudad o) {
        return this.nombre.compareTo(o.getNombre());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ciudad) {
            Ciudad c = (Ciudad) obj;
            return this.id.equalsIgnoreCase(c.getId()) && this.pais != null && c.getPais() != null && this.pais.equals(c.getPais());
        }
        return false;
    }
}
