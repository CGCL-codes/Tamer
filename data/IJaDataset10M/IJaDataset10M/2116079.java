package org.gwanted.gwt.jswidget.progressbar.client.ui;

import java.util.Map;
import org.gwanted.gwt.core.client.ui.AbstractComposite;
import org.gwanted.gwt.core.client.util.ParameterDictionary;
import org.gwanted.gwt.core.client.util.ParameterHelper;
import org.gwanted.gwt.widget.progressbar.client.ui.ProgressBar;
import com.google.gwt.user.client.Element;

/**
 * Widget for progress bar, a line graph that shows current progress of a task.
 * If progress is unknown (-1) a generic image is shown instead.
 *
 * @author Joshua Hewitt aka Sposh
 * @author Miguel A. Rager
 *
 * @gwt.class visibility="public" package="org.gwanted.gwt.jswidget.progressbar.client.ui"
 * @gwt.runtime-code
 */
public class ProgressBarJS extends AbstractComposite {

    private ProgressBar progressBar;

    private int barNumber = 20;

    private String text = "";

    private boolean showTimeRemaining = false;

    private boolean showOnFinish = false;

    private String unknownProgImgUrl = "";

    /**
   * Constructor with no parameters. Required for GWT.
   */
    public ProgressBarJS() {
    }

    public ProgressBarJS(final Element container, final Map params) {
        super(container, params);
        this.barNumber = ParameterHelper.getInt(params, ParameterDictionary.BAR_NUMBER, this.barNumber);
        this.text = ParameterHelper.getString(params, ParameterDictionary.TEXT, this.text);
        this.showTimeRemaining = ParameterHelper.getBoolean(params, ParameterDictionary.SHOW_TIME_REMAINING, this.showTimeRemaining);
        this.showOnFinish = ParameterHelper.getBoolean(params, ParameterDictionary.SHOW_ON_FINISH, this.showOnFinish);
        this.unknownProgImgUrl = ParameterHelper.getString(params, ParameterDictionary.UNKNOW_PROGRESS_IMAGE, this.unknownProgImgUrl);
        int options = (showTimeRemaining ? org.gwtwidgets.client.ui.ProgressBar.SHOW_TIME_REMAINING : 0) + (showOnFinish ? org.gwtwidgets.client.ui.ProgressBar.SHOW_TEXT : 0);
        progressBar = new ProgressBar(barNumber, text, options, unknownProgImgUrl);
        initWidget(progressBar);
    }

    /**
   * Constructor with esential parameters, using default values for the rest.
   *
   * @param unknownProgImgUrl
   *            Image to show if progress is "unknown" (ie, -1)
   */
    public ProgressBarJS(final String unknownProgImgUrl) {
        super();
        this.unknownProgImgUrl = unknownProgImgUrl;
    }

    /**
   * @param progress
   *            The percentage of the progress completed (-1 for unknown)
   *
   * @gwt.method visibility="public"
   */
    public final void setProgress(final int progress) {
        progressBar.setProgress(progress);
    }

    /**
   * @return The percentage of the progress completed (-1 for unknown)
   *
   * @gwt.method visibility="public"
   */
    public final int getProgress() {
        return progressBar.getProgress();
    }

    /**
   * @return The text displayed before the widget
   *
   * @gwt.method visibility="public"
   */
    public final String getText() {
        return this.text;
    }
}
