package mipt.crec.lab.compmath.gui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import mipt.aaf.edit.swing.data.DataFormCreator;
import mipt.common.DoubleFormatter;
import mipt.crec.lab.common.modules.gui.LabFormDelegate;
import mipt.crec.lab.common.modules.gui.ParamsDataFormCreator;
import mipt.crec.lab.common.reflect.NumberValueFieldSetter;
import mipt.crec.lab.compmath.CompMathNames;
import mipt.data.Data;
import mipt.gui.DoubleTextField;
import mipt.gui.choice.DefaultCellEditor;
import mipt.gui.choice.DefaultTableRenderer;
import mipt.math.ui.GraphicFormulaCellRenderer;

/**
 * ������� �o������� ����� �������������� ���������� ������ ��� ��� �� �������������� ����������
 * @author Evdokimov
 */
public class CompLabFormDelegate extends LabFormDelegate implements CompMathNames {

    /**
	 * Convenience method
	 */
    public final Data getModelData() {
        return ((DataFormCreator) parent).getModelData();
    }

    /**
	 * Convenience method. Be careful: can often return null.
	 */
    public final ParamsDataFormCreator getParentCreator() {
        return ((ParamsDataFormCreator) parent).getParentCreator();
    }

    /**
	 * 
	 */
    public CompLabFormDelegate() {
        super();
    }

    /**
	 * Overridden to place autostart to the bottom
	 * @see mipt.crec.lab.common.modules.gui.LabFormDelegate#getMaximumHeightComponentIndex(int)
	 */
    public int getMaximumHeightComponentIndex(int fieldCount) {
        return super.getMaximumHeightComponentIndex(fieldCount) - 1;
    }

    /**
	 * @see mipt.crec.lab.common.modules.gui.LabFormDelegate#initListOfNotNecessaryFieldNames()
	 */
    public List initListOfNotNecessaryFieldNames() {
        List list = super.initListOfNotNecessaryFieldNames();
        list.add(NUMBER);
        list.add(RESULT);
        list.add(PLOT_POINTS);
        list.add(PLOT_POINTS_BEFORE_UPDATE);
        return list;
    }

    /**
	 * @see mipt.crec.lab.common.modules.gui.LabFormDelegate#initFormulaCellEditor()
	 */
    public DefaultCellEditor initFormulaCellEditor() {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(getCellAlignment());
        return new DefaultCellEditor(field) {

            protected EditorDelegate createDelegate(final JTextField field) {
                return new EditorDelegate() {

                    public void setValue(Object value) {
                        field.setText((value != null) ? value.toString() : getNullText());
                        field.selectAll();
                    }

                    public Object getCellEditorValue() {
                        return field.getText();
                    }

                    public boolean shouldStopEditing() {
                        String text = field.getText();
                        if (text.length() == 0) return false;
                        return shouldStopCellEditing(text);
                    }
                };
            }

            protected String getNullText() {
                return "0.0";
            }
        };
    }

    protected boolean shouldStopCellEditing(String text) {
        mipt.math.Number value = NumberValueFieldSetter.getFormulaValue(text, null);
        if (value == null || value.toString().length() == 0) return false;
        return true;
    }

    /**
	 * @see mipt.crec.lab.common.modules.gui.LabFormDelegate#updateTable(javax.swing.JTable, java.lang.String, int)
	 */
    public void updateTable(JTable t, String fieldName, int fieldType) {
        super.updateTable(t, fieldName, fieldType);
        DefaultTableRenderer r = new DefaultTableRenderer();
        r.setDoubleFormat(new DoubleFormatter.Fractional9Format());
        t.setDefaultRenderer(Double.class, r);
    }

    /**
	 * @see mipt.crec.lab.common.modules.gui.LabFormDelegate#updateTextField(javax.swing.JTextField, java.lang.String, int)
	 */
    public void updateTextField(JTextField f, String fieldName, int fieldType) {
        if (f instanceof DoubleTextField) {
            DoubleTextField df = (DoubleTextField) f;
            boolean accuracy = fieldName.contains("olerance") || fieldName.contains("ccuracy");
            if (accuracy || fieldName.contains("eviation") || fieldName.contains("step")) {
                df.setMin(Double.MIN_VALUE);
                df.setDoubleFormat(new DoubleFormatter.Fractional9Format(4, 2, true));
                if (accuracy) df.setDelta(10.);
            } else {
                super.updateTextField(f, fieldName, fieldType);
                if (fieldName.startsWith("x") || fieldName.startsWith("y")) {
                    if (fieldName.length() > 1) {
                        String s = fieldName.substring(1, 2);
                        if (!s.equals(s.toUpperCase())) return;
                    }
                    df.setMin(-Double.MAX_VALUE);
                }
            }
        } else super.updateTextField(f, fieldName, fieldType);
    }

    /**
	 * ����� �� ������ ���� ��� �����
	 * @param renderer - ���� ����� ������������ �������, ����� ���������� �� �� ������
	 * @param maxContents - ����� ������������� �������
	 * @param yInset - ������� � ������� �� y; ��� �������, ����������� �������� ���������
	 */
    protected final GraphicFormulaCellRenderer setFixedSize(GraphicFormulaCellRenderer viewer, String maxContents, int yInset) {
        viewer.setText(maxContents);
        Dimension d = viewer.getPreferredSize();
        d.height += yInset;
        viewer.setMinimumSize(d);
        viewer.setPreferredSize(d);
        return viewer;
    }
}
