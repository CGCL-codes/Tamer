package com.lagerplan.basisdienste.stammdaten.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import com.lagerplan.basisdienste.stammdaten.business.ArtikelVerwalter;
import com.lagerplan.basisdienste.stammdaten.data.ArtikelTO;

/**
 * <p>
 * Title:        ArtikelBrowserViewManager.java<br>
 * Description:  Controller Methoden und Datencontainer zugleich f�r View-relevante Inhalte.<br>
 * 					- Properties for form data
 * 					- Action controller method
 * 					- Placeholders for results data
 * 					(Unlike STRUTS, no separation between View content and View business logic)
 * Copyright:    Copyright (c) 2009<br>
 * Company:      LAGERPLAN Organisation
 * </p>
 *
 * @author  %author: Michael Felber%
 * @version %version: 1%
 */
public class ArtikelBrowserViewManager implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3564159327123674569L;

    /**
	 * Aufnahme der ManagedBeans (alle Artikelstammdaten) in eine Liste
	 */
    private ArrayList<ArtikelViewManager> artikelUebersicht;

    public void initialisieren() {
        ArtikelVerwalter artikelVerwalter = new ArtikelVerwalter();
        setArtikelUebersicht(artikelVerwalter.getArtikelStammdaten());
    }

    private void setArtikelUebersicht(ArrayList<ArtikelTO> artikel) {
        artikelUebersicht = new ArrayList<ArtikelViewManager>();
        Iterator<ArtikelTO> it = artikel.iterator();
        while (it.hasNext()) {
            ArtikelViewManager artikelViewManager = new ArtikelViewManager(it.next());
            artikelUebersicht.add(artikelViewManager);
        }
    }
}
