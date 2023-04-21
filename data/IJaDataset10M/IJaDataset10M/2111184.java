package pikes.html.xhtml;

import java.util.LinkedList;
import java.util.List;
import pikes.xml.XMLTag;

/**
 * <code>&lt;body/&gt;</code> tag, see <a href="http://www.w3.org/TR/html401/struct/global.html#h-7.5.1">BODY element</a>
 * in HTML specification.
 * @author Peter Bona
 * @see <a href="http://www.w3.org/TR/html401/struct/global.html#h-7.5.1">The BODY element in HTML specification</a>
 */
public class Body extends NonRootTagFactory implements TagDecorator, CoreAttributes, I18N, Events, BlockList {

    private List<Flow> flows = new LinkedList<Flow>();

    private CharSequence onLoad = null;

    private CharSequence onUnload = null;

    /**
	 * @return "body"
	 */
    @Override
    protected final CharSequence getTagName() {
        return "body";
    }

    public void add(Block block) {
        if (block != null) {
            flows.add(block);
        }
    }

    public void add(Script script) {
        if (script != null) {
            flows.add(script);
        }
    }

    @Override
    protected void decorateCreatedTag(XMLTag tag) {
        if (onLoad != null) {
            tag.addAttribute("onload", onLoad);
        }
        if (onUnload != null) {
            tag.addAttribute("onunload", onUnload);
        }
        for (Flow flow : flows) {
            flow.decorateTag(tag);
        }
    }

    public void setOnLoad(CharSequence onLoad) {
        this.onLoad = onLoad;
    }

    public void setOnUnload(CharSequence onUnload) {
        this.onUnload = onUnload;
    }
}
