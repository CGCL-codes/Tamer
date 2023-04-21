package com.mindbox.viajes.dto;

import java.util.ArrayList;

/**
 * Esta clase identifica a la entidad TipoEvento
 * MindBox 2009
 * @author Julian
 *
 */
public class TipoEvento implements Comparable<TipoEvento> {

    /**
	 * Identificaci�n del Tipo de evento
	 */
    private String id;

    /**
	 * Nombre del Tipo de evento.
	 */
    private String nombre;

    /**
	 * Descripcion del tipo de evento
	 */
    private String descripcion;

    /**
	 * Eventos que tiene asociado el tipo de evento.
	 */
    private ArrayList<Evento> eventos;

    /**
	 * Constructor por omision 
	 */
    public TipoEvento() {
        this.eventos = new ArrayList<Evento>();
        this.id = "";
        this.nombre = "";
        this.descripcion = "";
    }

    /**
	 * @param id
	 * @param nombre
	 * @param descripcion
	 */
    public TipoEvento(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
	 * Obtiene el valor del atributo descripcion
	 * @return Valor de descripcion
	 */
    public String getDescripcion() {
        return descripcion;
    }

    /**
	 * Asigna el valor al atributo descripcion
	 * @param descripcion Valor de descripcion para asignar
	 */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @Override
    public String toString() {
        return this.nombre;
    }

    public int compareTo(TipoEvento o) {
        return this.nombre.compareTo(o.getNombre());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoEvento) {
            TipoEvento t = (TipoEvento) obj;
            return this.id.equalsIgnoreCase(t.getId());
        }
        return false;
    }
}
