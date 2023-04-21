package net.sourceforge.ck2httt.rules;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.sourceforge.ck2httt.ck.County;
import net.sourceforge.ck2httt.cv.CvProvince;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.BaseData;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.BaseField;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.Field;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.ListField;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.StructField;
import net.sourceforge.ck2httt.utils.FieldLoadable;
import net.sourceforge.ck2httt.utils.FieldMap;
import net.sourceforge.ck2httt.utils.FieldSet;
import net.sourceforge.ck2httt.utils.KeyString;

public class ProvinceCvRules extends KeyString implements FieldLoadable {

    public String _id;

    public String[] _ck;

    public String[] _prefered;

    public static FieldSet<ProvinceCvRules> _list = new FieldSet<ProvinceCvRules>(ProvinceCvRules.class);

    static ProvinceCvRules.ProvinceRule _kingdomCapital = new ProvinceCvRules.ProvinceRule();

    static ProvinceCvRules.ProvinceRule _duchyCapital = new ProvinceCvRules.ProvinceRule();

    static ProvinceCvRules.ProvinceRule _countyCapital = new ProvinceCvRules.ProvinceRule();

    static FieldMap.StringFloat _improvements = new FieldMap.StringFloat();

    static boolean _weightMoney = false;

    static float _improvementsOther;

    static FieldMap.StringAny<BuildingRule> _buildingTable = new FieldMap.StringAny<BuildingRule>(BuildingRule.class);

    static FieldMap.StringAny<ImprovementRule> _improvementTable = new FieldMap.StringAny<ImprovementRule>(ImprovementRule.class);

    static FieldMap.IntFloat _baseTaxTable = new FieldMap.IntFloat();

    static FieldMap.IntFloat _manpowerTable = new FieldMap.IntFloat();

    ;

    static boolean _contentWeightMoney;

    ;

    static boolean _eu3tax;

    static boolean _eu3manpower;

    static int _popFactor;

    static double _popScale;

    static double _mpScale;

    static double _courtTaxScale;

    static double _ckTaxScale;

    public static void setPopScale(double argPopScale) {
        _popScale = argPopScale;
    }

    public static void setMpScale(double argMpScale) {
        _mpScale = argMpScale;
    }

    public static void setCourtTaxScale(double argCourtTaxScale) {
        _courtTaxScale = argCourtTaxScale;
    }

    public static void setCKTaxScale(double argCKTaxScale) {
        _ckTaxScale = argCKTaxScale;
    }

    public static void loadProvinces(StructField provinces) {
        _list.loadOptionSection(provinces, "convert");
    }

    public static void loadRules(StructField rules) {
        BaseField f = rules.getBase("weight");
        if (f != null) _weightMoney = f.get().equals("money");
        StructField g = rules.getStruct("modifier");
        if (g != null) {
            _kingdomCapital.load(g.getStruct("kingdom_capital"));
            _duchyCapital.load(g.getStruct("duchy_capital"));
            _countyCapital.load(g.getStruct("county_capital"));
        }
        g = rules.getStruct("improvements");
        if (g != null) for (Field<?> x : g._data) {
            if (x.name().equals("other")) _improvementsOther = ((BaseField) x).getAsFloat(); else _improvements.put(x);
        }
    }

    public static void loadConvertRules(StructField rules) {
        BaseField f = rules.getBase("county_mapping");
        if (f != null) _contentWeightMoney = f.get().equals("income_average");
        f = rules.getBase("pop_factor");
        if (f != null) _popFactor = f.getAsInt();
        _manpowerTable.load(rules.getStruct("manpower"));
        _baseTaxTable.load(rules.getStruct("base_tax"));
        _buildingTable.load(rules.getStruct("ck_buildings"));
        _improvementTable.load(rules.getStruct("improvements"));
        _eu3tax = _baseTaxTable.get(_baseTaxTable.firstKey())._f < 0;
        _eu3manpower = _manpowerTable.get(_manpowerTable.firstKey())._f < 0;
    }

    public static void loadAll(Field<?> root) {
        if (root == null || !(root instanceof StructField)) return;
        StructField f = (StructField) root;
        loadProvinces(f.getStruct("province"));
        loadRules(f.getStruct("province_assignement_rules"));
        loadConvertRules(f.getStruct("province_conversion"));
    }

