package br.ufmg.lcc.pcollecta.controller;

import java.util.ArrayList;
import java.util.List;
import br.ufmg.lcc.arangi.controller.bean.SelectList;
import br.ufmg.lcc.pcollecta.dto.ETC;
import br.ufmg.lcc.pcollecta.dto.Repositorio;

public class ListaRepositorio extends SelectList {

    @Override
    protected List beforeBuildDynamicList(List listData) {
        List novaLista = new ArrayList();
        for (int i = 0; i < listData.size(); i++) {
            Repositorio repositorio = (Repositorio) listData.get(i);
            if (repositorio.getTipo().equals(Repositorio.TIPO_SGBD)) {
                novaLista.add(repositorio);
            }
        }
        return novaLista;
    }
}
