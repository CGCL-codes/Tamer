package net.sourceforge.squirrel_sql.plugins.netezza.tab;

import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.FormattedSourceTab;
import net.sourceforge.squirrel_sql.fw.codereformat.ICodeReformator;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This class provides the necessary information to the parent tab to display the source for a Netezza 
 * procedure.  It uses a custom formatter class (NetezzaProcedureFormator) that can handle formatting Netezza 
 * stored procedures (and not much else).
 */
public class ProcedureSourceTab extends FormattedSourceTab {

    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(ProcedureSourceTab.class);

    public static interface i18n {

        String hint = s_stringMgr.getString("ProcedureSourceTab.hint");
    }

    /**
	 * Constructor
	 * 
	 * @param stmtSep
	 *           the string to use to separate SQL statements
	 */
    public ProcedureSourceTab(String stmtSep) {
        super(i18n.hint);
        ICodeReformator formator = new NetezzaProcedureFormator(stmtSep);
        super.setupFormatter(formator, stmtSep, null);
        super.setCompressWhitespace(true);
        super.appendSeparator = false;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.table.PSFormattedSourceTab#getSqlStatement()
	 */
    @Override
    protected String getSqlStatement() {
        return "SELECT " + "'create or replace procedure ' || proceduresignature || ' returns ' || returns || " + "' LANGUAGE NZPLSQL AS BEGIN_PROC ' || proceduresource || ' END_PROC;' " + "FROM _v_procedure " + "WHERE owner = ? " + "and procedure = ? ";
    }

    /**
    * Overridden as the super implementation binds schemaname rather than catalogname as is used
    * in Netezza.
    * 
    * @return a String array of bind variable values
    */
    @Override
    protected String[] getBindValues() {
        final IDatabaseObjectInfo doi = getDatabaseObjectInfo();
        return new String[] { doi.getSchemaName(), doi.getSimpleName() };
    }
}
