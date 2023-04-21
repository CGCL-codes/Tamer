package br.furb.furbot.suporte;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Adilson Vahldick
 *
 */
public class GrupoObjetos {

    public GrupoObjetos() {
        elementos = new ArrayList<ElementoExercicio>();
        grupos = new ArrayList<GrupoObjetos>();
    }

    public void addElemento(ElementoExercicio elemento) {
        if (elemento.getId() == null) elemento.setId(String.valueOf(elemento.hashCode()));
        elementos.add(elemento);
    }

    public List<ElementoExercicio> getElementos() {
        return elementos;
    }

    public void addGrupo(GrupoObjetos grupo) {
        grupos.add(grupo);
    }

    public List<GrupoObjetos> getGruposObjetos() {
        return grupos;
    }

    private List<ElementoExercicio> elementos;

    private List<GrupoObjetos> grupos;
}
