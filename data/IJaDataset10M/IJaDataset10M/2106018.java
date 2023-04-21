package net.sourceforge.mxupdate.update.userinterface;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import matrix.db.Context;
import matrix.util.MatrixException;
import net.sourceforge.mxupdate.update.util.InfoAnno_mxJPO;
import net.sourceforge.mxupdate.util.Mapping_mxJPO.AdminTypeDef;
import static net.sourceforge.mxupdate.update.util.StringUtil_mxJPO.convertTcl;
import static net.sourceforge.mxupdate.util.MqlUtil_mxJPO.execMql;

/**
 *
 * @author tmoxter
 * @version $Id: Menu_mxJPO.java 123 2009-01-16 10:33:47Z tmoxter $
 */
@InfoAnno_mxJPO(adminType = AdminTypeDef.Menu)
public class Menu_mxJPO extends Command_mxJPO {

    /**
     * Defines the serialize version unique identifier.
     */
    private static final long serialVersionUID = 3617033695673460587L;

    boolean treeMenu = false;

    final Stack<MenuChild> childs = new Stack<MenuChild>();

    public Menu_mxJPO() {
        super();
    }

    @Override
    protected void parse(final String _url, final String _content) {
        if ("/commandRefList".equals(_url)) {
        } else if ("/commandRefList/commandRef".equals(_url)) {
            final MenuChild child = new MenuChild();
            child.type = "command";
            this.childs.add(child);
        } else if ("/commandRefList/commandRef/name".equals(_url)) {
            this.childs.peek().name = _content;
        } else if ("/commandRefList/commandRef/order".equals(_url)) {
            this.childs.peek().order = Integer.parseInt(_content);
        } else if ("/menuRefList".equals(_url)) {
        } else if ("/menuRefList/menuRef".equals(_url)) {
            final MenuChild child = new MenuChild();
            child.type = "menu";
            this.childs.add(child);
        } else if ("/menuRefList/menuRef/name".equals(_url)) {
            this.childs.peek().name = _content;
        } else if ("/menuRefList/menuRef/order".equals(_url)) {
            this.childs.peek().order = Integer.parseInt(_content);
        } else {
            super.parse(_url, _content);
        }
    }

    @Override
    protected void prepare(final Context _context) throws MatrixException {
        final StringBuilder cmd = new StringBuilder().append("print menu \"").append(this.getName()).append("\" select parent[Tree] dump");
        if ("TRUE".equalsIgnoreCase(execMql(_context, cmd))) {
            this.treeMenu = true;
        }
        super.prepare(_context);
    }

    @Override
    protected void writeObject(Writer _out) throws IOException {
        super.writeObject(_out);
        final Map<Integer, MenuChild> tmpChilds = new TreeMap<Integer, MenuChild>();
        for (final MenuChild child : this.childs) {
            tmpChilds.put(child.order, child);
        }
        for (final MenuChild child : tmpChilds.values()) {
            _out.append(" \\\n    add ").append(child.type).append(" \"").append(convertTcl(child.name)).append("\"");
        }
        if (this.treeMenu) {
            _out.append("\n\nmql mod menu \"Tree\" \\\n    add menu \"${NAME}\"");
        }
    }

    /**
     * The method overwrites the original method to append the MQL statements
     * in the <code>_preMQLCode</code> to reset this menu:
     * <ul>
     * <li>remove all child commands / menus</li>
     * <li>remove definition as tree menu</li>
     * </ul>
     * The description etc. is reseted in the command super class.
     *
     * @param _context          context for this request
     * @param _preMQLCode       MQL statements which must be called before the
     *                          TCL code is executed
     * @param _postMQLCode      MQL statements which must be called after the
     *                          TCL code is executed
     * @param _preTCLCode       TCL code which is defined before the source
     *                          file is sourced
     * @param _tclVariables     map of all TCL variables where the key is the
     *                          name and the value is value of the TCL variable
     *                          (the value is automatically converted to TCL
     *                          syntax!)
     * @param _sourceFile       souce file with the TCL code to update
     */
    @Override
    protected void update(final Context _context, final CharSequence _preMQLCode, final CharSequence _postMQLCode, final CharSequence _preTCLCode, final Map<String, String> _tclVariables, final File _sourceFile) throws Exception {
        final StringBuilder preMQLCode = new StringBuilder().append("mod ").append(this.getInfoAnno().adminType().getMxName()).append(" \"").append(this.getName()).append('\"');
        for (final MenuChild child : this.childs) {
            preMQLCode.append(" remove ").append(child.type).append(" \"").append(child.name).append("\"");
        }
        preMQLCode.append(";\n");
        if (this.treeMenu) {
            preMQLCode.append("mod menu Tree remove menu \"").append(this.getName()).append("\";\n");
        }
        preMQLCode.append(_preMQLCode);
        super.update(_context, preMQLCode, _postMQLCode, _preTCLCode, _tclVariables, _sourceFile);
    }

    /**
     *
     */
    class MenuChild {

        String type = null;

        String name = null;

        Integer order = null;
    }
}
