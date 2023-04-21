package de.catsoft.rdbs2j.objectmodel;

import java.util.*;
import java.lang.reflect.*;
import java.io.Serializable;
import org.w3c.dom.Node;

/**
 * OMOperation repr�sentiert die Methoden, die f�r eine Klasse generiert werden 
 * sollen. Im konkreten Fall sind es die Informationen f�r Java Code.
 * @author <a href="mailto:GBeutler@cat-gmbh.de">Guido Beutler</a>
 * @version 1.0
 * @copyright (c)2002,2003 by CAT Computer Anwendung Technologie GmbH
 * Oststr. 34
 * 50374 Erftstadt
 * Germany
 * 
 * @license This file is part of RDBS2J.
 * 
 * RDBS2J is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * RDBS2J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RDBS2J; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class OMOperation extends OMBase implements Serializable, OMRemovable {

    private static final long serialVersionUID = 200201231;

    private boolean _final = false;

    private boolean _static = false;

    private boolean _synchronized = false;

    /**
    * OMClassPersistenceData Objekt, zu der ddie Operation geh�rt.
    */
    private OMClassPersistenceData _class;

    private OMVisibility _visibility;

    /**
    * Typ des R�ckgabewertes.
    */
    private OMType _result;

    /**
    * Liste der Typen der Argumente (die Liste der Namen fehlt noch).
    */
    private Collection _parameter = new ArrayList();

    /**
    * @param om
    * @param node
    * @throws de.catsoft.rdbs2j.objectmodel.OMDomConstructorException
    * @roseuid 3C6D37180346
    */
    public OMOperation(ObjectModel om, Node node) throws OMDomConstructorException {
    }

    /**
    * @roseuid 3C4FE14501DE
    */
    public OMOperation() {
        try {
            _visibility = new OMVisibility(OMVisibility.PUBLIC);
        } catch (OMIllegalTypeIDException e) {
            e.printStackTrace();
        }
    }

    /**
    * @param recursiv
    * @throws de.catsoft.rdbs2j.objectmodel.OMReferenceException
    * @roseuid 3C5817AB038B
    */
    public void remove(boolean recursiv) throws OMReferenceException {
    }
}
