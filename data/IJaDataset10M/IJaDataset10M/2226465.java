package org.jcvi.common.core.assembly.util.slice;

import org.jcvi.common.core.Direction;
import org.jcvi.common.core.assembly.util.slice.DefaultSliceElement;
import org.jcvi.common.core.symbol.qual.PhredQuality;
import org.jcvi.common.core.symbol.residue.nt.Nucleotide;

/**
 * @author dkatzel
 *
 *
 */
public class TestDefaultSliceElement extends AbstractTestIdedSliceElement {

    /**
    * {@inheritDoc}
    */
    @Override
    protected IdedSliceElement create(String id, Nucleotide base, PhredQuality qual, Direction direction) {
        return new DefaultSliceElement(id, base, qual, direction);
    }
}
