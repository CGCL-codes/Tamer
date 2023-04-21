package cw.studentmanagementmodul.gui;

import com.jgoodies.binding.list.SelectionInList;
import cw.boardingschoolmanagement.app.CWUtils;
import cw.boardingschoolmanagement.gui.component.CWView.CWHeaderInfo;
import cw.studentmanagementmodul.pojo.Student;
import cw.studentmanagementmodul.pojo.StudentClass;
import cw.studentmanagementmodul.pojo.manager.StudentManager;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * @author CreativeWorkers.at
 */
public class StudentsOverviewPresentationModel {

    private SelectionInList<Student> studentSelection;

    private StudentClass studentClass;

    private CWHeaderInfo headerInfo;

    public StudentsOverviewPresentationModel(StudentClass studentClass) {
        this.studentClass = studentClass;
        initModels();
        initEventHandling();
    }

    private void initModels() {
        studentSelection = new SelectionInList<Student>(StudentManager.getInstance().getAllActive(studentClass));
        headerInfo = new CWHeaderInfo("Schueler anzeigen", "<html>Hier haben Sie einen Ueberblick ueber alle Schueler der Klasse '<b>" + studentClass + "</b>'.<br>Anzahl: <b>" + studentSelection.getSize() + " Schueler</b></html>", CWUtils.loadIcon("cw/studentmanagementmodul/images/student.png"), CWUtils.loadIcon("cw/studentmanagementmodul/images/student.png"));
    }

    private void initEventHandling() {
    }

    public void dispose() {
    }

    public CWHeaderInfo getHeaderInfo() {
        return headerInfo;
    }

    public SelectionInList<Student> getStudentSelection() {
        return studentSelection;
    }

    public TableModel createStudentTableModel(ListModel listModel) {
        return new StudentTableModel(listModel);
    }

    public static class StudentTableModel extends AbstractTableModel {

        private ListModel listModel;

        public StudentTableModel(ListModel listModel) {
            this.listModel = listModel;
        }

        public int getRowCount() {
            return listModel.getSize();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Anrede";
                case 1:
                    return "Vorname";
                case 2:
                    return "2. Vorname";
                case 3:
                    return "Nachname";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Student s = (Student) listModel.getElementAt(rowIndex);
            switch(columnIndex) {
                case 0:
                    if (s.getCustomer().getGender()) {
                        return "Herr";
                    }
                    return "Frau";
                case 1:
                    return s.getCustomer().getForename();
                case 2:
                    return s.getCustomer().getSurname();
                default:
                    return "";
            }
        }
    }
}
