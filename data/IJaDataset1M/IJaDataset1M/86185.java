package hotel.form.context;

import hotel.form.main.MainFrame;
import hotel.service.CommandService;
import hotel.service.dingroom.DingRoomServiceCommand;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AllQuery extends BasePanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public JButton btnInsert, btnDelete, btnUpdate, allDingRoomInfo, inUse, btnSelect2;

    public JComboBox jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5, jComboBox6;

    public JTextField jTextField1, jTextField2, jTextField3;

    public JTextArea jTextArea1;

    public JTable tabShow;

    public AllQuery(MainFrame parent) {
        super(parent);
        tabShow = new JTable();
        refresh();
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(tabShow), BorderLayout.CENTER);
        this.add(new SouthPanel(), BorderLayout.SOUTH);
        this.parent.setTitle("万能查询");
        this.setVisible(true);
    }

    private class SouthPanel extends JPanel implements ActionListener {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public SouthPanel() {
            super(new FlowLayout());
            allDingRoomInfo = new JButton("查询所有订房信息");
            inUse = new JButton("使用房间");
            btnSelect2 = new JButton("已退房间");
            this.add(allDingRoomInfo);
            this.add(inUse);
            this.add(btnSelect2);
            allDingRoomInfo.addActionListener(this);
            inUse.addActionListener(this);
            btnSelect2.addActionListener(this);
            allDingRoomInfo.setActionCommand("select");
            inUse.setActionCommand("select1");
            btnSelect2.setActionCommand("select2");
        }

        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            if (str.equals("select")) {
                refresh();
            }
            if (str.equals("select1")) {
                DefaultTableModel dingRoom = new DefaultTableModel((Object[][]) CommandService.getInstance().execute(new DingRoomServiceCommand(DingRoomServiceCommand.getUnCheckOutDingRoomAsArrayCommand())), hotel.model.dingroom.DingRoom.getFieldMapLabel().values().toArray());
                tabShow.setModel(dingRoom);
            }
            if (str.equals("select2")) {
                DefaultTableModel dingRoom = new DefaultTableModel((Object[][]) CommandService.getInstance().execute(new DingRoomServiceCommand(DingRoomServiceCommand.getCheckOutDingRoomAsArrayCommand())), hotel.model.dingroom.DingRoom.getFieldMapLabel().values().toArray());
                tabShow.setModel(dingRoom);
            }
        }
    }

    public void access(MainFrame vistor) {
        vistor.visit(this);
    }

    @Override
    public void refresh() {
        DefaultTableModel dingRoom = new DefaultTableModel((Object[][]) CommandService.getInstance().execute(new DingRoomServiceCommand(DingRoomServiceCommand.getAllAsArrayCommand())), hotel.model.dingroom.DingRoom.getFieldMapLabel().values().toArray());
        tabShow.setModel(dingRoom);
    }
}
