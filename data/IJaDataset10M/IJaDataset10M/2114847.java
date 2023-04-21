package org.sample.xmlsearch.jetty.core;

import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.IStringQueryBuilder;
import org.eclipse.wst.xml.search.core.queryspecifications.querybuilder.StartsWithStringQueryBuilder;

public class StartsWithStringQueryBuilderIgnoreRef extends StartsWithStringQueryBuilder {

    public static IStringQueryBuilder INSTANCE = new StartsWithStringQueryBuilderIgnoreRef();

    public String getId() {
        return StartsWithStringQueryBuilderIgnoreRef.class.getSimpleName();
    }

    @Override
    protected void build(StringBuilder xpath, String[] targetNodes, int startIndex) {
        super.build(xpath, targetNodes, startIndex);
        xpath.append("[name() != \"Ref\"]");
    }
}
