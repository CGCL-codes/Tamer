package org.light.portlets.weather;

/**
 * 
 * @author Jianmin Liu
 **/
public class WeatherLocation {

    private String name;

    private String id;

    public WeatherLocation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
