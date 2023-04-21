package haw.prp2.a2;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Reiseangebot extends ObjectReiseModell implements Comparable<Reiseangebot> {

    protected String name;

    protected static HashMap<String, Reiseangebot> raVerzeichnis = new HashMap<String, Reiseangebot>();

    protected Reiseangebot() {
        name = "";
    }

    public Reiseangebot(String name) {
        this.name = name;
        Reiseangebot.raVerzeichnis.put((this.getClass().getSimpleName() + name), this);
    }

    public String getName() {
        return this.name;
    }

    public Euro getPreisProTag() {
        return this.getPreis().dividiere(this.getDauer().getTage());
    }

    public static Reiseangebot from(String einRA) {
        try {
            return Reiseangebot.raVerzeichnis.get(einRA);
        } catch (Exception ex) {
            return null;
        } finally {
        }
    }

    public static void remove(Object o) {
        for (Entry<String, Reiseangebot> e : raVerzeichnis.entrySet()) {
            if (e.getValue() == o) {
                raVerzeichnis.remove(e.getKey());
                break;
            }
        }
    }

    public static Set<Reiseangebot> getByPrefix(String prefix) {
        Set<Reiseangebot> retSet = new HashSet<Reiseangebot>();
        for (Map.Entry<String, Reiseangebot> elem : raVerzeichnis.entrySet()) {
            if (elem.getKey().startsWith(prefix)) retSet.add(elem.getValue());
        }
        return retSet;
    }

    public static Set<Reiseangebot> getByInstance(Class<?> instance) {
        Set<Reiseangebot> retSet = new HashSet<Reiseangebot>();
        for (Map.Entry<String, Reiseangebot> elem : raVerzeichnis.entrySet()) {
            if (instance.isInstance(elem.getValue())) retSet.add(elem.getValue());
        }
        return retSet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public static Set<Reiseangebot> all() {
        Set<Reiseangebot> retSet = new HashSet<Reiseangebot>();
        for (Map.Entry<String, Reiseangebot> elem : raVerzeichnis.entrySet()) {
            retSet.add(elem.getValue());
        }
        return retSet;
    }

    public abstract Ort startOrt();

    public abstract Ort zielOrt();

    public abstract Euro getPreis();

    public abstract Dauer getDauer();

    public abstract boolean istEinfach();

    public abstract GregorianCalendar getStart();

    public boolean istLangreise() {
        return false;
    }

    public boolean istKurzreise() {
        return false;
    }

    public boolean istNormalreise() {
        return !istLangreise() && !istKurzreise();
    }

    public Euro nettoPreis() {
        return getPreis().subtrahiere(rabatt());
    }

    public boolean istKombination() {
        return !this.istEinfach();
    }

    public boolean istFlug() {
        return false;
    }

    public boolean istKeinFlug() {
        return !this.istFlug();
    }

    public boolean istHotel() {
        return false;
    }

    public boolean istKeinHotel() {
        return !this.istHotel();
    }

    public boolean istMitFlug() {
        return false;
    }

    public boolean istMitHotel() {
        return false;
    }

    public boolean istOhneFlug() {
        return !this.istMitFlug();
    }

    public boolean istOhneHotel() {
        return !this.istMitHotel();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return name + " Preis:" + getPreis() + " von " + startOrt() + " nach " + zielOrt() + " Dauer:" + getDauer() + " Beginn:" + sdf.format(getStart().getTime());
    }

    @Override
    public int compareTo(Reiseangebot o) {
        if (this == o) {
            return 0;
        }
        if (o == null) {
            return 1;
        }
        return getStart().compareTo(o.getStart());
    }

    public Euro rabatt() {
        Euro preis = getPreis();
        if (istKurzreise()) {
            return preis.multipliziere(5).dividiere(100);
        } else if (istLangreise()) {
            return preis.multipliziere(15).dividiere(100);
        } else {
            return preis.multipliziere(3).dividiere(100);
        }
    }
}
