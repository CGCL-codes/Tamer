package net.sourceforge.xsurvey.xsviewer.views;

import java.util.Map;
import javax.xml.transform.Source;
import net.sourceforge.xsurvey.xsviewer.view.XsltCustomView;

/**
 * Survey View implementation.
 *
 * @author Maciej Pawlik
 */
public class SurveyPage extends XsltCustomView {

    @Override
    protected Source locateSource(Map model) throws Exception {
        Source source = (Source) model.get("data");
        return source;
    }

    @Override
    protected Source getStylesheetSource(Map model) {
        Source source = (Source) model.get("style");
        return source;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        return;
    }
}
