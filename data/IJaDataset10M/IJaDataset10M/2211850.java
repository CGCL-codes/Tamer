package org.ikasan.framework.component.sequencing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.ikasan.common.FilePayloadAttributeNames;
import org.ikasan.common.Payload;
import org.ikasan.framework.component.Event;

/**
 * Implementation of @see {@link org.ikasan.framework.component.sequencing.Sequencer}.
 * <p>
 * An incoming <code>Event</code> will have a zip file entry as its <code>Payload</code>(s).<br>
 * The <code>UnzipSplitter</code> will unzip each of the <code>Event</code>'s payloads,<br>
 * such that each compressed file within the the zipped entry will be split into a new <code>Payload</code>.<br>
 * A new <code>Event</code> will be created for each new <code>Payload</code>.
 * </p>
 * <p>
 * Examples: @see {@link org.ikasan.framework.component.sequencing.UncompressDataSplitterTest}
 * </p>
 * 
 * @author Ikasan Development Team
 */
public class UncompressDataSplitter implements Sequencer {

    /** Logger instance */
    private static Logger logger = Logger.getLogger(UncompressDataSplitter.class);

    /** Constant representing end-of-file is reached. */
    private static final int END_OF_FILE = -1;

    /**
     * Implementation of {@link org.ikasan.framework.component.sequencing.Sequencer#onEvent(Event)}
     * 
     * @param event - The incoming event with payload containing a zip file
     * @throws SequencerException Wrapper exception thrown when cloning and/or transforming the<br>
     *         <code>Event</code>/<code>Payload</code>
     */
    public List<Event> onEvent(Event event, String moduleName, String componentName) throws SequencerException {
        List<Event> newEvents = new ArrayList<Event>();
        List<Payload> payloads = event.getPayloads();
        if (logger.isDebugEnabled()) {
            logger.debug("Unzipping event " + event.idToString() + "].");
        }
        for (Payload payload : payloads) {
            try {
                List<Payload> newPayloads = this.unzipPayload(payload);
                for (int i = 0; i < newPayloads.size(); i++) {
                    Event newEvent = event.spawnChild(moduleName, componentName, i, newPayloads.get(i));
                    newEvents.add(newEvent);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Incoming event [" + event.getId() + "] split into event [" + newEvent.getId() + "].");
                    }
                }
            } catch (CloneNotSupportedException e) {
                throw new SequencerException(e);
            } catch (IOException e) {
                throw new SequencerException(e);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Returning [" + newEvents.size() + "] new events.");
            logger.debug("Splitting event compelted successfully.");
        }
        return newEvents;
    }

    /**
     * Unzip incoming payload event into a list of payloads each representing a single file.
     * 
     * @param payload The incoming payload containing BYTE_ZIP data
     * @return List of payloads representing one unzipped file.
     * @throws IOException Thrown if ZipEntry cannot be read from payload.
     * @throws CloneNotSupportedException Thrown if error cloning a payload.
     */
    private List<Payload> unzipPayload(Payload payload) throws IOException, CloneNotSupportedException {
        List<Payload> newPayloads = new ArrayList<Payload>();
        byte[] payloadDataContent = payload.getContent();
        ByteArrayInputStream inputDataInByteArrayFormat = new ByteArrayInputStream(payloadDataContent);
        ZipInputStream inputDataInZippedFormat = new ZipInputStream(inputDataInByteArrayFormat);
        ZipEntry zippedEntry = null;
        int zippedFileCount = 0;
        while ((zippedEntry = inputDataInZippedFormat.getNextEntry()) != null) {
            if (zippedEntry.isDirectory()) {
                logger.debug("Ignoring directory entry [" + zippedEntry.getName() + "]");
                continue;
            }
            ByteArrayOutputStream outputDataInByteArrayFormat = new ByteArrayOutputStream();
            for (int c = inputDataInZippedFormat.read(); c != END_OF_FILE; c = inputDataInZippedFormat.read()) {
                outputDataInByteArrayFormat.write(c);
            }
            byte[] newPayloadDataContent = outputDataInByteArrayFormat.toByteArray();
            String newPayloadName = zippedEntry.getName().toLowerCase();
            Payload newPayload = payload.spawnChild(zippedFileCount++);
            newPayload.setAttribute(FilePayloadAttributeNames.FILE_NAME, newPayloadName);
            newPayload.setContent(newPayloadDataContent);
            newPayloads.add(newPayload);
            if (logger.isDebugEnabled()) {
                logger.debug("Incomding event's payload [" + payload.getId() + "] split to payload [" + newPayload.getId() + "].");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Returning [" + newPayloads.size() + "] new payloads.");
            logger.info("Splitting payload to its individual unzipped files completed successfully.");
        }
        return newPayloads;
    }
}
