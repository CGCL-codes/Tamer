package org.ufacekit.core.dbel;

import org.ufacekit.core.dbel.impl.XAMLDBELParserImpl;

/**
 * Default {@link IDBELManager} implementation which register
 * {@link XamlDBELParser}.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class DefaultDBELManager extends AbstractDBELManager {

    private static final DBELManager instance = new DefaultDBELManager();

    public static DBELManager getInstance() {
        return instance;
    }

    protected void registerDefaultBindingExpressionParsers() {
        super.registerDBELParser(XAMLDBELParserImpl.getInstance());
    }
}
