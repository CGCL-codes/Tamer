package iaik.pkcs.pkcs11.wrapper;

/**
 * class CK_KEA_DERIVE_PARAMS  provides the parameters to the CKM_KEA_DERIVE
 * mechanism.<p>
 * <B>PKCS#11 structure:</B>
 * <PRE>
 * typedef struct CK_KEA_DERIVE_PARAMS {
 *   CK_BBOOL isSender;
 *   CK_ULONG ulRandomLen;
 *   CK_BYTE_PTR pRandomA;
 *   CK_BYTE_PTR pRandomB;
 *   CK_ULONG ulPublicDataLen;
 *   CK_BYTE_PTR pPublicData;
 * } CK_KEA_DERIVE_PARAMS;
 * </PRE>
 *
 * @author Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
 * @author Martin Schl�ffer <schlaeff@sbox.tugraz.at>
 */
public class CK_KEA_DERIVE_PARAMS {

    /**
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_BBOOL isSender;
   * </PRE>
   */
    public boolean isSender;

    /**
   * ulRandomLen == pRandomA.length == pRandomB.length<p>
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_BYTE_PTR pRandomA;
   *   CK_ULONG ulRandomLen;
   * </PRE>
   */
    public byte[] pRandomA;

    /**
   * ulRandomLen == pRandomA.length == pRandomB.length<p>
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_BYTE_PTR pRandomB;
   *   CK_ULONG ulRandomLen;
   * </PRE>
   */
    public byte[] pRandomB;

    /**
   * <B>PKCS#11:</B>
   * <PRE>
   *   CK_BYTE_PTR pPublicData;
   *   CK_ULONG ulPublicDataLen;
   * </PRE>
   */
    public byte[] pPublicData;

    /**
   * Returns the string representation of CK_KEA_DERIVE_PARAMS.
   *
   * @return the string representation of CK_KEA_DERIVE_PARAMS
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Constants.INDENT);
        buffer.append("isSender: ");
        buffer.append(isSender);
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("ulRandomLen: ");
        buffer.append(pRandomA.length);
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("pRandomA: ");
        buffer.append(Functions.toHexString(pRandomA));
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("pRandomB: ");
        buffer.append(Functions.toHexString(pRandomB));
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("ulPublicDataLen: ");
        buffer.append(pPublicData.length);
        buffer.append(Constants.NEWLINE);
        buffer.append(Constants.INDENT);
        buffer.append("pPublicData: ");
        buffer.append(Functions.toHexString(pPublicData));
        return buffer.toString();
    }
}
