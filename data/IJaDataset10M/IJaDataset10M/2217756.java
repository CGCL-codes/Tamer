package com.proyecto.tropero.gui.interfaces;

import java.util.List;
import com.proyecto.tropero.core.domain.Campo;

public interface IVentanaReportesAdmin {

    public String getFechaDesde();

    public String getFechaHasta();

    public String getArchivo();

    public List<Campo> getListCampos();

    public void setListCampos(List<Campo> listCampos);

    public List<Campo> getListCamposSeleccionados();
}
