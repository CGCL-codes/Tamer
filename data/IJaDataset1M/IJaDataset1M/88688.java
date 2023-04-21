package com.wwg.market.ui.code.client;

/**
 * Created by IntelliJ IDEA.
 *
 * @author gaoyang
 * @version $Id: $
 * @date 11-12-22 下午11:34
 */
public class CodeFormFactory {

    private static CodeForm editCodeForm = null;

    private static ViewCodeForm viewCodeForm = null;

    public static CodeForm getEditCodeForm() {
        if (editCodeForm == null) {
            editCodeForm = new CodeForm();
            editCodeForm.setTitle("编辑代码");
        }
        return editCodeForm;
    }

    public static ViewCodeForm getViewCodeForm() {
        if (viewCodeForm == null) {
            viewCodeForm = new ViewCodeForm();
            viewCodeForm.setTitle("查看代码");
        }
        return viewCodeForm;
    }
}
