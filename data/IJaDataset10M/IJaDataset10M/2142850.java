package druid.util.jdbc.dataeditor.record;

import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.flextable.FlexTableColumn;
import org.dlib.gui.treeview.TreeView;
import org.dlib.gui.treeview.TreeViewNode;
import org.dlib.gui.treeview.TreeViewSelEvent;
import org.dlib.gui.treeview.TreeViewSelListener;
import ddf.type.SqlType;
import druid.core.DataModel;
import druid.util.gui.ImageFactory;
import druid.util.gui.renderers.SimpleTreeViewRenderer;
import druid.util.jdbc.ResultSetEditor;

public class RecordView extends JPanel implements TreeViewSelListener {

    private TreeView treeView = new TreeView();

    private DataModel dataModel;

    private TreeViewNode currNode;

    public RecordView() {
        FlexLayout flexL = new FlexLayout(1, 1);
        flexL.setColProp(0, FlexLayout.EXPAND);
        flexL.setRowProp(0, FlexLayout.EXPAND);
        setLayout(flexL);
        treeView.addSelectionListener(this);
        treeView.setCellRenderer(new SimpleTreeViewRenderer(ImageFactory.FIELD));
        treeView.setRootNode(new TreeViewNode());
        add("0,0,x,x", treeView);
    }

    public void setRootNode(ResultSetEditor rse) {
        currNode = null;
        TreeViewNode rootNode = new TreeViewNode();
        for (int i = 0; i < rse.getColumnCount(); i++) {
            FlexTableColumn ftc = rse.getColumnAt(i);
            SqlType type = rse.getSqlType(i);
            TreeViewNode node = new TreeViewNode();
            node.setText((String) ftc.getHeaderValue());
            node.setToolTipText("Type is " + type.sName);
            rootNode.addChild(node);
        }
        treeView.setRootNode(rootNode);
    }

    public void setDataModel(DataModel dm) {
        dataModel = dm;
    }

    public void nodeSelected(TreeViewSelEvent e) {
        dataModel.saveDataToNode(currNode);
        currNode = e.getSelectedNode();
        dataModel.setCurrentNode(currNode);
    }
}
