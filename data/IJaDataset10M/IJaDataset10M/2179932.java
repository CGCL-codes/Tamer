package finalprj;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ManagementEmphasis extends JPanel implements ActionListener, ListSelectionListener, CourseModelListener {

    private static final long serialVersionUID = 6792018962872912599L;

    private CourseController courseController;

    private JList emphasisList, coursesList, courseGroupsList;

    private JTextField nameTextField;

    private JButton newButton, saveButton, deleteButton, addCourseButton, removeCourseButton, addCourseGroupButton, removeCourseGroupButton;

    private JComboBox coursesComboBox, courseGroupsComboBox;

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
        intialize();
        courseController.addCourseModelListener(this);
        courseModelChangePerformed(courseController);
        courseGroupModelChangePerformed(courseController);
        emphasisModelChangePerformed(courseController);
        validate();
    }

    private void intialize() {
        this.setLayout(new BorderLayout());
        this.add(getEmphasisSelectionPanel(), BorderLayout.LINE_START);
        this.add(getEmphasisMutationPanel(), BorderLayout.CENTER);
    }

    private JScrollPane getEmphasisSelectionPanel() {
        if (emphasisList == null) {
            emphasisList = new JList();
            emphasisList.addListSelectionListener(this);
        }
        JScrollPane scrollPane = new JScrollPane(emphasisList);
        scrollPane.setPreferredSize(new Dimension(124, 0));
        return scrollPane;
    }

    private JPanel getEmphasisMutationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(getEmphasisDetailMutationPanel());
        panel.add(Box.createHorizontalStrut(10));
        panel.add(getEmphasisMembersPanel());
        panel.add(Box.createHorizontalStrut(10));
        return panel;
    }

    private JPanel getEmphasisDetailMutationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalStrut(25));
        panel.add(getNameTextField());
        panel.add(getEmphasisControllButtons());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel getEmphasisMembersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setPreferredSize(new Dimension(250, 600));
        panel.setMaximumSize(new Dimension(250, 600));
        panel.add(Box.createVerticalStrut(10));
        panel.add(getCoursesPanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(getCourseGroupsPanel());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private Component getNameTextField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setMaximumSize(new Dimension(600, 22));
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setMaximumSize(new Dimension(250, 22));
            nameTextField.setPreferredSize(new Dimension(250, 22));
        }
        JLabel label = new JLabel("Name");
        panel.add(label);
        panel.add(Box.createHorizontalGlue());
        panel.add(nameTextField);
        return panel;
    }

    private Component getEmphasisControllButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setMaximumSize(new Dimension(600, 22));
        if (newButton == null) {
            newButton = new JButton("New");
            newButton.addActionListener(this);
        }
        if (saveButton == null) {
            saveButton = new JButton("Save");
            saveButton.addActionListener(this);
        }
        if (deleteButton == null) {
            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(this);
        }
        panel.add(newButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(saveButton);
        panel.add(deleteButton);
        return panel;
    }

    private JPanel getCoursesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setMinimumSize(new Dimension(800, 300));
        panel.setMaximumSize(new Dimension(800, 300));
        panel.add(getCoursesLabel());
        panel.add(getCoursesComboBox());
        panel.add(getCoursesList());
        panel.add(getCoursesButtons());
        return panel;
    }

    private JPanel getCoursesLabel() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        JLabel label = new JLabel("Courses");
        panel.add(label);
        return panel;
    }

    private Component getCoursesComboBox() {
        if (coursesComboBox == null) {
            coursesComboBox = new JComboBox();
            coursesComboBox.setMaximumSize(new Dimension(800, 22));
            coursesComboBox.addActionListener(this);
        }
        return coursesComboBox;
    }

    private Component getCoursesList() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        if (coursesList == null) {
            coursesList = new JList();
        }
        JScrollPane scrollpane = new JScrollPane(coursesList);
        panel.add(scrollpane);
        return panel;
    }

    private Component getCoursesButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setMaximumSize(new Dimension(800, 22));
        if (addCourseButton == null) {
            addCourseButton = new JButton("Add");
            addCourseButton.addActionListener(this);
        }
        if (removeCourseButton == null) {
            removeCourseButton = new JButton("Remove");
            removeCourseButton.addActionListener(this);
        }
        panel.add(addCourseButton);
        panel.add(removeCourseButton);
        return panel;
    }

    private JPanel getCourseGroupsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setMinimumSize(new Dimension(800, 300));
        panel.setMaximumSize(new Dimension(800, 300));
        panel.add(getCourseGroupsLabel());
        panel.add(getCourseGroupsComboBox());
        panel.add(getCourseGroupsList());
        panel.add(getCourseGroupsButtons());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel getCourseGroupsLabel() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        JLabel label = new JLabel("CourseGroups");
        panel.add(label);
        return panel;
    }

    private Component getCourseGroupsComboBox() {
        if (courseGroupsComboBox == null) {
            courseGroupsComboBox = new JComboBox();
            courseGroupsComboBox.setMaximumSize(new Dimension(800, 22));
            courseGroupsComboBox.addActionListener(this);
        }
        return courseGroupsComboBox;
    }

    private Component getCourseGroupsList() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        if (courseGroupsList == null) {
            courseGroupsList = new JList();
        }
        JScrollPane scrollpane = new JScrollPane(courseGroupsList);
        panel.add(scrollpane);
        return panel;
    }

    private Component getCourseGroupsButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setMaximumSize(new Dimension(800, 22));
        if (addCourseGroupButton == null) {
            addCourseGroupButton = new JButton("Add");
            addCourseGroupButton.addActionListener(this);
        }
        if (removeCourseGroupButton == null) {
            removeCourseGroupButton = new JButton("Remove");
            removeCourseGroupButton.addActionListener(this);
        }
        panel.add(addCourseGroupButton);
        panel.add(removeCourseGroupButton);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newButton) {
            courseController.selectEmphasis(new Emphasis());
        } else if (e.getSource() == saveButton) {
            Emphasis emphasis = new Emphasis(nameTextField.getText());
            courseController.addEmphasis(emphasis);
            emphasisList.setSelectedValue(emphasis, true);
            courseController.selectEmphasis(emphasis);
        } else if (e.getSource() == deleteButton) {
            courseController.removeEmphasis();
            courseController.save_Emphasis_File();
        } else if (e.getSource() == addCourseButton) {
            Emphasis emphasis = courseController.getSelectedEmphasis();
            if (emphasis != null && coursesComboBox.getSelectedItem() != null) {
                if (!(emphasis.getCourses().contains((Course) coursesComboBox.getSelectedItem()))) emphasis.getCourses().add((Course) coursesComboBox.getSelectedItem());
                courseController.save_Emphasis_File();
                showEmphasis(emphasis);
            }
        } else if (e.getSource() == removeCourseButton) {
            Emphasis emphasis = courseController.getSelectedEmphasis();
            if (emphasis != null && coursesList.getSelectedValue() != null) {
                emphasis.getCourses().remove((Course) coursesList.getSelectedValue());
                courseController.save_Emphasis_File();
                showEmphasis(emphasis);
            }
        } else if (e.getSource() == addCourseGroupButton) {
            Emphasis emphasis = courseController.getSelectedEmphasis();
            if (emphasis != null && courseGroupsComboBox.getSelectedItem() != null) {
                if (!(emphasis.getCourseGroups().contains((CourseGroup) courseGroupsComboBox.getSelectedItem()))) emphasis.getCourseGroups().add((CourseGroup) courseGroupsComboBox.getSelectedItem());
                courseController.save_Emphasis_File();
                showEmphasis(emphasis);
            }
        } else if (e.getSource() == removeCourseGroupButton) {
            Emphasis emphasis = courseController.getSelectedEmphasis();
            if (emphasis != null && courseGroupsList.getSelectedValue() != null) {
                emphasis.getCourseGroups().remove((CourseGroup) courseGroupsList.getSelectedValue());
                courseController.save_Emphasis_File();
                showEmphasis(emphasis);
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == emphasisList) {
            if (emphasisList.getSelectedValue() != null && emphasisList.getSelectedValue().getClass().getName().equals("finalprj.Emphasis")) {
                courseController.selectEmphasis((Emphasis) emphasisList.getSelectedValue());
            } else {
                courseController.selectEmphasis(new Emphasis());
            }
        }
    }

    public void courseModelChangePerformed(CourseController controller) {
        Vector<Course> courses = new Vector<Course>(controller.getCourses());
        coursesComboBox.removeAllItems();
        for (int i = 0; i < courses.size(); i++) coursesComboBox.addItem(courses.get(i));
    }

    public void courseGroupModelChangePerformed(CourseController controller) {
        Vector<CourseGroup> courseGroups = new Vector<CourseGroup>(controller.getCourseGroups());
        courseGroupsComboBox.removeAllItems();
        for (int i = 0; i < courseGroups.size(); i++) courseGroupsComboBox.addItem(courseGroups.get(i));
    }

    public void emphasisModelChangePerformed(CourseController controller) {
        emphasisList.setListData(new Vector<Emphasis>(controller.getEmphasis()));
    }

    public void showEmphasis(Emphasis emphasis) {
        if (emphasis != null) {
            nameTextField.setText(emphasis.getName());
            coursesList.setListData(new Vector<Course>(emphasis.getCourses()));
            courseGroupsList.setListData(new Vector<CourseGroup>(emphasis.getCourseGroups()));
        } else {
            nameTextField.setText("");
            coursesList.setListData(new Object[] {});
            courseGroupsList.setListData(new Object[] {});
        }
    }
}
