package com.mainatom.apt.uidb.forms;

import com.mainatom.ui.*;
import com.mainatom.utils.*;

/**
 * worflow для ui операций
 */
public class ActUiWorkflow extends AAction {

    protected void onClick() throws Exception {
        exec();
    }

    protected void exec() {
        try {
            onExec();
        } catch (Exception e) {
            throw new MfErrorWrap(e);
        }
    }

    /**
     * Для перекрытия функционала
     */
    protected void onExec() throws Exception {
    }

    public void save() {
        try {
            onSave();
        } catch (Exception e) {
            throw new MfErrorWrap(e);
        }
    }

    /**
     * Реализация записи. Вызывается фреймом, который редактирует запись.
     *
     * @throws Exception
     */
    protected void onSave() throws Exception {
    }

    public void refresh() {
        try {
            onRefresh();
        } catch (Exception e) {
            throw new MfErrorWrap(e);
        }
    }

    /**
     * Реализация обновления списка.
     *
     * @throws Exception
     */
    protected void onRefresh() throws Exception {
    }

    public boolean checkEnable() {
        try {
            return onCheckEnable();
        } catch (Exception e) {
            throw new MfErrorWrap(e);
        }
    }

    /**
     * Реализация проверки на разрешенность операции
     *
     * @throws Exception
     */
    protected boolean onCheckEnable() throws Exception {
        return true;
    }
}
