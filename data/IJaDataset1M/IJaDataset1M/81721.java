package com.meterware.website;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public abstract class FragmentTemplate {

    public static final String LINE_BREAK = System.getProperty("line.separator");

    private static ArrayList _templates = new ArrayList();

    public abstract FragmentTemplate newFragment();

    public abstract String asText();

    protected abstract String getRootNodeName();

    public static void registerTemplate(FragmentTemplate template) {
        _templates.add(template);
    }

    static FragmentTemplate getTemplateFor(String nodeName) {
        for (int i = 0; i < _templates.size(); i++) {
            FragmentTemplate fragmentTemplate = (FragmentTemplate) _templates.get(i);
            if (fragmentTemplate.getRootNodeName().equals(nodeName)) return fragmentTemplate.newFragment();
        }
        throw new RuntimeException("No template defined for root node " + nodeName);
    }
}
