package org.isakiev.xl.view.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.isakiev.xl.view.sheet.SheetAppearance;

/**
 * Expression editor
 * 
 * @author Ruslan Isakiev
 */
public class ExpressionEditor extends JComponent {

    private static final long serialVersionUID = 1L;

    private List<ExpressionChangeListener> listeners = new ArrayList<ExpressionChangeListener>();

    private JLabel addressLabel;

    private JTextField expressionTextField;

    private String expression;

    public ExpressionEditor(SheetAppearance appearance) {
        Border border = new CompoundBorder(new EmptyBorder(1, 1, 1, 1), new LineBorder(appearance.getHeaderLineColor()));
        addressLabel = new JLabel();
        Dimension addressLabelDimension = new Dimension(150, 20);
        addressLabel.setPreferredSize(addressLabelDimension);
        addressLabel.setMaximumSize(addressLabelDimension);
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        addressLabel.setVerticalAlignment(SwingConstants.CENTER);
        addressLabel.setBorder(border);
        addressLabel.setFont(appearance.getFont());
        addressLabel.setBackground(appearance.getHeaderBackgroundColor());
        expressionTextField = new JTextField();
        expressionTextField.setDocument(new LimitedLengthDocument(10000));
        expressionTextField.setBorder(new CompoundBorder(border, new EmptyBorder(0, 2, 0, 2)));
        expressionTextField.setFont(appearance.getFont());
        expressionTextField.setForeground(appearance.getCellFontColor());
        expressionTextField.setDisabledTextColor(appearance.getCellFontColor());
        expressionTextField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                processKeyPressed();
            }
        });
        setLayout(new BorderLayout());
        add(addressLabel, BorderLayout.WEST);
        add(expressionTextField, BorderLayout.CENTER);
    }

    public void setExpression(String address, String expression) {
        this.expression = expression;
        addressLabel.setText(address);
        expressionTextField.setText(expression);
    }

    public String getExpression() {
        return expression;
    }

    public void addExpressionChangeListener(ExpressionChangeListener listener) {
        listeners.add(listener);
    }

    public void removeExpressionChangeListener(ExpressionChangeListener listener) {
        listeners.remove(listener);
    }

    private void fireExpressionChanged(String expression) {
        for (ExpressionChangeListener listener : listeners) {
            listener.expressionChanged(expression);
        }
    }

    private void processKeyPressed() {
        String newExpression = expressionTextField.getText();
        if (newExpression.trim().length() == 0) {
            newExpression = null;
        }
        if (expression == null) {
            if (newExpression != null) {
                expression = newExpression;
                fireExpressionChanged(expression);
            }
        } else {
            if (!expression.equals(newExpression)) {
                expression = newExpression;
                fireExpressionChanged(expression);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        expressionTextField.setEnabled(enabled);
    }
}
