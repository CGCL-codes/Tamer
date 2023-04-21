package com.gerodp.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import com.gerodp.model.ModelSingleton;
import com.gerodp.view.AppMessagesSingleton;
import com.gerodp.view.MainWindow;
import com.gerodp.view.ScreenEditArticle;

public class SaveArticleAction extends Action {

    private MainWindow mainWindow;

    public SaveArticleAction(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setText(AppMessagesSingleton.getMessage("MenuSaveArticle"));
    }

    public void run() {
        ModelSingleton.getModel().saveCurrentArticle();
    }
}
