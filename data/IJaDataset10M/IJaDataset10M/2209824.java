package com.proyecto.bigbang.gui.jpanels;

import javax.swing.DefaultComboBoxModel;
import com.proyecto.bigbang.common.BaseInternalWindow;
import com.proyecto.bigbang.core.common.GenericTableDataModel;
import com.proyecto.bigbang.core.presenter.IAdministracionAnormalidadesPresenter;
import com.proyecto.bigbang.core.presenter.IAdministracionProgramasCurricularesPresenter;

public interface IAdministrarProgramasCurriculares {

    public void setDataProvider(GenericTableDataModel dataProvider);

    public void setModel(GenericTableDataModel model);

    public IAdministracionProgramasCurricularesPresenter getPresenter();

    public void setPresenter(IAdministracionProgramasCurricularesPresenter presenter);

    public void setTopContainer(BaseInternalWindow topContainer);

    public void getCurrentRowInfo();

    public void setComboModel(DefaultComboBoxModel model);

    public String getComboSelectedColumn();

    public String getFechaCreacion();

    public void setFechaCreacion(String fecha);

    public String getNivel();

    public void setNivel(String nivel);

    public String getEspecialidad();

    public void setEspecialidad(String especialidad);

    public String getId();

    public void setId(String id);

    public void setDataProvider();

    public void setView(IAdministracionAnormalidadesPresenter anormalidades);

    public void updatePanel();
}
