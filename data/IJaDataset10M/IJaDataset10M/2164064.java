package com.visitrend.ndvis.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import com.visitrend.ndvis.event.BaseChangedEvent;
import com.visitrend.ndvis.event.DataInfoChangedEvent;
import com.visitrend.ndvis.event.DataInfoListener;
import com.visitrend.ndvis.event.ImagePanelAddedEvent;
import com.visitrend.ndvis.event.NDVisChangedEvent;
import com.visitrend.ndvis.event.NDVisListener;
import com.visitrend.ndvis.event.ParameterNameChangedEvent;
import com.visitrend.ndvis.event.ParametersChangedEvent;
import com.visitrend.ndvis.event.ParametersListener;
import com.visitrend.ndvis.model.DataInfoINF;

/**
 * <p>
 * "Parameters" (not this class) are columns in a database that this app is
 * working with, and are used to create the axis of the images produced by this
 * app. More specifically, they are used to determine the location of each pixel
 * in an image, which is associated with one row in the database or one
 * datapoint of a set. See javadoc in
 * {@link com.visitrend.ndvis.model.DataInfo com.visitrend.ndvis.model.DataInfo}
 * for more info.
 * </p>
 * <p>
 * This is a GUI that allows the user to change the order of parameters and what
 * parameters are no what axis. It holds state on parameters like their current
 * order and how many are assigned to each axis. Anything changing the order of
 * parameters or how many are on each axis should do it through here (so that
 * other relevant classes can update themselves as necessary). This can also
 * display the values for parameters (currently in a one row
 * com.visitrend.ndvis.table, though you can implement this however you want).
 * For instance, this works with
 * {@link com.visitrend.ndvis.gui.ParametersController com.visitrend.ndvis.gui.ParametersController}
 * to show the parameter values associated with each pixel of an image as the
 * user mouses over them.
 * </p>
 * <p>
 * The parameters order and values are displayed in a JTable. Reading the
 * parameters left to right in the com.visitrend.ndvis.table, the 0th through
 * the numParametersOnX are the parameters on the X axis (and the rest are on
 * Y). Its important to note that in Java's TableModel, the values and indexes
 * into its data do not change order even though the gui JTable may change the
 * order of the columns by TableColumnModel's move(int oldIndex, int newIndex)
 * method or the user dragging columns about. The methods in this class account
 * for that.
 * </p>
 * <p>
 * <h3>Parameter Order:</h3>
 * The order of parameters in here is internal to the JTable, and returned from
 * the local {@link #getParameterOrder()} methods (there's 2). Parameter order
 * implicitly refers to what parameters are on the x axis versus the y axis.
 * This order is passed around as a short[]. The value at each index of the
 * parameter order short[] is an index into
 * {@link com.visitrend.ndvis.model.DataInfo#parameterNames} and
 * {@link com.visitrend.ndvis.model.DataInfo#bases}. To determine what
 * parameters are on the X axis, one reads through the parameter order short[]
 * from index 0 to {@link #numParametersOnX}. For each index into parameter
 * order short[], get the value, and use that as an index into
 * {@link com.visitrend.ndvis.model.DataInfo#parameterNames} or
 * {@link com.visitrend.ndvis.model.DataInfo#bases}, and those are the
 * parameters on the X axis. For example, if {@link #numParametersOnX} is 2, and
 * the parameter order short[] were:<br>
 * {0,3, 1, 2}, and {@link com.visitrend.ndvis.model.DataInfo#parameterNames}
 * was<br>
 * {a, b, c, d}, then the parameters on the X axis would be: {a, d}. The default
 * parameter order is simply the order that parameters and bases are in when fed
 * into the constructor for DataInfo. Thus, a default order short[] is simply
 * like this: {0, 1, 2, ...} for however many parameters there are. This is
 * simply used to associate the original order of
 * {@link com.visitrend.ndvis.model.DataInfo#parameterNames} and
 * {@link com.visitrend.ndvis.model.DataInfo#bases} as the default order, which
 * is used as a "common point" that any parameter order can map back to for many
 * calculations.
 * </p>
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 */
public class Parameters extends JPanel implements NDVisListener, DataInfoListener, TableColumnModelListener, ActionListener {

    EventListenerList listeners;

    /**
	 * DataInfo is simply used in here to construct the parameter
	 * com.visitrend.ndvis.table in the beginning. No values are retrieved or
	 * set on it. It should probably stay this way to ensure only certain things
	 * are actually messing with that.
	 */
    private DataInfoINF dataInfo;

