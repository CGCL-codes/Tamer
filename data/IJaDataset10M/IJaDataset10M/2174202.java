package org.kabeja.parser;

import org.kabeja.dxf.DXFDocument;

/**
 * @author <a href="mailto:simon.mieth@gmx.de>Simon Mieth</a>
 *
 */
public abstract class AbstractSectionHandler implements DXFSectionHandler {

    protected DXFDocument doc;

    public void setDXFDocument(DXFDocument doc) {
        this.doc = doc;
    }
}
