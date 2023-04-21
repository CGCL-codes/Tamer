package net.sf.isolation.template;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import net.sf.isolation.core.IsoInformation;

/**
 * <p>
 * This class defines the minimal functionality of a template processor.
 * </p>
 * <p>
 * A template processor, normally generates text (like html for example), based
 * on a template (like JSP) and builds a new document using the passed
 * parameters. The templates must be generated with jasper reports, java emitter
 * templates (from eclipse), webmacro or any other engine which support
 * parameters
 * </p>
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>See implementation</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 225 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/template/IsoTemplateProcessor.java $", lastChangedDate = "$LastChangedDate: 2010-08-11 13:41:11 -0400 (Wed, 11 Aug 2010) $")
public interface IsoTemplateProcessor {

    /**
	 * This method must be used for use with text based template. The creation
	 * and managing of the writer are delegated to the caller function. This
	 * function only writes in the writer and never close that.
	 * 
	 * @param name
	 *            The name of the template
	 * @param parameters
	 *            The parameters to be used in the template
	 * @param writer
	 *            Where the template processor write the result of processing
	 *            the template
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    void process(String name, Writer writer, Map<String, Serializable> parameters);

    /**
	 * This method must be used for use with text based template. The creation
	 * and managing of the writer are delegated to the caller function. This
	 * function only writes in the writer and never close that.
	 * 
	 * @param name
	 *            The name of the template
	 * @param parameter
	 *            The parameters to be used in the template
	 * @param writer
	 *            Where the template processor write the result of processing
	 *            the template
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    void process(String name, Writer writer, Serializable parameter);

    /**
	 * This method must be used for use with text binary data template, like an
	 * image. The creation and managing of the outputStream are delegated to the
	 * caller function. This function only writes in the outputStream and never
	 * close that.
	 * 
	 * @param name
	 *            The name of the template
	 * @param parameters
	 *            The parameters to be used in the template
	 * @param stream
	 *            Where the template processor write the result of processing
	 *            the template
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    void process(String name, OutputStream stream, Map<String, Serializable> parameters);

    /**
	 * This method must be used for use with text binary data template, like an
	 * image. The creation and managing of the outputStream are delegated to the
	 * caller function. This function only writes in the outputStream and never
	 * close that.
	 * 
	 * @param name
	 *            The name of the template
	 * @param parameter
	 *            The parameters to be used in the template
	 * @param stream
	 *            Where the template processor write the result of processing
	 *            the template
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    void process(String name, OutputStream stream, Serializable parameter);

    String getMimeType(String name);

    String getCharset(String name);

    Collection<InputStream> getFiles();
}
