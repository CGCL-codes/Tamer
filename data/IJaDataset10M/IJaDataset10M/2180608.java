package iaik.pkcs.pkcs11.wrapper;

/**
 * class CK_MECHANISM specifies a particular mechanism and any parameters it
 * requires.<p>
 * <B>PKCS#11 structure:</B>
 * <PRE>
 *  typedef struct CK_MECHANISM {&nbsp;&nbsp;
 *    CK_MECHANISM_TYPE mechanism;&nbsp;&nbsp;
 *    CK_VOID_PTR pParameter;&nbsp;&nbsp;
 *    CK_ULONG ulParameterLen;&nbsp;&nbsp;
 *  } CK_MECHANISM;
 * </PRE>
 *
 * @author Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
 * @author Martin Schl�ffer <schlaeff@sbox.tugraz.at>
 */
public class CK_MECHANISM {

    /**
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_MECHANISM_TYPE mechanism;
   * </PRE>
   */
    public long mechanism;

    /**
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_VOID_PTR pParameter;
   *   CK_ULONG ulParameterLen;
   * </PRE>
   */
    public Object pParameter;

    /**
   * Returns the string representation of CK_MECHANISM.
   *
   * @return the string representation of CK_MECHANISM
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Constants.INDENT);
        buffer.append("mechanism: ");
        buffer.append(mechanism);
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("pParameter: ");
        buffer.append(pParameter.toString());
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("ulParameterLen: ??");
        return buffer.toString();
    }
}
