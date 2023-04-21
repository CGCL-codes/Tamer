package eulergui.util;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;

/**
 * @author Jean-Marc Vanel
 *
 */
public class ClassLauncherAction extends AbstractAction {

    private Class<?> launchedClass;

    private String[] arguments;

    public ClassLauncherAction() {
        launchedClass = n3_project.ProjectGUI.class;
    }

    public ClassLauncherAction(Class<?> launchedClass, String[] args, String label) {
        this.launchedClass = launchedClass;
        this.arguments = args;
        setLabel(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Method m = launchedClass.getMethod("main", String[].class);
            m.invoke(null, (Object) arguments);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void setLabel(String label) {
        putValue(NAME, label);
    }

    public Class<?> getLaunchedClass() {
        return launchedClass;
    }

    public void setLaunchedClass(Class<?> launchedClass) {
        this.launchedClass = launchedClass;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public void setArgument(String argument) {
        this.arguments = new String[] { argument };
    }
}
