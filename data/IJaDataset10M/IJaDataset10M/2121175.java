package com.proyecto.bigbang.core.presenter;

import com.proyecto.bigbang.gui.jpanels.IAdministracionAnormalidades;

public interface IAdministracionAnormalidadesPresenter {

    public void delete();

    public void setView(IAdministracionAnormalidades anormalidades);

    public void setDataProvider();
}
