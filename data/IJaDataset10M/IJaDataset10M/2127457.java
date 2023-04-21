package com.google.gwt.resources.client;

import com.google.gwt.resources.ext.ResourceGeneratorType;
import com.google.gwt.resources.rg.DataResourceGenerator;
import com.google.gwt.safehtml.shared.SafeUri;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A non-text resource. Use {@link MimeType} to provide MIME Types for embedded
 * resources which may not be determined automatically at compile time. Use
 * {@link DoNotEmbed} to prevent a resource from being embedded.
 */
@ResourceGeneratorType(DataResourceGenerator.class)
public interface DataResource extends ResourcePrototype {

    /**
   * Specifies that the resource or resources associated with the
   * {@link ResourcePrototype} should not be embedded into the compiled output.
   * This may be useful, for exmaple, when it a particular browser or plugin is
   * unable to handle RFC 2397 data URLs.
   */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DoNotEmbed {
    }

    /**
   * Specifies the MIME Type of the resource or resources associated with the
   * {@link ResourcePrototype}.
   */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MimeType {

        String value();
    }

    /**
   * Retrieves a URL by which the contents of the resource can be obtained. This
   * will be an absolute URL.
   */
    SafeUri getSafeUri();

    /**
   * Retrieves a URL by which the contents of the resource can be obtained. This
   * will be an absolute URL.
   *
   * @deprecated Use {@link #getSafeUri()} instead.
   */
    @Deprecated
    String getUrl();
}
