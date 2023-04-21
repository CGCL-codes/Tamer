package com.movilnet.clom.web.controller.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.movilnet.clom.framework.repository.IGenericRepositoryRemote;

/**
 * @author jpacheco
 *
 */
@Stateless(name = "referenceData", mappedName = "referenceData")
public class ReferenceDataUtil<T> {

    @SuppressWarnings("rawtypes")
    @EJB(name = "ejb/genericRepository")
    private IGenericRepositoryRemote repository;

    /**
	 * Consulta una lista de data referencial basado en las entidades <b>Class</b> del mapa del argumento.
	 * El tipo de consulta ealizado es basado en <b>findAll</b>, no tiene criterio de busqueda, se consultan
	 * todos los elementos de la tabla en funcion Class type del mapa.
	 * 
	 * @param params - Objeto java.util.Map con las entidades de busqueda y su nombre de atributo
	 * que se asocia en la vista (formulario o vista). En el <b>key</b> se carga el nombre del atributo que se
	 * envia a la vista y el <b>value</b> el <b>Class</b> de la entidad a consultar.<br>
	 * Ejemplo:<br>
	 * Map<String, Class<T>> params = ....<br>
	 * params.put("personas", Person.class):<br>
	 * params.put("materias", Materia.class):<br>
	 * ......<br>
	 * ReferenceDataUtil::getAllDataListWithOutCriteria(params);
	 * 
	 * @return Objeto java.util.Map con la lista de objetos cargados y con el mismo key del argumento
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, List<T>> getAllDataListWithOutCriteria(Map<String, Class<T>> params) {
        Map<String, List<T>> result = new HashMap<String, List<T>>(0);
        for (final Entry<String, Class<T>> param : params.entrySet()) {
            result.put(param.getKey(), repository.findAll((Class) param.getValue()));
        }
        return result;
    }
}
