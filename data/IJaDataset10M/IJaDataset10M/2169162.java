package wfrpv2.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;
import rpg.XMLFile.XMLDocument;
import wfrpv2.dataTypes.Character;

/**
 * @author Mathew Anderson
 * @author www.snotling.org
 *
 */
public class GeneralFunctions {

    static List<String> checkForOR(List<String> listOfStuff) {
        for (int i = 0; i < listOfStuff.size(); i++) {
            String itemElement = (String) listOfStuff.get(i);
            if (itemElement.contains(" OR ")) {
                String[] ORS = itemElement.split(" OR ");
                int keeper = rpg.dieRoller.dieRoller.main(ORS.length) - 1;
                listOfStuff.set(i, ORS[keeper]);
            }
        }
        return listOfStuff;
    }

    public static List<String> checkForRandom(List<String> listOfStuff, String listName, String race) {
        for (int i = 0; i < listOfStuff.size(); i++) {
            String itemElement = (String) listOfStuff.get(i);
            if (itemElement.equals("RANDOM")) {
                String randomValue = getRandom(listName, race);
                listOfStuff.set(i, randomValue);
            }
        }
        return listOfStuff;
    }

    private static String getRandom(String listName, String race) {
        XMLDocument XMLGeneric = new XMLDocument("race");
        String file = wfrpv2.dataTypes.Race.openRaceFile(race);
        XMLGeneric.setFileName(file);
        XMLGeneric.loadFile(false);
        HashMap<String, String> randomBits = new HashMap<String, String>();
        for (int i = 1; i < 40; i++) {
            Element bits;
            try {
                bits = XMLGeneric.getElement("/Race/Random" + listName + "[" + i + "]");
                String name = bits.getAttribute("name");
                String value = bits.getAttribute("value");
                randomBits.put(name, value);
            } catch (Exception e) {
            }
        }
        int die = rpg.dieRoller.dieRoller.main(1, 100);
        while (!randomBits.containsKey(misc.intsStrings.toString(die))) {
            die++;
            if (die == 101) {
                System.out.println("Error in Obtaining Random " + listName + ": XML badly formatted");
                break;
            }
        }
        String result = randomBits.get(misc.intsStrings.toString(die));
        return result;
    }

    public static boolean checkForOR(String value) {
        if (value.contains(" OR ")) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] getORs(String value) {
        if (value.contains(" OR ")) {
            String[] ORS = value.split(" OR ");
            return ORS;
        }
        return null;
    }

    public static Character checkForTalentBonus(Character character, Object myTalent) {
        if (myTalent.equals("Coolheaded")) {
            character.addtostat(6, 5);
        }
        if (myTalent.equals("Fleet Footed")) {
            character.addtostat(12, 1);
        }
        if (myTalent.equals("Hardy")) {
            character.addtostat(9, 1);
        }
        if (myTalent.equals("Lightning Reflexes")) {
            character.addtostat(4, 5);
        }
        if (myTalent.equals("Marksman")) {
            character.addtostat(1, 5);
        }
        if (myTalent.equals("Savvy")) {
            character.addtostat(5, 5);
        }
        if (myTalent.equals("Sauve")) {
            character.addtostat(7, 5);
        }
        if (myTalent.equals("Very Resilient")) {
            character.addtostat(3, 5);
        }
        if (myTalent.equals("Very Strong")) {
            character.addtostat(2, 5);
        }
        if (myTalent.equals("Warrior Born")) {
            character.addtostat(0, 5);
        }
        return character;
    }

    public static boolean checkForANY(String value) {
        if (value.contains("ANY")) {
            return true;
        }
        return false;
    }

    public static Character sortTalents(Character character) {
        Collections.sort(character.talents);
        return character;
    }

    public static Character sortSkills(Character character) {
        Collections.sort(character.skills);
        return character;
    }

    public static Character sortTrappings(Character character) {
        Collections.sort(character.trappings);
        return character;
    }

    public static String removeFromOR(String[] ORS, Object toRemove) {
        String newstring = "";
        for (int z = 0; z < ORS.length; z++) {
            if (z == 0) {
                if (!ORS[z].equals(toRemove)) {
                    newstring = ORS[z];
                }
            } else {
                if (!ORS[z].equals(toRemove)) {
                    if (newstring.equals("")) {
                        newstring = ORS[z];
                    } else {
                        newstring = newstring.concat(" OR " + ORS[z]);
                    }
                }
            }
        }
        return newstring;
    }
}
