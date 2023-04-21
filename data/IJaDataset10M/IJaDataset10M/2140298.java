package de.sambalmueslie.ds.helptool.parser;

import de.sambalmueslie.ds.helptool.DSHelpTool;
import de.sambalmueslie.ds.helptool.misc.DSVillage;
import de.sambalmueslie.ds.helptool.model.VillagesModel;

public class SpyReportsParser {

    public SpyReportsParser() {
        model = DSHelpTool.getModel().getEnemyVillagesModel();
    }

    public VillagesModel getModel() {
        return model;
    }

    public void parseText(final String text) {
        isAttackerParsing = false;
        isDefenderParsing = false;
        String[] lines = text.split("\n");
        boolean validArea = false;
        for (String line : lines) {
            if (validArea) {
                parseLine(line);
            }
            if (line.startsWith("Gesendet")) {
                validArea = true;
            }
        }
        if (defender != null) {
            model.addVillage(defender);
        }
    }

    private void parseCasulties(final String line) {
    }

    private void parseLine(final String line) {
        if (line.startsWith("Moral:")) {
            parseMoral(line);
        } else if (line.startsWith("Angreifer:")) {
            parsePlayer(line);
            isAttackerParsing = true;
        } else if (line.startsWith("Verteidiger:")) {
            isAttackerParsing = false;
            isDefenderParsing = true;
            parsePlayer(line);
        } else if (line.startsWith("Dorf:")) {
            parseVillage(line);
        } else if (line.startsWith("Anzahl:")) {
            parseUnits(line);
        } else if (line.startsWith("Verluste:")) {
            parseCasulties(line);
        }
    }

    private void parseMoral(final String line) {
    }

    private void parsePlayer(final String line) {
    }

    private void parseUnits(final String line) {
    }

    private void parseVillage(final String line) {
        if (isAttackerParsing) {
        } else if (isDefenderParsing) {
            final String villageName = line.substring(0, line.indexOf("("));
            final String villagePos = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            final String posx = villagePos.substring(0, 3);
            final String posy = villagePos.substring(4, 7);
            defender = new DSVillage(villageName, Integer.parseInt(posx), Integer.parseInt(posy));
        }
    }

    private DSVillage defender = null;

    private boolean isAttackerParsing = false;

    private boolean isDefenderParsing = false;

    private VillagesModel model = null;
}
