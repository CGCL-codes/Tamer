package org.gvsig.xmlschema.som.impl;

import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSExtension;
import org.gvsig.xmlschema.som.IXSNode;
import org.gvsig.xmlschema.som.IXSRestriction;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.utils.SchemaCollection;
import org.gvsig.xmlschema.utils.SchemaObjectsMapping;
import org.gvsig.xmlschema.utils.SchemaTags;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class XSContentImpl extends XSComponentImpl implements IXSContentType {

    public XSContentImpl(IXSSchema schema) {
        super(schema);
    }

    public IXSExtension getExtension() {
        IXSNode node = new SchemaCollection(getSchema(), getElement(), getExtensionMapping()).getFirstNode();
        if (node != null) {
            return (IXSExtension) node;
        }
        return null;
    }

    /**
	 * @return The extension mapping
	 * @throws TypeNotFoundException
	 */
    private SchemaObjectsMapping getExtensionMapping() {
        SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
        elementTm.addType(SchemaTags.EXTENSION, XSExtensionImpl.class);
        return elementTm;
    }

    public IXSRestriction getRestriction() {
        IXSNode node = new SchemaCollection(getSchema(), getElement(), getRestrictionMapping()).getFirstNode();
        if (node != null) {
            return (IXSRestriction) node;
        }
        return null;
    }

    /**
	 * @return The restriction mapping
	 * @throws TypeNotFoundException
	 */
    private SchemaObjectsMapping getRestrictionMapping() {
        SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
        elementTm.addType(SchemaTags.RESTRICTION, XSRestrictionImpl.class);
        return elementTm;
    }
}
