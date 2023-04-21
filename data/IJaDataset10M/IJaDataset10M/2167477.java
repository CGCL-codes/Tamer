package com.tomgibara.cluster.gvm.demo.city;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.JApplet;

public class CityApplet extends JApplet {

    private static final long serialVersionUID = 1077860670861265613L;

    @Override
    public void init() {
        InputStream in = null;
        try {
            in = CityApp.class.getClassLoader().getResourceAsStream("cities.txt");
            List<City> cities = City.readCities(in, 0);
            CityDemo.insert(this, cities, null, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
