package org.quantum.error.code.ui;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author sunkai
 * @version 09-03-27
 * 
 */
public class InputValueDialog extends TitleAreaDialog {

    /**
	 * 
	 */
    private Label comp;

    /**
	 * 
	 */
    private Display display;

    /**
	 * 
	 */
    private Button okay;

    /**
	 * 
	 */
    private Button conc;

    /**
	 * 
	 */
    private Text inputNumText;

    /**
	 * 
	 */
    private Label inputNum;

    /**
	 * 
	 */
    private Text inputKText;

    /**
	 * 
	 */
    private Text inputRText;

    /**
	 * 
	 */
    private Text inputNText;

    /**
	 * 
	 */
    private Label inputK;

    /**
	 * 
	 */
    private Label inputR;

    /**
	 * 
	 */
    protected Object result;

    /**
	 * 
	 */
    protected Shell shell;

    /**
	 * 
	 */
    protected Label inputN;

    /**
	 * 
	 */
    private int n;

    /**
	 * 
	 */
    private int r;

    /**
	 * 
	 */
    private int k;

    /**
	 * 
	 */
    private int num;

    /**
	 * @param parent
	 */
    public InputValueDialog(Shell parent) {
        super(parent);
    }

    /**
	 * @param parent
	 * @return
	 */
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, 0);
        GridLayout layout = new GridLayout(2, true);
        GridData data = new GridData();
        data.grabExcessVerticalSpace = false;
        data.verticalAlignment = 3;
        container.setLayout(layout);
        container.setLayoutData(data);
        this.inputN = new Label(container, 0);
        this.inputN.setText("����N: ");
        this.inputNText = new Text(container, 2048);
        this.inputNText.setSize(100, 20);
        this.inputNText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                n = Integer.parseInt(inputNText.getText().trim());
            }
        });
        this.inputR = new Label(container, 0);
        this.inputR.setText("����R: ");
        this.inputRText = new Text(container, 2048);
        this.inputRText.setSize(100, 20);
        this.inputRText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                r = Integer.parseInt(inputRText.getText().trim());
            }
        });
        this.inputK = new Label(container, 0);
        this.inputK.setText("����K: ");
        this.inputKText = new Text(container, 2048);
        this.inputKText.setSize(100, 20);
        this.inputKText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                k = Integer.parseInt(inputKText.getText().trim());
            }
        });
        this.inputNum = new Label(container, 0);
        this.inputNum.setText("����1�ĸ���: ");
        this.inputNumText = new Text(container, 2048);
        this.inputNumText.setSize(100, 20);
        this.inputNumText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                num = Integer.parseInt(inputNumText.getText().trim());
            }
        });
        setTitle("�����������Ϣ:");
        setMessage("�����������Ϣ:");
        return container;
    }

    /**
	 * @return
	 */
    public int getN() {
        return this.n;
    }

    /**
	 * @param n
	 */
    public void setN(int n) {
        this.n = n;
    }

    /**
	 * @return
	 */
    public int getR() {
        return this.r;
    }

    /**
	 * @param r
	 */
    public void setR(int r) {
        this.r = r;
    }

    /**
	 * @return
	 */
    public int getK() {
        return this.k;
    }

    /**
	 * @param k
	 */
    public void setK(int k) {
        this.k = k;
    }

    /**
	 * @return
	 */
    public int getNum() {
        return this.num;
    }

    /**
	 * @param num
	 */
    public void setNum(int num) {
        this.num = num;
    }
}
