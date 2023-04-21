package net.sf.gwtruts.client.mvc.controller;

import java.util.Map;
import net.sf.gwtruts.client.mvc.ModelAndView;

/**
 *
 * @author Reza Alavi
 */
public class ViewResolver extends NameResolver {

    @Override
    public ModelAndView handleRequest(Map post, Map sharedModel) {
        return new ModelAndView(formatName(getContext().getName()));
    }
}
