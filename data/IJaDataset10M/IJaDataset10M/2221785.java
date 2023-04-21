package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TUWorkerDialog<T> extends JDialog implements ListChangeListener {

    private TUWorker<T> worker;

    private JPanel panel;

    private JProgressBar progressBar;

    private JTextArea progressStatus;

    public TUWorkerDialog(Frame frame, String title) {
        super(frame);
        this.initialize(title);
    }

    @Override
    public void listChange(ListChangeEvent event) {
        if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
            if (event.getValue() instanceof ProgressMessage) {
                this.appendToProgressStatus(event.getValue().toString() + "\n");
            }
        }
    }

    public void appendToProgressStatus(String text) {
        this.progressStatus.append(text);
    }

    public T getResult() {
        try {
            return this.worker.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public TUWorker<T> getWorker() {
        return this.worker;
    }

    public void setWorker(TUWorker<T> worker) {
        this.worker = worker;
    }

    public void setSouthComponent(JComponent component) {
        this.panel.add(component, BorderLayout.SOUTH);
    }

    private void initialize(String title) {
        this.setModal(true);
        this.setTitle(title);
        this.setSize(600, 300);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        if (this.getOwner() != null) this.setLocationRelativeTo(this.getOwner());
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout(5, 5));
        this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
        this.progressBar.setIndeterminate(true);
        this.progressBar.setString("");
        this.progressStatus = new JTextArea();
        this.progressStatus.setEditable(false);
        JScrollPane scrollStatus = ComponentFactory.createJScrollPane(this.progressStatus, true);
        scrollStatus.setAutoscrolls(true);
        scrollStatus.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        this.panel.add(this.progressBar, BorderLayout.NORTH);
        this.panel.add(scrollStatus, BorderLayout.CENTER);
        this.add(this.panel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.worker.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    TUWorkerDialog.this.setCursor(null);
                    TUWorkerDialog.this.setVisible(false);
                    TUWorkerDialog.this.dispose();
                }
            });
            this.worker.execute();
        }
        super.setVisible(visible);
    }
}
