package mipt.crec.lab.compmath.gui;

import java.awt.Component;
import javax.swing.JTabbedPane;
import mipt.crec.lab.compmath.CompMathNames;
import mipt.data.Data;
import mipt.data.event.DataModelEvent;

/**
 * ���������� ��� view �������, ������� �������� DataModelEvents ��� ���������� ��������
 *  (�������������� ������� - ������ � ��������� (Comp)GraphsModuleView - ����� ����������, ��
 *  �� ��������� ���������� ������ ������ - ����������� ��� �� �������� ������).
 * ������������ ��� ���� �������:
 *  1) "�������" (��� ���� ����������� "��������� ������ �� ������" ����� ��������,
 *  ���� ��� ����; � ����� �������� ���������� Data-���� result) � 2) "���������"
 *  (��� ���� ���������� ������ Data-���� result � ����������� "��������� �� �������
 *  �������� �������" �����)
 * � ������ ����� ������� ����� �������������� ��-�������:
 * �) "������� �������" ������������ ������ ��� ��� ��������� ������   
 * �) "������� �������" ������������ ������ ��� ��������� ������, ����� - ������
 *  "��������� �������", �� ����������� �������� �������� ����������, ���������� �� ������
 *  ��� ��� ���� ��������� �������� ������� ��� ��� �� ����� ������. � ���� ������
 *  setDataToGraphs ���������� �� �����.
 * � ������ �����, ��� ������ setDataToGraphs � setResultToGraphs ����� ���������� false
 *  ��� ����������� ����������� ��������� �������
 * @author Evdokimov
 */
public abstract class CompGraphsDataModuleView extends GraphsDataModuleView {

    /**
	 * ���������� ������������ �������� ��� null ���� �� �� �������� JTabbedPane
	 * ����� ��������������, ��������, ���� ����������� ������� ������ 1 
	 */
    protected JTabbedPane getParentPane() {
        Component c = getParentComponent();
        if (c instanceof JTabbedPane) return (JTabbedPane) c; else return null;
    }

    /**
	 * ������ ���������� �������, �������������� ������� ����������
	 * ���� ��� ���� ��������, �������� ������ ������� (��� ������, ���� ������ �������� ���) 
	 */
    public void setEnabledResult(boolean enable) {
        JTabbedPane tabs = getParentPane();
        if (tabs == null) return;
        int index = tabs.indexOfComponent(getComponent());
        tabs.setEnabledAt(index, enable);
        if (!enable && tabs.getSelectedIndex() == index) tabs.setSelectedIndex(index == 0 ? 1 : 0);
    }

    /**
	 * @see mipt.data.event.DataModelListener#dataChanged(mipt.data.event.DataModelEvent)
	 */
    public void dataChanged(DataModelEvent e) {
        Data data = e.getData(), result = data;
        boolean hasResult = true;
        if (!data.getType().equals(CompMathNames.RESULT)) {
            hasResult = setDataToGraphs(data);
            if (hasResult) result = data.getData(CompMathNames.RESULT);
        }
        if (result != null) {
            if (hasResult) hasResult = setResultToGraphs(result);
            if (hasResult) setResultToLabel(result, false);
        } else {
            hasResult = false;
        }
        setEnabledResult(hasResult);
    }

    /**
	 * Must be called only at the end of iterations (unlike dataChanged()!).
	 * @see mipt.crec.lab.common.modules.gui.GraphsModuleView#updateResults()
	 */
    public void updateResults() {
        setCountsToLabel();
    }

    /**
	 * @see mipt.crec.lab.common.modules.gui.GraphsModuleView#updateIncompleteResults()
	 */
    public void updateIncompleteResults() {
        setResultToLabel(getData().getData(CompMathNames.RESULT), true);
    }

    /**
	 * ��������� getSolvedLabel() ������� ���������� ������� result. ��� ������� �� ������ �������
	 *  ������� �������� setErrorToLabel (� ������ isError).
	 * @param isFinal - ������ ������� ��-������� �������� ������������� � ������������� ���������
	 */
    protected abstract void setResultToLabel(Data result, boolean isFinal);

    /**
	 * ��������� ������� ������� ���������� ������� result 
	 * ���������� false ���� ������� ���������� ��� ������ (��� �����)
	 */
    protected abstract boolean setResultToGraphs(Data result);

    /**
	 * ���������/�������� ���������� �������� ����������� �� ������ � ������ �����, ����� result 
	 * ���������� false ���� ������� ���������� ��� ������ (��� �����)
	 */
    protected abstract boolean setDataToGraphs(Data data);
}