    public static double getCKDefaultBaseTax(CvProvince p) {
        double ck = 0.0;
        for (County c : p._counties) {
            ck += c._baseIncome;
        }
        return ck;
    }

    public static double getBaseTax(CvProvince p) {
        double eu3 = 0.0;
        double ck = 0.0;
        double court = 0.0;
        double cpop = 0;
        for (County c : p._counties) {
            cpop += c.getCourtPopulation();
            ck += c._baseIncome;
        }
        if (CvProvince.USE_AVERAGE) {
            cpop = p._owner._main._owned.first().getCourtPopulation(true);
        }
        if (cpop >= 1) {
            court = (Math.log10(cpop) / Math.log10(2));
        }
        eu3 = p._province.getEU3DefaultBaseTax();
        court = court * _courtTaxScale;
        ck = ck * _ckTaxScale;
        double tax = (eu3 * 0.4) + (ck * 0.4) + (court * 0.2);
        if (tax < 1.0) {
            tax = 1.0;
        }
        return tax;
    }

    public static double getBaseManpower(CvProvince p) {
        double eu3 = 0.0;
        double ma = 0.0;
        double mpop = 0;
        for (County c : p._counties) {
            mpop += c.getMartialPopulation();
        }
        if (CvProvince.USE_AVERAGE) {
            mpop = p._owner._main._owned.first().getMartialPopulation(true);
        }
        if (mpop >= 1) {
            ma = (Math.log10(mpop) / Math.log10(2));
        }
        eu3 = p._province.getEU3DefaultMP();
        ma = ma * _mpScale;
        double mp = (eu3 * 0.8) + (ma * 0.2);
        if (mp < 1.0) {
            mp = 1.0;
        }
        return mp;
    }

    public static int getCitySize(CvProvince p) {
        double eu3 = 0.0;
        double pop = 0.0;
        int apop = 0;
        for (County c : p._counties) {
            apop += c.getPopulation();
        }
        if (apop >= 1) {
            pop = (Math.log10(apop) / Math.log10(2));
        }
        eu3 = p._province.getEU3DefaultCitySize();
        pop = pop * _popScale;
        int ret = (int) Math.round((eu3 * 0.8) + (pop * 0.2));
        if (ret < 500) {
            ret = 500;
        } else if (ret > 999999) {
            ret = 999999;
        }
        return ret;
    }

    public static String getReligion(Collection<County> counties) {
        TreeMap<String, Float> x = new TreeMap<String, Float>();
        for (County c : counties) {
            if (x.containsKey(c._religion)) x.put(c._religion, x.get(c._religion) + c._income); else x.put(c._religion, c._income);
        }
        float m = 0;
        String ck_religion = null;
        for (Map.Entry<String, Float> e : x.entrySet()) if (e.getValue() > m) ck_religion = e.getKey();
        if (ck_religion.equals("catholic")) return "catholic";
        if (ck_religion.equals("orthodox")) return "orthodox";
        if (ck_religion.equals("jewish")) return CountryCvRules._jewish;
        if (ck_religion.equals("pagan")) return CountryCvRules._pagan;
        if (ck_religion.equals("muslim")) {
            TreeMap<String, Float> y = new TreeMap<String, Float>();
            for (County c : counties) {
                if (y.containsKey(c._baseCulture)) y.put(c._baseCulture, y.get(c._baseCulture) + c._income); else y.put(c._baseCulture, c._income);
            }
            String ck_base_culture = null;
            for (Map.Entry<String, Float> e : x.entrySet()) if (e.getValue() > m) ck_base_culture = e.getKey();
            if (ck_base_culture.equals("turkish") || ck_base_culture.equals("muslim")) return null; else return "sunni";
        }
        return "shamanism";
    }

