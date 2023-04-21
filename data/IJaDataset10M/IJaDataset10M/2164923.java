package org.jcvi.assembly.ace.consed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jcvi.assembly.ace.AceAssembly;
import org.jcvi.assembly.ace.AceContig;
import org.jcvi.assembly.ace.AceFileWriter;
import org.jcvi.assembly.ace.AcePlacedRead;
import org.jcvi.assembly.slice.SliceMapFactory;
import org.jcvi.datastore.DataStore;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.io.IOUtil;
import org.jcvi.io.fileServer.DirectoryFileServer;
import org.jcvi.io.fileServer.ReadWriteFileServer;
import org.jcvi.trace.sanger.phd.Phd;
import org.jcvi.trace.sanger.phd.PhdWriter;

public class ConsedWriter {

    public static void writeConsedPackage(final AceAssembly aceAssembly, final SliceMapFactory sliceMapFactory, File consedOuputDir, String prefix, final boolean calculateBestSegments) throws Exception {
        ReadWriteFileServer consedFolder = DirectoryFileServer.createReadWriteDirectoryFileServer(consedOuputDir);
        consedFolder.createNewDir("edit_dir");
        consedFolder.createNewDir("phd_dir");
        File aceFile = consedFolder.createNewFile("edit_dir/" + prefix + ".ace.1");
        final String phdPath = "phd_dir/" + prefix + ".phd.ball";
        File phdFile = consedFolder.createNewFile(phdPath);
        final OutputStream phdOutputStream = new FileOutputStream(phdFile);
        final OutputStream aceOutputStream = new FileOutputStream(aceFile);
        final DataStore<Phd> phdDataStore = aceAssembly.getPhdDataStore();
        final DataStore<AceContig> contigDataStore = aceAssembly.getContigDataStore();
        final ExecutorService executor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()) {

            @Override
            protected synchronized void afterExecute(Runnable r, Throwable t) {
                if (t != null) {
                    t.printStackTrace();
                    this.shutdownNow();
                    throw new RuntimeException(t);
                }
            }
        };
        List<Callable<Void>> writers = new ArrayList<Callable<Void>>();
        try {
            final Callable<Void> phdWriter = new Callable<Void>() {

                @Override
                public Void call() throws IOException, DataStoreException {
                    for (AceContig contig : contigDataStore) {
                        for (AcePlacedRead read : contig.getPlacedReads()) {
                            String id = read.getId();
                            PhdWriter.writePhd(phdDataStore.get(id), phdOutputStream);
                        }
                    }
                    return null;
                }
            };
            writers.add(phdWriter);
            final Callable<Void> aceWriter = new Callable<Void>() {

                @Override
                public Void call() throws IOException, DataStoreException {
                    AceFileWriter.writeAceFile(aceAssembly, sliceMapFactory, aceOutputStream, calculateBestSegments);
                    return null;
                }
            };
            writers.add(aceWriter);
            for (Future<Void> futures : executor.invokeAll(writers)) {
                futures.get();
            }
            consedFolder.createNewSymLink("../" + phdPath, "edit_dir/phd.ball");
        } finally {
            executor.shutdownNow();
            IOUtil.closeAndIgnoreErrors(phdOutputStream);
            IOUtil.closeAndIgnoreErrors(aceOutputStream);
            IOUtil.closeAndIgnoreErrors(consedFolder);
        }
    }
}
