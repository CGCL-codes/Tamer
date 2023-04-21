package org.genxdm.xs.facets;

import org.genxdm.xs.exceptions.SchemaRegExCompileException;

public interface SchemaRegExCompiler {

    RegExPattern compile(String regex) throws SchemaRegExCompileException;

    RegExPattern compile(String regex, String flags) throws SchemaRegExCompileException;
}
