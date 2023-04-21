package jsp.tags.dapact.lookup;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import jsp.tags.dapact.conf.UserClassFactory;

/**
 * Looks up the value by looking in the parameter list in the request, then the contexts.
 * @todo rewrite to make one parameter driven class that uses properties as default.
 */
public class LookupValueByParamContext extends LookupValueByParam {

    /**
   * Default constructor...
   */
    public LookupValueByParamContext() {
    }

    /**
   * This function will be called by the tags when they want to lookup a general
   * non-String object - in this case, it returns the value of a HTTP request parameter
   * or if that is null it looks in the contexts (page, request, session, and then application [servlet]).
   *
   * @param key the key of the value to be saved.
   * @param value the value to be added - this will be overridden by what is returned by
   *   this function.
   * @param tag the tag that will be used to save the value.
   * @param pc the page context associated with the tag that is also a parameter. This
   *   will be used to search parameters and contexts (page, session, request, and servlet).
   *
   * @return the lookedup value or null if not found.
   */
    public Object lookupValue(String key, Object value, TagSupport tag, PageContext pc) {
        Object result = super.lookupValue(key, value, tag, pc);
        if (result == null) {
            if ((key != null) && (pc != null)) {
                result = pc.findAttribute(key);
            }
        }
        return result;
    }

    /**
   * This function will be called by the tags when they want to lookup a string object
   * (most common case: storing attributes) - in this case, it returns the value of a HTTP request parameter
   * or if that is null it looks in the contexts (page, request, session, and then application [servlet]).
   *
   * @param key the key of the value to be saved.
   * @param value the value to be added - this will be overridden by what is returned by
   *   this function.
   * @param tag the tag that will be used to save the value.
   * @param pc the page context associated with the tag that is also a parameter. This
   *   will be used to search parameters and contexts (page, session, request, and servlet).
   *
   * @return the lookedup value.
   */
    public String lookupValue(String key, String value, TagSupport tag, PageContext pc) {
        String result = super.lookupValue(key, value, tag, pc);
        if (result == null) {
            if ((key != null) && (pc != null)) {
                try {
                    result = (String) pc.findAttribute(key);
                } catch (ClassCastException e) {
                    UserClassFactory.getLogger().log("The lookup value for the following name is not a String: " + key, e);
                }
            }
        }
        return result;
    }
}
