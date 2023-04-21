package org.spantus.work.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.work.SpantusBundle;

public class BundleZipDaoImpl implements BundleDao {

    private MarkerDao markerDao;

    private ReaderDao readerDao;

    public static final String BUNDLE_FILE_SAMPLE = "sample.sspnt.xml";

    public static final String BUNDLE_FILE_MARKER = "markers.mspnt.xml";

    public SpantusBundle read(File zipFile) {
        SpantusBundle bundle = new SpantusBundle();
        ZipFile input;
        IExtractorInputReader reader;
        MarkerSetHolder holder;
        try {
            input = new ZipFile(zipFile);
            InputStream inputStream = input.getInputStream(input.getEntry(BUNDLE_FILE_SAMPLE));
            reader = WorkServiceFactory.createReaderDao().read(inputStream);
            inputStream = input.getInputStream(input.getEntry(BUNDLE_FILE_MARKER));
            holder = WorkServiceFactory.createMarkerDao().read(inputStream);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bundle.setHolder(holder);
        bundle.setReader(reader);
        return bundle;
    }

    public void write(SpantusBundle bundle, File zipFile) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_SAMPLE));
            readerDao.write(bundle.getReader(), out);
            out.putNextEntry(new ZipEntry(BUNDLE_FILE_MARKER));
            markerDao.write(bundle.getHolder(), out);
            out.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setMarkerDao(MarkerDao markerDao) {
        this.markerDao = markerDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }
}
