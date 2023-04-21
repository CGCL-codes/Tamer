package ar.com.oddie.core.xmlwrapper;

/**
 * <p>
 * Interfaz para las primitivas correspondientes a la lectura de archivos XML.
 * </p>
 * 
 * @author hernan
 * 
 */
public interface Reader<T> {

    /**
	 * Obtiene el siguiente elemento del archivo XML que se esta leyendo.
	 * 
	 * @return instancia del objeto leido del archivo XML.
	 */
    public T getNextElement();

    /**
	 * Indica si existe un elemento mas para ser leido en el archivo XML.
	 * 
	 * @return true en caso de existir un proximo elemtno. False en caso
	 *         contrario.
	 */
    public boolean hasNextElement();
}
