package org.jbrix.gui.xform;

import org.jbrix.gui.zoom.*;
import org.jbrix.xml.*;
import org.w3c.dom.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *   XZoomButton is an XComponent that allows a subform to be pushed
 *   on top of the current form. By allowing forms to be "nested" in
 *   this way, user interfaces can be made very manageable despite the
 *   need to display large and complex data sets.
 *
 *   <p/>The following XML example shows the xforms syntax of this component:
 *
 *<XML-EXAMPLE>
 *<zoombutton ref="info/about"             <---- @ref: required
 *    button.key="{lastName}, {firstName}" <---- @button.key: required
 *    <caption>About:</caption>            <---- caption: required
 *    <form ref=".">                       <---- form: optional
 *        . . .
 *    </form>
 *</zoombutton>
 *</XML-EXAMPLE>
 *
 */
public class XZoomButton extends XComponent implements ActionListener {

    private static XMLDriver xmlDriver = XMLDriver.getDefaultDriver();

    private JButton button;

    private Node node;

    private Element zoomViewElement = null;

    private String buttonKey;

    private ZoomMaster zoomer;

    public boolean isZoomable() {
        return true;
    }

    public void clear() {
        this.node = null;
    }

    public void setEnabled(boolean flag) {
        button.setEnabled(flag);
    }

    public void addDataNode(Node node) {
        this.node = node;
        String s = null;
        if (buttonKey != null) {
            s = ExpressionUtilities.dereferencePhrase(buttonKey, node);
        }
        button.setText(s);
    }

    public void refreshFromDataNodes() {
        Node n = node;
        clear();
        addDataNode(n);
    }

    public void configure(Element element) {
        setLabel(getCaption());
        buttonKey = XMLUtil.getAttribute(element, "button.key");
        zoomViewElement = XMLUtil.selectFirstElement(element, "form");
    }

    /**
	 *   This method is called when the button is pressed.
	 *
	 *   @param event The action event.
	 */
    public void actionPerformed(ActionEvent event) {
        zoomIn();
    }

    /**
	 *   This method "zooms" the data set in by pushing a new form onto
	 *   the ZoomMaster in which the current form resides. If this form
	 *   (or any parent form) is not in a ZoomMaster, nothing happens.
	 */
    public void zoomIn() {
        if (getZoomMaster() == null) {
            return;
        } else if (!isVisible()) {
            return;
        }
        try {
            XForm view = null;
            if (zoomViewElement != null) {
                view = new XForm(zoomViewElement);
            } else if (node instanceof Element) {
                view = XForm.forElement((Element) node);
            }
            if (view != null) {
                view.addXComponentListener(new XComponentListener() {

                    public void nodeEdited(XComponent xcomp, Node node) {
                        fireEditedEvent(xcomp, node);
                    }
                });
                if (zoomer != getZoomMaster()) {
                    zoomer = getZoomMaster();
                    zoomer.addZoomListener(new ZoomAdapter() {

                        public void zoomLayerExposed(ZoomMaster zoomer, ZoomLayerPanel layer) {
                            if (layer == getZoomLayer()) {
                                String s = "ZoomButton";
                                if (buttonKey != null) {
                                    s = ExpressionUtilities.dereferencePhrase(buttonKey, node);
                                }
                                button.setText(s);
                            }
                        }
                    });
                }
                if (zoomer != null) {
                    if (view != null) {
                        view.setBorder(new EmptyBorder(4, 4, 4, 4));
                        view.clear();
                        view.addDataNode(node);
                        zoomer.addLayer(view.getCaption(), new JScrollPane(view));
                    }
                }
                fireEditedEvent(node);
            }
        } catch (Exception ex) {
        }
    }

    /**
	 *   Constructs an unconfigured instance with <CODE>label</CODE> for
	 *   its label text.
	 *
	 *   @param label The label for this instance.
	 */
    public XZoomButton(String label) {
        button = new JButton();
        button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        button.setBorder(new CompoundBorder(new EtchedBorder(Color.white, new Color(.3f, .3f, .3f)), new EmptyBorder(1, 3, 1, 3)));
        button.setHorizontalAlignment(button.LEFT);
        button.setText("ZoomButton");
        button.addActionListener(this);
        setLayout(new BorderLayout());
        add("Center", button);
        setLabel(label);
        setPreferredSize((new JTextField()).getPreferredSize());
    }

    /**
	 *   The default constructor; creates an unconfigured instance with
	 *   "New field:" for its label. All XComponent subclasses <b>must</b>
	 *   provide a public default constructor that takes no arguments.
	 */
    public XZoomButton() {
        this("New field:");
    }
}
