package org.ztemplates.render.script;

import java.util.List;
import org.ztemplates.web.script.css.ZICssPreprocessor;

public interface ZICssProcessor {

    public String computeHtmlTags(List<String> cssExposed, ZICssPreprocessor preprocessor, String ztemplatesCssDigest) throws Exception;
}