    private JTable paramTable;

    /**
	 * A combobox for selecting the number of parameters on X
	 */
    private JComboBox xpar;

    private JPanel xparPanel;

    private short[] order, defaultParameterOrder;

    /**
	 * The number of parameters on the X and Y axis. This should typically be a
	 * function of the current parameter order and the bases of the parameters.
	 */
    private int numParametersOnX, numParametersOnY;

    /**
	 * The parameters will be initialized in the default order as in DataInfo.
	 * Many other calculations are then based on this order, but use the table
	 * as reference. This should never lead to any problems as any changes to
	 * DataInfo shoudl be managed in here.
	 * 
	 * @param dataInfo
	 */
    public Parameters(DataInfoINF dataInfo) {
        super();
        setLayout(new BorderLayout());
        listeners = new EventListenerList();
        setDataInfo(dataInfo);
    }

    public void addParametersListener(ParametersListener listener) {
        listeners.add(ParametersListener.class, listener);
    }

    public void removeParametersListener(ParametersListener listener) {
        listeners.remove(ParametersListener.class, listener);
    }

    /**
	 * 
	 * @param answerHolder
	 *            a short[] to be filled with the values of the parameters
	 *            displayed in the JTable, in the default order of the
	 *            parameters
	 * @return the short[] answerHolder parameter filled with the current values
	 *         for parameters held in here
	 */
    public short[] getParameterValuesInDefaultOrder(short[] answerHolder) {
        TableModel model = paramTable.getModel();
        for (int k = 0; k < answerHolder.length; k++) {
            answerHolder[k] = Short.parseShort("" + model.getValueAt(0, k));
        }
        return answerHolder;
    }

    /**
	 * IMPORTANT: the short[] vals parameter should be in the default parameter
	 * order. This method should usually only be called by MouseEvents and
	 * UnitTests. Parameter values are set by mouse events as the user moves the
	 * mouse over the dimensional stacking image. It is assumed that the "vals"
	 * arg is in the default parameter order (i.e. the order of DataInfo's
	 * "parameterNames" and "bases" variables).
	 * 
	 * @param vals
	 *            parameter values IN THE DEFAULT PARAMETER ORDER
	 */
    protected void setParamValues(short[] vals) {
        TableModel model = paramTable.getModel();
        for (int k = 0; k < vals.length; k++) {
            model.setValueAt(vals[k] + "", 0, k);
        }
    }

    /**
	 * <p>
	 * The user can pass in a short[] to store the current order in so as not to
	 * require the construction of a new short[]. This method should be used by
	 * anything calling this method a significant number of times during its
	 * lifetime.
	 * </p>
	 * <p>
	 * Parameters are ordered going from 0 index up and left to right in the
	 * JTable. Reading them in these "directions" the 0th through
	 * numParametersOnX are used in the X coordiante (and on the X axis), and
	 * the remaining are those used in the Y coordinate (i.e. if you had 4
	 * parameters, a and b assigned to X and c and d assigned to Y, then their
	 * order would be abcd). See class javadoc for more info.
	 * </p>
	 * 
	 * @return a short[] holding the order of parameters
	 */
    public short[] getParameterOrder(short[] answerHolder) {
        TableColumnModel tcm = paramTable.getColumnModel();
        int n = tcm.getColumnCount();
        for (int k = 0; k < n; k++) {
            answerHolder[k] = ((Short) tcm.getColumn(k).getIdentifier()).shortValue();
        }
        return answerHolder;
    }

    /**
	 * <p>
	 * This method creates a new short[] and returns the answer. Try to use
	 * {@link com.visitrend.ndvis.gui.Parameters#getParameterOrder(short[])} so
	 * I don't have to construct a new short array. However, if you're only
	 * calling this once, or very seldomly, then it shouldn't be a big deal.
	 * </p>
	 * 
	 * @return a newly created short[] that represents the current order of the
	 *         columNames (which represent the parameters) in paraTable going
	 *         left to right.
	 * @see #getParameterOrder(short[]) getParameterOrder(short[])
	 */
    public short[] getParameterOrder() {
        short[] paramOrder = new short[dataInfo.getNumParameters()];
        return getParameterOrder(paramOrder);
    }

    /**
	 * This method returns a reference to a local short[] that contains the
	 * current parameter order. This is provided so other classes don't have to
	 * create or manage short[]s. However, this should only be used if the
	 * calling methods only read the returned short[].
	 * 
	 * @return a reference to a local short[] that contains the current
	 *         parameter order
	 */
    public short[] getParameterOrderReference() {
        getParameterOrder(order);
        return getParameterOrder(order);
    }

