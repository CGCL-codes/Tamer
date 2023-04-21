package de.tu_clausthal.in.agrausch.weit.export.ootrans.transform;

import java.util.ArrayList;
import com.foursoft.fourever.vmodell.regular.Ablaufbaustein;

public class AblaufbausteinArrayList extends ArrayList<Ablaufbaustein> {

    private static final long serialVersionUID = -6632480644170440991L;

    @Override
    public boolean contains(Object elem) {
        if (!(elem instanceof Ablaufbaustein)) return false;
        Ablaufbaustein comparee = (Ablaufbaustein) elem;
        for (Ablaufbaustein a : this) {
            if (a.getId().equals(comparee.getId())) return true;
        }
        return false;
    }
}
