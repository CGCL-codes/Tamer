package spacetrader.gui;

import java.awt.Point;
import java.util.Arrays;
import jwinforms.Button;
import jwinforms.EventArgs;
import jwinforms.EventHandler;
import jwinforms.FormSize;
import jwinforms.ISupportInitialize;
import jwinforms.Label;
import jwinforms.NumericUpDown;
import jwinforms.WinformForm;
import jwinforms.enums.DialogResult;
import jwinforms.enums.FlatStyle;
import jwinforms.enums.FormBorderStyle;
import jwinforms.enums.FormStartPosition;
import spacetrader.Functions;
import spacetrader.Strings;

public class FormGetLoan extends WinformForm {

    private Button btnOk;

    private Button btnMax;

    private Button btnNothing;

    private Label lblQuestion;

    private Label lblStatement;

    private NumericUpDown numAmount;

    public FormGetLoan(int max) {
        InitializeComponent();
        numAmount.setMaximum(max);
        numAmount.setValue(numAmount.getMinimum());
        lblStatement.setText(Functions.StringVars("You can borrow up to ^1.", Functions.Multiples(max, Strings.MoneyUnit)));
    }

    private void InitializeComponent() {
        lblQuestion = new Label();
        numAmount = new NumericUpDown();
        btnOk = new Button();
        btnMax = new Button();
        btnNothing = new Button();
        lblStatement = new Label();
        ((ISupportInitialize) (numAmount)).BeginInit();
        SuspendLayout();
        lblQuestion.setAutoSize(true);
        lblQuestion.setLocation(new Point(8, 24));
        lblQuestion.setName("lblQuestion");
        lblQuestion.setSize(new FormSize(178, 13));
        lblQuestion.setTabIndex(3);
        lblQuestion.setText("How much do you want to borrow?");
        numAmount.setLocation(new Point(184, 22));
        numAmount.setMaximum(99999);
        numAmount.setMinimum(1);
        numAmount.setName("numAmount");
        numAmount.setSize(new FormSize(58, 20));
        numAmount.setTabIndex(1);
        numAmount.ThousandsSeparator = true;
        numAmount.setValue(88888);
        btnOk.setDialogResult(DialogResult.OK);
        btnOk.setFlatStyle(FlatStyle.Flat);
        btnOk.setLocation(new Point(52, 48));
        btnOk.setName("btnOk");
        btnOk.setSize(new FormSize(41, 22));
        btnOk.setTabIndex(2);
        btnOk.setText("Ok");
        btnMax.setDialogResult(DialogResult.OK);
        btnMax.setFlatStyle(FlatStyle.Flat);
        btnMax.setLocation(new Point(100, 48));
        btnMax.setName("btnMax");
        btnMax.setSize(new FormSize(41, 22));
        btnMax.setTabIndex(3);
        btnMax.setText("Max");
        btnMax.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnMax_Click();
            }
        });
        btnNothing.setDialogResult(DialogResult.Cancel);
        btnNothing.setFlatStyle(FlatStyle.Flat);
        btnNothing.setLocation(new Point(148, 48));
        btnNothing.setName("btnNothing");
        btnNothing.setSize(new FormSize(53, 22));
        btnNothing.setTabIndex(4);
        btnNothing.setText("Nothing");
        lblStatement.setLocation(new Point(8, 8));
        lblStatement.setName("lblStatement");
        lblStatement.setSize(new FormSize(189, 13));
        lblStatement.setTabIndex(5);
        lblStatement.setText("You can borrow up to 88,888 credits.");
        setAcceptButton(btnOk);
        setAutoScaleBaseSize(new FormSize(5, 13));
        setCancelButton(btnNothing);
        setClientSize(new FormSize(252, 79));
        setControlBox(false);
        Controls.addAll(Arrays.asList(lblStatement, btnNothing, btnMax, btnOk, numAmount, lblQuestion));
        setFormBorderStyle(FormBorderStyle.FixedDialog);
        setName("FormGetLoan");
        setShowInTaskbar(false);
        setStartPosition(FormStartPosition.CenterParent);
        setText("Get Loan");
        ((ISupportInitialize) (numAmount)).EndInit();
        ResumeLayout(false);
    }

    private void btnMax_Click() {
        numAmount.setValue(numAmount.getMaximum());
    }

    public int Amount() {
        return numAmount.getValue();
    }
}
