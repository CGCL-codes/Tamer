package com.explosion.datastream.exql.gui.dbbrowser.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import junit.framework.TestCase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.datastream.TestConstants;
import com.explosion.datastream.exql.EnvironmentHelper;
import com.explosion.datastream.gui.dbbrowser.tree.ExqlTree;
import com.explosion.datastream.gui.dbbrowser.tree.ExqlTreeNode;
import com.explosion.datastream.gui.dbbrowser.tree.TreeCellRendererFactory;
import com.explosion.expfmodules.rdbmsconn.dbom.DBEntity;
import com.explosion.expfmodules.rdbmsconn.dbom.DBEntityColumn;

/**
 * @author Stephen Cowx
 * Created: 20-Sep-2005 23:39:47 
 */
public class ExqlTreeTest extends TestCase {

    private static Logger log = LogManager.getLogger(ExqlTreeTest.class);

    public void testExqlTree() throws Exception {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane();
        final ExqlTree tree = getTree(scrollPane);
        scrollPane.getViewport().add(tree);
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                log.debug("mousePressed()");
            }
        };
        tree.addMouseListener(ml);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
        Thread.sleep(TestConstants.VISUALTEST_DELAY);
    }

    private ExqlTree getTree(Component parent) throws Exception {
        TreeCellRendererFactory renderer = new TreeCellRendererFactory(parent);
        ExqlTreeNode root = new ExqlTreeNode("TEST");
        DBEntity tableEntity = EnvironmentHelper.getTableDBEntity();
        ExqlTreeNode table = new ExqlTreeNode(tableEntity);
        List list = tableEntity.getColumns();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            DBEntityColumn element = (DBEntityColumn) iter.next();
            ExqlTreeNode col = new ExqlTreeNode(element);
            table.add(col);
        }
        root.add(table);
        ExqlTree tree = new ExqlTree(root, null);
        tree.setBorder(null);
        tree.setCellRenderer(renderer);
        return tree;
    }
}
