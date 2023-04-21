package npc.gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import npc.kernel.Register;

/**
 * �������������ʾ�Ĵ�����Ϣ<h1><font color = red>(δ���!)</font></h1>
 * <font color = red>1. �޷����column name</font>
 * <font color = red>2. ���û�����ò���ѡ���</font>
 * @author IBM_USER
 *
 */
public class RegTable extends JTable {

    /**
	 * Generated Serial Version UID
	 */
    private static final long serialVersionUID = 3627724218112323749L;

    /**
	 * ���к���Ĵ����ĸ���
	 */
    private final int TABLEWIDE = 8;

    /**
	 * ��������Ĵ����ĸ���
	 */
    private final int TABLEHIGHT = 6;

    /**
	 * ��¼�Ĵ���
	 */
    Register register;

    /**
	 * ���ڱ������ı�ģ��
	 */
    private DefaultTableModel model = new DefaultTableModel(TABLEHIGHT, TABLEWIDE * 2);

    public RegTable() {
        super();
        this.setDragEnabled(false);
        for (int i = 0; i < TABLEHIGHT; i++) {
            for (int j = 0; j < TABLEWIDE; j++) {
                model.setValueAt("R" + (i * TABLEWIDE + j), i, j * 2);
                model.setValueAt(0, i, j * 2 + 1);
            }
        }
        this.setModel(model);
        this.setCellSelectionEnabled(false);
        register = new Register();
        for (int i = 0; i < 32; i++) {
            register.R[i] = 0;
        }
        for (int i = 0; i < 15; i++) {
            register.F[i] = 0;
        }
        refresh();
    }

    /**
	 * ���üĴ���
	 * @param register �Ĵ���
	 */
    void setRegister(Register inputRegister) {
        register = inputRegister;
    }

    /**
	 * ���ڸ��¡�������ı�֮����Ҫ�����������
	 */
    public void refresh() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < TABLEWIDE; j++) {
                model.setValueAt("R" + (i * TABLEWIDE + j), i, j * 2);
                model.setValueAt(register.R[i * TABLEWIDE + j], i, j * 2 + 1);
            }
        }
        for (int i = 0; i < TABLEWIDE; i++) {
            model.setValueAt("F" + i, 4, i * 2);
            model.setValueAt(register.F[i], 4, 2 * i + 1);
        }
        for (int i = 0; i < TABLEWIDE - 1; i++) {
            model.setValueAt("F" + (i + TABLEWIDE), 5, i * 2);
            model.setValueAt(register.F[i + TABLEWIDE], 5, 2 * i + 1);
        }
        model.setValueAt("fState", 5, 2 * TABLEWIDE - 2);
        model.setValueAt(register.fState, 5, 2 * TABLEWIDE - 1);
    }
}
