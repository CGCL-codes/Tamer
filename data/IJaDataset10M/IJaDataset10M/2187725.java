package org.netbeans.cubeon.gcode.query.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.cubeon.common.ui.TextValueCompleter;
import org.netbeans.cubeon.gcode.query.AbstractGCodeQuery;
import org.netbeans.cubeon.gcode.query.GCodeFilterQuery;
import org.netbeans.cubeon.gcode.query.GCodeQuerySupport;
import org.netbeans.cubeon.tasks.spi.query.TaskQuery;
import org.netbeans.cubeon.tasks.spi.query.TaskQuerySupportProvider;
import org.openide.util.NbBundle;

/**
 *
 * @author Anuradha
 */
public class GCodeFilterQueryEditor extends javax.swing.JPanel implements TaskQuerySupportProvider.ConfigurationHandler {

    private static final long serialVersionUID = -4455036599583142301L;

    private final GCodeQuerySupport querySupport;

    private GCodeFilterQuery query;

    private List<String> queryOptions = Arrays.asList("summary:", "description:", "comment:", "status:", "reporter:", "owner:", "cc:", "commentby:", "label:", "has:", "opened-before", "modified-before", "closed-before", "opened-after", "modified-after", "closed-after", "is:");

    /** Creates new form GcodeFilterQueryEditor */
    public GCodeFilterQueryEditor(final GCodeQuerySupport querySupport) {
        initComponents();
        this.querySupport = querySupport;
        TextValueCompleter.CallBackFilter callBackFilter = new TextValueCompleter.DefultCallBackFilter() {

            @Override
            public Collection<String> getFilterdCollection(String prifix, Collection<String> completions) {
                List<String> items = new ArrayList<String>();
                if (prifix.contains("label:")) {
                    List<String> labels = querySupport.getTaskRepository().getRepositoryAttributes().getLabels();
                    for (String label : labels) {
                        items.add("label:" + label);
                    }
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("status:")) {
                    List<String> statuses = querySupport.getTaskRepository().getRepositoryAttributes().getStatuses();
                    for (String status : statuses) {
                        items.add("status:" + status);
                    }
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("is:")) {
                    items.add("is:open");
                    items.add("is:starred");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("has:")) {
                    items.addAll(Arrays.asList("has:summary", "has:description", "has:comment", "has:status", "has:reporter", "has:owner", "has:cc", "has:commentby", "has:label", "has:attachment"));
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("opened-before")) {
                    items.add("opened-before:YYYY/MM/DD");
                    items.add("opened-before:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("opened-after")) {
                    items.add("opened-after:YYYY/MM/DD");
                    items.add("opened-after:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("closed-before")) {
                    items.add("closed-before:YYYY/MM/DD");
                    items.add("closed-before:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("closed-after")) {
                    items.add("closed-after:YYYY/MM/DD");
                    items.add("closed-after:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("modified-before")) {
                    items.add("modified-before:YYYY/MM/DD");
                    items.add("modified-before:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                if (prifix.contains("modified-after")) {
                    items.add("modified-after:YYYY/MM/DD");
                    items.add("modified-after:today-N");
                    return super.getFilterdCollection(prifix, items);
                }
                return super.getFilterdCollection(prifix, completions);
            }

            @Override
            public boolean needSeparators(String compelted) {
                return !queryOptions.contains(compelted);
            }
        };
        new TextValueCompleter(queryOptions, txtQuery, " ", callBackFilter);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblName1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lblHint = new javax.swing.JLabel();
        txtQuery = new javax.swing.JTextField();
        lblMaxResultCount = new javax.swing.JLabel();
        txtMaxResultCount = new javax.swing.JTextField();
        lblName.setLabelFor(txtName);
        lblName.setText(NbBundle.getMessage(GCodeFilterQueryEditor.class, "GCodeFilterQueryEditor.lblName.text"));
        txtName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        lblName1.setLabelFor(txtName);
        lblName1.setText(NbBundle.getMessage(GCodeFilterQueryEditor.class, "GCodeFilterQueryEditor.lblName1.text"));
        lblHint.setText(NbBundle.getMessage(GCodeFilterQueryEditor.class, "GCodeFilterQueryEditor.lblHint.text"));
        jScrollPane2.setViewportView(lblHint);
        lblMaxResultCount.setText(org.openide.util.NbBundle.getMessage(GCodeFilterQueryEditor.class, "GCodeFilterQueryEditor.lblMaxResultCount.text"));
        txtMaxResultCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxResultCount.setText("0");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE).add(lblName).add(txtName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE).add(lblName1).add(txtQuery, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE).add(layout.createSequentialGroup().add(lblMaxResultCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 401, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtMaxResultCount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(lblName).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lblName1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtQuery, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lblMaxResultCount).add(txtMaxResultCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
    }

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JLabel lblHint;

    private javax.swing.JLabel lblMaxResultCount;

    private javax.swing.JLabel lblName;

    private javax.swing.JLabel lblName1;

    private javax.swing.JTextField txtMaxResultCount;

    private javax.swing.JTextField txtName;

    private javax.swing.JTextField txtQuery;

    public void setQuery(GCodeFilterQuery query) {
        this.query = query;
        txtQuery.setText(query.getQuery());
        txtName.setText(query.getName());
        txtMaxResultCount.setText("" + query.getMaxResults());
    }

    public TaskQuery getTaskQuery() {
        if (query == null) {
            query = (GCodeFilterQuery) querySupport.createTaskQuery(AbstractGCodeQuery.Type.FILTER);
        }
        query.setQuery((txtQuery.getText()));
        query.setName(txtName.getText());
        try {
            query.setMaxResults(Integer.parseInt(txtMaxResultCount.getText().trim()));
        } catch (NumberFormatException numberFormatException) {
            query.setMaxResults(GCodeFilterQuery.MAX_RESULTS);
        }
        return query;
    }

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public boolean isValidConfiguration() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }
}
