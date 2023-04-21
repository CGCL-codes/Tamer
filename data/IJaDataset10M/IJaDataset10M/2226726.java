package org.jcvi.glk.elvira;

import org.junit.Before;
import org.junit.Test;
import org.jcvi.glk.CloningSystem;
import org.jcvi.glk.EUID;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.Library;
import org.jcvi.glk.SequenceDirection;
import org.jcvi.glk.TrimSequence;
import org.jcvi.glk.TrimSequenceAttributeType;
import org.jcvi.glk.elvira.AmpliconExtent;
import static org.junit.Assert.*;

public class TestAmpliconExtent {

    private class AmpliconExtentTestDouble extends AmpliconExtent {

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         */
        public AmpliconExtentTestDouble() {
            super();
        }

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         * @param id
         */
        public AmpliconExtentTestDouble(EUID id) {
            super(id);
        }

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         * @param type
         * @param reference
         * @param parent
         * @param description
         * @param library
         */
        public AmpliconExtentTestDouble(ExtentType type, String reference, Extent parent, String description, Library library) {
            super(type, reference, parent, description, library);
        }

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         * @param type
         * @param reference
         * @param parent
         * @param desc
         */
        public AmpliconExtentTestDouble(ExtentType type, String reference, Extent parent, String desc) {
            super(type, reference, parent, desc);
        }

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         * @param type
         * @param reference
         * @param parent
         */
        public AmpliconExtentTestDouble(ExtentType type, String reference, Extent parent) {
            super(type, reference, parent);
        }

        /**
         * Creates a new <code>AmpliconExtentTestDouble</code>. 
         *
         * @param type
         * @param reference
         * @param description
         */
        public AmpliconExtentTestDouble(ExtentType type, String reference, String description) {
            super(type, reference, description);
        }

        @Override
        public Long getCoordinateFor(TrimSequence trimSeq) {
            return super.getCoordinateFor(trimSeq);
        }
    }

    private AmpliconExtentTestDouble ampliconExtent;

    private CloningSystem cloningSystem;

    private TrimSequence forwardSequence, reverseSequence;

    @Before
    public void setUp() throws Exception {
        ampliconExtent = new AmpliconExtentTestDouble();
        Library library = new Library();
        cloningSystem = new CloningSystem();
        library.setCloningSystem(cloningSystem);
        ampliconExtent.setLibrary(library);
        forwardSequence = new TrimSequence("name", SequenceDirection.FORWARD);
        reverseSequence = new TrimSequence("name", SequenceDirection.REVERSE);
    }

    @Test
    public void noTrimSequences_startShouldReturnNull() {
        assertNull(ampliconExtent.getStartCoordinate());
    }

    @Test
    public void noTrimSequences_endShouldReturnNull() {
        assertNull(ampliconExtent.getEndCoordinate());
    }

    @Test
    public void noForwardTrimSequence_startShouldReturnNull() {
        cloningSystem.addTrimSequences(reverseSequence);
        assertNull(ampliconExtent.getStartCoordinate());
    }

    @Test
    public void noReverseTrimSequence_endShouldReturnNull() {
        cloningSystem.addTrimSequences(forwardSequence);
        assertNull(ampliconExtent.getEndCoordinate());
    }

    @Test
    public void getCoordinateFor_noCoordinateAttribute() {
        assertNull(ampliconExtent.getCoordinateFor(forwardSequence));
    }

    @Test
    public void getCoordinateFor() {
        Long oneBasedValue = Long.valueOf(1234);
        forwardSequence.setAttribute(new TrimSequenceAttributeType(1, AmpliconExtent.COORDINATE_ATTR), oneBasedValue.toString());
        assertEquals(Long.valueOf(oneBasedValue - 1), ampliconExtent.getCoordinateFor(forwardSequence));
    }

    @Test
    public void getCoordinateFor_unparsable_shouldReturnMIN_VALUE() {
        forwardSequence.setAttribute(new TrimSequenceAttributeType(1, AmpliconExtent.COORDINATE_ATTR), "INVALID LONG");
        assertEquals(Long.MIN_VALUE, ampliconExtent.getCoordinateFor(forwardSequence).longValue());
    }

    @Test
    public void getCoordinateFor_coordinateDNE_shouldReturnNull() {
        assertNull(ampliconExtent.getCoordinateFor(forwardSequence));
    }
}
