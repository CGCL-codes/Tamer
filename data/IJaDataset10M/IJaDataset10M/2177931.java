package net.sourceforge.ck2httt.ck;

import java.io.IOException;
import net.sourceforge.ck2httt.pxAnalyzer.PXAdvancedAnalyzer;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.Field;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.StructField;

public class Analyzer {

    public static StructField __root;

    String __filter = "{" + "header," + "globaldata{war{attacker,defender}}," + "relations{alliance{primary,secondary}}," + "character{id,dyn,tag,name,date,religion,culture,deathdate,attributes,traits,score,court}," + "province{id,religion,culture,privileges,improvements,effects,advances}," + "country{tag,form_of_goverment,capital,tier,ruler,laws,controlledprovinces,law_date_3,stability,badboy}," + "title{tag,tier,liege,holder}" + "}";

    /**
	 * First load all characters (even dead ones may be important
	 * Second load all titles : we may immediately assign titles to characters
	 * Third load all counties : we may also assign the county owner and county title
	 * Last load all countries : we may immediately assign owner, country list and title
	 * @param CKPath
	 * @param filename
	 * @throws IOException
	 */
    private Analyzer(String CKPath, String filename) throws IOException {
        PXAdvancedAnalyzer a = new PXAdvancedAnalyzer(filename, true);
        __root = a.analyze(__filter);
        Characters.loadAll(__root);
        Title.loadAll(__root);
        County.loadAll(CKPath, __root);
        Country.loadAll(__root);
        Dynasty.loadAll(CKPath, __root);
        for (Field<?> f : __root._data) {
            if (!f.name().equals("character")) continue;
            StructField x = (StructField) f;
            if (x.get("deathdate") == null) {
                boolean martial = false;
                boolean court = false;
                boolean church = false;
                if (x.get("traits") instanceof StructField) {
                    StructField traits = x.getStruct("traits");
                    for (Field<?> t : traits._data) {
                        martial |= "misguided_warrior".equals(t._name._value);
                        martial |= "tough_soldier".equals(t._name._value);
                        martial |= "knowledged_tactician".equals(t._name._value);
                        martial |= "brilliant_strategist".equals(t._name._value);
                        court |= "amateurish_pettifogger".equals(t._name._value);
                        court |= "proven_dealbreaker".equals(t._name._value);
                        court |= "charismatic_negotiator".equals(t._name._value);
                        court |= "grey_eminence".equals(t._name._value);
                        court |= "naive_wirepuller".equals(t._name._value);
                        court |= "flamboyant_schemer".equals(t._name._value);
                        court |= "intricate_webweaver".equals(t._name._value);
                        court |= "illusive_shadow".equals(t._name._value);
                        court |= "hole_in_the_pocket".equals(t._name._value);
                        court |= "proven_accountant".equals(t._name._value);
                        court |= "fortune_builder".equals(t._name._value);
                        court |= "midas_touched".equals(t._name._value);
                        church |= "detached_priest".equals(t._name._value);
                        church |= "martial_cleric".equals(t._name._value);
                        church |= "scholarly_theologian".equals(t._name._value);
                        church |= "mastermind_theologian".equals(t._name._value);
                        if (martial || court || church) {
                            break;
                        }
                    }
                } else {
                }
                String tag = x.getBase("tag").get();
                Country country = net.sourceforge.ck2httt.ck.Country.__list.search(tag);
                if (null != country) {
                    for (County c : country._owned) {
                        c.incPopulation();
                        if (martial) {
                            c.incMartialPopulation();
                        } else if (court) {
                            c.incCourtPopulation();
                        } else if (church) {
                            c.incChurchPopulation();
                        }
                    }
                }
            }
        }
    }

    public static String getSaveFile() {
        return "CK_Converted.eu3";
    }

    public static void loadAnalyzer(String CKPath, String filename) throws IOException {
        new Analyzer(CKPath, filename);
    }
}
