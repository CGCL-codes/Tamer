package sunlabs.brazil.template;

import sunlabs.brazil.util.Format;

/**
 * Template class for performing ${...} substitutions inside
 * javascript and style tags.
 * This class is used by the TemplateHandler
 * <p>
 * A new attribute <code>eval</code> is defined for the <code>script</code>
 * and <code>style</code> tags.
 * If <code>eval</code> is present, any ${...} constructs are evaluated in the
 * body of the "script" or "style".
 * <p>
 * If the attribute <code>esc</code> is true, then strings of the form
 * "\X" are replaced as per {@link sunlabs.brazil.util.Format}.  Otherwise
 * "\X" is treated specially only for X = $, to escape variable
 * substitution.
 * <p>
 * Both "eval" and "esc" attributes are removed from the "script" or "style"
 * tags.
 *
 * @author		Stephen Uhler
 * @version		@(#)ScriptEvalTemplate.java	2.2
 */
public class ScriptEvalTemplate extends Template {

    public void tag_script(RewriteContext hr) {
        if (hr.isTrue("eval")) {
            boolean noesc = !hr.isTrue("esc");
            hr.remove("eval");
            hr.remove("esc");
            hr.nextToken();
            hr.append(Format.subst(hr.request.props, hr.getBody(), noesc));
        }
    }

    public void tag_style(RewriteContext hr) {
        tag_script(hr);
    }
}
