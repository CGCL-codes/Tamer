package org.jcvi.glk;

import java.util.Map;
import java.util.Set;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentAttribute;
import org.jcvi.glk.ExtentAttributeType;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.Library;
import org.jcvi.glk.SequenceRead;

/**
 * A Test Double for {@link Extent}.
 * 
 * @author jsitz
 * @author dkatzel
 */
public class ExtentTestDouble extends Extent {

    public ExtentTestDouble(ExtentType type, String reference, Extent parent, String description, Library library) {
        super(type, reference, parent, description, library);
    }

    public ExtentTestDouble(ExtentType type, String reference, Extent parent, String desc) {
        super(type, reference, parent, desc);
    }

    public ExtentTestDouble(ExtentType type, String reference, Extent parent) {
        super(type, reference, parent);
    }

    @Override
    public Map<ExtentAttributeType, ExtentAttribute> getAttributeMap() {
        return super.getAttributeMap();
    }

    @Override
    public Set<Extent> getRealChildren() {
        return super.getRealChildren();
    }

    @Override
    public Set<SequenceRead> getRealSequences() {
        return super.getRealSequences();
    }
}
