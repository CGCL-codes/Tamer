package org.jcvi.vapor.task;

import java.io.File;
import java.io.FileNotFoundException;
import org.jcvi.assembly.Contig;
import org.jcvi.assembly.PlacedRead;
import org.jcvi.assembly.analysis.ContigChecker;
import org.jcvi.datastore.ContigDataStore;
import org.jcvi.datastore.DefaultContigFileDataStore;
import org.jcvi.vapor.SampleTarget;
import org.jcvi.vapor.VaporExecutive;
import org.jcvi.vapor.VaporRuntimeException;

/**
 * A <code>LocalContigCheckerTask</code> runs a {@link ContigChecker} against the contigs
 * contained in a local result .contig file.
 *
 * @author jsitz@jcvi.org
 */
public class LocalContigCheckerTask extends ContigCheckerTask {

    /** The extension placed on the end of the master contig file. */
    private static final String CONTIG_EXTENSION = ".contig";

    /**
     * Creates a new <code>LocalContigCheckerTask</code>.
     *
     * @param target The {@link SampleTarget} to check.
     * @param exec The {@link VaporExecutive} managing the sample.
     */
    public LocalContigCheckerTask(SampleTarget target, VaporExecutive exec) {
        super(target, exec);
    }

    @Override
    protected ContigDataStore<PlacedRead, ? extends Contig<PlacedRead>> getContigs() {
        final File contigFile = new File(this.getTarget().getAssemblyDir(), this.getTarget().getReference() + LocalContigCheckerTask.CONTIG_EXTENSION);
        try {
            return new DefaultContigFileDataStore(contigFile);
        } catch (final FileNotFoundException e) {
            throw new VaporRuntimeException(e.getMessage(), e);
        }
    }
}
