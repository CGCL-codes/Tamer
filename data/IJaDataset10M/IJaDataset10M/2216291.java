package org.eclipse.wst.xml.search.core.queryspecifications.querybuilder;

public interface IStringQueryBuilderProvider {

    IStringQueryBuilder getEqualsStringQueryBuilder();

    IStringQueryBuilder getStartsWithStringQueryBuilder();
}
