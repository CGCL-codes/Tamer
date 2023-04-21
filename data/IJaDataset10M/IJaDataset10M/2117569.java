package edu.gatech.ece.cs1372;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * Manage the grades and criteria for a gradebook assignment. Contains utility classes for accessing the data via
 * ListModel, etc
 * 
 * @author eko
 */
public class GradeBook implements Serializable {

    private static final long serialVersionUID = -1930348958105508204L;

    private static Logger m_log = Logger.getLogger(AutoGrader.LOG_NAMESPACE);

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private static final int GB_COL_COUNT = 5;

    private static final int GB_COL_FIRST = 3;

    private static final int GB_COL_GRADE = 4;

    private static final int GB_COL_LAST = 2;

    private static final int GB_COL_USERNAME = 0;

    private static final int GB_HDR_COUNT = 2;

    private static final int GB_HDR_POINTS = 1;

    transient ArrayList<GradeBookListener> m_listeners;

    Integer m_rubric_compilevalue;

    String[] m_rubric_items;

    Integer[] m_rubric_values;

    ArrayList<String> m_student_list;

    ArrayList<StudentList> m_student_lists;

    HashMap<Integer, String> m_submission_comments;

    HashMap<Integer, String> m_submission_compileoutput;

    HashMap<Integer, Integer> m_submission_compilescore;

    HashMap<Integer, String> m_submission_execoutput;

    HashMap<Integer, Integer[]> m_submission_grades;

    public GradeBook() {
        m_log.setLevel(Level.ALL);
        m_student_lists = new ArrayList<StudentList>();
        m_listeners = new ArrayList<GradeBookListener>();
        m_rubric_compilevalue = 20;
        m_rubric_items = new String[] { "Code Style", "Commenting", "Completion" };
        m_rubric_values = new Integer[] { 15, 15, 50 };
        m_student_list = new ArrayList<String>();
        m_submission_compilescore = new HashMap<Integer, Integer>();
        m_submission_compileoutput = new HashMap<Integer, String>();
        m_submission_execoutput = new HashMap<Integer, String>();
        m_submission_comments = new HashMap<Integer, String>();
        m_submission_grades = new HashMap<Integer, Integer[]>();
    }

    public void addGradeBookListener(GradeBookListener gbl) {
        m_listeners.add(gbl);
    }

    public void addStudent(String student) {
        m_student_list.add(student);
        m_submission_compilescore.put(student.hashCode(), 0);
        m_submission_compileoutput.put(student.hashCode(), "");
        m_submission_execoutput.put(student.hashCode(), "");
        m_submission_comments.put(student.hashCode(), "");
        Integer[] studentRubricItems = new Integer[m_rubric_items.length];
        for (int i = 0; i < m_rubric_items.length; ++i) {
            studentRubricItems[i] = 0;
        }
        m_submission_grades.put(student.hashCode(), studentRubricItems);
        for (StudentList sl : m_student_lists) {
            sl.appended(1);
        }
    }

    public Integer getCompileValue() {
        return m_rubric_compilevalue;
    }

    public String getRubricItem(int i) {
        return m_rubric_items[i];
    }

    public int getRubricItemCount() {
        return m_rubric_items.length;
    }

    public Integer getRubricItemValue(int i) {
        return m_rubric_values[i];
    }

    public Integer getRubricTotal() {
        Integer sum = m_rubric_compilevalue;
        for (Integer item : m_rubric_values) {
            sum += item;
        }
        return sum;
    }

    public String getStudentComments(String student) {
        if (!m_student_list.contains(student)) return "";
        return m_submission_comments.get(student.hashCode());
    }

    public String getStudentCompileOutput(String student) {
        if (!m_student_list.contains(student)) return "";
        return m_submission_compileoutput.get(student.hashCode());
    }

    public Integer getStudentCompileValue(String student) {
        if (!m_student_list.contains(student)) return 0;
        return m_submission_compilescore.get(student.hashCode());
    }

    public int getStudentCount() {
        return m_student_list.size();
    }

