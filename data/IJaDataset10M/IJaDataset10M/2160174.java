package org.intellij.plugins.junit4.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.log4j.Logger;
import org.intellij.plugins.junit4.JavaFile;
import org.intellij.plugins.junit4.JavaMethod;
import org.intellij.plugins.junit4.SelectedFile;

/**
 * @author tomichj
 */
public class ExpectExceptionAction extends AnAction {

    private final Logger log = Logger.getLogger("CONSOLE-WARN");

    public void actionPerformed(AnActionEvent e) {
        ActionEvent event = new ActionEvent(e);
        JavaMethod method = event.getSelectedMethod();
        if (method == null) return;
        if (!method.isTest()) return;
        method.annotateTest();
    }

    private String askUserForExceptionClass() {
        return "java.lang.Object";
    }
}
