package org.form4j.form.field.label;

import javax.swing.JLabel;
import org.apache.log4j.Logger;
import org.form4j.form.field.AbstractField;
import org.form4j.form.main.Form;
import org.w3c.dom.Element;

/**
 * Common Functionality for Display Only Form Elements.
 * <p>
 * (see: Form definition reference  and examples for
 * <a href="../../../../../../manual/displayFields.html" target="_top">Display Fields</a>
 * in the <a href="../../../../../../manual/index.html" target="_top">form4j Manual</a>!)
 * </p>
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.6 $ $Date: 2004/06/15 13:36:10 $
 **/
public class AbstractLabel extends AbstractField implements Label {

    /**
     * Constructs abstract label form4j Field Control.
     * @param form the parent form
     * @param desc the xml field descriptor
     * @throws Exception Exception
     */
    public AbstractLabel(final Form form, final Element desc) throws Exception {
        super(form, desc);
    }

    /**
     * set the text for this label.
     * @param label the text for this label
     */
    public void setText(final String label) {
        ((JLabel) getComponent()).setText(label);
    }

    /**
     * get the text for this label.
     * @return the text for this label
     */
    public String getText() {
        return ((JLabel) getComponent()).getText();
    }

    /**
     * complete standard initialisation of labels.
     * @throws Exception Exception
     */
    public void init() throws Exception {
        LOG.debug("");
        super.init();
    }

    private static final Logger LOG = Logger.getLogger(AbstractLabel.class.getName());
}