    public String getStudentExecOutput(String student) {
        if (!m_student_list.contains(student)) return "";
        return m_submission_execoutput.get(student.hashCode());
    }

    public Integer getStudentItemScore(String student, int index) {
        if (!m_student_list.contains(student)) return 0;
        if (index >= m_rubric_items.length) return 0;
        Integer score = m_submission_grades.get(student.hashCode())[index];
        if (score == null) return 0;
        return score;
    }

    public Integer getStudentTotal(String student) {
        if (!m_student_list.contains(student)) return 0;
        Integer sum = m_submission_compilescore.get(student.hashCode());
        for (Integer item : m_submission_grades.get(student.hashCode())) {
            if (item != null) sum += item;
        }
        return sum;
    }

    private void fireGradeBookChanged() {
        for (GradeBookListener gbl : m_listeners) {
            gbl.gradeBookChanged();
        }
    }

    private void fireRubricChanged() {
        for (GradeBookListener gbl : m_listeners) {
            gbl.rubricChanged();
        }
    }

    public void removeGradeBookListener(GradeBookListener gbl) {
        m_listeners.remove(gbl);
    }

    public void setCompileValue(int points) {
        m_rubric_compilevalue = points;
        for (Integer i : m_submission_compilescore.keySet()) {
            if (m_submission_compilescore.get(i) > points) m_submission_compilescore.put(i, points);
        }
        fireGradeBookChanged();
    }

    public void setStudentComments(String student, String text) {
        if (!m_student_list.contains(student)) return;
        m_submission_comments.put(student.hashCode(), text);
    }

    public void setStudentCompiles(String student, boolean compiles, String compileOutput) {
        if (!m_student_list.contains(student)) return;
        int hashCode = student.hashCode();
        if (compiles) m_submission_compilescore.put(hashCode, m_rubric_compilevalue); else m_submission_compilescore.put(hashCode, 0);
        m_submission_compileoutput.put(hashCode, compileOutput);
        fireGradeBookChanged();
    }

    public void setStudentExecution(String student, String execOutput) {
        if (!m_student_list.contains(student)) return;
        int hashCode = student.hashCode();
        m_submission_execoutput.put(hashCode, execOutput);
        fireGradeBookChanged();
    }

    public void setStudentItemScore(String student, int index, int newGrade) {
        if (!m_student_list.contains(student)) return;
        if (index >= m_rubric_items.length) return;
        m_submission_grades.get(student.hashCode())[index] = newGrade;
        fireGradeBookChanged();
    }

