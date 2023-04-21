package ck;

import static utils.Check.checkFatal;
import java.util.TreeSet;
import pxAnalyzer.PXTree.BaseData;
import pxAnalyzer.PXTree.BaseField;
import pxAnalyzer.PXTree.StructField;
import pxAnalyzer.PXTree.Field;
import utils.*;

public class Country implements FieldLoadable, Key<String> {

    /** country ruler */
    public Characters.Rulers _holder;

    /** controlled provinces */
    public FieldSet<County> _controlled;

    /** owned provinces */
    public FieldSet<County> _owned;

    /** tag for that country */
    public String _tag;

    /** capital province */
    public County _capital;

    /** associated title */
    public Title _title;

    /** liege country for that country */
    public Country _liege;

    /** vassals countries for that country */
    public FieldSet<Country> _vassals;

    /** analyzed data */
    public StructField _base;

    /** global income */
    public float _income;

    /** laws used */
    public TreeSet<String> _laws;

    public double _badboy = 0.0;

    public double _stability = 1.0;

    public static FieldSet<Country> __list = new FieldSet<Country>(Country.class);

    public int ownedSize() {
        int n = _owned.size();
        for (Country c : _vassals) n += c.ownedSize();
        return n;
    }

    public FieldSet<County> allProvinces() {
        FieldSet<County> x = new FieldSet<County>(County.class);
        allProvinces(x);
        return x;
    }

    private void allProvinces(FieldSet<County> x) {
        x.addAll(_owned);
        for (Country c : _vassals) c.allProvinces(x);
    }

    public static void sanityCheck() {
        int n = 0;
        int m = 0;
        for (Country c : __list) {
            n += c._controlled.size();
            m += c._owned.size();
        }
        if (n != County.__list.size()) {
            System.out.format("%d counties associated to countries (control) ; %d expected\n", n, County.__list.size());
            System.exit(0);
        }
        if (m != County.__list.size()) {
            System.out.format("%d counties associated to countries (ownership) ; %d expected\n", n, County.__list.size());
            System.exit(0);
        }
    }

    public static void loadAll(StructField root) {
        __list.load(root);
        for (Country t : __list) {
            if (t._holder._main == null) {
                System.out.format("Title for country %s not found for owner whose id is=%s\n", t._tag, t._holder.Characters()._id);
                System.out.format("This character court is %s\n", t._holder.Characters()._tag);
                System.exit(0);
            }
            if (t._holder._main._liege != null) {
                if (null != t._holder._main._liege._holder._country) {
                    t._liege = t._holder._main._liege._holder._country;
                    t._liege._vassals.add(t);
                } else {
                    System.out.format("Dead title found for liege of %s: %s", t._tag, t._holder._main._liege._tag);
                    t._holder._main._liege = null;
                }
            }
        }
        for (County c : County.__list) {
            c._owner = c._county._holder._country;
            c._owner._owned.add(c);
        }
        sanityCheck();
    }

    /**
	 * first processing pass : build the basis 
	 * @param root
	 */
    public boolean load(Field<?> x) {
        if (!x.name().equals("country")) return false;
        StructField f = (StructField) x;
        _tag = f.getBase("tag").get();
        _controlled = new FieldSet<County>(County.class);
        _owned = new FieldSet<County>(County.class);
        _vassals = new FieldSet<Country>(Country.class);
        _base = f;
        StructField r = f.getStruct("ruler");
        if (r == null) return false;
        _holder = Characters.search(r.getStruct("character")).getRuler();
        checkFatal(_holder, "ruler", "country " + _tag);
        _capital = County.__list.search(f.getBase("capital").get());
        if (_capital != null) _capital._isCapital = true;
        _title = Title.__list.search(_tag);
        checkFatal(_title, "title", _tag);
        for (BaseData b : f.getList("controlledprovinces")._data) {
            County p = County.__list.search(b._value);
            checkFatal(p, "county " + b._value, " country " + _tag);
            p._controller = this;
            _controlled.add(p);
            _income += p._income;
        }
        _holder._country = this;
        _laws = new TreeSet<String>();
        Field<?> laws = f.get("laws");
        if (laws != null && laws instanceof StructField) {
            for (Field<?> xx : ((StructField) laws)._data) {
                _laws.add(xx.name());
            }
        }
        BaseField b = f.getBase("badboy");
        if (null != b) {
            _badboy = b.getAsDouble();
        }
        b = f.getBase("stability");
        if (null != b) {
            _stability = b.getAsDouble();
        }
        return true;
    }

    public Country() {
    }

    public String getKey() {
        return _tag;
    }

    public void setKey(String k) {
        _tag = k;
    }

    public void setSuccessor(String k) {
        _tag = k + "\0";
    }
}
