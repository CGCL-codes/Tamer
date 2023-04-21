package org.exist.xquery.modules.facet;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

/**
 * Module function definitions for Facet search
 *
 * @author aschuth
 *
 */
public class FacetModule extends AbstractInternalModule {

    public static final String NAMESPACE_URI = "http://exist-db.org/xquery/facet";

    public static final String PREFIX = "facet";

    public static final String INCLUSION_DATE = "2010-10-10";

    public static final String RELEASED_IN_VERSION = "eXist-1.5";

    public static final FunctionDef[] functions = { new FunctionDef(Query.signatures[0], Query.class), new FunctionDef(Query.signatures[1], Query.class), new FunctionDef(Counts.signatures[0], Counts.class), new FunctionDef(Counts.signatures[1], Counts.class), new FunctionDef(Counts.signatures[2], Counts.class), new FunctionDef(Add.signatures[0], Add.class) };

    public FacetModule() {
        super(functions, false);
    }

    public String getNamespaceURI() {
        return NAMESPACE_URI;
    }

    public String getDefaultPrefix() {
        return PREFIX;
    }

    public String getDescription() {
        return "A module for facet search.";
    }

    public String getReleaseVersion() {
        return RELEASED_IN_VERSION;
    }
}
