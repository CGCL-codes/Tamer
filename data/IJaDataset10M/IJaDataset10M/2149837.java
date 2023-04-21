package painter.app.ui.genview;

import genetic.component.method.Method;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Calvin Ashmore
 */
public class MethodPanel extends JPanel {

    private String methodName;

    private Method method;

    public MethodPanel(String methodName, Method method) {
        this.methodName = methodName;
        this.method = method;
        setLayout(new BorderLayout());
        JLabel label = new JLabel(methodLine());
        add(label, BorderLayout.NORTH);
        StatementListPanel slPanel = new StatementListPanel(method.getBody());
        add(slPanel, BorderLayout.CENTER);
    }

    private String methodLine() {
        StringBuffer sb = new StringBuffer();
        sb.append("method " + methodName + "(");
        for (int i = 0; i < method.getNumberArguments(); i++) {
            String argumentName = method.getArgumentName(i);
            Class argumentType = method.getArgumentType(i);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(argumentType.getSimpleName() + " " + argumentName);
        }
        sb.append(")");
        return sb.toString();
    }
}