    /**
	 * Only methods planning on only reading (and not writing to) the returned
	 * short[] should call this method. All others should call
	 * {@link #getDefaultParameterOrder() getDefaultParameterOrder()}
	 * 
	 * @return a reference to a local short[] that represents the default
	 *         parameter order
	 */
    public short[] getDefaultParameterOrderReference() {
        return defaultParameterOrder;
    }

    /**
	 * 
	 * @return a newly created short[] that holds the defaultParameterOrder
	 *         values.
	 */
    public short[] getDefaultParameterOrder() {
        short[] b = new short[defaultParameterOrder.length];
        for (int k = 0; k < b.length; k++) {
            b[k] = defaultParameterOrder[k];
        }
        return b;
    }

    /**
	 * This sets the order of the parameters.
	 * 
	 * @param ord
	 */
    public void setParamOrder(short[] ord) {
        TableColumnModel tcm = paramTable.getColumnModel();
        tcm.removeColumnModelListener(this);
        TableColumn[] tmp = new TableColumn[tcm.getColumnCount()];
        for (int k = 0; k < ord.length; k++) {
            int index = tcm.getColumnIndex(new Short(ord[k]));
            tmp[k] = tcm.getColumn(index);
        }
        for (int k = 0; k < ord.length; k++) {
            TableColumn tc = tcm.getColumn(0);
            tcm.removeColumn(tc);
        }
        for (int k = 0; k < ord.length; k++) {
            tcm.addColumn(tmp[k]);
        }
        tcm.addColumnModelListener(this);
        fireParametersOrderChanged();
    }

    /**
	 * This returns the number of parameters on the X axis, given the current
	 * parameter ordering.
	 * 
	 * @return int - the number of parameters on the x axis
	 */
    public int getNumParametersOnX() {
        return numParametersOnX;
    }

    /**
	 * Right now this only changes the number of parameters on the X axis (and Y
	 * axis as a consequent), it does not change the order of the parameteres.
	 * It is envisioned that a future version will allow users to drag and drop
	 * dimensions from/to each axis in which case both the number of parameters
	 * on each axis and the parameter order could change at the same time.
	 * 
	 * @param numParametersOnX
	 *            the number of parameters on the X axis
	 */
    public void setNumParametersOnX(int numParametersOnX) {
        this.numParametersOnX = numParametersOnX;
        this.numParametersOnY = dataInfo.getNumParameters() - this.numParametersOnX;
        fireNumParametersOnXchanged();
    }

    /**
	 * This returns the number of parameters on the X axis, given the current
	 * parameter ordering.
	 * 
	 * @return int - the number of parameters on the y axis
	 */
    public int getNumParametersOnY() {
        return numParametersOnY;
    }

    /**
	 * @return Returns the dataInfo.
	 */
    public DataInfoINF getDataInfo() {
        return dataInfo;
    }

    /**
	 * This method sets the local dataInfo reference (it is only read, not
	 * written to) and updates the JTable in this component and the number of
	 * parameters considered to be on the X-axis. The word "parameters" is used
	 * interchangeably with "dimensions".
	 * 
	 * @param di
	 *            The dataInfo to set.
	 */
    public void setDataInfo(DataInfoINF di) {
        if (dataInfo != null) {
            dataInfo.removeDataInfoListener(this);
        }
        this.dataInfo = di;
        dataInfo.addDataInfoListener(this);
        if (xpar != null) {
            xpar.removeAllItems();
        } else {
            xpar = new JComboBox();
        }
        int parBases = 0;
        int numParameters = dataInfo.getNumParameters();
        Object[][] paramValues = new Object[1][numParameters];
        for (int k = 0; k < numParameters; k++) {
            paramValues[0][k] = "0";
            parBases += dataInfo.getBase(k);
            xpar.addItem("" + (k + 1));
        }
        parBases = parBases / 2;
        int tmp = 0;
        numParametersOnX = 0;
        for (int k = 0; k < numParameters; k++) {
            numParametersOnX++;
            tmp += dataInfo.getBase(k);
            if (tmp >= parBases) {
                break;
            }
        }
        numParametersOnY = numParameters - numParametersOnX;
        if (numParametersOnY == 0) {
            numParametersOnY = 1;
            numParametersOnX = numParametersOnX -= 1;
        }
        if (order == null || order.length != numParameters) {
            order = new short[numParameters];
            defaultParameterOrder = new short[numParameters];
        }
        for (short k = 0; k < order.length; k++) {
            defaultParameterOrder[k] = k;
            order[k] = k;
        }
        if (paramTable != null) {
            TableColumnModel tcm = paramTable.getColumnModel();
            tcm.removeColumnModelListener(this);
            remove(paramTable.getTableHeader());
            remove(paramTable);
        }
        paramTable = new JTable(paramValues, dataInfo.getParameterNames(new String[numParameters]));
        add(paramTable.getTableHeader(), BorderLayout.PAGE_START);
        add(paramTable, BorderLayout.CENTER);
        TableColumnModel tcm = paramTable.getColumnModel();
        for (short k = 0; k < numParameters; k++) {
            TableColumn tc = tcm.getColumn(k);
            tc.setIdentifier(new Short(k));
        }
        paramTable.getColumnModel().addColumnModelListener(this);
        if (xparPanel != null) {
            xparPanel.remove(xpar);
            xparPanel.add(xpar);
        } else {
            xparPanel = new JPanel();
            xparPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            xparPanel.add(new JLabel("Number of parameters on X:     "));
            xparPanel.add(xpar);
            xpar.addActionListener(this);
            add(xparPanel, BorderLayout.SOUTH);
        }
        xparPanel.validate();
        xpar.setSelectedIndex(numParametersOnX - 1);
        this.revalidate();
        this.repaint();
    }

