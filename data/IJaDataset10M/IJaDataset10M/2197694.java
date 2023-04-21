package com.volantis.mcs.runtime.packagers;

import com.volantis.mcs.context.MarinerRequestContext;

/**
 * A packager's responsibility is to take a response's body and
 * appropriate associated resources (e.g. image files etc.) and to
 * package them together into some form of composite response.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface Packager {

    /**
     * A package is created using this method. The body of the package is
     * obtained from the given body source, while the resources or references
     * to the resources are obtained from the ApplicationContext's
     * PackageResources instance.
     *
     * @param context     the request context associated with the packaging
     *                    operation
     * @param bodySource  the source of the package's body content
     * @param bodyContext a contextual object of relevance to the package
     *                    bodySource
     */
    void createPackage(MarinerRequestContext context, PackageBodySource bodySource, Object bodyContext) throws PackagingException;
}
