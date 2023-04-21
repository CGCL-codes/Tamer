package org.ibit.avanthotel.persistence.logic.data.consulta;

import org.ibit.avanthotel.persistence.logic.data.BasicData;

/**
 * <p>
 * Aquesta classe modifica la superclasse en un sentit que s'ha de tenir en compte.<br>
 * El camp preusRegims heretats de HabitacioDisponibleData, contendran valors del tipus
 * PreuAmbDipositData (subtipus de PreuBasicData). Aixo es degut a que tots els preus guardats
 * per aquest tipus han de tenir informacio de quin diposit impliquen.
 *
 * @created 17 / novembre / 2003
 */
public class HabitacioDisponibleIDipositsData extends HabitacioDisponibleData {

    private double dipositEspecificat;

    private double dipositMaximLegal;

    private double dipositAplicat;

    /**
    *Construeix una nova instancia de HabitacioDisponibleIDipositsData
    *
    * @param tipusHabitacioStandar
    * @param tipusHabitacioPropi
    * @param preu
    * @param descripcio
    */
    public HabitacioDisponibleIDipositsData(BasicData tipusHabitacioStandar, BasicData tipusHabitacioPropi, double preu, String descripcio) {
        super(tipusHabitacioStandar, tipusHabitacioPropi, preu, descripcio);
    }

    /**
    * retorna el valor per la propietat dipositEspecificat
    *
    * @return valor de dipositEspecificat
    */
    public double getDipositEspecificat() {
        return dipositEspecificat;
    }

    /**
    * fitxa el valor de dipositEspecificat
    *
    * @param dipositEspecificat nou valor per dipositEspecificat
    */
    public void setDipositEspecificat(double dipositEspecificat) {
        this.dipositEspecificat = dipositEspecificat;
    }

    /**
    * retorna el valor per la propietat dipositMaximLegal
    *
    * @return valor de dipositMaximLegal
    */
    public double getDipositMaximLegal() {
        return dipositMaximLegal;
    }

    /**
    * fitxa el valor de dipositMaximLegal
    *
    * @param dipositMaximLegal nou valor per dipositMaximLegal
    */
    public void setDipositMaximLegal(double dipositMaximLegal) {
        this.dipositMaximLegal = dipositMaximLegal;
    }

    /**
    * retorna el valor per la propietat dipositAplicat
    *
    * @return valor de dipositAplicat
    */
    public double getDipositAplicat() {
        return dipositAplicat;
    }

    /**
    * fitxa el valor de dipositAplicat
    *
    * @param dipositAplicat nou valor per dipositAplicat
    */
    public void setDipositAplicat(double dipositAplicat) {
        this.dipositAplicat = dipositAplicat;
    }
}
