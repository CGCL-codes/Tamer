package de.KW4FT.KW_Base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author HrabovszZ
 *
 */
public class KW_Option {

    protected String Option;

    protected String Count;

    protected static Pattern p_KW_Option = null;

    KW_Option() {
        Option = "";
        Count = "";
        if (p_KW_Option == null) {
            p_KW_Option = Pattern.compile("^([a-zA-Z�������]*?)(\\d+)$");
        }
    }

    protected boolean getnnnFromOption(String fps_Option) {
        Matcher m;
        boolean lvb_Return;
        m = p_KW_Option.matcher(fps_Option);
        if (m.matches()) {
            this.Option = m.group(1) + "nnn";
            this.Count = m.group(2);
            lvb_Return = true;
        } else {
            this.Option = fps_Option;
            this.Count = "";
            lvb_Return = false;
        }
        return lvb_Return;
    }
}
