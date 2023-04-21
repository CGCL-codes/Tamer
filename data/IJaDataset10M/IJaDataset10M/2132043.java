package cz.vse.gebz.podpora.nest;

public final class GebzNestPomocnik {

    public static final PrevodnikNestDoGebz doGebz = new PrevodnikNestDoGebz();

    public static final PrevodnikGebzDoNest doNest = new PrevodnikGebzDoNest();

    private GebzNestPomocnik() {
    }

    public static void main(String[] args) {
        doGebz.preved("Princ.xml", "Princ.bz");
    }
}
