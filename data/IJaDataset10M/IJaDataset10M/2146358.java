package org.carabiner.examples;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import org.carabiner.CarabinerTestCase;
import org.jmock.Mock;

/**
 * Simple example of the usage of a window harness to test a JFrame.
 *
 * <p> Carabiner Testing Framework</p>
 * <p>Copyright: GNU Public License (http://www.gnu.org/licenses/gpl.html)</p>
 *
 * @author John Januskey (john.januskey@gmail.com)
 */
public class TestMyWindow extends CarabinerTestCase {

    private MyWindow testSubject;

    private Mock mockDataModel;

    public void setUp() throws Exception {
        mockDataModel = new Mock(MyDataModel.class);
        mockDataModel.expects(once()).method("getStringData").will(returnValue("(Baked beans are off)"));
        mockDataModel.expects(once()).method("addListDataListener");
        testSubject = new MyWindow((MyDataModel) mockDataModel.proxy());
        testSubject.getContentPane().add(new JButton("Click Me"), BorderLayout.SOUTH);
    }

    protected Component getComponent() {
        return testSubject;
    }

    public static void main(String[] args) throws Exception {
        TestMyWindow test = new TestMyWindow();
        test.showHarness();
        System.exit(0);
    }
}
