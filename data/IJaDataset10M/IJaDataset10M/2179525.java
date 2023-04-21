package net.sf.javascribe.designs.resource;

import net.sf.javascribe.generator.accessor.DataSourceManagerTypeAccessor;
import net.sf.javascribe.generator.context.processor.JavaCodeExecutionContext;
import net.sf.javascribe.generator.java.JavaCodeSnippet;
import net.sf.javascribe.generator.sql.DataSourceObjectNameResolver;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractJdbcDataSourceManager implements DataSourceManagerTypeAccessor {

    String dataSourceName = null;

    public String getClassName() {
        return "Connection";
    }

    public String getImport() {
        return "java.sql.Connection";
    }

    public JavaCodeSnippet rollbackTransaction(String varName, JavaCodeExecutionContext execCtx) {
        JavaCodeSnippet ret = new JavaCodeSnippet();
        ret.getSource().append(varName + ".rollback();\n");
        execCtx.throwException("SQLException");
        return ret;
    }

    public JavaCodeSnippet commitTransaction(String varName, JavaCodeExecutionContext execCtx) {
        JavaCodeSnippet ret = new JavaCodeSnippet();
        ret.getSource().append(varName + ".commit();\n");
        execCtx.throwException("SQLException");
        return ret;
    }

    public String getName() {
        return dataSourceName;
    }

    public abstract DataSourceObjectNameResolver getResolver();
}
