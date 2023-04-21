package jpicedt.graphic;

import jpicedt.graphic.toolkit.EditorKit;
import jpicedt.graphic.toolkit.AbstractCustomizer;
import jpicedt.graphic.io.formatter.FormatterFactory;
import jpicedt.graphic.view.ViewFactory;
import java.util.*;

/**
 * Specifies a content-type (aka mime-type) for a Drawing document created by an editor-kit. <p>
 * This interface is aimed at lumping together top-level classes needed by a given content-type
 * in the View-Model-Control framework : so far this involves obtaining an instance of EditorKit
 * appropriate for the given content-type, as well as a corresponding FormatterFactory.
 * A <code>configure</code> method is provided for conveniently configuring editor-kits and factories before use.
 * <p>
 * [SR:pending] better move to jpicedt.graphic.io ?
 * @since jpicedt 1.3.2
 * @author Sylvain Reynal
 * @version $Id: ContentType.java,v 1.9 2011/10/29 04:53:40 vincentb1 Exp $
 *
 */
public interface ContentType {

    /**
	 * Return the presentation name of this content-type
	 */
    String getPresentationName();

    /**
	 * Creates a ViewFactory that's suited for this content-type
	 */
    ViewFactory createViewFactory();

    /**
	 * Creates a FormatterFactory that's suited for this content-type
	 */
    FormatterFactory createFormatter();

    /**
	 * Configure the EditorKit and the FormatterFactory returned by the factory methods,
	 * from the given Properties
	 */
    void configure(Properties p);

    /**
	 * Returns a customizer panel for this content-type
	 * @return null if no customizer is available for this content-type
	 * @param prop used to init the component or to store preferences on-demand.
	 */
    AbstractCustomizer createCustomizer(Properties prop);
}
