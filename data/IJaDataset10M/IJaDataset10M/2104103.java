package com.anlugifa.editor;

import com.anlugifa.editor.base.EditorLog;
import com.anlugifa.editor.ui.JEditor;

public class Editor implements Thread.UncaughtExceptionHandler {

    public Editor() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread t, Throwable e) {
        EditorLog.thrown(getClass(), "uncaughtException", e);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }
}
