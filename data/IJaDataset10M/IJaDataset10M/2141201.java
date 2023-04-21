package com.lagerplan.transporthilfsmitteloptimierung.data;

import java.util.Iterator;

/**
 * Speicherstruktur der Ergebnisse von Clusterverfahren
 * @author MIFE
 *
 */
public class ClusterVerfahren extends Optimierungsverfahren {

    private ArtikeleinteilungCfgTO artikeleinteilungCfg;

    private GewichtsfaktorenCfgTO gewichtsfaktorenCfg;

    private ArtikelOberklasseCollection herleitungsstruktur;

    private ArtikelOberklasseCollection artikelOberklasseStatistik;

    public ArtikeleinteilungCfgTO getArtikeleinteilungCfg() {
        return artikeleinteilungCfg;
    }

    public void setArtikeleinteilungCfg(ArtikeleinteilungCfgTO artikeleinteilungCfg) {
        this.artikeleinteilungCfg = artikeleinteilungCfg;
    }

    public GewichtsfaktorenCfgTO getGewichtsfaktorenCfg() {
        return gewichtsfaktorenCfg;
    }

    public void setGewichtsfaktorenCfg(GewichtsfaktorenCfgTO gewichtsfaktorenCfg) {
        this.gewichtsfaktorenCfg = gewichtsfaktorenCfg;
    }

    public ArtikelOberklasseCollection getHerleitungsstruktur() {
        return herleitungsstruktur;
    }

    public void setHerleitungsstruktur(ArtikelOberklasseCollection _herleitungsstruktur) {
        this.herleitungsstruktur = _herleitungsstruktur;
    }

    public ArtikelOberklasseCollection getArtikelOberklasseStatistik() {
        return artikelOberklasseStatistik;
    }

    /**
	 * m�ssen Elemente umkopieren, da nur Referenzen gespeichert werden
	 * @param artikelOberklasseStatistik
	 */
    public void setArtikelOberklasseStatistik(ArtikelOberklasseCollection _artikelOberklasseStatistik) {
        artikelOberklasseStatistik = new ArtikelOberklasseCollection(false);
        Iterator<ArtikelOberklasseTO> it = _artikelOberklasseStatistik.iterator();
        while (it.hasNext()) {
            artikelOberklasseStatistik.add(it.next());
        }
    }
}
