package net.sf.javascribe.designs.model.hibernate;

import java.io.File;
import java.util.HashMap;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.appdef.wrapper.model.DataSourceWrapper;
import net.sf.javascribe.designs.resource.HibernateSessionDataSourceManager;
import net.sf.javascribe.generator.SourceFile;
import net.sf.javascribe.generator.accessor.DataSourceManagerTypeAccessor;
import net.sf.javascribe.generator.context.processor.ComponentProcessorContext;
import net.sf.javascribe.generator.context.processor.TxLocatorProcessorContext;
import net.sf.javascribe.generator.java.JavaCodeGenerator;
import net.sf.javascribe.generator.processor.TxLocatorProcessor;

/**
 * @author DCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultHibernateSessionTxLocatorProcessor extends JavaCodeGenerator implements TxLocatorProcessor {

    DataSourceWrapper dataSource = null;

    TxLocatorProcessorContext ctx = null;

    public void setProcessorContext(ComponentProcessorContext c) throws ProcessingException {
        ctx = (TxLocatorProcessorContext) c;
        dataSource = ctx.getDataSource();
        super.setProcessorContext(c);
    }

    public void processDataSource() throws ProcessingException {
        DataSourceManagerTypeAccessor txObjectType = null;
        SourceFile file = null;
        HashMap<String, String> params = null;
        String pkg = null;
        String className = null;
        txObjectType = ctx.getDataSourceManager();
        if (!(txObjectType instanceof HibernateSessionDataSourceManager)) throw new ProcessingException("Hibernate Tx Locator may only be used for a Hibernate Session DataSourceManager");
        pkg = ctx.getProperty("java.root.package") + '.' + ctx.getProperty("model");
        className = ctx.getDataSource().getDataSourceName() + "HibernateTxLocator";
        file = new SourceFile();
        file.setPath(ctx.getProperty("java.source") + File.separatorChar + pkg.replace('.', File.separatorChar) + File.separatorChar + className + ".java");
        params = new HashMap<String, String>();
        params.put("package", pkg);
        params.put("class.name", className);
        file.setSource(super.parseTemplate("HibernateTxLocator.template", params));
        ctx.addSourceFile(file);
    }
}
