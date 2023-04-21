package org.tzi.ugt.parser;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.tzi.ugt.main.Exception;
import org.tzi.ugt.model.ObjectDiagram;
import org.tzi.ugt.model.uml.Association;
import org.tzi.ugt.model.uml.Link;
import org.tzi.ugt.model.uml.LinkEnd;
import org.tzi.use.parser.SemanticException;

/**
 * Represents a link of the UML meta model in the abstract syntax tree.
 * 
 * @author lschaps
 */
class ASTLink {

    private ASTObjectName m_ObjectName;

    private Vector m_LinkEnds;

    /**
	 * Constructs a link node for the abstract syntax tree,
	 * 
	 * @param in_Name
	 */
    ASTLink(ASTObjectName in_Name) {
        m_ObjectName = in_Name;
        m_LinkEnds = new Vector();
    }

    /**
	 * Adds a LinkEnd node to the link node.
	 * 
	 * @param in_LinkEnd
	 *            The LinkEnd for the Link node.
	 */
    void addLinkEnd(ASTLinkEnd in_LinkEnd) {
        m_LinkEnds.add(in_LinkEnd);
    }

    /**
	 * Generate the link node for the UGT data model.
	 * 
	 * @param ctx
	 *            The context of the link node.
	 * @param in_mod
	 *            The object diagram of the link node.
	 * 
	 * @return Returns a Link node for the UGT data model.
	 * 
	 * @throws SemanticException
	 *             Exception is thrown when semantic conditions are violated.
	 *             (e.g. not all AssociationEnds defined.)
	 */
    Link gen(UGTContext ctx, ObjectDiagram in_mod) throws SemanticException {
        Association ma;
        try {
            ma = ctx.getUGTMModel().getAssociation(m_ObjectName.getType().getText());
        } catch (Exception e) {
            throw new SemanticException(m_ObjectName.getType(), e);
        }
        Link link = ctx.getUGTModelFactory().createLink(m_ObjectName.getName().getText(), ma);
        List assocEnds = new Vector(ma.associationEnds());
        Iterator it = m_LinkEnds.iterator();
        while (it.hasNext()) {
            ASTLinkEnd astLinkEnd = (ASTLinkEnd) it.next();
            LinkEnd linkEnd = astLinkEnd.gen(ctx, link, in_mod, assocEnds);
            link.addEnd(linkEnd);
            assocEnds.remove(linkEnd.getAssociationEnd());
        }
        if (0 < assocEnds.size()) {
            throw new SemanticException(m_ObjectName.getName(), "Not all AssociationEnds defined");
        }
        return link;
    }
}
