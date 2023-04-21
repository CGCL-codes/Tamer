package com.gencom.fun.ogame.client;

import java.util.ArrayList;
import java.util.List;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PlanetSwitch {

    List<PlanetInfo> planetInfos = new ArrayList<PlanetInfo>();

    public void addPlanet(String planetName, String planetSwitchURL, String planetPosition) {
        PlanetInfo planet = new PlanetInfo(planetName, planetPosition, planetSwitchURL);
        planetInfos.add(planet);
    }

    public List<PlanetInfo> getPlanets() {
        return planetInfos;
    }

    public PlanetInfo getPlanet(int index) {
        return planetInfos.get(index);
    }

    public HtmlPage switchToPlanet(int index, WebClient client) {
        try {
            Thread.sleep(500);
            return client.getPage("http://s49.ogame.onet.pl" + getPlanet(index).getSwitchURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
