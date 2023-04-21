package nickyb.sqleonardo.environment.mdi;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.content.UpdateModel;
import nickyb.sqleonardo.environment.ctrl.define.TableMetaData;
import nickyb.sqleonardo.environment.io.FileStreamSQL;
import nickyb.sqleonardo.environment.io.FileStreamXLQ;
import nickyb.sqleonardo.querybuilder.DiagramLayout;
import nickyb.sqleonardo.querybuilder.QueryActions;
import nickyb.sqleonardo.querybuilder.QueryBuilder;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class ClientQueryBuilder extends MDIClient {

    public static final String DEFAULT_TITLE = "QUERY";

    public static int counter = 0;

    private QueryBuilder builder;

    private JMenuItem[] m_actions;

    private Toolbar toolbar;

    private String keycah = null;

    private String filename = null;

    public ClientQueryBuilder(String keycah) {
        super(DEFAULT_TITLE);
        setMaximizable(false);
        setResizable(false);
        setComponentCenter(builder = new QueryBuilder());
        this.keycah = keycah;
        createToolbar();
        initMenuActions();
        if (keycah != null) builder.setConnection(ConnectionAssistant.getHandler(keycah).get());
        setFileName(null);
        addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameActivated(InternalFrameEvent ife) {
                ClientQueryBuilder.this.setQueryParameters();
            }
        });
    }

    private void createToolbar() {
        JButton btn = new JButton(builder.getActionMap().get(QueryActions.DIAGRAM_SAVE_AS_IMAGE));
        btn.setIcon(Application.resources.getIcon(Application.ICON_DIAGRAM_SAVE));
        btn.setToolTipText("save as image");
        btn.setText(null);
        toolbar = new Toolbar(Toolbar.HORIZONTAL);
        toolbar.add(new ActionLaunch());
        toolbar.add(new ActionSave());
        toolbar.add(btn);
        setComponentEast(toolbar);
    }

    private void initMenuActions() {
        JCheckBoxMenuItem cbxm = new JCheckBoxMenuItem(builder.getActionMap().get(QueryActions.FIELDS_DRAGGABLE));
        cbxm.setSelected(builder.isDragAndDropEnabled());
        m_actions = new JMenuItem[] { cbxm, null, MDIMenubar.createItem(builder.getActionMap().get(QueryActions.ENTITIES_ARRANGE)), MDIMenubar.createItem(builder.getActionMap().get(QueryActions.ENTITIES_PACK)), MDIMenubar.createItem(builder.getActionMap().get(QueryActions.ENTITIES_REMOVE)), null, MDIMenubar.createItem(builder.getActionMap().get(QueryActions.COPY_SYNTAX)) };
    }

    public JMenuItem[] getMenuActions() {
        return m_actions;
    }

    public Toolbar getSubToolbar() {
        return toolbar;
    }

    protected void setPreferences() {
    }

    public final void setFileName(String filename) {
        this.filename = filename;
        String filename2 = filename == null ? ("<untitled" + (++counter) + ">") : filename;
        super.setTitle(this.getID() + " - " + DEFAULT_TITLE + " : " + filename2 + " : " + keycah);
    }

    public final void setDiagramLayout(DiagramLayout layout) {
        builder.setDiagramLayout(layout);
    }

    private void setQueryParameters() {
        if (ConnectionAssistant.hasHandler(keycah)) {
            QueryBuilder.identifierQuoteString = ConnectionAssistant.getHandler(keycah).getObject("$identifierQuoteString").toString();
            QueryBuilder.maxColumnNameLength = ((Integer) ConnectionAssistant.getHandler(keycah).getObject("$maxColumnNameLength")).intValue();
        }
    }

    private class ActionLaunch extends MDIActions.AbstractBase {

        private ActionLaunch() {
            super(I18n.getString("application.launchQuery", "launch query"));
            setIcon(Application.ICON_QUERY_LAUNCH);
            setTooltip(I18n.getString("application.launchQuery", "launch query"));
        }

        public void actionPerformed(ActionEvent ae) {
            if (ClientQueryBuilder.this.builder.getConnection() == null) {
                Application.alert(Application.PROGRAM, "no connection!");
                return;
            }
            int pos = ClientQueryBuilder.this.getTitle().indexOf(":");
            String subtitle = ClientQueryBuilder.this.getTitle().substring(pos);
            UpdateModel um = null;
            QueryModel qm = null;
            try {
                qm = (QueryModel) ClientQueryBuilder.this.builder.getQueryModel().clone();
            } catch (CloneNotSupportedException cnse) {
                qm = ClientQueryBuilder.this.builder.getQueryModel();
            }
            if (qm.getQueryExpression().getQuerySpecification().getFromClause().length == 1 && qm.getQueryExpression().getQuerySpecification().getFromClause()[0] instanceof QueryTokens.Table) {
                QueryTokens.Table qtoken = (QueryTokens.Table) qm.getQueryExpression().getQuerySpecification().getFromClause()[0];
                TableMetaData tmd = new TableMetaData(ClientQueryBuilder.this.keycah, qtoken.getSchema(), qtoken.getName());
                um = tmd.createUpdateModel();
                um.getTable().setAlias(qtoken.getAlias());
                QueryTokens._Expression[] e = qm.getQueryExpression().getQuerySpecification().getSelectList();
                for (int i = 0; i < um.getRowIdentifierCount(); i++) {
                    QueryTokens.Column cpk = um.getRowIdentifier(i);
                    for (int j = 0; j < e.length; j++) {
                        if (e[j] instanceof QueryTokens.Column) {
                            QueryTokens.Column c = (QueryTokens.Column) e[j];
                            if (cpk.getName().equalsIgnoreCase(c.getName())) {
                                cpk.setAlias(c.getAlias());
                            }
                        }
                    }
                }
            }
            ClientContent client = new ClientContent(ClientQueryBuilder.this.keycah, qm, um);
            client.setTitle("PREVIEW : " + subtitle);
            Application.window.add(client);
        }
    }

    public class ActionSave extends MDIActions.AbstractBase {

        private ActionSave() {
            setText(I18n.getString("application.saveQuery", "save query"));
            setIcon(Application.ICON_SAVE);
            setTooltip(I18n.getString("application.saveQuery", "save query"));
        }

        private void saveAs() {
            Object[] ret = DialogQuery.showSave(ClientQueryBuilder.this.builder.getDiagramLayout());
            if (ret[0] != null) ClientQueryBuilder.this.setFileName(ret[0].toString());
        }

        private void replace() {
            try {
                QueryModel qm = ClientQueryBuilder.this.builder.getQueryModel();
                String fn = ClientQueryBuilder.this.filename;
                if (fn.endsWith(".sql")) {
                    FileStreamSQL.write(fn, qm);
                } else {
                    if (!fn.endsWith(".xlq")) fn += ".xlq";
                    FileStreamXLQ.write(fn, ClientQueryBuilder.this.builder.getDiagramLayout());
                }
                ClientQueryBuilder.this.setFileName(fn);
            } catch (Exception e) {
                Application.println(e, true);
                e.printStackTrace();
            }
        }

        public void actionPerformed(ActionEvent ae) {
            if (ClientQueryBuilder.this.filename == null) {
                saveAs();
            } else {
                String message = I18n.getFormattedString("application.message.replaceFile", "{0}\nreplace existing file?", new Object[] { "" + ClientQueryBuilder.this.filename });
                int ret = JOptionPane.showConfirmDialog(Application.window, message, "query.save", JOptionPane.YES_NO_CANCEL_OPTION);
                if (ret == JOptionPane.YES_OPTION) replace(); else if (ret == JOptionPane.NO_OPTION) saveAs();
            }
        }
    }
}
