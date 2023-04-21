package com.proyecto.tropero.core.service.model.Interface;

import com.proyecto.tropero.core.domain.UnidadMedida;
import com.proyecto.tropero.core.service.IGenericService;

public interface IUnidadMedidaService extends IGenericService {

    UnidadMedida getUnidadMedidaByDescription(String descripcion);
}
