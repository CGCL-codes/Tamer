package gov.nist.atlas;

/**
 * AIFSerializable defines the behavior of ATLASElements that can be
 * serialized to AIF.
 *
 * @version $Revision: 1.13 $
 * @author Christophe Laprun
 */
public interface AIFSerializable {

    /**
   * Prints the contextual AIF representation of this AIFSerializable to the
   * specified StringBuffer. This AIF representation is called contextual because it
   * uses contextual information (most notably role and propagated indent) from its parent element.
   * Note that this
   * method is not meant to be called directly but is needed by the framework in
   * particular to support export to AIF.
   *
   * @param buffer the StringBuffer to which the AIF representation of this
   * AIFSerializable should be printed
   * @param indent
   * @param role   the role of this AIFSerializable in the
   * context of its parent
   * @param context
   *
   * @deprecated Move to SPI
   */
    void toAIFBuffer(StringBuffer buffer, String indent, String role, ATLASElement context);

    /**
   * <p>Returns the context-free (without role or propagated indent) AIF representation of this
   * AIFSerializable as a String. This is a convenience method and should
   * typically be equivalent to:</p>
   *
   * <pre><code>
   * StringBuffer buffer = new StringBuffer();
   * toAIFBuffer(buffer, "  ", "", false);
   * buffer.toString();
   * </code>
   * </pre>
   *
   * @return the context-free AIF representation of this
   * AIFSerializable as a String
   *
   * @see #toAIFBuffer(StringBuffer,String,String,ATLASElement)
   */
    String asAIFString();
}
