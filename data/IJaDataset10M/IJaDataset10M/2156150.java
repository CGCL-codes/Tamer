package jTests;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import visualModels.frames.MyPanel;

public class TestDFrame extends JFrame {

    private JPanel contentPane;

    private JTable table;

    private JButton button;

    private JButton button_1;

    private JButton button_2;

    private JScrollPane scrollPane;

    private MyPanel pane;

    private MyPanel pane1;

    private MyPanel pane2;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    TestDFrame frame = new TestDFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
    public TestDFrame() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        pane = new MyPanel("����������������.gif");
        pane.setBounds(181, 8, 101, 94);
        contentPane.add(pane);
        button = new JButton("���");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    drowImage("����������.gif");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                table = new JTable();
                table.setModel(new DefaultTableModel(new Object[][] { { "1", "2", "3" }, { "1", "2", "3" }, { "1", "2", "3" } }, new String[] { "���", "����", "����" }));
                scrollPane.setViewportView(table);
            }
        });
        button.setBounds(335, 11, 89, 23);
        contentPane.add(button);
        button_1 = new JButton("����");
        button_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    drowImage("�������������.gif");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                table = new JTable();
                table.setModel(new DefaultTableModel(new Object[][] { { "11", "22", "33" }, { "11", "22", "33" }, { "11", "22", "33" } }, new String[] { "���", "�����", "�����" }));
                scrollPane.setViewportView(table);
            }
        });
        button_1.setBounds(335, 45, 89, 23);
        contentPane.add(button_1);
        button_2 = new JButton("����");
        button_2.setBounds(335, 79, 89, 23);
        contentPane.add(button_2);
        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 8, 161, 84);
        contentPane.add(scrollPane);
    }

    public void drowImage(String img) throws IOException {
        pane1 = new MyPanel(img);
        pane1.setBounds(181, 8, 101, 94);
        contentPane.add(pane1);
    }
}
