package src.scenario.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import src.scenario.MiscScenarioData;

public class DifficultyWriter {

    public DifficultyWriter(Document doc, Element rootElem) {
        Element difficultyElem = doc.createElement("difficulty");
        Element elem = doc.createElement("health");
        elem.setTextContent(String.valueOf(MiscScenarioData.HEALTH_SCALE));
        difficultyElem.appendChild(elem);
        elem = doc.createElement("ability");
        elem.setTextContent(String.valueOf(MiscScenarioData.ABILITY_SCALE));
        difficultyElem.appendChild(elem);
        elem = doc.createElement("damage");
        elem.setTextContent(String.valueOf(MiscScenarioData.DAMAGE_SCALE));
        difficultyElem.appendChild(elem);
        rootElem.appendChild(difficultyElem);
    }
}
