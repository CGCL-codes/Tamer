package de.zeroboyz.ggms.model.combatant;

import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import de.zeroboyz.ggms.logging.LoggerFactory;

/**
 * This class takes a String (e.g. <i>2d+4 (2) p</i>) and returns a
 * {@link WeaponDamage}-object.
 * @author jbierwagen
 */
public class WeaponDamageParser {

    /**
	 * Parses a String and returns a {@link WeaponDamage}-object
	 * @param damage the String to parse.
	 * @return A WeaponDamage-object.
	 */
    public static WeaponDamage parseString(String damage) {
        Logger log = LoggerFactory.getLogger(WeaponDamageParser.class);
        log.entering(WeaponDamageParser.class.toString(), "parseString", damage);
        String regEx = "(?m)^([1-9]+[0-9]*)[d]((?:[-+]\\d+)?)(?:\\((\\d+(?:\\.\\d*)?)\\))?((aff|burn|cr|cut|fat|imp|p(\\+{0,2}|-{1})|spec|tox)?)$";
        damage = damage.replaceAll("\\s", "");
        damage = damage.toLowerCase();
        String dice = "";
        String bonus = "";
        String armordivisor = "";
        String damagetype = "";
        WeaponDamage d = new WeaponDamage();
        try {
            Boolean FoundMatch = damage.matches(regEx);
            if (FoundMatch) {
                dice = damage.replaceAll(regEx, "$1").trim();
                bonus = damage.replaceAll(regEx, "$2").trim();
                armordivisor = damage.replaceAll(regEx, "$3").trim();
                damagetype = damage.replaceAll(regEx, "$4").trim();
            } else {
                System.out.println("Fehler!");
                log.warning("The String \"" + damage + "\" can not be parsed. Check your syntax");
            }
        } catch (PatternSyntaxException ex) {
            log.severe(ex.getMessage());
        }
        if (!dice.equals("")) {
            d.setNumberOfDice(Integer.parseInt(dice));
        }
        if (!bonus.equals("")) {
            d.setBonus(Integer.parseInt(bonus.replaceAll("\\+", "")));
        }
        if (!armordivisor.equals("")) {
            d.setArmorDivisor(Double.parseDouble(armordivisor));
        }
        if (!damagetype.equals("")) {
            d.setDamageType(damagetype);
        }
        return d;
    }
}
