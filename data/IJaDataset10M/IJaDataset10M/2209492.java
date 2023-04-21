package ru.jasatoo.diff.rp;

import java.io.Serializable;
import ru.jasatoo.AtmGOST25645Bean;
import ru.jasatoo.SputnikBean;
import ru.jnano.bal.models.planet.PlanetBean;

public class NoCenterOfGravityRPData implements Serializable {

    private PlanetBean planetBean;

    private AtmGOST25645Bean atmGOST25645Bean;

    private SputnikBean sputnikBean;

    public NoCenterOfGravityRPData(PlanetBean planetBean, AtmGOST25645Bean atmGOST25645Bean, SputnikBean sputnikBean) {
        super();
        this.planetBean = planetBean;
        this.atmGOST25645Bean = atmGOST25645Bean;
        this.sputnikBean = sputnikBean;
    }

    public PlanetBean getPlanetBean() {
        return planetBean;
    }

    public void setPlanetBean(PlanetBean planetBean) {
        this.planetBean = planetBean;
    }

    public AtmGOST25645Bean getAtmGOST25645Bean() {
        return atmGOST25645Bean;
    }

    public void setAtmGOST25645Bean(AtmGOST25645Bean atmGOST25645Bean) {
        this.atmGOST25645Bean = atmGOST25645Bean;
    }

    public SputnikBean getSputnikBean() {
        return sputnikBean;
    }

    public void setSputnikBean(SputnikBean sputnikBean) {
        this.sputnikBean = sputnikBean;
    }
}
