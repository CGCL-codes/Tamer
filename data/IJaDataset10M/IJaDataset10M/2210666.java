package com.editor.data;

import com.editor.util.IPersistencia;
import com.editor.util.Persistencia;
import com.editor.util.Util;
import org.kxml2.kdom.Element;

/**
 * Classe de dados destinada o objetos do tipo Data
 * @author Jo�o Victor
 */
public class Data extends Pergunta implements IPersistencia {

    /**
     * Gera o c�digo xml relativo a classe Data
     * @param pai
     * @return Element
     * @see IPersistencia
     * @see Element
     */
    @Override
    public Element toXMLElement(Element pai) {
        Element root = Persistencia.createElement(pai, "form");
        root.setAttribute(null, "id", Integer.toString(getId()));
        root.setAttribute(null, "requerid", getObrig().toString());
        Persistencia.createElement(root, "name", Util.xmlString(getTitulo()));
        Persistencia.createElement(root, "label", Util.xmlString(getPergunta()));
        Persistencia.createElement(root, "help", Util.xmlString(getHelp()));
        Element root2 = Persistencia.createElement(root, "text_field");
        Persistencia.createElement(root2, "type", "DATA");
        Persistencia.createElement(root2, "valida", "DATA");
        Persistencia.createElement(root2, "max_size", "8");
        Persistencia.createElements(root, "conditions", getRelacionamentos());
        return root;
    }

    /**
     *
     * @return String "Data"
     */
    @Override
    public String toString() {
        return "Data";
    }
}
