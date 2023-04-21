package org.form4j.form.mapper;

import org.apache.log4j.Logger;
import org.form4j.form.util.RefedElementEnumeration;
import org.w3c.dom.Element;

/**
 * Enumerate Mappings.
 * The Enumeration enumerates all child <code>&lt;Mappings&gt;</code> elements of
 * a parent element
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.2 $ $Date: 2004/05/29 05:08:17 $
 **/
public class MappingEnumeration extends RefedElementEnumeration {

    /**
     * construct mapping enumeration. The Enumeration enumerates
     * all child <code>&lt;Mappings&gt;</code> elements of
     * a parent element
     * @param parent parent xml element containing <code>&lt;Mappings&gt;</code> elements
     */
    public MappingEnumeration(final Element parent) {
        super(parent, "Mappings");
        LOG.debug("");
    }

    private static final Logger LOG = Logger.getLogger(MappingEnumeration.class.getName());
}
