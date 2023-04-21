package noteBookProject.UI;

import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import noteBookProject.DAO.Book;
import noteBookProject.DAO.Records;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DropMode;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTextPane;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Frame;
import javax.swing.JScrollBar;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class DefineRecords extends JFrame {

    private JPanel contentPane;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    DefineRecords frame = new DefineRecords();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the frame.
	 */
    public DefineRecords() {
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setTitle("Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 585, 485);
        contentPane = new JPanel();
        contentPane.setAutoscrolls(true);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JLabel lblBookName = new JLabel("Book Name:");
        lblBookName.setBounds(21, 11, 72, 23);
        contentPane.add(lblBookName);
        JLabel lblAuthor = new JLabel("Author");
        lblAuthor.setBounds(21, 54, 46, 14);
        contentPane.add(lblAuthor);
        final JLabel lblAuthorName = new JLabel("New label");
        lblAuthorName.setBounds(183, 54, 114, 14);
        contentPane.add(lblAuthorName);
        final JComboBox cmbBxBook = new JComboBox();
        cmbBxBook.setBounds(183, 11, 238, 23);
        cmbBxBook.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                String bookName = cmbBxBook.getSelectedItem().toString();
                ArrayList<String> aList = book.selectAuthorNameAndId(bookName);
                String authorName = aList.get(0).toString();
                bookId = Integer.parseInt(aList.get(1));
                lblAuthorName.setText(authorName);
            }
        });
        fillBookCmb(cmbBxBook);
        contentPane.add(cmbBxBook);
        JLabel lblPageNo = new JLabel("Page No:");
        lblPageNo.setBounds(22, 94, 57, 14);
        contentPane.add(lblPageNo);
        txtFieldPageNo = new JTextField();
        txtFieldPageNo.setBounds(185, 91, 46, 20);
        contentPane.add(txtFieldPageNo);
        txtFieldPageNo.setColumns(10);
        JLabel lblRecordExplanation = new JLabel("Record Explanation:");
        lblRecordExplanation.setBounds(21, 136, 114, 23);
        contentPane.add(lblRecordExplanation);
        JButton btnSave = new JButton("Save");
        btnSave.setBounds(184, 385, 167, 35);
        btnSave.setFont(new Font("Traditional Arabic", Font.BOLD | Font.ITALIC, 16));
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Records record = new Records();
                record.setBookId(bookId);
                record.setPageNo(Integer.parseInt(txtFieldPageNo.getText()));
                record.setRecordExplanation(txtAreaRecordExp.getText());
                record.setRecordTime(recordDate);
                String strMsg = record.insertRecord();
                JOptionPane.showMessageDialog(contentPane, strMsg);
            }
        });
        contentPane.add(btnSave);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(183, 136, 278, 160);
        contentPane.add(scrollPane);
        txtAreaRecordExp = new JTextArea();
        scrollPane.setViewportView(txtAreaRecordExp);
        txtAreaRecordExp.setLineWrap(true);
        txtAreaRecordExp.setDropMode(DropMode.INSERT);
        txtAreaRecordExp.setRows(2);
        txtAreaRecordExp.setColumns(2);
        txtAreaRecordExp.setWrapStyleWord(true);
        txtAreaRecordExp.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtAreaRecordExp.scrollRectToVisible(getBounds());
        txtAreaRecordExp.setAutoscrolls(true);
    }

    private void fillBookCmb(JComboBox combo) {
        ArrayList<String> ls = book.selectAllBook();
        for (int i = 0; i < ls.size(); i++) {
            combo.addItem(ls.get(i));
        }
    }

    java.sql.Date recordDate = new java.sql.Date(new java.util.Date().getTime());

    Book book = new Book();

    private Integer bookId;

    private JTextField txtFieldPageNo;

    private JTextArea txtAreaRecordExp;

    protected void initDataBindings() {
    }
}
