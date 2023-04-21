package org.encog.workbench.tabs.query.general;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.encog.EncogError;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.workbench.EncogWorkBench;
import org.encog.workbench.dialogs.error.ErrorDialog;
import org.encog.workbench.frames.document.tree.ProjectEGFile;
import org.encog.workbench.models.NetworkQueryModel;
import org.encog.workbench.tabs.EncogCommonTab;

public class RegressionQueryTab extends EncogCommonTab implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JTable inputTable;

    private JTable outputTable;

    private int inputCount;

    private int outputCount;

    private JButton calculateButton;

    public RegressionQueryTab(final ProjectEGFile data) {
        super(data);
        this.inputCount = getData().getInputCount();
        this.outputCount = getData().getOutputCount();
        this.setSize(640, 480);
        this.setLayout(new BorderLayout());
        final JPanel body = new JPanel();
        body.setLayout(new GridLayout(1, 2, 10, 10));
        this.add(body, BorderLayout.CENTER);
        final JPanel left = new JPanel();
        final JPanel right = new JPanel();
        body.add(left);
        body.add(right);
        left.setLayout(new BorderLayout());
        right.setLayout(new BorderLayout());
        left.add(new JLabel("Input"), BorderLayout.NORTH);
        right.add(new JLabel("Output"), BorderLayout.NORTH);
        left.add(this.inputTable = new JTable(new NetworkQueryModel(this.inputCount, 2)), BorderLayout.CENTER);
        right.add(this.outputTable = new JTable(new NetworkQueryModel(this.outputCount, 2)), BorderLayout.CENTER);
        this.add(this.calculateButton = new JButton("Calculate"), BorderLayout.SOUTH);
        this.outputTable.setEnabled(false);
        for (int i = 1; i <= this.inputCount; i++) {
            this.inputTable.setValueAt("Input " + i + ":", i - 1, 0);
            this.inputTable.setValueAt("0.0", i - 1, 1);
        }
        for (int i = 1; i <= this.outputCount; i++) {
            this.outputTable.setValueAt("Output " + i + ":", i - 1, 0);
            this.outputTable.setValueAt("0.0", i - 1, 1);
        }
        this.calculateButton.addActionListener(this);
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.calculateButton) {
            try {
                setDirty(true);
                final BasicMLData input = new BasicMLData(this.inputCount);
                for (int i = 0; i < this.inputCount; i++) {
                    double value = 0;
                    final String str = (String) this.inputTable.getValueAt(i, 1);
                    try {
                        value = Double.parseDouble(str);
                    } catch (final NumberFormatException e2) {
                        EncogWorkBench.displayError("Data Error", "Please enter a valid input number.");
                    }
                    input.setData(i, value);
                }
                final MLData output = getData().compute(input);
                for (int i = 0; i < this.outputCount; i++) {
                    this.outputTable.setValueAt(output.getData(i), i, 1);
                }
            } catch (EncogError ex) {
                EncogWorkBench.displayError("Query Error", ex.getMessage());
            } catch (Throwable t) {
                ErrorDialog.handleError(t, this.getEncogObject(), null);
            }
        }
    }

    public MLRegression getData() {
        return (MLRegression) ((ProjectEGFile) getEncogObject()).getObject();
    }

    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public String getName() {
        return "Reg :" + this.getEncogObject().getName();
    }
}
