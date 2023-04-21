package com.carbonfive.flash;

import com.carbonfive.flash.decoder.*;
import com.carbonfive.flash.encoder.*;
import org.apache.commons.logging.*;

/**
  * ASTranslator provides the ability to translate between ASObjects used by
  * Macromedia Flash Remoting and Java objects in your application.
  * <a href="package-summary.html#documentation">See the project documentation</a> for details.
  * <p>
  * $Id: ASTranslator.java 99 2004-03-03 20:42:38Z wynholds $
  */
public class ASTranslator {

    private static final Log log = LogFactory.getLog(ASTranslator.class);

    private TranslationFilter filter = null;

    private boolean useEquivalence = false;

    public ASTranslator() {
        filter = TranslationFilter.getBaseFilter();
    }

    /**
    * Ignore objects that are an instance of <code>klass</code> when translating.  For
    * example, <code>ignoreClass(Object.class)</code> would cause ASTranslator to ignore
    * everything.<br/>
    * Objects that are ignored are returned from ASTranslator as null.
    * @param klass The class (or superclass or interface) to ignore
    */
    public void ignoreClass(Class klass) {
        filter.ignoreClass(klass);
    }

    /**
    * Ignore the specified <code>property</code> of objects that are instances of
    * <code>klass</code>.
    * Properties that are ignored are returned from ASTranslator as null.
    * @param klass The class (or superclass or interface) of the property to ignore
    * @param property The name of the property to ignore
    */
    public void ignoreProperty(Class klass, String property) {
        filter.ignoreProperty(klass, property);
    }

    /**
    * Determines whether object that are equivalent (using Object.equals()) should be
    * considered identical by ASTranslator.
    * <p>
    * When walking the object graph ASTranslator stores a cache of objects viewed.  This
    * cache is used to maintain references within the object graph.  If <code>useEquivalence</code>
    * is set to true, this cache is an equivalence cache, which means objects are compared by
    * Object.equals() instead of the == operator.  So if two objects are equal (but not
    * identical) in the object graph, one will be stored as a reference to the other one when
    * translated to ASObjects.
    * <p>
    * The consequences of this are:<br/>
    * <ul>
    * <li>ASObject graphs can be <b>much</b> smaller</li>
    * <li>Circular references in object graphs will cause infinite loops (within
    *     the object's hashCode() method)</li>
    * <li>Modifying an object in Flash may modify other Flash objects unexpectedly</li>
    * </ul>
    * <p>
    * You should use this only if your Flash code is using the ASObjects in a read-only manner,
    * or you are very well aware of what is going on.
    * <p>
    * This setting also assumes that the Flash Remoting gateway supports maintaining references.
    * Unfortunately, this is not currently the case.  However, it is pretty easy to hack this
    * behavior in to Flash Remoting or OpenAMF.
    * <p>
    * The default value for <code>useEquivalence</code> is false.
    *
    * @param b Whether to use equivalence or not
    */
    public void setUseEquivalence(boolean b) {
        this.useEquivalence = b;
    }

    /**
   * Given an Object, toActionScript creates a corresponding ASObject
   * or Collection of ASObjects that maps the source object's JavaBean
   * properties to ASObject fields, Collections and Sets to
   * ArrayLists, and all Numbers to Doubles while maintaining object
   * references (including circular references).
   * <p>
   * These mappings are consistent with Flash Remoting's rules for
   * converting Objects to ASObjects. They just add the behavior of
   * using JavaBean-style introspection to determine property
   * names. Additionally, toActionScript sets the "type" field of all
   * ASObjects created to be the class name of the source
   * JavaBean. This enables two-way translation between ASObjects and
   * JavaBeans.
   *
   * @param serverObject  an Object to translate to ASObjects or
   *                      corresponding primitive or Collection classes
   * @return              an Object that may be an ASObject or nested
   *                      Collections of ASObjects or null if translation fails
   */
    public Object toActionScript(Object serverObject) {
        if (serverObject == null) return null;
        CachingManager.createEncoderCache(this.useEquivalence);
        Object result = null;
        try {
            Context ctx = Context.getBaseContext();
            ctx.setFilter(filter);
            ActionScriptEncoder encoder = EncoderFactory.getInstance().getEncoder(ctx, serverObject);
            result = encoder.encodeObject(ctx, serverObject);
        } finally {
            CachingManager.removeEncoderCache();
        }
        return result;
    }

    /**
   * Given an Object that is either an ASObject or Collection of
   * ASObjects, fromActionScript creates a corresponding JavaBean or
   * Collection of JavaBeans.
   * <p>
   * The "type" field of an ASObject identifies the class name of
   * the JavaBean to create. If the type field is null, an ASObject
   * becomes a HashMap. The interface of the JavaBean is more specific
   * that the relatively loosely-typed ASObject and therefore drives
   * the translation of ASObject fields.

   *
   * @param asObject an Object that is usually an ASObject but may also be
   *                           a Collection or primitive
   * @return    an Object value that is either a JavaBean or Collection
   *            of JavaBeans or null if translation fails
   */
    public Object fromActionScript(Object asObject) {
        if (asObject == null) return null;
        Class desiredBeanClass = DecoderFactory.decideClassToTranslateInto(asObject);
        return fromActionScript(asObject, desiredBeanClass);
    }

    /**
   * Translate an object to another object of type 'desiredBeanClass'
   * obj types should be ASObject, Boolean, String, Number, Date, ArrayList
   */
    public Object fromActionScript(Object actionScriptObject, Class desiredBeanClass) {
        if (actionScriptObject == null) return null;
        CachingManager.getDecoderCache();
        Object result = null;
        try {
            ActionScriptDecoder decoder = DecoderFactory.getInstance().getDecoder(actionScriptObject, desiredBeanClass);
            result = decoder.decodeObject(actionScriptObject, desiredBeanClass);
        } finally {
            CachingManager.removeDecoderCache();
        }
        return result;
    }
}
