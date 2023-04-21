package GUI;

import javax.swing.JPanel;
import Data.*;

public class Method extends AttributeMethod {

    public Method(String text, int type, ClassFrame classFrame) {
        super(text, type, classFrame);
    }

    public Method(Method a, ClassFrame classFrame) {
        super(a, classFrame);
    }

    public void save() {
        Data.Class c = classFrame.getClassData();
        c.addMethod(nameTextField.getText());
    }

    public void deleteButton_Clicked() {
        Data.Class c = classFrame.getClassData();
        if (nameTextField.getName() != null) {
            c.removeMethod(nameTextField.getName());
        }
        JPanel tmp = (JPanel) me.getParent();
        tmp.remove(me);
        tmp.updateUI();
        MainFrame.isSaved = false;
        MainFrame.isLogged = false;
    }
}
