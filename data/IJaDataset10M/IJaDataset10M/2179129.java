package nl.headspring.photoz.imagecollection.fs;

import nl.headspring.photoz.common.Chrono;
import nl.headspring.photoz.common.eventbus.EventBus;
import nl.headspring.photoz.imagecollection.Annotation;
import nl.headspring.photoz.imagecollection.Annotations;
import nl.headspring.photoz.imagecollection.ImageCollection;
import nl.headspring.photoz.imagecollection.fs.events.ScanCompleteBusEvent;
import nl.headspring.photoz.imagecollection.fs.events.ScanFolderCompleteBusEvent;
import nl.headspring.photoz.imagecollection.fs.events.ScanStartBusEvent;
import nl.headspring.photoz.imagecollection.fs.metadata.ExifMetadataReader;
import nl.headspring.photoz.imagecollection.fs.metadata.ExifMetadataWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.*;
import java.io.File;
import java.util.Set;

/**
 * Class Scanner.
 * Scans the given image directories to update the image annotation database.
 * Tasks:
 * <ul>
 * <li>Find new images and generate them to the database</li>
 * <li>Check the file-based URI for each image, and update the URI in the annotation if required.</li>
 * <li>Create thumbnails for new images, or fix missing ones</li>
 * <li>Remove annotations for removed images (or simply mark then 'deleted')</li>
 * </ul>
 * <p/>
 *
 * @author Eelco Sommer
 * @since Sep 17, 2010
 */
public class Scanner {

    private static final Log LOG = LogFactory.getLog(Scanner.class);

    private final EventBus eventBus;

    private final ImageCollection imageCollection;

    private final Annotations annotations;

    private final ExifMetadataReader metaDataReader;

    private final ExifMetadataWriter exifMetadataWriter;

    private boolean scanning;

    public Scanner(ImageCollection imageCollection, Annotations annotations, ExifMetadataReader metaDataReader, ExifMetadataWriter exifMetadataWriter, EventBus eventBus) {
        this.imageCollection = imageCollection;
        this.annotations = annotations;
        this.metaDataReader = metaDataReader;
        this.exifMetadataWriter = exifMetadataWriter;
        this.eventBus = eventBus;
    }

    public void scan(Set<Folder> folders) {
        scan(false, folders);
    }

    public void scan(boolean recursive, Set<Folder> folders) {
        if (scanning) {
            LOG.warn("Already scannning");
            return;
        }
        try {
            Chrono c = new Chrono();
            eventBus.publish(new ScanStartBusEvent(folders));
            scanning = true;
            for (Folder folder : folders) {
                scan(recursive, folder);
            }
            LOG.info(c.stop("Scan completed in {0} ms"));
        } finally {
            this.scanning = false;
            eventBus.publish(new ScanCompleteBusEvent());
        }
    }

    private void scan(boolean recursive, Folder folder) {
        LOG.debug("Scanning " + folder);
        for (Entry entry : folder.getEntries()) {
            if (entry instanceof Folder) {
                if (recursive) {
                    scan(recursive, (Folder) entry);
                }
            } else {
                add((Resource) entry);
            }
        }
        eventBus.publish(new ScanFolderCompleteBusEvent(folder));
    }

    /**
   * Adds the image identified by imageSource to the underlaying collection.
   * The implementation must provide a unique id for the image, and store it in the
   * image meta data. If an image already contains a unique id, its value must NOT
   * be overwrittem. The current value should be used instead.
   * The unique id ensures that an image survives relocation or renaming.
   *
   * @param resources Image locations
   * @return Initial generated annotation
   */
    private void add(Resource... resources) {
        for (Resource resource : resources) {
            if (!resource.getName().toLowerCase().endsWith(".jpg") && !resource.getName().toLowerCase().endsWith(".jpeg")) {
                continue;
            }
            File imageFile = new File(resource.getAbsolutePath());
            Annotation annotation = annotations.get(imageFile);
            Image image = imageCollection.getThumbnail(imageFile);
        }
    }
}
