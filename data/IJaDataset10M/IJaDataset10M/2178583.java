package org.inigma.utopia.parser;

import java.util.Collection;
import org.inigma.utopia.Kingdom;
import org.inigma.utopia.Province;

public class KingdomParserData {

    private Kingdom kingdom;

    private Collection<Province> provinces;

    public KingdomParserData(Kingdom kingdom, Collection<Province> provinces) {
        this.kingdom = kingdom;
        this.provinces = provinces;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Collection<Province> getProvinces() {
        return provinces;
    }
}
