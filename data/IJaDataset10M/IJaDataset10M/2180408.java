package hudson.zipscript.template;

import hudson.zipscript.ResourceContainer;
import hudson.zipscript.parser.context.Context;
import hudson.zipscript.parser.exception.ExecutionException;
import java.io.Writer;
import java.util.Locale;

public interface Template {

    /**
	 * This is optional but can be used in conjunction with the [#initialize]
	 * directive. Any code within that block will be executed. There are
	 * caviats, however. They can only refer to global variables (no scoped
	 * variables or macro parameters). They will also not be executed multiple
	 * times within a looping directive.
	 * 
	 * @param context
	 *            the context to inialize
	 * @return the initialized context
	 * @throws ExecutionException
	 */
    public Context initialize(Object context) throws ExecutionException;

    /**
	 * This is optional but can be used in conjunction with the [#initialize]
	 * directive. Any code within that block will be executed. There are
	 * caviats, however. They can only refer to global variables (no scoped
	 * variables or macro parameters). They will also not be executed multiple
	 * times within a looping directive.
	 * 
	 * @param context
	 *            the context to inialize
	 * @param locale
	 *            the locale
	 * @return the initialized context
	 * @throws ExecutionException
	 */
    public Context initialize(Object context, Locale locale) throws ExecutionException;

    /**
	 * Merge this template with the data provided with the context. The context
	 * can be any typee of object.
	 * 
	 * @param context
	 *            the context
	 * @return a String containing the merged result
	 * @throws ExecutionException
	 */
    public String merge(Object context) throws ExecutionException;

    /**
	 * Merge this template with the data provided with the context. The context
	 * can be any typee of object.
	 * 
	 * @param context
	 *            the context
	 * @param locale
	 *            the locale
	 * @return a String containing the merged result
	 * @throws ExecutionException
	 */
    public String merge(Object context, Locale locale) throws ExecutionException;

    /**
	 * Merge this template with the data provided with the context. Used the
	 * writer to append the string results.
	 * 
	 * @param context
	 *            the context
	 * @param writer
	 *            the writer
	 * @throws ExecutionException
	 */
    public void merge(Object context, Writer writer) throws ExecutionException;

    /**
	 * Merge this template with the data provided with the context. Used the
	 * writer to append the string results.
	 * 
	 * @param context
	 *            the context
	 * @param writer
	 *            the writer
	 * @param locale
	 * @throws ExecutionException
	 */
    public void merge(Object context, Writer writer, Locale locale) throws ExecutionException;

    /**
	 * Set the resource container to retrieve ZipEngine resources
	 */
    public void setResourceContainer(ResourceContainer resourceContainer);

    /**
	 * Return the resource container
	 */
    public ResourceContainer getResourceContainer();
}
