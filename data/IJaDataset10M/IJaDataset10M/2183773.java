package com.proyecto.tropero.core.service.model.Interface;

import java.util.List;
import com.proyecto.tropero.core.domain.Raza;
import com.proyecto.tropero.core.service.IGenericService;

public interface IRazaService extends IGenericService {

    public Raza getRazaByDescripcion(String descripcion);
}
