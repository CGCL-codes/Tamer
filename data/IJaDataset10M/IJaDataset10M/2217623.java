package com.cosylab.gui.components.aboutdialog;

/**
 * Creates GUI panels for about tab models.
 *
 * @author <a href="mailto:igor.kriznar@cosylab.com">Igor Kriznar</a>
 */
public interface AboutTabFactory {

    /**
	 * DOCUMENT ME!
	 *
	 * @param model DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public AboutTab getAboutTab(AboutTabModel model);
}
