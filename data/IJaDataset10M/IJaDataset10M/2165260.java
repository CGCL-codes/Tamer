package com.museum4j.modelo;

import java.util.Comparator;
import com.museum4j.modelo.*;
import org.apache.log4j.Logger;

public class IdiomaMuseoComparator implements Comparator {

    private Logger logger = Logger.getLogger(this.getClass());

    public int compare(Object o1, Object o2) {
        IdiomaMuseo i1 = (IdiomaMuseo) o1;
        IdiomaMuseo i2 = (IdiomaMuseo) o2;
        String codigoIdioma1 = i1.getIdiomaMuseoId().getIdioma().getCodigo();
        String codigoIdioma2 = i2.getIdiomaMuseoId().getIdioma().getCodigo();
        return codigoIdioma1.compareTo(codigoIdioma2);
    }

    public boolean equals(Object o) {
        return this == o;
    }
}
