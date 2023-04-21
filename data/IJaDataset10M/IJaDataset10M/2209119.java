package com.proyecto.bigbang.core.presenter;

import com.proyecto.bigbang.gui.jpanels.IPanel;

public interface IPanelPresenter {

    public void setView(IPanel view);

    /**
	 * La clase que lo implemente va a necesitar conocer el tipo de clase que va a recivir de la pantalla para persistirlo
	 *
	 */
    public void save();
}
