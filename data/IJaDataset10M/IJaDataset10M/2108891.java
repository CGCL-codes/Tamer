package net.sf.bacchus.records;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import net.sf.bacchus.Header;
import net.sf.bacchus.InvalidRecordException;
import net.sf.bacchus.Record;
import net.sf.bacchus.Rtn;
import net.sf.bacchus.Record.Type;

/** A file header record. */
public class FileHeader extends AbstractRecord implements Header {

    /** the default block size. */
    public static final int DEFAULT_BLOCK_SIZE = 10;

    /** the maximum priority code. */
    public static final int MAX_PRIORITY = 99;

    /**
     * Format for the file header record.
     * @see #build()
     */
    private static final String RECORD_FORMAT = "%02d" + " %08d%01d" + " %08d%01d" + "%ty%<tm%<td%<tH%<tM" + "%c" + "%03d" + "%02d" + "%c" + "%-23s" + "%-23s" + "%-8s";

    /** Regex for valid file id modifier. */
    private static final String MODIFIER_REGEX = "[A-Z0-9]";

    /** Formatter for the file creation date field. */
    private final DateFormat creationFormat;

    /** the priority code. */
    private Integer priorityCode;

    /** the file creation timestamp. */
    private Date fileCreation;

    /** the immediate destination DFI. */
    private final Rtn destination = new Rtn();

    /** the immediate destination name. */
    private String destinationName;

    /** the immediate origin DFI. */
    private final Rtn origin = new Rtn();

    /** the immediate origin name. */
    private String originName;

    /** the file id modifier. */
    private Character fileIdModifier;

    /** the block size. */
    private Integer blockSize;

    /** the record size. */
    private Integer recordSize;

    /** the format code. */
    private Character formatCode;

    /** the reference code. */
    private String referenceCode;

    /** Create a file header. */
    public FileHeader() {
        this.creationFormat = new SimpleDateFormat("yyMMddHHmm", Locale.US);
        this.creationFormat.setLenient(false);
    }

    /**
     * {@inheritDoc}
     * @return Always returns {@link Type#FILE_HEADER}.
     */
    @Override
    protected final Type getExpectedType() {
        return Type.FILE_HEADER;
    }

    /**
     * Gets the priority code.
     * @return the priority code.
     */
    public int getPriorityCode() {
        return this.priorityCode == null ? 1 : this.priorityCode;
    }

    /**
     * Sets the priority code.
     * @param priorityCode the priority code.
     */
    public void setPriorityCode(final int priorityCode) {
        this.priorityCode = priorityCode;
    }

    /**
     * Gets the file creation timestamp.
     * @return the file creation timestamp.
     */
    public Date getFileCreation() {
        return this.fileCreation == null ? new Date() : (Date) this.fileCreation.clone();
    }

    /**
     * Sets the file creation timestamp.
     * @param fileCreation the file creation timestamp.
     */
    public void setFileCreation(final Date fileCreation) {
        this.fileCreation = fileCreation == null ? null : (Date) fileCreation.clone();
    }

    /**
     * Gets the 8 digit routing transit number of the immediate destination DFI,
     * with no check digit.
     * @return the RTN of the immediate destination.
     */
    public int getDestination() {
        return this.destination.getRtn();
    }

    /**
     * Sets the 8 digit routing transit number of the immediate destination DFI,
     * with no check digit.
     * @param rtn the RTN of the immediate destination.
     * @see #setDestinationCheck(int)
     */
    public void setDestination(final int rtn) {
        this.destination.setRtn(rtn);
    }

    /**
     * Gets the check digit for the RTN of the immediate destination DFI.
     * @return the check digit of the immediate destination DFI.
     */
    public int getDestinationCheck() {
        return this.destination.getCheck();
    }

    /**
     * Sets the check digit for the RTN of the immediate destination DFI.
     * @param check the check digit of the immediate destination DFI.
     */
    public void setDestinationCheck(final int check) {
        this.destination.setCheck(check);
    }

    /**
     * Gets the immediate destination name.
     * @return the immediate destination name.
     */
    public String getDestinationName() {
        return this.destinationName == null ? "" : this.destinationName;
    }