    public boolean writeToFile(String baseDir, String gradeFile, String commentFile, String gradebookFile) {
        ArrayList<String> lines = new ArrayList<String>();
        m_log.log(Level.INFO, "Writing grade book into {0}: {1} and {2}", new Object[] { baseDir, gradeFile, commentFile });
        try {
            File path = new File(baseDir + FILE_SEPARATOR + gradeFile);
            BufferedReader fin = new BufferedReader(new FileReader(path));
            String line;
            int lineno = 0;
            while ((line = fin.readLine()) != null) {
                line = line.trim();
                System.err.println("< " + line);
                if (lineno == 0) {
                    String[] pieces = line.split(",", -1);
                    if (pieces.length != GB_HDR_COUNT) {
                        JOptionPane.showMessageDialog(null, "Bad input line format for GradeBook header: Expected  " + GB_HDR_COUNT + " columns, found" + pieces.length, "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    pieces[GB_HDR_POINTS] = "Points [" + getRubricTotal() + ".0]";
                    line = "";
                    for (int i = 0; i < GB_HDR_COUNT; ++i) {
                        if (i > 0) line += ",";
                        line += pieces[i];
                    }
                    lines.add(line);
                } else if (lineno < 3) {
                    lines.add(line);
                } else {
                    String[] pieces = line.split(",", -1);
                    if (pieces.length != GB_COL_COUNT) {
                        JOptionPane.showMessageDialog(null, "Bad input line format for GradeBook file: Expected  " + GB_COL_COUNT + " columns, found" + pieces.length, "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    String student = pieces[GB_COL_LAST] + "," + pieces[GB_COL_FIRST] + "(" + pieces[GB_COL_USERNAME] + ")";
                    if (!m_student_list.contains(student)) {
                        JOptionPane.showMessageDialog(null, "Bad student in GradeBook file: " + student, "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    pieces[GB_COL_GRADE] = String.valueOf(this.getStudentTotal(student));
                    line = "";
                    for (int i = 0; i < GB_COL_COUNT; ++i) {
                        if (i > 0) line += ",";
                        line += pieces[i];
                    }
                    lines.add(line);
                }
                ++lineno;
            }
            fin.close();
        } catch (FileNotFoundException e) {
            m_log.log(Level.WARNING, "File not found", e);
            return false;
        } catch (IOException e) {
            m_log.log(Level.SEVERE, "I/O Error", e);
            return false;
        }
        try {
            File path = new File(baseDir + FILE_SEPARATOR + gradeFile);
            FileWriter fout = new FileWriter(path);
            for (String line : lines) {
                System.err.println("> " + line);
                fout.write(line + "\n");
            }
            fout.close();
        } catch (FileNotFoundException e) {
            m_log.log(Level.WARNING, "File not found", e);
            return false;
        } catch (IOException e) {
            m_log.log(Level.SEVERE, "I/O Error", e);
            return false;
        }
        try {
            for (String student : m_student_list) {
                File path = new File(baseDir + FILE_SEPARATOR + student + FILE_SEPARATOR + commentFile);
                FileWriter fout = new FileWriter(path);
                fout.write("Grade report for " + student + ":\n\n");
                int hashCode = student.hashCode();
                fout.write("Compilation: " + m_submission_compilescore.get(hashCode) + "/" + m_rubric_compilevalue + "\n");
                Integer[] scores = m_submission_grades.get(hashCode);
                for (int i = 0; i < m_rubric_items.length; ++i) {
                    fout.write(m_rubric_items[i] + ": " + scores[i] + "/" + m_rubric_values[i] + "\n");
                }
                fout.write("\nGrader comments:\n\n");
                fout.write(m_submission_comments.get(hashCode) + "\n");
                fout.write("\nCompilation output:\n\n");
                fout.write(m_submission_compileoutput.get(hashCode) + "\n");
                fout.write("\nExecution output:\n\n");
                fout.write(m_submission_execoutput.get(hashCode) + "\n");
                fout.write("\nGrade report produced by " + AutoGrader.VERSION + " on " + AutoGrader.SYSTEM);
                fout.close();
            }
        } catch (FileNotFoundException e) {
            m_log.log(Level.WARNING, "File not found", e);
            return false;
        } catch (IOException e) {
            m_log.log(Level.SEVERE, "I/O Error", e);
            return false;
        }
        m_log.info("Writing gradebook to " + baseDir + FILE_SEPARATOR + gradebookFile);
        writeGradeBook(baseDir + FILE_SEPARATOR + gradebookFile, this);
        return true;
    }

    public static interface GradeBookListener {

        public void gradeBookChanged();

        public void rubricChanged();
    }

    public class StudentList extends AbstractListModel {

        private static final long serialVersionUID = -3295804772826904771L;

        public StudentList() {
            m_student_lists.add(this);
        }

        public void appended(int count) {
            this.fireIntervalAdded(this, getSize() - count, getSize());
        }

        public Object getElementAt(int index) {
            return m_student_list.get(index);
        }

        public int getSize() {
            return m_student_list.size();
        }
    }

    public class RubricTable extends AbstractTableModel implements GradeBookListener {

        private static final long serialVersionUID = 7492105081696029635L;

        public static final int COL_ITEM = 0;

        public static final int COL_POINTS = 1;

        public static final int COL_COUNT = 2;

        public final String[] COLUMN_NAMES = { "Rubric Item", "Point Value" };

        public RubricTable() {
            addGradeBookListener(this);
        }

        public void gradeBookChanged() {
        }

        public void rubricChanged() {
            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return COL_COUNT;
        }

        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        public int getRowCount() {
            return m_rubric_items.length + 2;
        }

        public boolean isCellEditable(int row, int col) {
            return (row < m_rubric_items.length || (row == m_rubric_items.length && col == COL_POINTS));
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case COL_ITEM:
                    if (rowIndex == m_rubric_items.length) return "Compilation";
                    if (rowIndex == m_rubric_items.length + 1) return "    Total:";
                    return m_rubric_items[rowIndex];
                case COL_POINTS:
                    if (rowIndex == m_rubric_items.length) return getCompileValue();
                    if (rowIndex == m_rubric_items.length + 1) return getRubricTotal();
                    return m_rubric_values[rowIndex];
                default:
                    return "Unknown";
            }
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            try {
                switch(columnIndex) {
                    case COL_ITEM:
                        if (rowIndex >= m_rubric_items.length) return;
                        m_rubric_items[rowIndex] = (String) value;
                        this.fireTableCellUpdated(rowIndex, columnIndex);
                        break;
                    case COL_POINTS:
                        if (rowIndex > m_rubric_items.length) return;
                        if (rowIndex == m_rubric_items.length) setCompileValue(Integer.parseInt((String) value)); else m_rubric_values[rowIndex] = Integer.parseInt((String) value);
                        this.fireTableCellUpdated(rowIndex, columnIndex);
                        this.fireTableCellUpdated(m_rubric_items.length + 1, columnIndex);
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException ex) {
                m_log.log(Level.INFO, "Number Format Exception", ex);
            }
        }
    }

    public void appendRubricItem(String itemName, int itemValue) {
        String[] newItems = new String[m_rubric_items.length + 1];
        for (int i = 0; i < m_rubric_items.length; ++i) newItems[i] = m_rubric_items[i];
        newItems[newItems.length - 1] = itemName;
        Integer[] newValues = new Integer[m_rubric_items.length + 1];
        for (int i = 0; i < m_rubric_items.length; ++i) newValues[i] = m_rubric_values[i];
        newValues[newValues.length - 1] = itemValue;
        m_rubric_items = newItems;
        m_rubric_values = newValues;
        this.fireRubricChanged();
    }

    public void removeRubricItems(int[] selectedRows) {
        int outIdx = 0;
        int newLength = m_rubric_items.length - selectedRows.length;
        for (int skipRow : selectedRows) {
            if (skipRow >= m_rubric_items.length) newLength++;
        }
        String[] newItems = new String[newLength];
        Integer[] newValues = new Integer[newLength];
        for (int inIdx = 0; inIdx < m_rubric_items.length; ++inIdx) {
            boolean skip = false;
            for (int skipRow : selectedRows) {
                if (skipRow == inIdx) skip = true;
            }
            if (!skip) {
                newItems[outIdx] = m_rubric_items[inIdx];
                newValues[outIdx] = m_rubric_values[inIdx];
                outIdx++;
            }
        }
        m_rubric_items = newItems;
        m_rubric_values = newValues;
        this.fireRubricChanged();
    }

    public String getStudentName(int i) {
        return m_student_list.get(i);
    }

    public static boolean writeGradeBook(String fileName, GradeBook gb) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(gb);
        } catch (FileNotFoundException e) {
            m_log.log(Level.SEVERE, "Error: Could Not Find File", e);
            return false;
        } catch (IOException e) {
            m_log.log(Level.SEVERE, "I/O Error Writing Object to File", e);
            return false;
        }
        return true;
    }

    public static GradeBook readGradeBook(String fileName) {
        try {
            ObjectInputStream out = new ObjectInputStream(new FileInputStream(fileName));
            Object ob = out.readObject();
            if (ob.getClass() == GradeBook.class) {
                GradeBook gb = (GradeBook) ob;
                gb.m_listeners = new ArrayList<GradeBookListener>();
                return gb;
            }
        } catch (FileNotFoundException e) {
            m_log.log(Level.SEVERE, "Error: Could Not Find File", e);
        } catch (IOException e) {
            m_log.log(Level.SEVERE, "I/O Error Reading Object from File", e);
        } catch (ClassNotFoundException e) {
            m_log.log(Level.SEVERE, "Class Load Error", e);
        }
        return null;
    }
}
