package org.jcvi.common.core.assembly.scaffold.agp;

import org.jcvi.common.core.Direction;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.io.TextFileVisitor;

/**
 * User: aresnick
 * Date: Sep 9, 2009
 * Time: 2:36:28 PM
 * <p/>
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 * <p/>
 * Description:
 */
public interface AgpFileVisitor extends TextFileVisitor {

    /**
     *
     * @param scaffoldId
     * @param contigRange
     * @param contigId
     * @param dir
     */
    void visitContigEntry(String scaffoldId, Range contigRange, String contigId, Direction dir);
}
