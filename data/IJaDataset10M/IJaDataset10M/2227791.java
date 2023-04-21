package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.util.RojacUtils;
import javax.swing.*;

/**
 * @author xBlackCat
 */
public abstract class ASwingThreadedHandler<T> implements IResultHandler<T> {

    @Override
    public void process(final T data) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    execute(data);
                } catch (Exception e) {
                    RojacUtils.showExceptionDialog(e);
                }
            }
        });
    }

    protected abstract void execute(T data);
}
