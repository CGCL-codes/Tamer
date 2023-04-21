package de.capacis.jzeemap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import de.capacis.jzeemap.ImageCreatorFactory.ImageCreatorException;
import de.capacis.jzeemap.TransformationFactory.TransformationException;
import de.capacis.jzeemap.config.v10.JzeemapConfigurationDocument;
import de.capacis.jzeemap.config.v10.OutputConfiguration;
import de.capacis.jzeemap.config.v10.Zmap;
import de.capacis.jzeemap.config.v10.ZmapConfiguration;
import de.capacis.jzeemap.creators.ImageCreator;
import de.capacis.jzeemap.transformation.Transformation;

/**
 * Hello world!
 * 
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class);

    private final ZmapConfiguration appConfig;

    private final ImageCreatorFactory imageCreatorFactory = new ImageCreatorFactory();

    private final TransformationFactory transformationFactory = new TransformationFactory();

    public App(final ZmapConfiguration appConfig) {
        this.appConfig = appConfig;
    }

    public void start() {
        List<Zmap> zmapList = appConfig.getZmapList();
        int i = 0;
        for (final Zmap zmap : zmapList) {
            try {
                processZmap(zmap, i++);
            } catch (IOException ex) {
                logger.error("could not process zmap: " + ex.getMessage());
            }
        }
    }

    private void processZmap(Zmap zmap, final int zmapId) throws IOException {
        MDC.put(MDCConstants.ZMAP, "zmap-" + zmapId);
        MDC.put(MDCConstants.OUTPUT, "preproc");
        final long start = System.currentTimeMillis();
        try {
            logger.info("start processing zmap...");
            final File inputFile = new File(zmap.getFile());
            SceneLoader loader = new SceneLoader();
            final Scene scene = loader.load(inputFile);
            final BackgroundImageProvider backgroundImageProvider = new BackgroundImageProvider();
            if (zmap.getBackgroundImages() != null) {
                backgroundImageProvider.load(zmap.getBackgroundImages());
            }
            final List<OutputConfiguration> outputList = zmap.getOutputConfigurationList();
            int i = 0;
            for (final OutputConfiguration output : outputList) {
                processZmapOutput(scene, output, backgroundImageProvider, i++);
            }
        } finally {
            logger.info("finished processing zmap in " + DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - start));
            MDC.remove(MDCConstants.ZMAP);
            MDC.remove(MDCConstants.OUTPUT);
        }
    }

    private void processZmapOutput(Scene scene, OutputConfiguration output, final BackgroundImageProvider backgroundImageProvider, int outputId) throws IOException {
        MDC.put(MDCConstants.OUTPUT, "out-" + outputId);
        final long start = System.currentTimeMillis();
        String generatorId = output.getGenerator();
        String transformationId = output.getTransformation();
        try {
            logger.info("start processing zmap output...");
            final ImageCreator creator = imageCreatorFactory.create(generatorId);
            final Transformation transformation = transformationFactory.create(transformationId);
            creator.setOutputConfiguration(output);
            creator.setScene(scene);
            creator.setBackgroundImageProvider(backgroundImageProvider);
            creator.setTransformation(transformation);
            creator.create();
        } catch (ImageCreatorException e) {
            logger.error("could not create ImageCreator for generator " + generatorId + ": " + e.getMessage());
        } catch (TransformationException e) {
            logger.error("could not create Transformation for transformation " + transformationId + ": " + e.getMessage());
        } finally {
            logger.info("finished processing zmap output in " + DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - start));
            MDC.remove(MDCConstants.OUTPUT);
        }
    }

    public static void main(String[] args) {
        System.out.println("  JZeeMAP image tile creator. Ver.0.0.1-SNAPSHOT");
        System.out.println("      Copyright (c) 2009 Michael Schaefers");
        if (args.length != 1) {
            usage();
        } else {
            try {
                File file = new File(args[0]);
                logger.info("starting with configuration file " + file.getAbsolutePath());
                final ArrayList<Object> list = new ArrayList<Object>();
                final XmlOptions options = new XmlOptions();
                options.setErrorListener(list);
                final JzeemapConfigurationDocument doc = JzeemapConfigurationDocument.Factory.parse(file);
                boolean validate = doc.validate(options);
                if (!validate) {
                    throw new XmlException("", null, list);
                } else {
                    final App app = new App(doc.getJzeemapConfiguration());
                    app.start();
                }
            } catch (XmlException ex) {
                logger.error("could not parse configuration:");
                for (final Object o : ex.getErrors()) {
                    logger.error(" XML-ERR: " + o);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static void usage() {
        System.out.println();
        System.out.println("usage: provide one configuration file as argument");
        InputStream resource = App.class.getResourceAsStream("LICENSE.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(resource));
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            logger.fatal(e, e);
        }
    }
}
