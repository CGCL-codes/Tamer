package com.sun.mail.handlers;

import javax.activation.ActivationDataFlavor;

/**
 * DataContentHandler for text/html.
 *
 * @version 1.3, 07/05/04
 */
public class text_html extends text_plain {

    private static ActivationDataFlavor myDF = new ActivationDataFlavor(java.lang.String.class, "text/html", "HTML String");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }
}
