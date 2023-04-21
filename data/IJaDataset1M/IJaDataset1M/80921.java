package com.sksdpt.kioskjui.gui.widget;

import my.jutils.string.Str;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import com.sksdpt.kioskjui.control.config.DSess;
import com.sksdpt.kioskjui.control.config.VSess;

/**
 * ProgressDialogComposite
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class ProgressDialogComposite extends BaseDialogComposite implements IBarUpdater {

    private static Logger logger = Logger.getLogger(ProgressDialogComposite.class);

    public static final int PROGRESS_BAR_HEIGHT = 40;

    protected ProgressBar progressBar;

    private int progressBarStyle;

    private IMyButton btnCancel;

    /**
     * @param parent
     * @param style
     */
    public ProgressDialogComposite(Composite parent, int style, int progressBarStyle) {
        super(parent, style);
        this.progressBarStyle = progressBarStyle;
    }

    public boolean isRunning() {
        if (progressBar == null || progressBar.isDisposed()) {
            logger.info("it was cancelled???");
            return false;
        }
        return true;
    }

    @Override
    public void paintIt() {
        super.paintIt();
        paintProgressBar();
        if (progressBarStyle == SWT.SMOOTH) paintCancel();
    }

    private static I18n i18n = I18nFactory.getI18n(ProgressDialogComposite.class);

    private void paintCancel() {
        int x = getBounds().width / 2;
        int y = VSess.sngltn().getGuiHelper().getYOffset(progressBar) + 30;
        String iCancel = i18n.tr("cancel");
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                ProgressDialogComposite parent = (ProgressDialogComposite) event.widget.getData();
                parent.progressBar.dispose();
            }
        };
        btnCancel = new OneImageButton(this, DSess.sngltn().brdr());
        btnCancel.setText(Str.capFirst(iCancel));
        btnCancel.setImagePath("icons/100x80/cancel.png");
        btnCancel.setData(this);
        btnCancel.setSelectListener(listener);
        btnCancel.setLocation(x, y);
    }

    public void updateBar(final int percent) {
        VSess.sngltn().asyncExec(new Runnable() {

            public void run() {
                if (!isRunning()) return;
                progressBar.setSelection(percent);
            }
        });
    }

    private void paintProgressBar() {
        int sep = DSess.sngltn().getSeparatorSpace();
        int y = lblMsg.getBounds().y + lblMsg.getBounds().height + 2 * sep;
        int x = 4 * sep + lblIcon.getBounds().width;
        int w = getBounds().width - x - 4 * sep;
        int h = PROGRESS_BAR_HEIGHT;
        progressBar = new ProgressBar(this, progressBarStyle);
        progressBar.setBounds(x, y, w, h);
    }
}
