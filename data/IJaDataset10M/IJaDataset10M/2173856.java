package com.proyecto.bigbang.core.presenter;

import com.proyecto.bigbang.core.reportes.filters.Filtro;
import com.proyecto.bigbang.core.reportes.panels.IFiltroReporte;

public interface IParametroPresenter {

    public void cargarDatos();

    public void setView(IFiltroReporte view);

    public Filtro getParametros();
}
