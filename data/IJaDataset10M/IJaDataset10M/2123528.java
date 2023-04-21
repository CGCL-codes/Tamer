package com.entelience.test.test04probe;

import java.io.InputStream;
import java.util.Map;
import com.entelience.objects.probes.ProbeParameter;
import com.entelience.probe.FileImport;
import com.entelience.probe.FileProbe;
import com.entelience.probe.FileState;
import com.entelience.probe.LocalFileState;
import com.entelience.sql.Db;

/**
 * Test to see that when a probe errors with a runtime exception it registers appropriate information in the database.
 * @see testBug116
 */
public class RTEFileProbe extends FileProbe {

    public String getName() {
        return "RTEFileProbe";
    }

    public void cliConfigure(Db statusDb) {
    }

    @Override
    public void configureProbeWithParameters(Db statusDb, Map<String, ProbeParameter> pp) throws Exception {
    }

    @Override
    protected void transferProbePreferences(FileImport imp) throws Exception {
    }

    protected void prepare() throws Exception {
    }

    protected void checkWouldConsume(Db statusDb, FileState fs) {
    }

    protected boolean checkFormat(LocalFileState lfs, InputStream is) throws Exception {
        return true;
    }

    @Override
    protected FileImport getFileImport() throws Exception {
        return new FileImport() {

            @Override
            protected void extractMetadata(LocalFileState lfs) throws Exception {
            }

            @Override
            protected boolean parseFile(InputStream is) throws Exception {
                throw new IllegalArgumentException("RTEFileProbe always errors with a Runtime Exception.");
            }

            @Override
            protected boolean nothingImported() throws Exception {
                return false;
            }

            @Override
            protected boolean wasEmptyFile() throws Exception {
                return false;
            }

            @Override
            protected void preParse() throws Exception {
            }

            @Override
            protected void prepare() throws Exception {
            }

            @Override
            protected long getCountElements() {
                return 0;
            }

            @Override
            protected long getCountValidElements() {
                return 0;
            }

            protected void treatmentAfterRead() {
            }
        };
    }

    @Override
    protected boolean isAssetProbe() {
        return false;
    }
}
