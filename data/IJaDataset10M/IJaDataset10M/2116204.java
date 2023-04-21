package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.section;

import java.io.IOException;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.SCFChromatogram;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.header.SCFHeader;

public interface SectionEncoder {

    EncodedSection encode(SCFChromatogram c, SCFHeader header) throws IOException;
}
