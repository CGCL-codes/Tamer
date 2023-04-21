package freemind.controller.actions.generated.instance;

/**
 * Java content class for text_node_action complex type.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;complexType name="text_node_action">
 *   &lt;complexContent>
 *     &lt;extension base="{}node_action">
 *       &lt;attribute name="text" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface TextNodeAction extends freemind.controller.actions.generated.instance.NodeAction {

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getText();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setText(java.lang.String value);
}
