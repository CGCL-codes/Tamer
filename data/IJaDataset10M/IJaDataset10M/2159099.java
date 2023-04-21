package sunlabs.brazil.template;

/**
 * Sample template class for removing all images
 * from a web page, and replacing them with their alt strings.
 * This class is used by the TemplateHandler
 * Each image is replaced by a text string defined by the server property
 * "template", which the first "%" replaced by the contents of the
 * alt attribute.  Defaults to "[&lt;b&gt;%&lt;b&gt;]".
 * <dl class=props>
 * <dt><code>template</code>
 * <dd>The text used to replace the image. The first "%" will contain the image
 * "alt" string, if any.
 * </dl>
 *
 * @author		Stephen Uhler
 * @version		NoImageTemplate.java
 */
public class NoImageTemplate extends Template {

    private String template = "[<b>%</b>]";

    private boolean filterOther;

    private String mine;

    private int index;

    /**
     * Save a reference to our request properties
     * We'll use it some day to tailor the image display
     */
    public boolean init(RewriteContext hr) {
        template = hr.request.props.getProperty(hr.prefix + "template", template);
        filterOther = (null != hr.request.props.getProperty(hr.prefix + "nonlocal", null));
        mine = hr.request.serverUrl();
        index = template.indexOf("%");
        return true;
    }

    /**
     * Convert the html tag img into text using the alt string
     * @param h	The name/value pairs
     *  src=
     *  alt=<...>
     */
    public void tag_img(RewriteContext hr) {
        if (filterOther) {
            String src = hr.get("src", null);
            if (src.indexOf("http://") < 0 || src.indexOf(mine) >= 0) {
                return;
            }
        }
        String alt = hr.get("alt", null);
        if (alt == null) {
            alt = "image";
        }
        if (index >= 0) {
            hr.append(template.substring(0, index) + alt + template.substring(index + 1));
        } else {
            hr.append(template);
        }
    }
}
