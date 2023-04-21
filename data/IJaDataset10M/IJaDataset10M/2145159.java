package org.viewaframework.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.ext.SpotLightUI;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.viewaframework.swing.pagination.PaginationEvent;
import org.viewaframework.swing.pagination.PaginationListener;
import org.viewaframework.swing.util.ResourceLocator;

/**
 *
 *	This component shows a pagination controller. It can be used to paginate any type of list, table...
 *  You can get pagination information from {@link PaginationStatus} object using getPaginationStatus(). 
 *  The following properties are available through that object: 
 *  
 *  <table>
 *  	<tr>
 *  		<td>Description</td>
 *          <td>PropertyName (getPaginationStatus().get<b>PropertyName</b>()</td>
 *          <td>Read/Write</td>
 *      </tr>
 *      <tr>
 *          <td>Offset or current page</td>
 *          <td>offset</td>
 *          <td>rw</td>
 *      </tr>
 *      <tr>
 *          <td>Maximum results per page</td>
 *          <td>maxResults</td>
 *          <td>rw</td>          
 *      </tr>
 *      <tr>
 *          <td>Total query results</td>
 *          <td>totalResults</td>
 *          <td>rw</td>          
 *      </tr>  
 *      <tr>
 *          <td>Total pagination pages</td>
 *          <td>totalPages</td>
 *          <td>r</td>          
 *      </tr>
 *  </table>
 *  <br/>
 *  
 *  You can also customize the shown records message showed at the right side of the component, applying a new
 *  formatter pattern (see {@link Formatter} patterns). That pattern will receive 2 integers as parameters. If the total number
 *  of records is equals or less than 1 then the default noRecords message will show up.<br/>
 *  
 *  You can disable the pagination panel whether clicking on the collapsible button or setting the panel disabled (setEnabled(false)),
 *  then a translucent panel will show up making impossible to click on it.
 *  
 *  The following code is an example about how to use this component.
 *
 *<pre>
    	PaginationPanel paginationPanel = new PaginationPanel();
 		paginationPanel.getPaginationStatus().setTotalResults(254);
 		paginationPanel.setEnabled(true);
 		paginationPanel.setRecordsShownPattern("Showing records %1$d to %2$d");
 		paginationPanel.addPaginationListener(new PaginationListener() {
 			public void paginationPerformed(PaginationEvent ev) {
 				System.out.println(ev.getPaginationStatus().offset);
 			}
 		});
    	 JXFrame frame = new JXFrame();    	 
    	 frame.setLayout(new BorderLayout());
    	 frame.add("South", paginationPanel);
    	 JScrollPane scroll = new JScrollPane(new JTree());
    	 frame.add("Center", scroll);
    	 frame.pack();
    	 frame.setVisible(true);
 *</pre>
 *
 *You can receive information from the pagination status adding a {@link PaginationListener} to
 *the paginationPanel.<br/>
 *
 * @author mgg
 */
public class PaginationPanel extends javax.swing.JPanel {

    /**
	 * Here you have the possible events triggered by the pagination component 
	 *
	 */
    public static enum Names {

        EXTERNAL, FIRST, LAST, NEXT, PAGE_SELECTOR_FIELD, PREVIOUS, RECORDS_SHOWN, RESULTS_PER_PAGE_PICKER
    }

