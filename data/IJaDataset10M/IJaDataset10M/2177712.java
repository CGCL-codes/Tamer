package br.com.mcampos.controller.anoto.model;

import br.com.mcampos.dto.anoto.PenDTO;
import br.com.mcampos.util.BaseComparator;

public class PenIdComparator extends BaseComparator {

    public PenIdComparator(boolean b) {
        super(b);
    }

    public PenIdComparator() {
        super();
    }

    public int compare(Object o1, Object o2) {
        PenDTO d1 = (PenDTO) o1;
        PenDTO d2 = (PenDTO) o2;
        int nRet = d1.getId().compareToIgnoreCase(d2.getId());
        if (!isAscending()) nRet *= -1;
        return nRet;
    }
}
