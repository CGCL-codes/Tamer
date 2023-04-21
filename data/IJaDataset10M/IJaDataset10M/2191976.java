package org.servingMathematics.mqat.ui.WizardEditor.VariableTypePanel;

import java.text.NumberFormat;
import javax.swing.*;
import org.imsglobal.qti.dom.Expression;
import org.imsglobal.qti.dom.ExpressionEnum;
import org.imsglobal.qti.dom.RandomFloat;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.servingMathematics.qti.domImpl.ElementImpl;
import org.servingMathematics.qti.domImpl.ExpressionImpl;
import org.servingMathematics.qti.domImpl.RandomFloatImpl;

/**
 * TODO Class description
 * 
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.1
 */
public class RandomFloatPanel extends AbstractVariableTypePanel {

    private JLabel lblMin;

    private JFormattedTextField edtMax;

    private JLabel lblMax;

    private JFormattedTextField edtMin;

    /**
    * Auto-generated main method to display this 
    * JPanel inside a new JFrame.
    */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new RandomFloatPanel(null));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public RandomFloatPanel(Expression expression) {
        super(expression);
        initGUI();
    }

    private void initGUI() {
        FormLayout thisLayout = new FormLayout("max(p;5px), max(p;5px), 4dlu, 26dlu, max(p;10px), 1dlu, 15dlu, max(p;10px), 23dlu, 5dlu", "5dlu, 15dlu, 5dlu");
        this.setLayout(thisLayout);
        this.setPreferredSize(new java.awt.Dimension(200, 42));
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        lblMin = new JLabel();
        add(lblMin, new CellConstraints("2, 2, 1, 1, fill, fill"));
        lblMin.setText("Min");
        edtMin = new JFormattedTextField(numberFormat);
        add(edtMin, new CellConstraints("4, 2, 1, 1, default, default"));
        lblMax = new JLabel();
        add(lblMax, new CellConstraints("7, 2, 1, 1, fill, fill"));
        lblMax.setText("Max");
        edtMax = new JFormattedTextField(numberFormat);
        add(edtMax, new CellConstraints("9, 2, 1, 1, default, default"));
        updateContent();
    }

    protected void updateContent() {
        RandomFloat randomFloat = new RandomFloatImpl((ExpressionImpl) expression);
        this.setDoubleBuffered(true);
        edtMin.setText("" + randomFloat.getMin());
        edtMax.setText("" + randomFloat.getMax());
    }

    public Expression getExpression() {
        RandomFloat randomFloat = new RandomFloatImpl((ElementImpl) ExpressionImpl.createExpression((ElementImpl) expression, ExpressionEnum.RANDOMFLOAT));
        randomFloat.setMin(Double.parseDouble(edtMin.getText()));
        randomFloat.setMax(Double.parseDouble(edtMax.getText()));
        return randomFloat;
    }
}
