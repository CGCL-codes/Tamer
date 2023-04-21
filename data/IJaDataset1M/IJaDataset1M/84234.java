package com.psm.core.plugins.model.gruppi.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import com.psm.core.interfaces.IGruppiNonBloccatiService;
import com.psm.core.plugins.model.gruppi.model.Gruppo;

public class GruppiNonBloccatiService implements IGruppiNonBloccatiService {

    private GruppoService gruppoService;

    public GruppoService getGruppoService() {
        return gruppoService;
    }

    @Required
    public void setGruppoService(GruppoService gruppoService) {
        this.gruppoService = gruppoService;
    }

    public List<Integer> getIdGruppiNonBloccatiByUrl(String url, Integer idAccount) {
        List<String> pagine = new ArrayList<String>();
        String path[] = url.split("/");
        String prev = "/";
        for (int i = 1; i < path.length; i++) {
            String stringa = prev + path[i];
            pagine.add(stringa);
            prev = stringa + "/";
        }
        List<Integer> nonBloccati = new ArrayList<Integer>();
        for (Gruppo gruppo : getGruppoService().getGruppiNonBloccati(pagine, idAccount)) {
            nonBloccati.add(gruppo.getId());
        }
        return nonBloccati;
    }
}
