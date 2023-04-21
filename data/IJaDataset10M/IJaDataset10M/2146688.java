package cn.ekuma.data.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.data.dao.bean.I_ViewBean;
import cn.ekuma.data.ui.swing.dnd.BeanListTransferable;
import cn.ekuma.data.ui.swing.dnd.BeanTransferable;

public abstract class JDragAbleChildDAOEditorPanel<K> extends JChildDAOEditorPanel<K> implements DragGestureListener, DragSourceListener {

    public JDragAbleChildDAOEditorPanel(I_DataLogic dlSales, AbstractDTOTableModel<K> tableModel, AbstractDAOJEditor editor) {
        super(dlSales, tableModel, editor);
        dropPanel = new JPanel();
        add(dropPanel, BorderLayout.SOUTH);
        dropPanel.setLayout(new BoxLayout(dropPanel, BoxLayout.X_AXIS));
        dropPanel.setPreferredSize(new Dimension(150, 80));
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(jXTable1, DnDConstants.ACTION_COPY_OR_MOVE, this);
        addDropCell(dropPanel);
    }

    public abstract void addDropCell(JPanel dropPanel);

    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        int[] selectedRows = jXTable1.getSelectedRows();
        List<K> rows = new ArrayList();
        for (int i = 0; i < selectedRows.length; i++) {
            rows.add((K) tableModel.getObj(jXTable1.convertRowIndexToModel(selectedRows[i])));
        }
        Transferable transferable = null;
        if (rows.size() == 1) {
            transferable = new BeanTransferable((I_ViewBean) rows.get(0));
        } else transferable = new BeanListTransferable((List<I_ViewBean>) rows);
        dge.startDrag(null, transferable, this);
    }

    JPanel dropPanel;
}
