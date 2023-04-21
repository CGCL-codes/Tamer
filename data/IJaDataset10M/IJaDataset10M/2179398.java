package org.eclipse.epsilon.egl.symmetric_ao.tasks.superimpose;

import de.ovgu.cide.fstgen.ast.FSTNode;

/**
 * Artifact handler for XML files.
 * 
 * @author zschaler
 */
public class XmlHandler implements ArtifactHandler {

    @Override
    public String print(FSTNode fst) {
        throw new UnsupportedOperationException("XML merging not currently supported.");
    }

    @Override
    public FSTNode process(String content) {
        throw new UnsupportedOperationException("XML merging not currently supported.");
    }
}
