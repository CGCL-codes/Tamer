package org.easyrec.model.web.flot;

import java.util.List;
import java.util.Vector;

public class FlotDataSet {

    List<FlotSeries> data = new Vector<FlotSeries>();

    public void add(FlotSeries series) {
        data.add(series);
    }

    public List<FlotSeries> getData() {
        return data;
    }

    /**
     * Creates a JSON formated return String containing the plot Data
     *
     * @return
     */
    @Override
    public String toString() {
        String returnString = "[";
        for (int i = 0; i < data.size(); i++) {
            FlotSeries flotSerie = data.get(i);
            returnString += flotSerie.toString();
            if (i != data.size() - 1) {
                returnString += ",";
            }
        }
        returnString += "]";
        return returnString;
    }
}
