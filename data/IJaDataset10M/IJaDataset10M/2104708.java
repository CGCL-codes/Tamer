package work;

import geometry.serialization.jsg.PolygonSerializer;
import geometry.voronoi.PlaceEntry;
import geometry.voronoi.PlaceType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.geometry.TaggedGeometry;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.OptionHelper;
import util.TLongObjectHashMapValueIteraterable;
import util.Unchecked;
import com.vividsolutions.jts.geom.Geometry;

enum OutputMode {

    MODE_MANY_FILES, MODE_SINGLE_FILE
}

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class LevelProviderExportGeometries {

    static final Logger logger = LoggerFactory.getLogger(LevelProviderExportGeometries.class);

    private static String HELP_MESSAGE = "LevelProviderExportGeometries <args>";

    private static String OPTION_LEVELPROVIDER = "levelprovider";

    private static String OPTION_VORONOI = "voronoi";

    private static String OPTION_OUTPUT_DIRECTORY = "output_dir";

    private static String OPTION_OUTPUT_FILE = "output_file";

    private static String OPTION_OUTPUT_MODE = "type";

    private static String OUTPUT_MODE_MANY_FILES = "ofpg";

    private static String OUTPUT_MODE_ONE_FILE = "mixed";

    /**
	 * @param args
	 *            serialized version of levelprovider, output directory
	 */
    public static void main(String[] args) {
        Options options = new Options();
        OptionHelper.add(options, OPTION_LEVELPROVIDER, true, true, "file", "A serialization of a LevelProvider");
        OptionHelper.add(options, OPTION_VORONOI, true, true, "file", "A serialization of a voronoi cells");
        OptionHelper.add(options, OPTION_OUTPUT_MODE, true, true, "ofpg, mixed", "the output mode");
        OptionHelper.add(options, OPTION_OUTPUT_DIRECTORY, true, false, "directory", "A directory to put the files in");
        OptionHelper.add(options, OPTION_OUTPUT_FILE, true, false, "file", "A file to put the files in");
        GnuParser parser = new GnuParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("unable to parse command line:" + e.getMessage());
            new HelpFormatter().printHelp(HELP_MESSAGE, options);
        }
        if (line == null) {
            System.exit(1);
            return;
        }
        String filenameLevelprovider = line.getOptionValue(OPTION_LEVELPROVIDER);
        String filenameVoronoi = line.getOptionValue(OPTION_VORONOI);
        String outputMode = line.getOptionValue(OPTION_OUTPUT_MODE);
        OutputMode mode = OutputMode.MODE_MANY_FILES;
        if (outputMode.equals(OUTPUT_MODE_MANY_FILES)) {
            mode = OutputMode.MODE_MANY_FILES;
        } else if (outputMode.equals(OUTPUT_MODE_ONE_FILE)) {
            mode = OutputMode.MODE_SINGLE_FILE;
        } else {
            logger.error("unknown export mode");
            new HelpFormatter().printHelp(HELP_MESSAGE, options);
            System.exit(1);
        }
        switch(mode) {
            case MODE_MANY_FILES:
                if (!line.hasOption(OPTION_OUTPUT_DIRECTORY)) {
                    logger.debug("in this mode you need to specify the output directory");
                    new HelpFormatter().printHelp(HELP_MESSAGE, options);
                    System.exit(1);
                }
                break;
            case MODE_SINGLE_FILE:
                if (!line.hasOption(OPTION_OUTPUT_FILE)) {
                    logger.debug("in this mode you need to specify the output file");
                    new HelpFormatter().printHelp(HELP_MESSAGE, options);
                    System.exit(1);
                }
                break;
        }
        String dirnameOutput = line.getOptionValue(OPTION_OUTPUT_DIRECTORY);
        String filenameOutput = line.getOptionValue(OPTION_OUTPUT_FILE);
        String path = null;
        if (mode == OutputMode.MODE_MANY_FILES) {
            File dir = new File(dirnameOutput);
            if (dir.exists() && !dir.isDirectory()) {
                logger.debug("output file exists, but is not a directory");
                System.exit(1);
            }
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    logger.debug("unable to create directory");
                }
            }
            if (!dir.exists()) {
                logger.debug("directory doesn' exist");
                System.exit(1);
            }
            try {
                path = dir.getCanonicalPath();
                logger.debug("canonical pathname is: " + path);
            } catch (IOException e) {
                logger.debug("unable to create canonical pathname");
            }
        } else if (mode == OutputMode.MODE_SINGLE_FILE) {
            path = filenameOutput;
        }
        if (path == null) {
            System.exit(1);
            return;
        }
        LevelProvider levelProvider = null;
        logger.debug("reading levelprovider");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filenameLevelprovider));
            levelProvider = Unchecked.cast(ois.readObject());
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (levelProvider == null) {
            logger.debug("unable to read levelprovider");
            System.exit(1);
            return;
        }
        Map<PlaceEntry, Geometry> voronois = null;
        logger.debug("reading voronoi");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filenameVoronoi));
            ois.readObject();
            voronois = Unchecked.cast(ois.readObject());
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (voronois == null) {
            System.exit(1);
            return;
        }
        LevelProviderExportGeometries lpeg = new LevelProviderExportGeometries();
        Exporter exporter = null;
        switch(mode) {
            default:
            case MODE_MANY_FILES:
                exporter = lpeg.new ExporterManyFiles(path);
                break;
            case MODE_SINGLE_FILE:
                exporter = lpeg.new ExporterSingleFile(path);
                break;
        }
        for (PartitionElement element : new TLongObjectHashMapValueIteraterable<PartitionElement>(levelProvider.getLevel4Selected())) {
            exporter.exportBoundary(element);
        }
        for (PartitionElement element : new TLongObjectHashMapValueIteraterable<PartitionElement>(levelProvider.getLevel6())) {
            exporter.exportBoundary(element);
        }
        for (PartitionElement element : new TLongObjectHashMapValueIteraterable<PartitionElement>(levelProvider.getLevel8())) {
            exporter.exportBoundary(element);
        }
        for (PlaceEntry entry : voronois.keySet()) {
            PartitionType type = PartitionType.PLACE_CITY;
            if (entry.getType() == PlaceType.PLACE_TOWN) type = PartitionType.PLACE_TOWN;
            if (entry.getType() == PlaceType.PLACE_VILLAGE) type = PartitionType.PLACE_VILLAGE;
            Geometry boundary = voronois.get(entry);
            PartitionElement element = new PartitionElement(type, entry.getNode(), boundary);
            exporter.exportBoundary(element);
        }
        if (mode == OutputMode.MODE_SINGLE_FILE) {
            ExporterSingleFile esf = (ExporterSingleFile) exporter;
            try {
                esf.close();
            } catch (Exception e) {
                logger.error("unable to write file: " + e.getMessage());
            }
        }
    }

    private interface Exporter {

        public void exportBoundary(PartitionElement element);
    }

    private class ExporterManyFiles implements Exporter {

        private String dirpath;

        public ExporterManyFiles(String dirpath) {
            this.dirpath = dirpath;
        }

        @Override
        public void exportBoundary(PartitionElement element) {
            long id = element.getEntity().getId();
            Geometry boundary = element.getBoundary();
            PartitionType type = element.getType();
            String filename = dirpath.concat("/").concat("" + type + "_" + id + ".jsg");
            logger.debug(filename);
            try {
                PolygonSerializer.write(filename, boundary);
            } catch (IOException e) {
                logger.debug("unable to write geometry: " + e.getMessage());
            }
        }
    }

    private class ExporterSingleFile implements Exporter {

        private String filename;

        private List<TaggedGeometry> elements = new ArrayList<TaggedGeometry>();

        public ExporterSingleFile(String filename) {
            this.filename = filename;
        }

        @Override
        public void exportBoundary(PartitionElement element) {
            TaggedGeometry tpe = new TaggedGeometry(element.getBoundary());
            String type = "type";
            String value = element.getType().toString();
            tpe.addTag(type, value);
            elements.add(tpe);
        }

        public void close() throws FileNotFoundException, IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(elements);
            oos.close();
        }
    }
}
