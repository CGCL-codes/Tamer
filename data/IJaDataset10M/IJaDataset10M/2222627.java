package plugin.converter;

import java.util.StringTokenizer;
import pcgen.cdom.base.CDOMObject;
import pcgen.gui.converter.event.TokenProcessEvent;
import pcgen.gui.converter.event.TokenProcessorPlugin;

public class PreDRConvertPlugin implements TokenProcessorPlugin {

    public String process(TokenProcessEvent tpe) {
        tpe.append(tpe.getKey());
        tpe.append(':');
        String formula = tpe.getValue();
        int commaLoc = formula.indexOf(',');
        if (commaLoc == -1) {
            return "Prerequisite " + tpe.getKey() + " must have a count: " + formula;
        }
        if (commaLoc == formula.length() - 1) {
            return "Prerequisite " + tpe.getKey() + " can not have only a count: " + formula;
        }
        String num = formula.substring(0, commaLoc);
        String rest = formula.substring(commaLoc + 1);
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
            return "'" + num + "' in " + tpe.getKey() + " is not a valid integer";
        }
        tpe.append(num);
        StringTokenizer st = new StringTokenizer(rest, ",");
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            int equalLoc = tok.indexOf('=');
            tpe.append(',');
            tpe.append(tok);
            if (equalLoc == -1) {
                tpe.append("=0");
            }
        }
        tpe.consume();
        return null;
    }

    public Class<? extends CDOMObject> getProcessedClass() {
        return CDOMObject.class;
    }

    public String getProcessedToken() {
        return "PREDR";
    }
}
