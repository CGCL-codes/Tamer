package srcp.common.exception;

/**
 *
 * @author  osc
 * @version $Revision: 65 $
  */
public class ExcListToShort extends ExcCommand {

    public ExcListToShort() {
        super(419, "list to short");
    }

    public SRCPException cloneExc() {
        return new ExcListToShort();
    }
}
