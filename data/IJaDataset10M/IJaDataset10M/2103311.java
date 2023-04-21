package net.tasecurity.taslib.web.chart.entity;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * Dataset to be represented.
 */
public class DummyDataset implements Dataset {

    /**
     * Returns asked axis of values.
     * @param index index of axis to return, starting from 0.
     */
    public List<Pair> getSeries(final int index) {
        final List<Pair> dummyList = new ArrayList<Pair>();
        dummyList.add(new Pair(46, "google"));
        dummyList.add(new Pair(20, "elpais.es"));
        dummyList.add(new Pair(15, "yahoo"));
        dummyList.add(new Pair(10, "hotmail"));
        dummyList.add(new Pair(10, "desconocido"));
        dummyList.add(new Pair(3, "intereconomia"));
        dummyList.add(new Pair(1, "elmundo"));
        dummyList.add(new Pair(1, "cajamadrid"));
        dummyList.add(new Pair(1, "telefonica"));
        return dummyList;
    }

    /**
     * Gets the name of the values for an axis.
     * @param index index of axis to get the name, starting from 0.
     */
    public String getSeriesName(final int index) {
        return "Valores de Prueba";
    }
}
