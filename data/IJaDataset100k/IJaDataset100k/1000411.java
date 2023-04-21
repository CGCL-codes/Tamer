package druid.dialogs.dtselector;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.GuiUtil;
import org.dlib.gui.ROTextField;
import org.dlib.gui.TButton;
import org.dlib.gui.TDialog;
import org.dlib.gui.TLabel;
import druid.core.DataTypeLib;
import druid.data.FieldNode;

public class DataTypeSelector extends TDialog implements ActionListener {

    private JTextField txtType = new ROTextField(20);

    private FKeyPanel fkeyPanel = new FKeyPanel();

    private final String ACTION_SET = "set";

    private final String ACTION_DONE = "done";

    private FieldNode fieldNode;

    private TButton tbSet = new TButton("Change", ACTION_SET, this);

    private TButton tbDone = new TButton("Done", ACTION_DONE, this);

    private SetDialog setDlg;

    public DataTypeSelector(Frame frame) {
        super(frame, "DataType Selector", true);
        Container cp = getContentPane();
        JPanel p = new JPanel();
        FlexLayout flexL = new FlexLayout(4, 2, 4, 4);
        flexL.setColProp(1, FlexLayout.EXPAND);
        flexL.setRowProp(1, FlexLayout.EXPAND);
        p.setLayout(flexL);
        p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        p.add("0,0", new TLabel("Type"));
        p.add("1,0,x,", txtType);
        p.add("2,0", tbSet);
        p.add("3,0", tbDone);
        p.add("0,1,x,c,4", fkeyPanel);
        cp.add(p);
    }

    public void run(FieldNode node) {
        fieldNode = node;
        if (!node.isFkey()) {
            handleSet();
            if (!node.isFkey()) return;
        }
        txtType.setText(DataTypeLib.getTypeDef(fieldNode));
        fkeyPanel.refresh(fieldNode);
        fkeyPanel.setVisible(true);
        showDialog();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(ACTION_SET)) handleSet();
        if (command.equals(ACTION_DONE)) handleDone();
    }

    private void handleSet() {
        if (setDlg == null) setDlg = new SetDialog(GuiUtil.getFrame(this));
        if (setDlg.run(fieldNode.getDatabase())) {
            if (setDlg.isFkeySelected()) {
                fieldNode.attrSet.setInt("type", 0);
                fieldNode.attrSet.setInt("refTable", setDlg.getId1());
                fieldNode.attrSet.setInt("refField", setDlg.getId2());
            } else {
                fieldNode.attrSet.setInt("type", setDlg.getId1());
                fieldNode.attrSet.setInt("refTable", 0);
                fieldNode.attrSet.setInt("refField", 0);
                setVisible(false);
            }
            txtType.setText(DataTypeLib.getTypeDef(fieldNode));
        }
    }

    private void handleDone() {
        this.setVisible(false);
        this.dispose();
    }
}
