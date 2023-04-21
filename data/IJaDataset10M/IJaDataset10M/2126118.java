package cz.cvut.felk.cig.jcool.experiment;

import cz.cvut.felk.cig.jcool.core.FunctionDetail;
import cz.cvut.felk.cig.jcool.core.OptimizationMethodDetail;
import cz.cvut.felk.cig.jcool.solver.demo.TestFunction;
import cz.cvut.felk.cig.jcool.solver.demo.TestMethod;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ytoh
 */
public class Boot {

    public static void main(String[] args) {
        final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
        DefaultListModel functionModel = new DefaultListModel();
        functionModel.addElement(new FunctionDetail(new TestFunction()));
        DefaultListModel methodModel = new DefaultListModel();
        methodModel.addElement(new OptimizationMethodDetail(new TestMethod()));
        ((JExpandList) context.getBean("functionList")).setListModel(functionModel);
        ((JExpandList) context.getBean("methodList")).setListModel(methodModel);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ((JCoolView) context.getBean("mainFrame")).getFrame().setVisible(true);
            }
        });
    }
}
