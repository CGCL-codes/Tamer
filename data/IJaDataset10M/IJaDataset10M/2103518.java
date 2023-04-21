package fr.x9c.cadmium.primitives.cadmiumxml;

import java.net.URL;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;
import fr.x9c.cadmium.primitives.cadmium.Cadmium;

/**
 * This class provides the primitives for schema manipulation.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Schemas {

    /**
     * No instance of this class.
     */
    private Schemas() {
    }

    /**
     * Constructs a schema factory.
     * @param ctxt context
     * @param lang schema language
     * @return a new schema factory
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schemafactory_make(final CodeRunner ctxt, final Value lang) throws Fail.Exception {
        try {
            final SchemaFactory res = SchemaFactory.newInstance(lang.asBlock().asString());
            return Cadmium.createObject(res);
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Sets the value of a feature flag.
     * @param ctxt context
     * @param fact factory
     * @param name feature name
     * @param value feature value
     * @return <i>unit</i>
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schemafactory_set_feature(final CodeRunner ctxt, final Value fact, final Value name, final Value value) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            sf.setFeature(name.asBlock().asString(), value == Value.TRUE);
            return Value.UNIT;
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Gets the value of a feature flag.
     * @param ctxt context
     * @param fact factory
     * @param name feature name
     * @return value of passed feature
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schemafactory_get_feature(final CodeRunner ctxt, final Value fact, final Value name) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            return sf.getFeature(name.asBlock().asString()) ? Value.TRUE : Value.FALSE;
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Sets the value of a property.
     * @param ctxt context
     * @param fact factory
     * @param name property name
     * @param obj property value
     * @return <i>unit</i>
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schemafactory_set_property(final CodeRunner ctxt, final Value fact, final Value name, final Value obj) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            sf.setProperty(name.asBlock().asString(), obj.asBlock().asCustom());
            return Value.UNIT;
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Gets the value of a property.
     * @param ctxt context
     * @param fact factory
     * @param name property name
     * @return value of passed property
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schemafactory_get_property(final CodeRunner ctxt, final Value fact, final Value name) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            return Cadmium.createObject(sf.getProperty(name.asBlock().asString()));
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Constructs a schema from a file.
     * @param ctxt context
     * @param fact factory
     * @param file filename
     * @return schema constructed from file
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schema_from_file(final CodeRunner ctxt, final Value fact, final Value file) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            return Cadmium.createObject(sf.newSchema(new StreamSource(ctxt.getContext().getInputStreamForPath(file))));
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }

    /**
     * Constructs a schema from a URL.
     * @param ctxt context
     * @param fact factory
     * @param url URL
     * @return schema constructed from url
     * @throws Fail.Exception if any error occurs
     */
    @Primitive
    public static Value cadmiumxml_schema_from_url(final CodeRunner ctxt, final Value fact, final Value url) throws Fail.Exception {
        try {
            final SchemaFactory sf = (SchemaFactory) fact.asBlock().asCustom();
            return Cadmium.createObject(sf.newSchema(new URL(url.asBlock().asString())));
        } catch (final Exception e) {
            Cadmium.fail(ctxt, e);
            return Value.UNIT;
        }
    }
}
