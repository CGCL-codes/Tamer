package com.intellij.tutorial.helloWorld;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.ContentManagerUtil;

public class SayHelloAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        ContentManager contentManager = ContentManagerUtil.getContentManagerFromContext(e.getDataContext(), false);
        Application application = ApplicationManager.getApplication();
        HelloWorldApplicationComponent helloWorldComponent = application.getComponent(HelloWorldApplicationComponent.class);
        helloWorldComponent.sayHello();
        if (contentManager != null) {
            contentManager.addContent(ContentFactory.SERVICE.getInstance().createContent(helloWorldComponent.createComponent(), "POPS", false));
        }
    }
}
