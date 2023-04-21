package ramon.xml;

import org.w3c.dom.Element;

/**
 * Representa un objeto que puede ser convertido en XML.
 */
public interface XmlTranslatable {

    /**
	 * Produce una versión XML del objeto. El método produce un elemento XML que
	 * puede ser puesto dentro de un XML mayor.
	 * @param padre El elemento que va a ser padre del elemento a generar
	 * @param nombre El nombre del tag a generar
	 */
    public void toXml(Element padre, String nombre);
}
