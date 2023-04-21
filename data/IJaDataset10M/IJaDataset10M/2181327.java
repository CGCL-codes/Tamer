package jam.sort.stream;

import static jam.sort.stream.L002Parameters.HEADER_START;
import static jam.sort.stream.L002Parameters.IMAGE_LENGTH;
import static jam.sort.stream.L002Parameters.TITLE_MAX;
import injection.GuiceInjector;
import jam.global.RunInfo;
import jam.util.StringUtilities;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Abstract superclass of all event output streams that write ORNL L002 headers.
 * @author Dale Visser
 */
abstract class AbstractL002HeaderWriter extends AbstractEventOutputStream {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yy HH:mm  ", Locale.getDefault());

    static {
        FORMATTER.setTimeZone(TimeZone.getDefault());
    }

    /**
     * Default constructor.
     */
    protected AbstractL002HeaderWriter() {
        super();
    }

    /**
     * @param eventSize
     *            number of parameters per event
     */
    protected AbstractL002HeaderWriter(final int eventSize) {
        super(eventSize);
    }

    /**
     * Writes the header block.
     * @exception EventException
     *                thrown for errors in the event stream
     */
    @Override
    public void writeHeader() throws EventException {
        String dateString;
        final RunInfo runInfo = RunInfo.getInstance();
        synchronized (FORMATTER) {
            dateString = FORMATTER.format(runInfo.runStartTime);
        }
        final String title = runInfo.runTitle;
        final int number = runInfo.runNumber;
        final byte[] reserved1 = new byte[8];
        final int numSecHead = 0;
        final int recLen = 0;
        final int blckImgRec = 0;
        final int paramsPerEvent = runInfo.runEventSize;
        final int dataRecLen = runInfo.runRecordLength;
        final byte[] reserved2 = new byte[92];
        try {
            dataOutput.writeBytes(HEADER_START);
            final StringUtilities stringUtilities = GuiceInjector.getObjectInstance(StringUtilities.class);
            dataOutput.writeBytes(stringUtilities.makeLength(dateString, 16));
            dataOutput.writeBytes(stringUtilities.makeLength(title, TITLE_MAX));
            dataOutput.writeInt(number);
            dataOutput.write(reserved1, 0, reserved1.length);
            dataOutput.writeInt(numSecHead);
            dataOutput.writeInt(recLen);
            dataOutput.writeInt(blckImgRec);
            dataOutput.writeInt(IMAGE_LENGTH);
            dataOutput.writeInt(paramsPerEvent);
            dataOutput.writeInt(dataRecLen);
            dataOutput.write(reserved2, 0, reserved2.length);
            dataOutput.flush();
        } catch (IOException io) {
            throw new EventException("Problem writing header.", io);
        }
    }
}
