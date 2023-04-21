package com.gencom.fun.ogame.client;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.w3c.dom.NodeList;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gencom.fun.ogame.model.Resources;
import com.gencom.fun.ogame.model.buildings.BaseBuilding;

public class StateCheckExecutor {

    private NumberFormat formatter = NumberFormat.getIntegerInstance(Locale.GERMAN);

    public void checkState(WebClient client, PlanetSwitch planetSwitch) {
        for (int i = 0; i < planetSwitch.getPlanets().size(); i++) {
            planetSwitch.switchToPlanet(i, client);
            HtmlPage currentPage = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
            PlanetInfo planetInfo = planetSwitch.getPlanet(i);
            retrieveResources(planetInfo, currentPage);
            retrieveBuildings(currentPage, planetInfo);
        }
    }

    private void retrieveBuildings(HtmlPage currentPage, PlanetInfo planetInfo) {
        currentPage = switchToBuildingsPage(currentPage);
        NodeList tables = currentPage.getElementsByTagName("table");
        HtmlTable buildingsTable = (HtmlTable) tables.item(tables.getLength() - 1);
        List<HtmlElement> tds = buildingsTable.getElementsByAttribute("td", "class", "l");
        for (int j = 0; j < tds.size(); j++) {
            HtmlTableCell cell = (HtmlTableCell) tds.get(j);
            if (cell.getChildNodes().item(0).getLocalName().equals("a")) {
                HtmlAnchor anchor = (HtmlAnchor) cell.getChildNodes().item(0);
                if (!anchor.asText().startsWith("Buduj") && !(anchor.asText().startsWith("Rozbudowa"))) {
                    if (cell.asText().lastIndexOf("(Poziom") > 0) {
                        int startPos = cell.asText().indexOf('(');
                        int endPos = cell.asText().indexOf(')');
                        int level = Integer.parseInt(cell.asText().substring(startPos + 1, endPos).replaceAll("Poziom", "").trim());
                        for (BaseBuilding building : planetInfo.getBuildings().getBuildings()) {
                            if (anchor.asText().startsWith(building.getInGameNamePrefix())) {
                                building.setLevel(level);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void retrieveResources(PlanetInfo planetInfo, HtmlPage currentPage) {
        HtmlTable table = (HtmlTable) currentPage.getElementById("resources");
        HtmlTableRow resourcesRow = table.getRow(2);
        Resources resources = Resources.getResourcesValues(getResourceValue(resourcesRow, 0), getResourceValue(resourcesRow, 1), getResourceValue(resourcesRow, 2));
        planetInfo.setResources(resources);
    }

    private HtmlPage switchToBuildingsPage(HtmlPage currentPage) {
        HtmlAnchor buildingAnchor = (HtmlAnchor) currentPage.getElementByAccessKey('b');
        try {
            currentPage = buildingAnchor.click();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return currentPage;
    }

    private long getResourceValue(HtmlTableRow resourcesRow, int i) {
        try {
            long value = formatter.parse(resourcesRow.getCell(i).getTextContent()).longValue();
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
