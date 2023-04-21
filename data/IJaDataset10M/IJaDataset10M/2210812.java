package ca.sqlpower.wabit.swingui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import ca.sqlpower.sql.Olap4jDataSource;
import ca.sqlpower.wabit.rs.olap.OlapQuery;
import ca.sqlpower.wabit.swingui.WabitSwingSession;
import ca.sqlpower.wabit.swingui.WabitSwingSessionImpl;

/**
 * A Swing Action for creating a new OLAP Query in a workspace.
 */
public class NewOLAPQueryAction extends AbstractAction {

    private static final Icon NEW_OLAP_QUERY_ICON = new ImageIcon(WabitSwingSessionImpl.class.getClassLoader().getResource("icons/query-olap-16.png"));

    private WabitSwingSession session;

    private Olap4jDataSource ds;

    private String newQueryName;

    public NewOLAPQueryAction(WabitSwingSession session) {
        super("New OLAP Query", NEW_OLAP_QUERY_ICON);
        this.session = session;
        this.ds = null;
        this.newQueryName = "New OLAP query";
    }

    public NewOLAPQueryAction(WabitSwingSession session, Olap4jDataSource ds) {
        super("New OLAP Query on '" + ds.getName() + "'", NEW_OLAP_QUERY_ICON);
        this.session = session;
        this.ds = ds;
        this.newQueryName = "New " + ds.getName() + " query";
    }

    public void actionPerformed(ActionEvent e) {
        OlapQuery newQuery = new OlapQuery(session.getContext());
        newQuery.setOlapDataSource(ds);
        newQuery.setName(newQueryName);
        session.getWorkspace().addOlapQuery(newQuery);
        JTree tree = session.getTree();
        int queryIndex = tree.getModel().getIndexOfChild(session.getWorkspace(), newQuery);
        tree.setSelectionRow(queryIndex + 1);
    }
}
