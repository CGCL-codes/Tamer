package com.proyecto.bigbang.gui.jpanels;

import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import com.proyecto.bigbang.core.common.GenericTableDataModel;

public interface ILicenciaProfesor {

    public String getLabelId();

    public void setLabelId(String labelId);

    public Date getTextFechaDesde();

    public Date getTextFechaHasta();

    public void setLabelProfesor(String labelProfesor);

    public void cargarDatos();

    public void setDataProvider(GenericTableDataModel dataProvider);

    public int getSelectedRow();

    public void setListMaterias(DefaultListModel modelo);

    public boolean isTodosLosCursos();

    public int getMateria();

    public JComboBox setComboTipoLicencia();

    public JComboBox setComboCurso();
}
