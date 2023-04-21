package net.sourceforge.seqware.queryengine.tools.importers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.BufferedReader;
import com.sleepycat.db.Transaction;
import net.sourceforge.seqware.queryengine.backend.factory.impl.BerkeleyDBFactory;
import net.sourceforge.seqware.queryengine.backend.model.Coverage;
import net.sourceforge.seqware.queryengine.backend.model.Variant;
import net.sourceforge.seqware.queryengine.backend.store.impl.BerkeleyDBStore;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareException;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareSettings;
import net.sourceforge.seqware.queryengine.tools.importers.workers.ImportWorker;
import net.sourceforge.seqware.queryengine.tools.importers.workers.PileupImportWorker;
import net.sourceforge.seqware.queryengine.tools.iterators.processors.VariantProcessor;

/**
 * @author boconnor
 *
 * TODO:
 * 
 */
public class VariantImporter extends Importer {

    /**
   * @param args
   */
    public static void main(String[] args) {
        if (args.length < 14) {
            System.out.println("VariantImporter <worker_module> <db_dir> <create_db> <min_coverage> <max_coverage> <min_snp_quality> " + "<compressed> <include_indels> <include_snv> <fastqConvNum> <cacheSize> <locks> " + "<max_thread_count> <input_file(s)>");
            System.exit(-1);
        }
        String workerModule = args[0];
        String dbDir = args[1];
        boolean create = false;
        if ("true".equals(args[2])) {
            create = true;
        }
        int minCoverage = Integer.parseInt(args[3]);
        int maxCoverage = Integer.parseInt(args[4]);
        int minSnpQuality = Integer.parseInt(args[5]);
        boolean compressed = false;
        if ("true".equals(args[6])) {
            compressed = true;
        }
        boolean includeIndels = false;
        if ("true".equals(args[7])) {
            includeIndels = true;
        }
        boolean includeSNV = false;
        if ("true".equals(args[8])) {
            includeSNV = true;
        }
        int fastqConvNum = Integer.parseInt(args[9]);
        long cacheSize = Long.parseLong(args[10]);
        int locks = Integer.parseInt(args[11]);
        int threadCount = Integer.parseInt(args[12]);
        boolean importCoverage = false;
        int binSize = 0;
        ArrayList<String> inputFiles = new ArrayList<String>();
        for (int i = 13; i < args.length; i++) {
            inputFiles.add(args[i]);
        }
        BerkeleyDBFactory factory = new BerkeleyDBFactory();
        BerkeleyDBStore store = null;
        VariantImporter pmi = new VariantImporter(threadCount);
        try {
            SeqWareSettings settings = new SeqWareSettings();
            settings.setStoreType("berkeleydb-mismatch-store");
            settings.setFilePath(dbDir);
            settings.setCacheSize(cacheSize);
            settings.setCreateMismatchDB(create);
            settings.setCreateConsequenceAnnotationDB(create);
            settings.setCreateDbSNPAnnotationDB(create);
            settings.setCreateCoverageDB(create);
            settings.setMaxLockers(locks);
            settings.setMaxLockObjects(locks);
            settings.setMaxLocks(locks);
            store = factory.getStore(settings);
            if (store != null) {
                Iterator<String> it = inputFiles.iterator();
                ImportWorker[] workerArray = new ImportWorker[inputFiles.size()];
                int index = 0;
                while (it.hasNext()) {
                    String input = (String) it.next();
                    System.out.println("Starting worker thread to process file: " + input);
                    Class processorClass = Class.forName("net.sourceforge.seqware.queryengine.tools.importers.workers." + workerModule);
                    workerArray[index] = (ImportWorker) processorClass.newInstance();
                    workerArray[index].setWorkerName("PileupWorker" + index);
                    workerArray[index].setPmi(pmi);
                    workerArray[index].setStore(store);
                    workerArray[index].setInput(input);
                    workerArray[index].setCompressed(compressed);
                    workerArray[index].setMinCoverage(minCoverage);
                    workerArray[index].setMaxCoverage(maxCoverage);
                    workerArray[index].setMinSnpQuality(minSnpQuality);
                    workerArray[index].setIncludeSNV(includeSNV);
                    workerArray[index].setFastqConvNum(fastqConvNum);
                    workerArray[index].setIncludeIndels(includeIndels);
                    workerArray[index].setIncludeCoverage(importCoverage);
                    workerArray[index].setBinSize(binSize);
                    workerArray[index].start();
                    index++;
                }
                System.out.println("Joining threads");
                for (int i = 0; i < workerArray.length; i++) {
                    workerArray[i].join();
                }
                System.out.println("Threads finished");
                store.close();
            }
        } catch (Exception e) {
            System.out.println("Exception!: " + e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public VariantImporter(int threadCount) {
        super(threadCount);
    }
}
