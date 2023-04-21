package jbced.citadellesspring.modele.quartiers.impl;

import jbced.citadellesspring.modele.quartiers.QuartierEnum;

public class PaquetForTests extends PaquetImpl {

    public QuartierEnum getQuartier(final int i) {
        return this.unPaquet.get(i);
    }
}
