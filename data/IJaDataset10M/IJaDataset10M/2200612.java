package net.confex.db.tree;

import java.sql.Connection;
import net.confex.action.RunBrowserAction;
import net.confex.db.DbUtils;
import net.confex.db.JdbcConnection;
import net.confex.db.directedit.HtpSqlPropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.html.IHtmlPart;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IRunBrowser;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;

public class HtmlSqlTableTreeObject extends TreeNode implements IRunBrowser, IHtmlPart {

    private String sql = "";

    public String getAboutString() {
        return Translator.getString("ABOUT_HTPSQLTREEOBJECT");
    }

    /**
	 * ��������� ������ �� ��������� ��� ������
	 */
    public String getDefaultImage() {
        return "eview16\\table.png";
    }

    public HtmlSqlTableTreeObject(ConfigTree configTree, IStateObserver stateObserver) {
        super(configTree, stateObserver);
    }

    /**
	 * C������ ��������� ���� �� ������ !!! ������ ���� ������������� � ��������
	 * �������
	 * 
	 * @return ITreeNode
	 */
    public ITreeNode createNewITreeNode() {
        return new HtmlSqlTableTreeObject(getConfigTree(), null);
    }

    /**
	 * ������������� ��� �������� �������� ������ ��� � ���������
	 * 
	 * @param prototype
	 */
    public void setPropertyLike(ITreeNode prototype) {
        super.setPropertyLike(prototype);
        if (!(prototype instanceof HtmlSqlTableTreeObject)) {
            System.err.println(Translator.getString("MSG_PROP_NOT_INSTANCEOF") + " HtmlSqlTableTreeObject");
            return;
        }
        setSql(((HtmlSqlTableTreeObject) prototype).getSql());
    }

    public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
        super.loadAttribFromXml_0_0_1(node, parent);
        Node attr = node.getAttributes().getNamedItem("sql");
        if (attr == null) {
            System.err.println(Translator.getString("MSG_CANT_READ_SQL_FOR") + this.toString());
        } else {
            String s_sql = attr.getNodeValue();
            this.sql = Utils.fromHtmlSpecialEntities(s_sql);
        }
    }

    protected String getPropertiesXml(boolean read_src_text) {
        String str_xml = super.getPropertiesXml(read_src_text);
        if (sql != null && !sql.equals("")) {
            str_xml += "<sql>\n" + Utils.toHtmlSpecialEntities(sql) + "\n";
            str_xml += "</sql>\n";
        }
        return str_xml;
    }

    protected void parsePropertyXml(Node property, boolean new_node) {
        super.parsePropertyXml(property, new_node);
        if (property.getNodeName().equals("sql")) {
            String text = "";
            Node nd = property.getFirstChild();
            if (nd != null) text = nd.getNodeValue();
            this.setSql(Utils.fromHtmlSpecialEntities(text.trim()));
        }
    }

    public WebBrowserView runBrowser(IWorkbenchPage page) {
        ITreeNode p = getParent();
        JdbcConnection connection = DbUtils.searchJdbcConnectionUp(this);
        WebBrowserView browser = null;
        try {
            String html_str = Translator.getString("MSG_EMPTY_HTML");
            Connection conn = connection.getConnection();
            if (conn == null) html_str = Translator.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION"); else html_str = DbUtils.retSelectAsHtmlTable(connection.getConnection(), TreeUtils.doAllSubstitutions(this, sql));
            String id = String.valueOf(getStringKey().hashCode());
            String title = getName();
            if (title == null || title.trim().equals("")) {
                title = this.getClassName().toString();
            }
            browser = (WebBrowserView) page.showView(WebBrowserView.VIEW_ID, id, IWorkbenchPage.VIEW_ACTIVATE);
            if (browser != null) {
                browser.setTitlePartName(title);
                String s = "<html><body>" + html_str + "</body></html>";
                browser.setPage(s);
                browser.refresh();
            }
        } catch (PartInitException e1) {
            System.err.println("-->" + e1.getMessage());
        }
        return browser;
    }

    public void run(IViewPart view) {
        RunBrowserAction runBrowserAction = new RunBrowserAction(view, this);
        runBrowserAction.run();
    }

    protected String getHtml() {
        String html_str = null;
        ITreeNode p = getParent();
        boolean isOk = false;
        JdbcConnection connection = null;
        while (p != null) {
            if (p instanceof JdbcConnectionNode) {
                isOk = ((JdbcConnectionNode) p).createConnection();
                connection = ((JdbcConnectionNode) p).getConnection();
                break;
            }
            p = p.getParent();
        }
        if (!isOk || connection == null || connection.isClosed()) {
            System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
            html_str = "<p><font color=#FF0000>" + Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC") + "</font></p>";
            return html_str;
        }
        Connection conn = connection.getConnection();
        if (conn == null) html_str = "<p><font color=#FF0000>" + Translator.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION") + "</font></p>"; else {
            html_str = DbUtils.retSelectAsHtmlTable(connection.getConnection(), TreeUtils.doAllSubstitutions(this, sql));
        }
        return html_str;
    }

    /**
	 * ������������ ������� ������� �� ���� �������� ����� HtmlTextNode
	 * 
	 * @return
	 */
    public String getFullHtmltext() {
        if (isNotRunInBatch()) return "";
        String ret_str = getHtml();
        return TreeUtils.doAllSubstitutions(this, ret_str);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    protected IPropertyDialog newPropertyDialog(Shell shell) {
        return new HtpSqlPropertyDialog(shell);
    }
}
