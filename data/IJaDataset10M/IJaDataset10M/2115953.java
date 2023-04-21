package plugin.lsttokens.equipmentmodifier.choose;

import pcgen.core.EquipmentModifier;
import pcgen.persistence.lst.EqModChooseLstToken;
import pcgen.util.Logging;

public class EquipmentToken implements EqModChooseLstToken {

    public String getTokenName() {
        return "EQUIPMENT";
    }

    public boolean parse(EquipmentModifier mod, String prefix, String value) {
        if (value == null) {
            Logging.errorPrint("CHOOSE:" + getTokenName() + " requires arguments");
            return false;
        }
        if (value.indexOf('[') != -1) {
            Logging.errorPrint("CHOOSE:" + getTokenName() + " arguments may not contain [] : " + value);
            return false;
        }
        if (value.charAt(0) == '|') {
            Logging.errorPrint("CHOOSE:" + getTokenName() + " arguments may not start with | : " + value);
            return false;
        }
        if (value.charAt(value.length() - 1) == '|') {
            Logging.errorPrint("CHOOSE:" + getTokenName() + " arguments may not end with | : " + value);
            return false;
        }
        if (value.indexOf("||") != -1) {
            Logging.errorPrint("CHOOSE:" + getTokenName() + " arguments uses double separator || : " + value);
            return false;
        }
        StringBuilder sb = new StringBuilder();
        if (prefix.length() > 0) {
            sb.append(prefix).append('|');
        }
        sb.append(getTokenName()).append('|').append(value);
        mod.setChoiceString(sb.toString());
        return true;
    }
}
