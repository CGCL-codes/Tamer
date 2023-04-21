package gnu.CORBA.GIOP.v1_2;

import gnu.CORBA.CDR.AbstractCdrInput;
import gnu.CORBA.CDR.AbstractCdrOutput;
import gnu.CORBA.GIOP.ServiceContext;
import gnu.CORBA.GIOP.CodeSetServiceContext;

/**
 * GIOP 1.2 reply header.
 *
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org)
 */
public class ReplyHeader extends gnu.CORBA.GIOP.v1_0.ReplyHeader {

    /**
   * Adds the standard encoding context.
   */
    public ReplyHeader() {
        service_context = new ServiceContext[] { CodeSetServiceContext.STANDARD };
    }

    /**
   * Return the message status as a string.
   */
    public String getStatusString() {
        String s = super.getStatusString();
        if (s != null) return s;
        switch(reply_status) {
            case LOCATION_FORWARD_PERM:
                return "moved permanently";
            case NEEDS_ADDRESSING_MODE:
                return "the alternative addressing mode required";
            default:
                return null;
        }
    }

    /**
   * Reads the header from the stream.
   * The fields go in different order than in the previous GIOP versions.
   *
   * Sets the code set of this stream to
   * the code set, specfied in the header.
   *
   * @param in a stream to read from.
   */
    public void read(AbstractCdrInput in) {
        request_id = in.read_ulong();
        reply_status = in.read_ulong();
        service_context = gnu.CORBA.GIOP.ServiceContext.readSequence(in);
        in.setCodeSet(CodeSetServiceContext.find(service_context));
    }

    /**
   * Writes the header to the stream.
   * The fields go in different order than in the previous GIOP versions.
   *
   * Sets the code set of this stream to
   * the code set, specfied in the header.
   *
   * @param out a stream to write into.
   */
    public void write(AbstractCdrOutput out) {
        out.write_ulong(request_id);
        out.write_ulong(reply_status);
        gnu.CORBA.GIOP.ServiceContext.writeSequence(out, service_context);
        out.setCodeSet(CodeSetServiceContext.find(service_context));
    }
}