    /**
	 * This method simply copies constant values (i.e. a '#' char) into all of
	 * the parameter values
	 */
    public void poundParamValues() {
        TableModel model = paramTable.getModel();
        int i = model.getColumnCount();
        for (int k = 0; k < i; k++) {
            model.setValueAt("#", 0, k);
        }
    }

    private void fireNumParametersOnXchanged() {
        ParametersChangedEvent pce = new ParametersChangedEvent(this);
        EventListener[] list = listeners.getListeners(ParametersListener.class);
        for (int i = list.length - 1; i >= 0; --i) {
            ((ParametersListener) list[i]).numParametersOnXchanged(pce);
        }
    }

    private void fireParametersOrderChanged() {
        getParameterOrder(order);
        ParametersChangedEvent pce = new ParametersChangedEvent(this);
        EventListener[] list = listeners.getListeners(ParametersListener.class);
        for (int i = list.length - 1; i >= 0; --i) {
            ((ParametersListener) list[i]).parametersOrderChanged(pce);
        }
    }

    /**
	 * @see com.visitrend.ndvis.event.NDVisListener#dataInfoChanged(com.visitrend.ndvis.event.DataInfoChangedEvent)
	 */
    public void dataInfoChanged(DataInfoChangedEvent evt) {
        this.setDataInfo((DataInfoINF) evt.getSource());
    }

    public void imagePanelAdded(ImagePanelAddedEvent evt) {
    }

    public void activeImagePanelChanged(NDVisChangedEvent evt) {
    }

    /**
	 * This class doesn't currently care about these changes.
	 * 
	 * @see com.visitrend.ndvis.event.DataInfoListener#baseChanged(com.visitrend.ndvis.event.BaseChangedEvent)
	 */
    public void baseChanged(BaseChangedEvent event) {
    }

    /**
	 * Updates the column names in the com.visitrend.ndvis.table because of
	 * parameter name changes.
	 * 
	 * @see com.visitrend.ndvis.event.DataInfoListener#parameterNameChanged(com.visitrend.ndvis.event.ParameterNameChangedEvent)
	 */
    public void parameterNameChanged(ParameterNameChangedEvent event) {
        paramTable.getColumnModel().getColumn(event.getIndex()).setHeaderValue(event.getParameter());
    }

    /**
	 * The only thing this class is listening to is a JComboBox that has the
	 * number of parameters on the X axis, so that's all this method is going to
	 * deal with. If that changes, then you'll have to make this method more
	 * intelligent.
	 */
    public void actionPerformed(ActionEvent arg0) {
        int numXpar = ((JComboBox) arg0.getSource()).getSelectedIndex() + 1;
        setNumParametersOnX(numXpar);
    }

    public void columnMoved(TableColumnModelEvent arg0) {
        fireParametersOrderChanged();
    }

    public void columnAdded(TableColumnModelEvent arg0) {
    }

    public void columnMarginChanged(ChangeEvent arg0) {
    }

    public void columnRemoved(TableColumnModelEvent arg0) {
    }

    public void columnSelectionChanged(ListSelectionEvent arg0) {
    }
}
