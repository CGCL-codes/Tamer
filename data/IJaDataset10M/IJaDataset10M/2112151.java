package gov.nasa.worldwind.formats.nitfs;

/**
 * @author Lado Garakanidze
 * @version $Id: NitfsExtendedHeaderSegment Mar 31, 2007 1:06:23 AM
 */
class NITFSExtendedHeaderSegment extends NITFSSegment {

    public NITFSExtendedHeaderSegment(java.nio.ByteBuffer buffer, int headerStartOffset, int headerLength, int dataStartOffset, int dataLength) {
        super(NITFSSegmentType.EXTENDED_HEADER_SEGMENT, buffer, headerStartOffset, headerLength, dataStartOffset, dataLength);
        this.restoreBufferPosition();
    }
}
