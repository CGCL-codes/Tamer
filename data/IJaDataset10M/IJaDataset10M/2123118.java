package unbbayes.datamining.datamanipulation;

/**
 * <code>UnassignedDatasetException</code> is used when
 * a method of an Instance is called that requires access to
 * the Instance structure, but that the Instance does not contain
 * a reference to any Instances (as set by Instance.setDataset(), or when
 * an Instance is added to a set of Instances)).
 *
 *  @author M�rio Henrique Paes Vieira (mariohpv@bol.com.br)
 *  @version $1.0 $ (16/02/2002)
 */
public class UnassignedDatasetException extends RuntimeException {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    /**
   * Creates a new <code>UnassignedDatasetException</code> instance
   * with no detail message.
   */
    public UnassignedDatasetException() {
        super();
    }

    /**
   * Creates a new <code>UnassignedDatasetException</code> instance
   * with a specified message.
   *
   * @param message a <code>String</code> containing the message.
   */
    public UnassignedDatasetException(String message) {
        super(message);
    }
}
