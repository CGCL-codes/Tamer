package org.ibit.avanthotel.persistence.logic.data.area;

import java.util.Arrays;
import org.ibit.avanthotel.persistence.logic.data.BasicData;

/**
 * @created 12 de enero de 2005
 */
public class MunicipiInterseccionsData extends BasicData {

    private ZonaInterseccioData[] zones;

    /**
    *Construeix una nova instancia de MunicipiInterseccionsData
    *
    * @param id
    * @param nom
    */
    public MunicipiInterseccionsData(Integer id, String nom) {
        super(id, nom);
    }

    /**
    * retorna el valor per la propietat zones
    *
    * @return valor de zones
    */
    public ZonaInterseccioData[] getZones() {
        return zones;
    }

    /**
    * fitxa el valor de zones
    *
    * @param zones nou valor per zones
    */
    public void setZones(ZonaInterseccioData[] zones) {
        this.zones = zones;
    }

    /**
    * @return
    */
    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(":");
        for (int i = 0; i < zones.length; i++) {
            ZonaInterseccioData zonaInterseccioData = zones[i];
            s.append(zonaInterseccioData + " ");
        }
        return s.toString();
    }

    /**
    * @param o
    * @return
    */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MunicipiInterseccionsData)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final MunicipiInterseccionsData municipiInterseccionsData = (MunicipiInterseccionsData) o;
        if (!Arrays.equals(zones, municipiInterseccionsData.zones)) {
            return false;
        }
        return true;
    }

    /**
    * @return
    */
    public int hashCode() {
        return super.hashCode() + 37 * (zones == null ? 0 : zones.hashCode());
    }
}
