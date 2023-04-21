package org.freelords.forms.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.freelords.xml.XMLHelper;
import org.w3c.dom.Node;

/** An empty widget that fills space.
  * 
  * <p>
  * Depending on the tag name, these elements will try to fill as much
  * space as possible in horizontal or vertical direction.
  * </p>
  *
  * <p>
  * Underlying SWT class: Composite
  * </p>
  *
  * <p>
  * XML tag name: vpadding or hpadding
  * </p>
  *
  * <p>
  * Attributes that can be set in xml: none
  * </p>
  *
  * @author James Andrews
  */
class FormSpacer extends FormStuff {

    /** {@inheritDoc} */
    @Override
    public void parse(XMLHelper helper, Node node) {
        super.parse(helper, node);
        if ("vpadding".equals(node.getNodeName())) {
            stretchVertical = true;
        } else if ("hpadding".equals(node.getNodeName())) {
            stretchHorizontal = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Control create(Composite parent, FormWidgetFactory form) {
        Composite comp = new Composite(parent, SWT.NONE);
        return comp;
    }

    /** Throws an exception.
	  * 
	  * This form primitive should never have children.
	  */
    @Override
    protected void add(FormStuff child) {
        throw new IllegalArgumentException("Can not add children to a spacer");
    }
}