    /**
     * Sets the immediate destination name.
     * @param destinationName the immediate destination name.
     */
    public void setDestinationName(final String destinationName) {
        this.destinationName = destinationName;
    }

    /**
     * Gets the 8 digit routing transit number of the immediate origin DFI, with
     * no check digit.
     * @return {@inheritDoc}
     */
    public int getOrigin() {
        return this.origin.getRtn();
    }

    /**
     * Sets the 8 digit routing transit number of the immediate origin DFI, with
     * no check digit.
     * @param rtn the RTN of the immediate origin DFI.
     * @see #setOriginCheck(int)
     */
    public void setOrigin(final int rtn) {
        this.origin.setRtn(rtn);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public int getOriginCheck() {
        return this.origin.getCheck();
    }

    /**
     * Sets the check digit for the RTN of the immediate origin DFI.
     * @param check the check digit of the immediate orgin DFI.
     */
    public void setOriginCheck(final int check) {
        this.origin.setCheck(check);
    }

    /**
     * Gets the immediate origin name.
     * @return the immediate origin name.
     */
    public String getOriginName() {
        return this.originName == null ? "" : this.originName;
    }

    /**
     * Sets the immediate origin name.
     * @param originName the immediate origin name.
     */
    public void setOriginName(final String originName) {
        this.originName = originName;
    }

    /**
     * Gets the file id modifier.
     * @return the file id modifier.
     */
    public char getFileIdModifier() {
        return this.fileIdModifier == null ? ' ' : this.fileIdModifier;
    }

    /**
     * Sets the file id modifier.
     * @param modifier the file id modifier.
     */
    public void setFileIdModifier(final char modifier) {
        this.fileIdModifier = modifier;
    }

    /**
     * Gets the record size. Default is {@link Record#ACH_RECORD_SIZE}.
     * @return the record size.
     */
    public final int getRecordSize() {
        return this.recordSize == null ? Record.ACH_RECORD_SIZE : this.recordSize;
    }

    /**
     * Sets the record size.
     * @param recordSize the record size.
     */
    public void setRecordSize(final int recordSize) {
        this.recordSize = recordSize;
    }

    /**
     * Gets the number of records per block. Default is
     * {@link #DEFAULT_BLOCK_SIZE}.
     * @return the number of records per block.
     */
    public final int getBlockSize() {
        return this.blockSize == null ? DEFAULT_BLOCK_SIZE : this.blockSize;
    }

    /**
     * Sets the number of records per block.
     * @param blockSize the number of records per block.
     */
    public void setBlockSize(final int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Gets the format code.
     * @return the format code.
     */
    public char getFormatCode() {
        return this.formatCode == null ? '1' : this.formatCode;
    }

    /**
     * Sets the format code.
     * @param formatCode the format code.
     */
    public void setFormatCode(final char formatCode) {
        this.formatCode = formatCode;
    }

    /**
     * Gets the reference code.
     * @return the reference code.
     */
    public String getReferenceCode() {
        return this.referenceCode == null ? "" : this.referenceCode;
    }

    /**
     * Sets the reference code.
     * @param referenceCode the reference code.
     */
    public void setReferenceCode(final String referenceCode) {
        this.referenceCode = referenceCode;
    }

    /**
     * {@inheritDoc}
     * @param record {@inheritDoc}
     */
    @Override
    public void load(final String record) {
        super.load(record);
        setPriorityCode(Integer.parseInt(record.substring(1, 3)));
        setDestination(Integer.parseInt(record.substring(3, 12).trim()));
        setDestinationCheck(Character.digit(record.charAt(12), BASE_TEN));
        setOrigin(Integer.parseInt(record.substring(13, 22).trim()));
        setOriginCheck(Character.digit(record.charAt(22), BASE_TEN));
        try {
            setFileCreation(creationFormat.parse(record.substring(23, 33)));
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e);
        }
        setFileIdModifier(record.charAt(33));
        setRecordSize(Integer.parseInt(record.substring(34, 37)));
        setBlockSize(Integer.parseInt(record.substring(37, 39)));
        setFormatCode(record.charAt(39));
        setDestinationName(record.substring(40, 63).trim());
        setOriginName(record.substring(63, 86).trim());
        setReferenceCode(record.substring(86, 94).trim());
    }

    /**
     * {@inheritDoc}
     * @throws InvalidRecordException {@inheritDoc}
     */
    @Override
    public void validate() throws InvalidRecordException {
        super.validate();
        if (this.priorityCode != null) {
            if (this.priorityCode < 0) {
                throw new InvalidRecordException("Negative priority code");
            } else if (this.priorityCode > MAX_PRIORITY) {
                throw new InvalidRecordException("Priority code too high");
            }
        }
        this.origin.validate();
        this.destination.validate();
        if (this.fileIdModifier != null && !String.valueOf(this.fileIdModifier).matches(MODIFIER_REGEX)) {
            throw new InvalidRecordException("Invalid file id modifier");
        }
        if (!RecordUtilities.consistent(this.recordSize, ACH_RECORD_SIZE)) {
            throw new InvalidRecordException("Invalid record size");
        }
        if (!RecordUtilities.consistent(this.blockSize, DEFAULT_BLOCK_SIZE)) {
            throw new InvalidRecordException("Invalid block size");
        }
        if (!RecordUtilities.consistent(this.formatCode, '1')) {
            throw new InvalidRecordException("Invalid format code");
        }
    }

    /**
     * Adds the priority code, immediate destination and origin, file creation
     * date and time, file id modifier, record size, blocking factor and format
     * code, immediate destination and origin names, and reference code.
     * @return {@inheritDoc}
     */
    @Override
    protected StringBuilder build() {
        return super.build().append(String.format(RECORD_FORMAT, getPriorityCode(), getDestination(), getDestinationCheck(), getOrigin(), getOriginCheck(), getFileCreation(), getFileIdModifier(), getRecordSize(), getBlockSize(), getFormatCode(), getDestinationName(), getOriginName(), getReferenceCode()));
    }

    /**
     * Performs the following normalizations:
     * <ul>
     * <li>The {@link AbstractRecord#normalize() superclass normalization}</li>
     * <li>Clears the {@link #getFileCreation() file creation date}</li>
     * <li>{@link Rtn#normalize() Normalizes} the origin and destination RTNs.</li>
     * <li>Clears the {@link #getRecordSize() record size} if it is
     * {@link Record#ACH_RECORD_SIZE}.</li>
     * <li>Clears the {@link #getBlockSize() block size} if it is
     * {@link #DEFAULT_BLOCK_SIZE}.</li>
     * <li>Clears the {@link #getFormatCode() format code} if it is
     * &apos;1&apos;.</li>
     * <li>Clears the {@link #getPriorityCode() priority code} if it is 1</li>
     * <li>Clears the {@link #getFileIdModifier()} if it is a space character.</li>
     * <li>Clears the {@link #getOriginName() origin} and
     * {@link #getDestinationName()} names if they are empty.</li>
     * </ul>
     * Classes that override this method should always call this superclass
     * method.
     * @see AbstractRecord#normalize()
     */
    @Override
    public void normalize() {
        super.normalize();
        this.fileCreation = null;
        this.origin.normalize();
        this.destination.normalize();
        if (RecordUtilities.redundant(this.recordSize, ACH_RECORD_SIZE)) {
            this.recordSize = null;
        }
        if (RecordUtilities.redundant(this.blockSize, DEFAULT_BLOCK_SIZE)) {
            this.blockSize = DEFAULT_BLOCK_SIZE;
        }
        if (RecordUtilities.redundant(this.formatCode, '1')) {
            this.formatCode = null;
        }
        if (RecordUtilities.redundant(this.priorityCode, 1)) {
            this.priorityCode = null;
        }
        if (RecordUtilities.redundant(this.fileIdModifier, ' ')) {
            this.fileIdModifier = null;
        }
        if (RecordUtilities.empty(this.originName)) {
            this.originName = null;
        }
        if (RecordUtilities.empty(this.destinationName)) {
            this.destinationName = null;
        }
    }
}
