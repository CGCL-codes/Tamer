package com.teknokala.xtempore;

import com.teknokala.xtempore.xml.BufferedXMLOutput;

/**
 * A template interface.
 * 
 * @author Timo Santasalo <timo.santasalo@teknokala.com>
 * @see TemplateFactory
 * @see TemplateImpl
 */
public interface Template extends ContentFactory {

    /**
	 * Namespace URI of Xtempore templating language and standard function library:
	 * <code>http://teknokala.com/xml-schema/xtempore</code>
	 * @see com.teknokala.xtempore.util.DefaultFunctions
	 */
    public static final String NS_URI = "http://teknokala.com/xml-schema/xtempore";

    /**
	 * Returns the system id of this template.
	 * @return System id of this template or null if the template has none.
	 */
    public String getSystemId();

    /**
	 * Transforms an object.
	 * 
	 * @param model An object to transform.
	 * @param out A stream to write output to.
	 * @throws TransformException If an unrecoverable error occurs during transformation.
	 */
    public void transform(Object model, BufferedXMLOutput out) throws TransformException;

    /**
	 * Invokes transformation which given context.
	 * 
	 * @param ctx Context stack.
	 * @param out A stream to write output to.
	 * @throws TransformException
	 */
    public void transform(ContextStack ctx, BufferedXMLOutput out) throws TransformException;

    /**
	 * Invokes transformation which given context and resolver.
	 * 
	 * @param ctx Context stack.
	 * @param out A stream to write output to.
	 * @param ncr Reference to content resolver.
	 * @throws TransformException
	 */
    public void transform(ContextStack ctx, BufferedXMLOutput out, ContentResolver ncr) throws TransformException;
}
