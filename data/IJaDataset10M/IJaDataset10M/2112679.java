package de.xirp.ui.widgets.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import de.xirp.util.II18nHandler;

/**
 * This widget is enabled with on-the-fly translation and on-the-fly
 * color change capabilities.
 * 
 * @author Matthias Gernand
 */
public class XTextField extends XText {

    /**
	 * Constructs a new text field. This constructor should be used in
	 * plugin-UI environment, because the
	 * {@link de.xirp.util.II18nHandler handler} is
	 * needed for translations.
	 * 
	 * @param parent
	 *            The parent.
	 * @param style
	 *            The style.
	 * @param handler
	 *            The handler.
	 * @see de.xirp.util.II18nHandler
	 */
    public XTextField(Composite parent, int style, II18nHandler handler) {
        super(parent, style | SWT.BORDER | SWT.SINGLE, handler, true);
    }

    /**
	 * Constructs a new text field. This constructor should be used in
	 * application-UI environment.
	 * 
	 * @param parent
	 *            The parent.
	 * @param style
	 *            The style.
	 */
    public XTextField(Composite parent, int style) {
        super(parent, style | SWT.BORDER | SWT.SINGLE, true);
    }
}
