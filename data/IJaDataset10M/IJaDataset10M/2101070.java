package org.dengues.core;

import org.dengues.core.process.ICompNode;
import org.dengues.core.scripts.ScriptResultBean;
import org.dengues.model.database.DBColumn;
import org.eclipse.swt.widgets.Shell;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-6-3 qiang.zhang $
 * 
 */
public interface IScriptsPluginService extends IDenguesService {

    public static final String MATATPYE_INPUT = "INPUT";

    public static final String MATATPYE_OUTPUT = "OUTPUT";

    public static final String MATATPYE_VAR = "VARs";

    String NODE_UNIQUE_NAME = "NODE_UNIQUE";

    String[] OPERATORS = new String[] { "+", "-", "*", "/", "=", "''", "||" };

    ScriptResultBean openScriptsManager(Shell shell, DBColumn table, ICompNode compNode, String expression);
}
