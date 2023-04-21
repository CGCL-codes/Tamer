package plugin.lsttokens.gamemode.tab;

import java.util.Map;
import java.util.StringTokenizer;
import pcgen.core.GameMode;
import pcgen.persistence.lst.TabLoader;
import pcgen.persistence.lst.TabLstToken;
import pcgen.util.enumeration.Tab;

/**
 * Class deals with SKILLTABLEHIDDENCOLUMNS Token
 */
public class SkilltablehiddencolumnsToken implements TabLstToken {

    public String getTokenName() {
        return "SKILLTABLEHIDDENCOLUMNS";
    }

    public boolean parse(GameMode gameMode, Map<String, String> tab, String value) {
        final Tab aTab = GameMode.getTab(tab.get(TabLoader.TAB));
        if (aTab != Tab.SKILLS) {
            return false;
        }
        for (int i = 0; i < 6; ++i) {
            gameMode.setSkillTabColumnVisible(i, true);
        }
        final StringTokenizer commaTok = new StringTokenizer(value, ",");
        while (commaTok.hasMoreTokens()) {
            String commaToken = commaTok.nextToken();
            try {
                gameMode.setSkillTabColumnVisible(Integer.parseInt(commaToken), false);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }
}
