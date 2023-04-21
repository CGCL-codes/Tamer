package net.sourceforge.ck2httt.rules;

import static net.sourceforge.ck2httt.utils.Check.checkFatal;
import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sourceforge.ck2httt.pxAnalyzer.Analyzer;
import net.sourceforge.ck2httt.pxAnalyzer.FileLoader;
import net.sourceforge.ck2httt.pxAnalyzer.Token;
import net.sourceforge.ck2httt.pxAnalyzer.Analyzer.NoHooks;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.BaseField;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.Field;
import net.sourceforge.ck2httt.pxAnalyzer.PXTree.StructField;
import net.sourceforge.ck2httt.utils.Check;
import net.sourceforge.ck2httt.utils.FieldLoadable;
import net.sourceforge.ck2httt.utils.FieldSet;
import net.sourceforge.ck2httt.utils.KeyString;
import net.sourceforge.ck2httt.utils.SearchSet;

public class CultureCvRules extends KeyString implements FieldLoadable {

    public String _CKculture;

    public EU3Culture _EU3culture;

    public boolean _exact;

    public static class EU3Culture extends KeyString {

        public String _culture;

        public String _group;

        public EU3Culture() {
        }

        public EU3Culture(String c, String g) {
            _culture = c;
            _group = g;
            _key = c;
        }

        private static SearchSet<String, EU3Culture> __list = new SearchSet<String, EU3Culture>(EU3Culture.class);

        private static class BasicHooks extends NoHooks {

            int _level;

            String _culture;

            String _group;

            public boolean beforeStruct(Token t) {
                if (0 == _level) {
                    _group = _lastName._value;
                }
                if (1 == _level) {
                    _culture = _lastName._value;
                    if (_culture.startsWith("ural")) {
                        int i = 0;
                    }
                }
                _level++;
                return false;
            }

            ;

            public boolean afterStruct(Token t) {
                _level--;
                if ((1 == _level) && (null != _culture)) {
                    __list.add(new EU3Culture(_culture, _group));
                    _culture = null;
                }
                return false;
            }

            ;

            public void beforeEmpty(Token t) {
                if (0 == _level) {
                    _group = _lastName._value;
                }
                if (1 == _level) {
                    _culture = _lastName._value;
                }
                _level++;
            }

            ;

            public boolean afterEmpty(Token t) {
                _level--;
                if ((1 == _level) && (null != _culture)) {
                    __list.add(new EU3Culture(_culture, _group));
                    _culture = null;
                }
                return false;
            }

            ;

            public boolean afterListData(Token t) {
                if (_lastName._value.equals("dynasty_names")) {
                    return false;
                }
                _culture = t._value;
                _group = _lastName._value;
                __list.add(new EU3Culture(_culture, _group));
                _culture = null;
                return false;
            }
        }

        public static void load(String fname) throws IOException {
            Analyzer.analyze(FileLoader.load(fname, FileLoader.Mode.READ), new BasicHooks());
        }

        public static EU3Culture search(String culture) {
            return __list.search(culture);
        }
    }

    public static FieldSet<CultureCvRules> __list = new FieldSet<CultureCvRules>(CultureCvRules.class);

    public static EU3Culture convertCulture(String ckBaseCulture, String ckCulture, EU3Culture capital) {
        CultureCvRules c = null;
        c = __list.search(ckCulture);
        checkFatal(c, "culture", ckCulture + " in cvdata.txt");
        if (c._exact || capital == null) return c._EU3culture;
        if (c._EU3culture._group.equals(capital._group)) return capital;
        return c._EU3culture;
    }

    public static EU3Culture convertCulture(String ckCulture) {
        CultureCvRules cr = __list.search(ckCulture);
        checkFatal(cr, "culture", ckCulture + " in cvdata.txt");
        return cr._EU3culture;
    }

    public CultureCvRules() {
    }

    public String toString() {
        return _CKculture + "=> (" + _EU3culture._culture + "<" + _EU3culture._group + ")";
    }

    public static void load(String EU3path, String Altpath, StructField root) throws IOException {
        File f = new File(Altpath + "common/cultures.txt");
        if (f.exists()) EU3Culture.load(Altpath + "common/cultures.txt"); else EU3Culture.load(EU3path + "common/cultures.txt");
        loadCultures(root);
    }

    private static void loadCultures(StructField root) {
        StructField cultures = root.getStruct("cultures");
        __list.loadOptionSection(cultures, "convert");
    }

    public boolean load(Field<?> f) {
        if (!(f instanceof StructField)) return false;
        StructField x = (StructField) f;
        StructField g = ((StructField) x).getStruct("default");
        _CKculture = x.name();
        _key = _CKculture;
        _exact = g == null;
        Field<?> h = (g == null) ? x._data.getFirst() : g._data.getFirst();
        _EU3culture = EU3Culture.search(((BaseField) h).get());
        if (_EU3culture == null) Check.checkFatal(null, ((BaseField) h).get() + " culture ", "EU3");
        return true;
    }

    public static class WCulture implements Comparable<WCulture> {

        EU3Culture _c;

        float _w;

        public WCulture(EU3Culture c, float w) {
            _c = c;
            _w = w;
        }

        public int compareTo(WCulture x) {
            return (_w < x._w) ? 1 : (_w == x._w) ? 0 : -1;
        }
    }

    public static class CultureCounter {

        EU3Culture[] culture = new EU3Culture[100];

        String[] group = new String[100];

        float[] weightC = new float[100];

        float[] weightG = new float[100];

        int nbC = 0;

        int nbG = 0;

        private static <T> int addTo(T[] l, float[] wl, float w, T v, int n) {
            for (int i = 0; i < n; i++) if (l[i].equals(v)) {
                wl[i] += w;
                return n;
            }
            l[n] = v;
            wl[n] += w;
            return n + 1;
        }

        public void add(EU3Culture c, float w) {
            nbC = addTo(culture, weightC, w, c, nbC);
            nbG = addTo(group, weightG, w, c._group, nbG);
        }

        public EU3Culture getCulture() {
            float t = 0;
            int n = 0;
            for (int i = 0; i < nbG; i++) if (t < weightG[i]) {
                t = weightG[i];
                n = i;
            }
            t = 0;
            int m = 0;
            for (int i = 0; i < nbC; i++) if (group[n].equals(culture[i]._group) && t < weightC[i]) {
                t = weightG[i];
                m = i;
            }
            return culture[m];
        }

        public EU3Culture[] getCulture(float limit) {
            float t = 0;
            TreeSet<WCulture> r = new TreeSet<WCulture>();
            for (int i = 0; i < nbC; i++) {
                t += weightC[i];
                r.add(new WCulture(culture[i], weightC[i]));
            }
            SortedSet<WCulture> sub = r.headSet(new WCulture(null, limit * t));
            EU3Culture[] res = new EU3Culture[sub.size()];
            int i = 0;
            for (WCulture c : sub) res[i++] = c._c;
            return res;
        }
    }
}
