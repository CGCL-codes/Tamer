package com.yubarta.docman;

import java.util.Iterator;
import org.openrdf.model.Graph;
import org.openrdf.model.Resource;

/**
 * Vocabulario de metadatos.
 * 
 * @author César
 */
public interface Vocabulary {

    /**
	 * Lista las clases de recursos definidas en el vocabulario.
	 * @return un iterador de objetos ResClass
	 */
    public Iterator getClasses();

    /**
	 * Lista las clases que no tienen definida una clase ancestro.
	 * @return un iterador de objetos ResClass
	 */
    public Iterator getRootClasses();

    /**
	 * Devuelve la definición de una clase en este vocabulario.
	 * @param sClass el identificador de la clase
	 */
    public ResClass getClass(String sClass);

    /**
	 * Devuelve la definición de una clase en este vocabulario a partir
	 * del tipo extendido de los recursos de la clase.
	 * @param sExtendedType el tipo extendido de la clase
	 */
    public ResClass getClassByExtendedType(String sExtendedType);

    /**
	 * Devuelve la definición de una propiedad en este vocabulario.
	 * @param sProperty el identificador de la propiedad
	 */
    public ResProperty getProperty(String sProperty);

    /**
	 * Devuelve la definición de una propiedad en este vocabulario a partir
	 * de su "short id"
	 * @see ResProperty#getShortId()
	 */
    public ResProperty getPropertyByShortId(String sShortId);

    /**
	 * Valida los metadatos de un recurso respecto al esquema.
	 */
    public void validateResource(ResMd rmd) throws ValidationException;

    /**
	 * Averigua el tipo mas concreto de un recurso dentro de un grafo.
	 * Dentro del grafo, un recurso puede tener varias definiciones de tipo;
	 * este método intenta devolver el tipo mas concreto.
	 * 
	 * @return un iterador de objetos ResClass con los tipos mas concretos,
	 * sin sus supertipos
	 */
    public Iterator getTypes(Graph gg, Resource rr);
}
