package com.scythebill.birdlist.ui.guice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.scythebill.birdlist.model.io.ProgressInputStream;
import com.scythebill.birdlist.model.sighting.ReportSet;
import com.scythebill.birdlist.model.taxa.Taxonomy;
import com.scythebill.birdlist.model.taxa.TaxonomyMappings;
import com.scythebill.birdlist.model.xml.XmlReportSetImport;
import com.scythebill.birdlist.ui.util.Progress;

/**
 * Loads ReportSets, possibly in the background
 */
public class ReportSetLoader implements Callable<ReportSet>, Progress {

    private final File file;

    private final long size;

    private final Future<Taxonomy> taxonomyFuture;

    private volatile ProgressInputStream progressStream;

    private final TaxonomyMappings mappings;

    public ReportSetLoader(File file, Future<Taxonomy> future, TaxonomyMappings mappings) {
        this.file = Preconditions.checkNotNull(file);
        this.taxonomyFuture = Preconditions.checkNotNull(future);
        this.mappings = mappings;
        this.size = file.length();
    }

    @Override
    public ReportSet call() throws Exception {
        Taxonomy taxonomy = taxonomyFuture.get();
        progressStream = new ProgressInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(progressStream, Charsets.UTF_8));
        try {
            XmlReportSetImport rsi = new XmlReportSetImport();
            return rsi.importReportSet(reader, taxonomy, mappings);
        } finally {
            reader.close();
        }
    }

    @Override
    public long current() {
        return (progressStream == null) ? 0 : progressStream.getCurrentPosition();
    }

    @Override
    public long max() {
        return size;
    }
}
