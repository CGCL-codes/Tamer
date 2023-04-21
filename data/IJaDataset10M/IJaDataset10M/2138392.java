package com.proyecto.bigbang.core.dao;

import java.util.List;
import com.proyecto.bigbang.core.dao.vo.ListadoInasistenciaProfesorVO;

public interface IListadoInasistenciasProfesorDAO {

    public List<ListadoInasistenciaProfesorVO> findAll(int idProfesor);
}
