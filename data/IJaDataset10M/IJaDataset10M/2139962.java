package com.gorillalogic.faces.beans;

import java.beans.Beans;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import com.gorillalogic.accounts.GXESession;
import com.gorillalogic.config.Bootstrap;
import com.gorillalogic.config.Preferences;
import com.gorillalogic.dal.AccessException;
import com.gorillalogic.dal.BoundsException;
import com.gorillalogic.dal.PathStrategy;
import com.gorillalogic.dal.PkgTable;
import com.gorillalogic.dal.StructureException;
import com.gorillalogic.dal.Table;
import com.gorillalogic.dal.Txn;
import com.gorillalogic.dal.Type;
import com.gorillalogic.dal.utils.GoshUtils;
import com.gorillalogic.faces.FacesException;
import com.gorillalogic.faces.FacesRuntimeException;
import com.gorillalogic.faces.components.Explorer;
import com.gorillalogic.faces.components.ExplorerItem;
import com.gorillalogic.faces.components.GorillaComponent;
import com.gorillalogic.faces.components.Messages;
import com.gorillalogic.faces.components.Panel;
import com.gorillalogic.faces.components.Command;
import com.gorillalogic.faces.components.glui.GLUICommand;
import com.gorillalogic.faces.components.glui.GLUIComponentBase;
import com.gorillalogic.faces.listeners.UsecaseActionListener;
import com.gorillalogic.faces.util.FacesUtils;
import com.gorillalogic.glob.GLBase;
import com.gorillalogic.glob.impl.CommonGLBase;
import com.gorillalogic.glob.impl.GLObjectImpl;
import com.gorillalogic.glob.impl.GLObjectListImpl;
import com.gorillalogic.gython.glob.impl.GyImpl;
import com.gorillalogic.gosh.commands.MsgCommand;
import com.gorillalogic.gython.GythonException;
import com.gorillalogic.modelx.state.common.FlowEventDispatcher;
import com.gorillalogic.util.ExceptionLogger;
import com.gorillalogic.webapp.actions.WebappAction;

/**
 * @author Stu
 * 
 */
public class GlSession extends GyImpl {

    static Logger logger = Logger.getLogger(GlSession.class);

    private static GlSession _designSession;

    private boolean _txnBlocked = false;

    private String _resultMsg = null;

    /**
	 * @throws GythonException
	 */
    public GlSession() throws GythonException {
        super();
        logger.debug("Painter enabled: " + System.getProperty("gorilla.config.painterEnabled"));
        FacesUtils.getSessionMap().put("glPainterEnabled", System.getProperty("gorilla.config.painterEnabled"));
    }

    protected String sessionKey = null;

    protected ArrayList messages = new ArrayList();

    public static final String SESSION_ID = "glSession";

    public static final String LAST_TIMESTAMP_ID = "glLastReq";

    public static final String CURRENT_CONTEXT = SESSION_ID + "[\".\"]";

    public static final String SCRIPT = "script";

    public static final String ACTION = "glAction";

    public static final String CONTEXT = "context";

    public static final String GXESESSION = WebappAction.GXESESSION;

    public static final String VIEW = "view";

    public static final String LIST = "glList";

    public static final String EDIT = "glEdit";

    public static final String DETAIL = "glDetail";

    public static final String HOME = "glHome";

    public static final String ROOT = "glRoot";

    public static final String ERROR = "glError";

    public static final String WELCOME = "glWelcome";

    public static final String LOGIN = "glLogin";

    public static final String SELECT = "glSelect";

    public static final String SEARCH = "glSearch";

    public static final String SEARCH_RESULTS = "glSearchResults";

    public static final String[] _viewIds = { EDIT, DETAIL, HOME, ROOT, ERROR, WELCOME, LOGIN, SELECT, SEARCH, SEARCH_RESULTS };

    /**
	 * MethodBindings
	 */
    public static final String EXPLORE_MB = "#{" + SESSION_ID + ".explore}";

    public static final String EXEC_MB = "#{" + SESSION_ID + ".exec}";

    /**
	 * ValueBindings
	 */
    public static final String CURRENT_VB = "#{" + CURRENT_CONTEXT + "}";

    private HashMap _openItems = new HashMap();

    private String _view;

