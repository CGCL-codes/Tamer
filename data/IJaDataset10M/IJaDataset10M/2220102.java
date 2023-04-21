package com._pmz0178.blogtxt.swing.util;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Message Showing
 * @author pasha
 *
 * @date 20080223
 */
public final class MessageUtil {

    private MessageUtil() {
    }

    public static void showMessageDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    public static void showMessageDialog(Object message) {
        showMessageDialog(getRootFrame(), message);
    }

    public static void showErrorDialog(final Object message) {
        showErrorDialog(getRootFrame(), message);
    }

    public static void showErrorDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(Component parentComponent, Throwable e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(Throwable e) {
        showErrorDialog(getRootFrame(), e);
    }

    public static int showOKCancelDialog(final Component parentComponent, final String messageParam, final String titleParam) {
        return JOptionPane.showConfirmDialog(parentComponent, messageParam, titleParam, JOptionPane.OK_CANCEL_OPTION);
    }

    public static int showOKCancelDialog(final String messageParam, final String titleParam) {
        return showOKCancelDialog(getRootFrame(), messageParam, titleParam);
    }

    /**
	 * @return
	 */
    private static JFrame getRootFrame() {
        return (JFrame) BlogBeanFactory.getBean(BlogBeanFactory.MAIN_FRAME);
    }
}
