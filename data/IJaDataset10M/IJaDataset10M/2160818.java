package com.psm.core.plugins.plugin.barreLaterali;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.psm.core.plugins.model.barreLaterali.model.BarraLaterale;

public class ListaBarreLateraliJson {

    public List<BarraLaterale> lista;

    public ListaBarreLateraliJson(List<BarraLaterale> lista) {
        this.lista = lista;
    }

    public List<BarraLateraleJson> getLista() {
        List<BarraLateraleJson> nuova = new ArrayList<BarraLateraleJson>();
        for (BarraLaterale item : lista) {
            nuova.add(new BarraLateraleJson(item));
        }
        return nuova;
    }

    private class BarraLateraleJson extends HashMap<String, Object> {

        public BarraLateraleJson(BarraLaterale barraLaterale) {
            this.put("posizione", barraLaterale.getPosizione().name());
            this.put("id", barraLaterale.getId());
            this.put("attivo", barraLaterale.getAttivo());
        }
    }
}
