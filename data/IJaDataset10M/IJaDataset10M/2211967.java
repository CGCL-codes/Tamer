package com.bluebrim.base.client;

import com.bluebrim.gui.client.CoAbstractScrolledPanel;

/**
 * En subklass till CoAbstractScrolledPanel som arrangerar sina barn i rader fr�n 
 * h�ger till v�nster i ett givet antal kolumner.
*/
public class CoScrolledHorizontalGridPanel extends CoAbstractScrolledPanel {

    /**
	 * Default-konstruktor
	 */
    public CoScrolledHorizontalGridPanel() {
        this(false);
    }

    /**
	 * Konstructor
	 * @param isDoubleBuffered boolean
	 */
    public CoScrolledHorizontalGridPanel(boolean isDoubleBuffered) {
        super(new CoScrolledHorizontalGridPanelLayout(), isDoubleBuffered);
    }

    /**
	 * Access-metod till kolumnantalet.
	 * @return antal kolumner
	 */
    public int getColumnCount() {
        return ((CoScrolledHorizontalGridPanelLayout) getLayout()).getColumnCount();
    }

    /**
	 * Access-metod f�r att �ndra kolumnantalet.
	 * @param cc antal kolumner
	 */
    public int setColumnCount(int cc) {
        return ((CoScrolledHorizontalGridPanelLayout) getLayout()).setColumnCount(cc);
    }
}
