package clear.dep.feat;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;

public class FeatCzech extends AbstractFeat {

    public static final String[] STR_TITLES = { "Cas", "Gen", "Gra", "Neg", "Num", "PGe", "PNu", "Per", "Sem", "SubPOS", "Ten", "Var", "Voi" };

    public static final ObjectIntOpenHashMap<String> MAP_TITLES = new ObjectIntOpenHashMap<String>() {

        {
            for (int i = 0; i < STR_TITLES.length; i++) put(STR_TITLES[i], i);
        }
    };

    public FeatCzech(String feats) {
        this.feats = new String[STR_TITLES.length];
        set(feats);
    }

    public void set(String feats) {
        for (String feat : feats.split("\\|")) {
            String key = feat.split("=")[0];
            if (MAP_TITLES.containsKey(key)) this.feats[MAP_TITLES.get(key)] = feat.substring(key.length() + 1);
        }
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < STR_TITLES.length; i++) {
            if (feats[i] != null) {
                build.append("|");
                build.append(STR_TITLES[i]);
                build.append("=");
                build.append(feats[i]);
            }
        }
        return build.toString().substring(1);
    }
}