    public static Set<String> getImprovements(Collection<County> counties) {
        ProvinceItemCounter cnt = new ProvinceItemCounter();
        for (County c : counties) {
            if (c._id.equals("94")) {
                int i = 0;
            }
            cnt.set(c);
            for (byte b : c._improvements) cnt.add("B_" + County.Improvements.__array[b]._name);
            for (short b : c._advances) cnt.add("A_" + County.Advances.__array[b]);
        }
        cnt.normalize();
        TreeSet<String> res = new TreeSet<String>();
        for (Map.Entry<FieldMap.StringKey, ImprovementRule> e : _improvementTable.entrySet()) {
            ImprovementRule r = e.getValue();
            boolean ok = true;
            for (Map.Entry<FieldMap.StringKey, ImprovementMiniRule> mr : r._rules.entrySet()) {
                Float f = cnt.x.get(mr.getKey()._s);
                if (f == null || 100 * f < mr.getValue()._req) {
                    ok = false;
                    break;
                }
            }
            if (ok) res.add(e.getKey()._s);
        }
        return res;
    }

    public ProvinceCvRules() {
    }

    public boolean load(Field<?> data) {
        if (!(data instanceof StructField)) throw new IllegalStateException("StructField expected");
        StructField f = (StructField) data;
        _id = f.name();
        _key = _id;
        ListField ck = f.getList("ck");
        _ck = new String[ck._data.size()];
        int i = 0;
        for (BaseData x : ck._data) {
            _ck[i++] = x._value;
        }
        ListField prefered = f.getList("prefered");
        if (prefered != null) {
            _prefered = new String[ck._data.size()];
            i = 0;
            for (BaseData x : prefered._data) {
                _prefered[i++] = x._value;
            }
        }
        return true;
    }

    static class ProvinceRule implements FieldLoadable {

        float _add = 0;

        float _mult = 1;

        float _weight = 0;

        ProvinceRule() {
        }

        public boolean load(Field<?> f) {
            if (f == null || !(f instanceof StructField)) return false;
            StructField g = (StructField) f;
            BaseField x;
            x = g.getBase("add");
            if (x != null) _add = x.getAsFloat();
            x = g.getBase("mult");
            if (x != null) _mult = x.getAsFloat();
            x = g.getBase("weight");
            if (x != null) _weight = x.getAsFloat();
            return true;
        }
    }

    public static class BuildingRule implements FieldLoadable {

        public int _pop;

        public float _man;

        public float _tax;

        public BuildingRule() {
        }

        public boolean load(Field<?> f) {
            if (f == null || !(f instanceof StructField)) return false;
            StructField g = (StructField) f;
            BaseField x;
            x = g.getBase("pop");
            if (x != null) _pop = x.getAsInt();
            x = g.getBase("manpower");
            if (x != null) _man = x.getAsFloat();
            x = g.getBase("tax");
            if (x != null) _tax = x.getAsFloat();
            return true;
        }
    }

    public static class ImprovementMiniRule implements FieldLoadable {

        String _name;

        boolean _isAdvance;

        int _req;

        public ImprovementMiniRule() {
        }

        public boolean load(Field<?> f) {
            if (f == null || !(f instanceof BaseField)) return false;
            BaseField g = (BaseField) f;
            _isAdvance = g.name().substring(0, 2).equals("A_");
            _name = g.name().substring(2);
            _req = g.getAsInt();
            return true;
        }
    }

    public static class ImprovementRule implements FieldLoadable {

        FieldMap.StringAny<ImprovementMiniRule> _rules = new FieldMap.StringAny<ImprovementMiniRule>(ImprovementMiniRule.class);

        public ImprovementRule() {
        }

        public boolean load(Field<?> f) {
            if (f == null || !(f instanceof StructField)) return false;
            _rules.load(f);
            return true;
        }
    }

    private static class ProvinceCounter {

        private float _total = 0;

        private float _value = 0;

        void add(County c, float v) {
            float w = (_contentWeightMoney) ? c._income : 1;
            _total += w;
            _value += w * v;
        }

        float get() {
            return _value / _total;
        }
    }

    private static class ProvinceItemCounter {

        public float _total = 0;

        private float _weight = 0;

        public TreeMap<String, Float> x = new TreeMap<String, Float>();

        void set(County c) {
            _weight = (_contentWeightMoney) ? c._income : 1;
            _total += _weight;
        }

        void add(String v) {
            if (x.containsKey(v)) x.put(v, x.get(v) + _weight); else x.put(v, _weight);
        }

        void normalize() {
            for (Map.Entry<String, Float> e : x.entrySet()) e.setValue(e.getValue() / _total);
        }
    }
}
