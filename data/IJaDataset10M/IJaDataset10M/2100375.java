package de.kumpe.hadooptimizer.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.SequenceFile.Reader;

abstract class FileReader<I> {

    public void readIndivuals(final Configuration conf, final Path... inputs) throws IOException {
        final FileSystem fileSystem = FileSystem.get(conf);
        for (final Path path : inputs) {
            if (fileSystem.getFileStatus(path).isDir()) {
                for (final FileStatus fileStatus : fileSystem.listStatus(path)) {
                    if (fileStatus.isDir()) {
                        continue;
                    }
                    readFile(conf, fileSystem, fileStatus.getPath());
                }
            } else {
                readFile(conf, fileSystem, path);
            }
        }
    }

    protected void readFile(final Configuration conf, final FileSystem fileSystem, final Path file) throws IOException {
        final DoubleWritable key = new DoubleWritable();
        final String name = file.getName();
        if (name.startsWith("_") || name.startsWith(".")) {
            return;
        }
        final Reader reader = new Reader(fileSystem, file, conf);
        while (reader.next(key)) {
            @SuppressWarnings("unchecked") final I i = (I) reader.getCurrentValue((Object) null);
            readIndividual(key, i);
        }
        reader.close();
    }

    protected abstract void readIndividual(DoubleWritable key, I individual);
}
