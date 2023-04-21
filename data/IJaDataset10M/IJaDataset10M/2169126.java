package pl.matt.media.extractor.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import pl.matt.media.extractor.AbstractExtractor;
import pl.matt.media.manager.MediaManagersFactory;
import pl.matt.model.Rectangle;

/**
 * @deprecated
 * @author mateusz
 * 
 */
public class FacesExtractor extends AbstractExtractor {

    @SuppressWarnings("unused")
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FacesExtractor.class);

    private String facesDirectory = "/tmp/frames2/";

    private String imageExtension = ".jpg";

    private String faceFileName = "00_face";

    private List<File> files;

    public FacesExtractor(String facesDirectory, String faceFileName) {
        this.facesDirectory = facesDirectory;
        this.faceFileName = faceFileName;
    }

    @Override
    protected Collection<Rectangle> extract(File file) {
        return MediaManagersFactory.getObjectsDetectorManager().detectFaces(file.getAbsolutePath());
    }

    @Override
    protected List<File> getFiles() {
        if (files == null) {
            File directory = new File(facesDirectory);
            File[] files = directory.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(imageExtension);
                }
            });
            List<File> filesList = Arrays.asList(files);
            Collections.sort(filesList);
            this.files = filesList;
        }
        return files;
    }

    @Override
    public List<Rectangle> extract() throws IOException {
        for (File file : getFiles()) {
            String dir = getOutputDirName(file);
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
        return super.extract();
    }

    private String getOutputDirName(File inputFile) {
        String dirName = facesDirectory + File.separator + inputFile.getName().split("\\.")[0];
        return dirName;
    }

    @Override
    protected File getResultFile(File inputFile, int imageIndex) {
        String dirName = getOutputDirName(inputFile);
        return new File(dirName + File.separator + faceFileName + imageExtension);
    }
}
