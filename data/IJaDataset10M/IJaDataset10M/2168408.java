package org.form4j.form.field.label;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.form4j.form.layout.FormLayout;
import org.form4j.form.main.Form;
import org.form4j.form.util.tip.ToolTipUtil;

/**
 * Filler Form Element.
 * <p>
 * (see: Form definition reference  and examples for
 * <a href="../../../../../../manual/displayFields.html#filler" target="_top">Filler</a>
 * in the <a href="../../../../../../manual/index.html" target="_top">form4j Manual</a>!)
 * </p>
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.10 $ $Date: 2004/07/09 01:52:44 $
 **/
public class Filler extends AbstractLabel {

    /**
     * Create invisible filler form4j element.
     * @param form the parent form
     * @param desc the XML field descriptor
     * @throws Exception Exception
     */
    public Filler(final Form form, final Element desc) throws Exception {
        super(form, desc);
        JLabel component = new JLabel() {

            public void setVisible(final boolean visible) {
                if (visible) setPreferredSize(new Dimension(width, height)); else setPreferredSize(new Dimension(0, 0));
                super.setVisible(visible);
            }

            public void paint(final Graphics g) {
                if (Logger.getLogger("org.form4j.form.painter").isDebugEnabled() || ToolTipUtil.isLayoutGridActive((FormLayout) getForm().getContainer().getLayout())) {
                    g.setColor(Color.cyan);
                    g.fillRect(0, 0, width, height);
                } else super.paint(g);
            }
        };
        setComponent(component);
        try {
            height = Integer.parseInt(desc.getAttribute("height"));
        } catch (Exception e) {
            height = 0;
        }
        try {
            width = Integer.parseInt(desc.getAttribute("width"));
        } catch (Exception e) {
            width = 0;
        }
        getComponent().setPreferredSize(new Dimension(width, height));
        init();
        LOG.debug("");
    }

    private int width = 0;

    private int height = 0;

    private static final Logger LOG = Logger.getLogger(Filler.class.getName());
}
