package com.novasurv.turtle.frontend.swing.comments;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.novasurv.turtle.backend.project.CommentItem;
import com.novasurv.turtle.frontend.swing.data.SwingDataManager;
import com.novasurv.turtle.frontend.swing.nav.AddCommentActionListener;
import com.novasurv.turtle.frontend.swing.reports.TextDetailedReport;

/** @author Jason Dobies */
public class CommentsPanel extends JPanel {

    private JFrame owner;

    private SwingDataManager dataManager;

    private CommentsTable commentsTable;

    private JTextArea fullDisplayArea;

    public CommentsPanel(JFrame owner, SwingDataManager dataManager) {
        super(new BorderLayout());
        this.owner = owner;
        this.dataManager = dataManager;
        commentsTable = new CommentsTable(dataManager);
        commentsTable.getSelectionModel().addListSelectionListener(new UpdateFullDisplayListener());
        JScrollPane commentsTableScroller = new JScrollPane(commentsTable);
        JButton addButton = new JButton("Add New Comment");
        addButton.addActionListener(new AddCommentActionListener(owner, dataManager));
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(addButton);
        JPanel topHalf = new JPanel(new BorderLayout());
        topHalf.add(commentsTableScroller, BorderLayout.CENTER);
        topHalf.add(buttonsPanel, BorderLayout.NORTH);
        fullDisplayArea = new JTextArea();
        fullDisplayArea.setEditable(false);
        JScrollPane editorScroller = new JScrollPane(fullDisplayArea);
        JPanel bottomHalf = new JPanel(new BorderLayout());
        bottomHalf.add(editorScroller, BorderLayout.CENTER);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topHalf, bottomHalf);
        splitPane.setDividerLocation(.9);
        this.setLayout(new BorderLayout());
        this.add(splitPane, BorderLayout.CENTER);
    }

    private class UpdateFullDisplayListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                List<CommentItem> comments = dataManager.getActiveProject().getGrade().getCommentItems();
                int selectedRow = commentsTable.getSelectedRow();
                if (selectedRow > -1 && selectedRow < comments.size()) {
                    CommentItem item = comments.get(selectedRow);
                    TextDetailedReport report = new TextDetailedReport();
                    String commentText = report.writeComment(item).toString();
                    fullDisplayArea.setText(commentText);
                } else {
                    fullDisplayArea.setText(null);
                }
            }
        }
    }
}
