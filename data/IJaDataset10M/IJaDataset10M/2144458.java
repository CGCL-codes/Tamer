package gogo.rzgw.client.gmap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class GMap2 extends JavaScriptObject {

    private static GMap2Impl impl = new GMap2Impl();

    protected GMap2() {
    }

    public static final GMap2 create(Element e, int szerokosc, int wysokosc) {
        return impl.create(e, szerokosc, wysokosc);
    }

    public static final void dodajMarker(String id, String tytul, String miejsce, String kategoria, String opis, String rok, String dzien, String szerokosc, String dlugosc, String lat, String lng) {
        impl.dodaj(id, tytul, miejsce, kategoria, opis, rok, dzien, szerokosc, dlugosc, lat, lng);
    }

    public static final GMap2 getGMap2() {
        return impl.getGMap2();
    }

    public static final void ukryj() {
        impl.ukryj();
    }

    public static final void idzDo(String dlugosc, String szerokosc) {
        impl.idzDo(dlugosc, szerokosc);
    }

    public static final void goToURL(String url) {
        impl.goToURL(url);
    }

    public static final void closeInfoWindow() {
        impl.closeInfoWindow();
    }
}
