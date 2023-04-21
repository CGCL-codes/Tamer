package com.lubq.lm.util;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * shellウィンドウツールクラス 1、ウィンドウをスクリーンの真中に置く 2、ウィンドウを全スクリーンまで拡大する。
 * 
 * @author wenyi <jhf@bestwiz.cn>
 * 
 * @copyright 2006, BestWiz(Dalian) Co.,Ltd
 * @version $Id: ShellUtil.java,v 1.3 2007/04/14 07:56:37 liyan Exp $
 */
public class ShellUtil {

    /**
     * Shellを設定する
     * 
     * @param shell
     */
    public static void centerShell(Shell shell) {
        Display d = shell.getDisplay();
        Rectangle mBounds = d.getPrimaryMonitor().getBounds();
        Rectangle sBounds = shell.getBounds();
        int centerX = mBounds.x + (mBounds.width - sBounds.width) / 2;
        int centerY = mBounds.y + (mBounds.height - sBounds.height) / 2;
        shell.setLocation(centerX, centerY);
    }

    /**
     * Textを設定する
     * 
     * @param shell
     * @param txt
     */
    public static void setText(Shell shell, String txt) {
        shell.setText(StringUtils.trimToEmpty(txt));
    }

    /**
     * Shellを設定する
     * 
     * @param shell
     */
    public static void setFullScreen(final Shell shell) {
        int width = shell.getDisplay().getBounds().width;
        int height = shell.getDisplay().getBounds().height - 30;
        shell.setLocation(0, 0);
        shell.setSize(width, height);
    }
}
