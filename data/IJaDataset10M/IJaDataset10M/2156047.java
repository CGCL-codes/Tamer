package at.gp.web.jsf.extval.beanval.label;

import org.apache.myfaces.extensions.validator.util.WebXmlUtils;

/**
 * @author Gerhard Petracek
 */
interface WebXmlParameter {

    static final String PREFIX = "at.gp.web.jsf.extval.label";

    static final String REQUIRED_MARKER = WebXmlUtils.getInitParameter(PREFIX, "REQUIRED_MARKER", true);

    static final String PLACE_MARKER = WebXmlUtils.getInitParameter(PREFIX, "PLACE_MARKER");

    static final String REQUIRED_STYLE_CLASS = WebXmlUtils.getInitParameter(PREFIX, "REQUIRED_STYLE_CLASS");
}
