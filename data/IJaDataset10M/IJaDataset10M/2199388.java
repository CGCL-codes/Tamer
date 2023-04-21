package org.xmlfield.core.internal;

import org.w3c.dom.Node;

/**
 * interface essentiellement utilisée par le moteur de lecture du XML vers Java,
 * mais rien n'empêche de déclarer une interface Java de manipulation du XML
 * héritant de <tt>INodeable</tt> afin de récupérer le nœud XML correspondant.
 * 
 * @author David Andrianavalontsalama
 * 
 */
public interface INodeable {

    /**
     * renvoie le nœud XML correspondant à cet objet.
     * 
     * @return le nœud XML.
     */
    Node toNode();
}
