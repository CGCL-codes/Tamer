package net.sf.tacos.ajax.components;

import java.text.NumberFormat;
import net.sf.tacos.ajax.AjaxDirectServiceParameter;
import net.sf.tacos.ajax.AjaxWebRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;

/**
 * Generates and renders a progress bar for use with pages that have <b>extremely</b>
 * heavy loads. It is intended that the {@link ProgressWorker} being managed by this component
 * be started as a seperate thread from the page requesting processing. This component
 * will cause the page to be re-loaded every few number of seconds, with the default being
 * 4.
 *
 * @author Jesse Kuhnert
 */
public abstract class ProgressBar extends BaseComponent implements IDirect {

    /** logger */
    private static final Log log = LogFactory.getLog(ProgressBar.class);

    /** gets the worker this component is managing */
    public abstract ProgressWorker getWorker();

    /** sets the worker */
    public abstract void setWorker(ProgressWorker worker);

    /** Gets the value of truncate */
    public abstract int getTruncateLength();

    /** Injected {@link net.sf.tacos.ajax.AjaxDirectService} */
    public abstract IEngineService getAjaxService();

    /** Injected request */
    public abstract AjaxWebRequest getAjaxRequest();

    /** Element to wrap body with */
    public abstract String getElement();

    /** Js object to call when completed */
    public abstract String getOnCompleteObject();

    /**
     * {@inheritDoc}
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        String element = getElement();
        if (!cycle.isRewinding() && element != null) {
            writer.begin(element);
            renderInformalParameters(writer, cycle);
        }
        super.renderComponent(writer, cycle);
        if (!cycle.isRewinding() && element != null) writer.end();
        if (getAjaxRequest().isValidRequest() && getWorker() != null && getWorker().isComplete() && getOnCompleteObject() != null) {
            getAjaxRequest().getResponseBuilder().getScriptWriter().printRaw("<script type=\"text/javascript\">\n" + " if (typeof " + getOnCompleteObject() + " != \"undefined\") {\n" + getOnCompleteObject() + ".progressFinished(\"" + getUpdateId() + "\");\n" + "}\n" + "</script>\n");
        }
    }

    /** Number formatter */
    public NumberFormat getNumberFormat() {
        return NumberFormat.getIntegerInstance();
    }

    /**
     * Used by UI to truncate a long string to a certain length,
     * followed by 3 ellipses.
     * @see StringUtils#abbreviate(java.lang.String, int)
     * @param input
     * @return
     */
    public String truncate(String input) {
        return StringUtils.abbreviate(input, getTruncateLength());
    }

    /**
     * Cancels the threaded run of the currently running
     * task, if selected by the user <i>and</i> if the specified
     * task adheres to the cancel request.
     *
     * @param cycle
     */
    public void cancelTask(IRequestCycle cycle) {
        log.debug("cancelTask()");
        ProgressWorker worker = getWorker();
        if (worker == null || worker.isComplete()) return;
        worker.cancelTask();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStateful() {
        return false;
    }

    /**
     * Called for each refresh of component. Does nothing.
     * {@inheritDoc}
     */
    public void trigger(IRequestCycle cycle) {
        log.debug("trigger worker exists?:" + (getWorker() != null));
        if (getWorker() != null) log.debug("trigger worker complete?:" + (getWorker().isComplete()));
    }

    /**
     * Gets this components unique id, or if available
     * the id specified in the html attribute.
     * @return
     */
    public String getUpdateId() {
        String id = getId();
        if (getBinding("id") != null && getBinding("id").getObject() != null) id = getBinding("id").getObject().toString();
        return id;
    }

    /**
     * Constructs a {@link net.sf.tacos.ajax.AjaxDirectService} link request.
     * @return The fully qualified URL string to call the
     * {@link net.sf.tacos.ajax.AjaxDirectService} with.
     */
    public String getLinkString() {
        return getAjaxService().getLink(false, new AjaxDirectServiceParameter(this, new Object[0], new String[] { getUpdateId() }, false, true)).getAbsoluteURL();
    }
}
