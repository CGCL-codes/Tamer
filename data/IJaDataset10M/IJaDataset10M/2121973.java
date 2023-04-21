package vademecum.externals.weka;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Enumeration;
import javax.swing.JDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.advisor.Advisor;
import vademecum.data.ClusterNumber;
import vademecum.data.Column;
import vademecum.data.DataGrid;
import vademecum.data.GridUtils;
import vademecum.data.IClusterNumber;
import vademecum.data.IDataGrid;
import vademecum.data.IDataRow;
import vademecum.data.SubsetSelector;
import vademecum.data.converter.WekaConverter;
import vademecum.extensionPoint.DefaultDataNode;
import vademecum.extensionPoint.IClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializedObject;
import weka.core.Utils;
import weka.gui.GenericObjectEditor;
import weka.gui.GenericObjectEditor.GOEPanel;

public class WekaJ48 extends DefaultDataNode implements IClassifier, PropertyChangeListener, ActionListener {

    /**
	 * private logger instance
	 */
    private static Log log = LogFactory.getLog(WekaJ48.class);

    private Instances data;

    private J48 tree = new J48();

    private JDialog dialog;

    public String getName() {
        return "Weka C4.5";
    }

    public Object getOutput(Class outputType) {
        if (outputType.equals(String.class)) {
            try {
                return properties.get("graph");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JDialog getPreferencesDialog(Frame owner) {
        if (dialog == null) {
            dialog = new JDialog();
            java.beans.PropertyEditorManager.registerEditor(weka.classifiers.Classifier.class, GenericObjectEditor.class);
            GenericObjectEditor ce = new GenericObjectEditor(false);
            ce.setClassType(weka.classifiers.Classifier.class);
            ce.setValue(tree);
            ce.addPropertyChangeListener(this);
            GOEPanel editor = (GOEPanel) ce.getCustomEditor();
            editor.addOkListener(this);
            editor.addCancelListener(this);
            dialog.add(editor);
            dialog.pack();
        }
        return dialog;
    }

    public String getResultText() {
        String text = "";
        try {
            text = tree.toSummaryString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public boolean hasFinished() {
        return true;
    }

    public void init() {
        try {
            tree.buildClassifier(data);
            setProperty("graph", tree.graph());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iterate() {
    }

    public void reset() {
    }

    public void load(File folder) {
    }

    public void save(File folder) {
    }

    public void setInput(Class inputType, Object data) {
        if (inputType.equals(IDataGrid.class)) {
            log.debug("Setting internal DATA, converting first to INSTANCES");
            this.data = WekaConverter.convertIDataGridToInstances((IDataGrid) data);
        }
    }

    public IClusterNumber classifyExample(IDataRow example) {
        return null;
    }

    public IDataGrid classifyExamples(IDataGrid data) {
        log.debug("classify data with #" + data.getNumRows() + " rows, #" + data.getNumCols() + " Attributes");
        IDataGrid classified = new DataGrid();
        Column col = new Column();
        classified.addColumn(col);
        Instances instances = WekaConverter.convertIDataGridToInstances(data);
        Enumeration inst = instances.enumerateInstances();
        int i = 0;
        while (inst.hasMoreElements()) {
            Instance instance = (Instance) inst.nextElement();
            try {
                int classification = (int) this.tree.classifyInstance(instance);
                ClusterNumber cnm = new ClusterNumber(classification);
                classified.setKey(i, i);
                classified.setPoint(i, 0, cnm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        return classified;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        log.debug(evt.getSource());
        log.debug(evt.getPropertyName() + " changed from " + evt.getOldValue() + "->" + evt.getNewValue());
    }

    public void actionPerformed(ActionEvent e) {
        log.debug("received ActionEvent:" + e.getActionCommand());
        if (e.getActionCommand().equals("OK")) {
            setProperty("weka.properties", Utils.joinOptions(tree.getOptions()));
            this.firePropertiesChangedEvent();
        }
    }
}
