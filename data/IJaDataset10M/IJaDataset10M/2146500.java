package su.msu.cs.lvk.xml2pixy.jdom;

import su.msu.cs.lvk.xml2pixy.transform.Node;
import su.msu.cs.lvk.xml2pixy.transform.astvisitor.VisitorException;

/**
 * Some visitor for JDOM tree wrapper su.msu.cs.lvk.xml2pixy.transform.Node.
 * @see su.msu.cs.lvk.xml2pixy.transform.Node
 */
public interface JdomVisitor {

    /**
     * This method is called by Walker for every xml tag.
     * @param node xml tag wrapper
     * @throws VisitorException if some error occured
     */
    void visit(Node node) throws VisitorException;
}