    /**
	 * This internal class helps you focusing only on the page number you
	 * have to enter when clicking on the page field. 
	 *
	 */
    private class PaginationFocusAdapter extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent e) {
            JFormattedTextField field = JFormattedTextField.class.cast(e.getSource());
            field.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
            field.setText(field.getValue().toString());
        }

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            JFormattedTextField field = JFormattedTextField.class.cast(e.getSource());
            field.setBackground(UIManager.getColor("FormattedTextField.background"));
        }
    }

    /**
	 * Once you have entered the number this class formats the field (pageentered/totalnumberofpages)
	 * */
    private class PaginationNumberFormatter extends NumberFormatter {

        private static final String SEPARATOR = "/";

        private static final long serialVersionUID = -6989561208489976527L;

        @Override
        public Object stringToValue(String text) throws ParseException {
            return text == null || text.isEmpty() ? 1 : Integer.parseInt(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            value = value != null ? value : 0;
            return new StringBuilder().append(Integer.parseInt(value.toString()) <= 1 ? 1 : value.toString()).append(SEPARATOR).append(paginationStatus.getTotalPages() <= 1 ? 1 : paginationStatus.getTotalPages()).toString();
        }
    }

    /**
	 * This is the most important class. It notifies all internal components whenever
	 * a pagination data has changed.
	 * */
    private class PaginationPerformedListener implements PaginationListener {

        public void paginationPerformed(PaginationEvent ev) {
            PaginationStatus status = ev.getPaginationStatus();
            if (isEnabled()) {
                switch(ev.getTrigger()) {
                    case FIRST:
                        status.offset = 1;
                        break;
                    case PREVIOUS:
                        status.offset -= 1;
                        break;
                    case NEXT:
                        status.offset += 1;
                        break;
                    case LAST:
                        status.offset = status.getTotalPages();
                        break;
                    case RESULTS_PER_PAGE_PICKER:
                        status.maxResults = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                        break;
                    case PAGE_SELECTOR_FIELD:
                        status.offset = Integer.parseInt(jFormattedTextField1.getValue().toString());
                        jButton1.requestFocus();
                        break;
                }
                status.offset = status.offset >= status.getTotalPages() ? status.getTotalPages() : status.offset <= 1 ? 1 : status.offset;
            }
            jFormattedTextField1.setValue(status.getTotalPages() <= 1 ? 1 : status.offset);
            jLabel1.setText(status.getTotalPages() <= 1 ? getNoRecordsMessage() : new Formatter().format(getRecordsShownPattern(), status.getFirstRecordInPage() + 1, status.getLastRecordInPage()).toString());
        }
    }

    /**
	 * This objet holds all the information related to a {@link PaginationPanel} object. It 
	 * can be listened by adding {@link PropertyChangeListener} to it.
	 * 
	 * @author mgg
	 *
	 */
    public class PaginationStatus {

        int maxResults = 10;

        int offset = 1;

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        int totalPages = 0;

        int totalResults = 0;

        /**
		 * @param listener
		 */
        public void addPaginationPropertyListener(PropertyChangeListener listener) {
            this.pcs.addPropertyChangeListener(listener);
        }

        public Integer getFirstRecordInPage() {
            return offset == 1 ? 0 : maxResults * (offset - 1);
        }

        public Integer getLastRecordInPage() {
            return offset == 1 ? getFirstRecordInPage() + maxResults : offset == getTotalPages() ? getFirstRecordInPage() + (totalResults - getFirstRecordInPage()) : getFirstRecordInPage() + maxResults;
        }

        /**
		 * @return
		 */
        public int getMaxResults() {
            return maxResults;
        }

        /**
		 * @return
		 */
        public int getOffset() {
            return offset;
        }

        /**
		 * @return
		 */
        public int getTotalPages() {
            if (maxResults > 0) {
                totalPages = Double.valueOf(Math.ceil(Integer.valueOf(totalResults).doubleValue() / Integer.valueOf(maxResults).doubleValue())).intValue();
            }
            return totalPages;
        }

        /**
		 * @return
		 */
        public int getTotalResults() {
            return totalResults;
        }

        /**
		 * @param listener
		 */
        public void removePaginationPropertyListener(PropertyChangeListener listener) {
            this.pcs.removePropertyChangeListener(listener);
        }

        /**
		 * @param maxResults
		 */
        public void setMaxResults(int maxResults) {
            int oldMaxResults = this.maxResults;
            this.maxResults = maxResults;
            this.pcs.firePropertyChange("maxResults", oldMaxResults, this.maxResults);
        }

        /**
		 * @param offset
		 */
        public void setOffset(int offset) {
            int oldOffset = this.offset;
            this.offset = offset;
            this.pcs.firePropertyChange("offset", oldOffset, this.offset);
        }

        /**
		 * @param totalResults
		 */
        public void setTotalResults(int totalResults) {
            int oldTotalResults = this.totalResults;
            this.totalResults = totalResults;
            this.pcs.firePropertyChange("totalResults", oldTotalResults, this.totalResults);
        }
    }

    private class PaginationStatusPropertyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireNewPaginationEvent(new ActionEvent(jFormattedTextField1, 0, null), Names.EXTERNAL);
        }
    }

    private class PaginationTriggerListener implements ActionListener {

        private Names trigger;

        public PaginationTriggerListener(Names trigger) {
            this.trigger = trigger;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PaginationPanel.this.fireNewPaginationEvent(e, this.trigger);
        }
    }

    private static final String defaultNoRecordsMessage = "No records";

    private static final Integer[] defaultRanges = new Integer[] { 10, 20, 30, 40, 50, 75, 100 };

    private static final String defaultRecordsShownPattern = "(%1$d - %2$d)";

    private static final long serialVersionUID = 4280943992747434817L;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JFormattedTextField jFormattedTextField1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelCollapsibleInner;

    private javax.swing.JPanel jPanelCollapsibleWrapper;

    private org.jdesktop.swingx.JXCollapsiblePane jxCollapsiblePanel1;

    private JXLayer<JXCollapsiblePane> lockableLayer;

    private SpotLightUI lockableUI;

    private String noRecordsMessage = defaultNoRecordsMessage;

    private List<PaginationListener> paginationListeners = new ArrayList<PaginationListener>();

    private PaginationStatus paginationStatus;

    private Integer[] ranges = defaultRanges;

    private String recordsShownPattern = defaultRecordsShownPattern;

    /** Creates new form NewJPanel */
    public PaginationPanel() {
        this(defaultRanges);
    }

    /**
     * This constructor lets you initialize the pagination setting
     * the max records per page allowed values.
     * 
     * @param ranges
     */
    public PaginationPanel(Integer[] ranges) {
        super();
        if (ranges != null) {
            this.ranges = ranges;
        }
        initPaginationStatus();
        initComponents();
    }

    /**
	 * If you want to be posted about pagination changes: previous clicked,
	 * results per page changed...etc, you should add a {@linkplain PaginationListener}
	 * 
     * @param listener
     */
    public void addPaginationListener(PaginationListener listener) {
        this.paginationListeners.add(listener);
    }

    /**
     * @param e
     * @param first
     */
    private void fireNewPaginationEvent(ActionEvent e, Names first) {
        for (PaginationListener listener : paginationListeners) {
            PaginationEvent paginationEvent = new PaginationEvent();
            paginationEvent.setSource(e.getSource());
            paginationEvent.setPaginationPanel(this);
            paginationEvent.setTrigger(first);
            paginationEvent.setPaginationStatus(paginationStatus);
            listener.paginationPerformed(paginationEvent);
        }
    }

    /**
	 * @return
	 */
    public String getNoRecordsMessage() {
        return noRecordsMessage;
    }

    /**
     * @return
     */
    public PaginationStatus getPaginationStatus() {
        return paginationStatus;
    }

    /**
     * @return
     */
    public String getRecordsShownPattern() {
        return recordsShownPattern;
    }

    /**
	 * 
	 */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton(ResourceLocator.getImageIcon(PaginationPanel.class, "org/viewaframework/swing/master/bullet_arrow_down.png"));
        jLabel1 = new javax.swing.JLabel();
        jPanelCollapsibleInner = new javax.swing.JPanel(new BorderLayout());
        jPanelCollapsibleWrapper = new javax.swing.JPanel(new BorderLayout());
        jxCollapsiblePanel1 = new org.jdesktop.swingx.JXCollapsiblePane();
        jFormattedTextField1 = new javax.swing.JFormattedTextField(new PaginationNumberFormatter());
        setName("Form");
        jLabel1.setName(Names.RECORDS_SHOWN.name());
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(ranges));
        jComboBox1.setName(Names.RESULTS_PER_PAGE_PICKER.name());
        jComboBox1.addActionListener(new PaginationTriggerListener(Names.RESULTS_PER_PAGE_PICKER));
        jButton1.setText("");
        jButton1.setName(Names.FIRST.name());
        jButton1.setOpaque(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setIcon(ResourceLocator.getImageIcon(PaginationPanel.class, "org/viewaframework/swing/master/resultset_first.png"));
        jButton1.addActionListener(new PaginationTriggerListener(Names.FIRST));
        jButton2.setText("");
        jButton2.setName(Names.PREVIOUS.name());
        jButton2.setOpaque(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setIcon(ResourceLocator.getImageIcon(PaginationPanel.class, "org/viewaframework/swing/master/resultset_previous.png"));
        jButton2.addActionListener(new PaginationTriggerListener(Names.PREVIOUS));
        jButton3.setText("");
        jButton3.setName(Names.NEXT.name());
        jButton3.setOpaque(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setIcon(ResourceLocator.getImageIcon(PaginationPanel.class, "org/viewaframework/swing/master/resultset_next.png"));
        jButton3.addActionListener(new PaginationTriggerListener(Names.NEXT));
        jButton4.setText("");
        jButton4.setName(Names.LAST.name());
        jButton4.setOpaque(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setIcon(ResourceLocator.getImageIcon(PaginationPanel.class, "org/viewaframework/swing/master/resultset_last.png"));
        jButton4.addActionListener(new PaginationTriggerListener(Names.LAST));
        jButton5.setPreferredSize(new Dimension(200, 12));
        jButton5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jxCollapsiblePanel1.setCollapsed(!jxCollapsiblePanel1.isCollapsed());
                jButton5.getParent().requestFocus();
            }
        });
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField1.setValue(1);
        jFormattedTextField1.setName(Names.PAGE_SELECTOR_FIELD.name());
        jFormattedTextField1.addActionListener(new PaginationTriggerListener(Names.PAGE_SELECTOR_FIELD));
        jFormattedTextField1.setFocusLostBehavior(JFormattedTextField.REVERT);
        jFormattedTextField1.addFocusListener(new PaginationFocusAdapter());
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanelCollapsibleInner);
        jPanelCollapsibleInner.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        lockableLayer = new JXLayer<JXCollapsiblePane>(jxCollapsiblePanel1);
        lockableUI = new SpotLightUI();
        lockableUI.setOverlayColor(new Color(1.0f, 1.0f, 1.0f, 0.65f));
        lockableLayer.setUI(lockableUI);
        lockableUI.setShadowEnabled(true);
        jxCollapsiblePanel1.add(jPanelCollapsibleInner);
        jPanelCollapsibleWrapper.add("North", jButton5);
        jPanelCollapsibleWrapper.add("South", lockableLayer);
        this.add("Center", jPanelCollapsibleWrapper);
    }

    /**
	 * 
	 */
    private void initPaginationStatus() {
        this.paginationStatus = new PaginationStatus();
        this.paginationStatus.setMaxResults(this.ranges[0]);
        this.paginationStatus.addPaginationPropertyListener(new PaginationStatusPropertyListener());
        this.addPaginationListener(new PaginationPerformedListener());
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
	 * @param listener
	 */
    public void removePaginationListener(PaginationListener listener) {
        this.paginationListeners.remove(listener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.lockableUI.setShadowEnabled(!enabled);
    }

    /**
	 * @param noRecordsMessage
	 */
    public void setNoRecordsMessage(String noRecordsMessage) {
        this.noRecordsMessage = noRecordsMessage;
    }

    /**
	 * @param status
	 */
    public void setPaginationStatus(PaginationStatus status) {
        this.paginationStatus = status;
    }

    /**
	 * @param recordsShownPattern
	 */
    public void setRecordsShownPattern(String recordsShownPattern) {
        this.recordsShownPattern = recordsShownPattern;
        this.fireNewPaginationEvent(new ActionEvent(jLabel1, 0, null), Names.EXTERNAL);
    }
}
