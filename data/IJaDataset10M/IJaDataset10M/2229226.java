package net.bcharris.photomosaic;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

class SourceImageFinder extends DirectoryWalker {

    public SourceImageFinder() {
        super(new AndFileFilter(notHiddenFileFilter(), new OrFileFilter(imageFileFilter(), DirectoryFileFilter.INSTANCE)), -1);
    }

    public List<File> findSourceImages(File directory) {
        List<File> results = Lists.newArrayList();
        try {
            walk(directory, results);
        } catch (IOException ex) {
            throw new RuntimeException("Problem finding source images", ex);
        }
        return results;
    }

    @Override
    protected void handleFile(File file, int depth, Collection results) throws IOException {
        results.add(file);
    }

    private static IOFileFilter notHiddenFileFilter() {
        return new NotFileFilter(new OrFileFilter(HiddenFileFilter.HIDDEN, new PrefixFileFilter(".")));
    }

    private static IOFileFilter imageFileFilter() {
        return new SuffixFileFilter(new String[] { "png", "jpg", "jpeg", "gif", "bmp", "tif", "tiff" }, IOCase.INSENSITIVE);
    }
}
