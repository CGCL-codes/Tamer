package eu.pisolutions.io.filter;

import java.io.FileFilter;
import java.io.FilenameFilter;

public abstract class AbstractFilenameFilterTest extends AbstractFileFilterTest {

    protected AbstractFilenameFilterTest() {
        super();
    }

    @Override
    protected final FileFilter createFileFilter() {
        return new FilenameFilterAdapter(this.createFilenameFilter());
    }

    protected abstract FilenameFilter createFilenameFilter();
}