    /**
	 * Execute gython code
	 * 
	 * @param source -
	 *            The gython code to be executed
	 */
    public void exec(String source) throws AbortProcessingException {
        if (source.equals(Command.SAVE_SCRIPT)) {
            if (Txn.mgr.inProgress()) {
                source = "end -f";
            } else {
                logger.debug("\"" + source + "\" issued with no open transaction, ignoring...");
                return;
            }
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("About to execute + \"" + source + "\" in following context:\n" + getCurrent().dump());
            }
            super.exec("cd .");
            Table shaved = getCurrentTable().shave();
            _gython.run(new java.io.StringReader(source));
            if (shaved.equals(getCurrentTable())) {
                super.exec("cd " + shaved.path(PathStrategy.encoded));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Resulting context after + \"" + source + "\"\n" + getCurrent().dump());
            }
        } catch (FacesRuntimeException e) {
            displayException(e, "Unexpected error: " + e.getMessage());
        } catch (GythonException e) {
            displayException(e, "Error executing command: " + e.getMessage());
        }
    }

    private void displayException(Exception e, String msg) {
        FacesContext fc = FacesUtils.getFc();
        FacesUtils.displayError(e);
        logger.debug(msg, e);
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, GlSession.ERROR);
    }

    /**
	 * Display a warning message
	 * 
	 * @param context -
	 *            gcl expression referencing error context or null if there is
	 *            none
	 * @param -
	 *            The message text. Can include markup.
	 */
    public void addMessage(String context, String message) {
        addMessage(context, message, Messages.WARN);
    }

    public void addMessage(String context, String message, String level) {
        Messages errors = (Messages) FacesUtils.findChild(FacesUtils.getFc().getViewRoot(), Messages.class);
        if (errors != null) {
            errors.setRendered(true);
        }
        messages.add(new String[] { context, message, level });
    }

    public GLBase getCurrent() {
        GLBase context = (GLBase) get(".");
        return context;
    }

    public void exec(ValueChangeEvent ev) throws AbortProcessingException {
        String source = (String) ev.getNewValue();
        if (source == null || source.trim().length() == 0) {
            return;
        }
        UIComponent component = (UIComponent) ev.getComponent();
        Map map = component.getAttributes();
        exec(source);
    }

    public void exec(ActionEvent ev) throws AbortProcessingException, FacesException {
        UIComponent component = ev.getComponent();
        Map map = component.getAttributes();
        String source = (String) map.get(SCRIPT);
        if (source != null) {
            exec(FacesUtils.evalVbExpr(source));
        }
        FacesContext fc = FacesUtils.getFc();
        Application app = fc.getApplication();
        if (component instanceof GLUIComponentBase.GLUIComponentExtension) {
            GLUICommand command = (GLUICommand) ((GLUIComponentBase.GLUIComponentExtension) component).getWrapper();
            if (command != null) {
                if (command.isImmediate()) {
                    FacesUtils.getFc().renderResponse();
                }
            }
        } else {
            if (component instanceof javax.faces.component.UICommand) {
                javax.faces.component.UICommand command = (javax.faces.component.UICommand) component;
                if (command != null) {
                    if (command.isImmediate()) {
                        FacesUtils.getFc().renderResponse();
                    }
                }
            }
        }
        String view = (String) map.get(GorillaComponent.VIEW);
        if (view != null) {
            FacesUtils.dispatchContext(FacesUtils.getCurrentTable(), view);
            return;
        }
        String href = (String) map.get(GorillaComponent.HREF);
        if (href != null) {
            FacesUtils.dispatchPage(href);
            return;
        }
        FacesUtils.dispatchContext(FacesUtils.getCurrentTable());
    }

    public static GlSession getCurrentInstance() {
        GlSession glSession = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            Application app = fc.getApplication();
            glSession = (GlSession) app.getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), SESSION_ID);
        }
        if (glSession == null && Beans.isDesignTime()) {
            if (_designSession == null) {
                initDesignSession();
            }
            glSession = _designSession;
        }
        return glSession;
    }

    private static void initDesignSession() {
        try {
            System.setProperty(Preferences.GORILLA_HOME, "/home/ghome");
            Bootstrap.init();
            _designSession = new GlSession();
            FacesUtils.getSessionMap().put(SESSION_ID, _designSession);
            if (logger.isDebugEnabled()) {
                ValueBinding vb = FacesUtils.getApp().createValueBinding("#{glSession[\".\"]}");
                Object obj = vb.getValue(FacesUtils.getFc());
                logger.debug("Verifying glSession: " + ((obj == null) ? null : obj.getClass().getName()));
            }
            logger.debug("Created design time session");
        } catch (Exception e) {
            throw new FacesRuntimeException(logger, "Error creating design time session", e);
        }
    }

    /**
	 * Action invoked by Explorer and ExplorerItem
	 * 
	 * @param ev
	 */
    public void explore(ActionEvent ev) throws FacesException {
        UIComponent c = ev.getComponent().getParent();
        ExplorerItem item;
        if (c instanceof Explorer) {
            item = (ExplorerItem) c.getFacets().get("glNode");
        } else {
            item = (ExplorerItem) ev.getComponent().getParent();
        }
        explore(item);
    }

    public void explore(ExplorerItem item) throws FacesException {
        if (item.getAbsoluteContext() == null) {
            return;
        }
        GLObjectListImpl globList = (GLObjectListImpl) FacesUtils.getGLObjectList(item.getAbsoluteContext());
        Table query = globList.getTable();
        PkgTable pkg;
        try {
            pkg = PkgTable.factory.gclToTable(query.path()).asPkgTable();
        } catch (AccessException e) {
            throw (AbortProcessingException) ExceptionLogger.log(logger, new AbortProcessingException("Error getting package: " + query.path() + ": " + e.getMessage(), e));
        }
        if (pkg == null) {
            exec("cd " + globList.gclKey());
            FacesUtils.dispatchContext(getCurrentTable(), LIST);
            return;
        }
        Explorer explorer = (Explorer) item.getParent();
        if (!explorer.isOpen()) {
            close(explorer, pkg);
        } else {
            _openItems.put(pkg.path(PathStrategy.encoded), null);
            open(explorer, pkg);
        }
    }

    private void close(Explorer explorer, PkgTable pkg) {
        _openItems.remove(pkg.path(PathStrategy.encoded));
        List kids = explorer.getChildren();
        Panel panel = (Panel) explorer.findChild(Panel.class);
        if (panel != null) {
            kids.remove(panel);
        }
    }

    private void open(Explorer explorer, PkgTable pkg) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Panel panel = (Panel) explorer.findChild(Panel.class);
        if (panel != null) {
            return;
        }
        panel = new Panel();
        panel.setId(panel.getId("panel"));
        explorer.getChildren().add(panel);
        panel.setStyleClass(Explorer.ITEMS_STYLE);
        List kids = panel.getChildren();
        PkgTable.PkgItr i = pkg.pkgLoopLock(false);
        String id;
        try {
            while (i.next()) {
                ExplorerItem exp;
                if (i.isPkg()) {
                    PkgTable p;
                    try {
                        p = i.getPkgEntry();
                    } catch (StructureException e) {
                        throw (AbortProcessingException) ExceptionLogger.log(logger, new AbortProcessingException("Error getting package: " + i.path() + ": " + e.getMessage()));
                    }
                    if (p.getName().startsWith(Type.GLPREFIX)) {
                        continue;
                    }
                    Explorer nextPkg = new Explorer();
                    nextPkg.setCreatedByExplorer(true);
                    addExplorerItem(nextPkg, kids, p);
                    String pkgId = null;
                    pkgId = p.path(PathStrategy.encoded);
                    if (_openItems.containsKey(pkgId)) {
                        nextPkg.setOpen(true);
                        open(nextPkg, (PkgTable) p);
                    }
                } else {
                    exp = new ExplorerItem();
                    addExplorerItem(exp, kids, i.getEntry());
                }
            }
        } finally {
            i.release();
        }
    }

    private void addExplorerItem(GorillaComponent exp, List kids, Table t) {
        if (t.rowCount() == 1 && t.getExtent().rowCount() == 1) {
            t = t.getExtent();
        }
        exp.setId(exp.getId() + "_" + t.getName());
        exp.setContext(t.path());
        exp.setLabel(t.getName());
        kids.add(exp);
    }

    public Object get(Object key) {
        String prop = (String) key;
        return get(key, getCurrentTable());
    }

    public Object put(Object key, Object value) {
        try {
            Table t;
            try {
                t = FacesUtils.gclToTable((String) key);
            } catch (FacesException e) {
                throw new FacesRuntimeException("Error trying to set value: " + e.getMessage(), e);
            }
            Table.Row r = t.asRow();
            if (t.column(0).getType().isValueType()) {
                r.setString(0, (String) value);
                return null;
            }
            r.setTable(0, (Table) value);
            return null;
        } catch (AccessException e) {
            throw new FacesRuntimeException("Error updating " + key);
        }
    }

    private static final Class[] parms = { ActionEvent.class };

    public static MethodBinding getExecMethodBinding() {
        return FacesUtils.getApp().createMethodBinding(EXEC_MB, parms);
    }

    public static Long getContext() {
        return new Long(new Date().getTime());
    }

    public void setGXESessionToPassivate(GXESession session) {
        FacesUtils.getSessionMap().put(GXESESSION, session);
    }

    public GXESession getGXESessionToActivate() {
        return (GXESession) FacesUtils.getSessionMap().get(GXESESSION);
    }

    public ArrayList getMessages() {
        return this.messages;
    }

    public void addMessage(String msg) {
        addMessage(null, msg);
    }

    /**
	 * Stores name of current view to allow components to find out which view
	 * their being displayed in.
	 * 
	 * @param view -
	 *            the name of the current view.
	 */
    public void setView(String view) {
        _view = view;
    }

    /**
	 * Gets name of current view. Used by components to find out which view
	 * they're being displayed in.
	 * 
	 */
    public String getView() {
        return _view;
    }

    protected Table getCurrentTable() {
        return _gosh.currentScope().getSelf();
    }

    protected class ListImpl extends GLObjectListImpl implements List {

        protected ListImpl(Table table) {
            super(table);
        }

        public Object get(int index) {
            try {
                return new MapImpl(getTable().nthRow(index));
            } catch (BoundsException e) {
                throw new FacesRuntimeException(logger, "Error retrieving row# " + index, e);
            }
        }

        public int size() {
            return getTable().rowCount();
        }

        public void clear() {
        }

        public boolean isEmpty() {
            return false;
        }

        public Object[] toArray() {
            return null;
        }

        public Object remove(int index) {
            return null;
        }

        public void add(int index, Object element) {
        }

        public int indexOf(Object o) {
            return 0;
        }

        public int lastIndexOf(Object o) {
            return 0;
        }

        public boolean add(Object o) {
            return false;
        }

        public boolean contains(Object o) {
            return false;
        }

        public boolean remove(Object o) {
            return false;
        }

        public boolean addAll(int index, Collection c) {
            return false;
        }

        public boolean addAll(Collection c) {
            return false;
        }

        public boolean containsAll(Collection c) {
            return false;
        }

        public boolean removeAll(Collection c) {
            return false;
        }

        public boolean retainAll(Collection c) {
            return false;
        }

        public List subList(int fromIndex, int toIndex) {
            return null;
        }

        public ListIterator listIterator() {
            return null;
        }

        public ListIterator listIterator(int index) {
            return null;
        }

        public Object set(int index, Object element) {
            return null;
        }

        public Object[] toArray(Object[] a) {
            return null;
        }
    }

    protected class MapImpl extends GLObjectImpl {

        Table.Row _row;

        public MapImpl(Table.Row row) {
            super(row);
            _row = row;
        }

        public Set entrySet() {
            return null;
        }

        public Object get(Object key) {
            if (key instanceof Long) {
                if (((Long) key).longValue() == 0) {
                    logger.debug("Received array reference on a map. Returning the map: " + gclKey());
                    return this;
                } else {
                    throw new FacesRuntimeException(logger, "Index " + key + " requested, but this is a single row.");
                }
            }
            if (((String) key).equals("gl")) {
                return getGl();
            }
            if (((String) key).equals(com.sun.faces.RIConstants.IMMUTABLE_MARKER)) {
                return null;
            }
            return GlSession.this.get(key, _row.asCommonTable());
        }

        public Object put(Object key, Object value) {
            Table t = null;
            try {
                String gcl = "(" + _row.asCommonTable().path() + ").(" + key + ")";
                try {
                    t = FacesUtils.gclToTable(gcl);
                } catch (FacesException e) {
                    throw new FacesRuntimeException(e.getMessage(), e);
                }
                if (value == null) {
                    if (t.rowCount() > 0) {
                        assureTxn();
                        t.extend().deleteAllRefs(false);
                    }
                } else if (!(value instanceof CommonGLBase)) {
                    String valString = value.toString();
                    Table.Row r = t.asRow();
                    String prev = r.getString(0);
                    if (prev == null || !prev.equals(valString)) {
                        assureTxn();
                        r.setString(0, valString);
                    }
                } else {
                    Table.Row newRow = ((CommonGLBase) value).getTable().asRow();
                    if (t.rowCount() != 1 || !(t.nthRow(0).equals(newRow))) {
                        assureTxn();
                        t.extend().deleteAllRefs(false);
                        t.extend().addRef(((CommonGLBase) value).getTable().asRow());
                    }
                }
                return null;
            } catch (AccessException e) {
                String msg = ExceptionLogger.getMessage(e);
                GlSession.getCurrentInstance().addMessage(_row.path(), key + ": " + msg);
                FacesUtils.getFc().renderResponse();
                logger.debug("Error caught trying to update " + key, e);
                return null;
            }
        }

        private void assureTxn() {
            if (!Txn.mgr.inProgress()) {
                try {
                    Txn.mgr.begin();
                } catch (AccessException e1) {
                    throw new FacesRuntimeException("Error begining transaction: " + e1.getMessage(), e1);
                }
            }
        }
    }

    public Object get(Object key, Table table) {
        try {
            if (table == null) {
                return null;
            }
            String gcl = (String) key;
            String path = table.path(PathStrategy.encoded);
            Table t = GoshUtils.gclToTable("(" + path + ")." + "(" + gcl + ")", getGosh());
            if (t == null) {
                return null;
            }
            if ((t.isQuery() || t.isValueType()) && t.rowCount() == 1) {
                Table.Row r = t.asRow();
                if (t.columnCount() == 1 && t.column(0).getType().isValueType()) {
                    return r.getString(0);
                }
                return new MapImpl(r);
            }
            return new ListImpl(t);
        } catch (AccessException e) {
            throw new FacesRuntimeException(logger, "Error evaluating gcl expression: " + key + ": " + e.getMessage(), e);
        }
    }

    /**
	 * @param b
	 */
    public void setTxnBlocked(boolean b) {
        _txnBlocked = b;
    }

    public boolean isTxnBlocked() {
        return _txnBlocked;
    }

    public void resume() {
        logger.debug("Resume");
    }

    /**
	 * @param string
	 */
    public void setResultMsg(String message) {
        _resultMsg = message;
    }

    public String getResultMsg() {
        return _resultMsg;
    }

    public void triggerTransition(ActionEvent e) {
        UsecaseActionListener.listener.processAction(e);
    }

    public static PrintWriter getWriter(Writer out) {
        return new PrintWriter(new GlWriter(out));
    }

    public static final String STATUS_MARKER = "-s";

    private static int MLENGTH = STATUS_MARKER.length();

    private static String NL = System.getProperty("line.separator");

    private static int NLLENGTH = NL.length();

    static class GlWriter extends Writer {

        StringBuffer currentLine = new StringBuffer();

        Writer _out;

        public GlWriter(Writer out) {
            super(out);
            _out = out;
        }

        public void flush() throws IOException {
            _out.flush();
        }

        public void close() throws IOException {
            _out.close();
        }

        public void write(String s) throws IOException {
            _out.write(s);
            buildLine(s);
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            _out.write(cbuf, off, len);
            buildLine(new String(cbuf, off, len));
        }

        private void buildLine(String s) {
            int newlinePos = s.indexOf(NL);
            if (newlinePos != -1) {
                currentLine.append(s.substring(0, newlinePos));
                writeLine();
                if (newlinePos < s.length() - NLLENGTH) {
                    currentLine.append(s.substring(newlinePos + NLLENGTH));
                }
            } else {
                currentLine.append(s);
            }
        }

        private void writeLine() {
            int marker = currentLine.indexOf(STATUS_MARKER);
            int marker1 = currentLine.indexOf(STATUS_MARKER, marker + MLENGTH);
            marker = (marker1 > -1) ? marker1 : marker;
            if (marker > -1) {
                int msgndx = currentLine.indexOf("msg");
                if (msgndx == -1 || msgndx > marker) {
                    String msg = currentLine.substring(marker + MLENGTH);
                    GlSession session = GlSession.getCurrentInstance();
                    if (session != null) {
                        session.addMessage(null, msg, Messages.INFO);
                    }
                }
            }
            currentLine.setLength(0);
        }
    }

    public static String[] getViewIds() {
        return _viewIds;
    }

    public static boolean isBootSession(GXESession curSession) {
        String kind = null;
        try {
            kind = curSession.getKind();
        } catch (AccessException e) {
            throw new FacesRuntimeException("error getting session kind: " + e.getMessage(), e);
        }
        if (kind.equals("boot")) {
            return true;
        }
        return false;
    }
}
