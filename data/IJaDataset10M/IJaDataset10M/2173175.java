package wheel.components;

import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;
import java.util.*;
import wheel.WheelException;

/**
 * Base class for all components that will render themselves as xhtml-tags. Other, more specialized components may want to
 * implement the IRenderable interface directly. Has some basic support methods for rendering components.
 * <br/>
 * <b>Rendering hints:</b><br/>
 * Rendering hints 
 *
 * @author Henri Frilund
 */
public abstract class RenderableComponent extends Component {

    static final Map<String, String> HTML = new HashMap<String, String>();

    static {
        HTML.put("h1", "h1");
        HTML.put("h2", "h2");
        HTML.put("h3", "h3");
        HTML.put("h4", "h4");
        HTML.put("h5", "h5");
        HTML.put("h6", "h6");
        HTML.put("i", "i");
        HTML.put("b", "b");
        HTML.put("ol", "ol");
        HTML.put("ul", "ul");
        HTML.put("dl", "dl");
        HTML.put("li", "li");
        HTML.put("dt", "dt");
        HTML.put("dd", "dd");
        HTML.put("br", "br");
        HTML.put("col", "col");
        HTML.put("head", "head");
        HTML.put("body", "body");
        HTML.put("link", "link");
        HTML.put("title", "title");
        HTML.put("thead", "thead");
        HTML.put("tbody", "tbody");
        HTML.put("tfoot", "tfoot");
        HTML.put("caption", "caption");
        HTML.put("colgroup", "colgroup");
        HTML.put("tr", "tr");
        HTML.put("th", "th");
        HTML.put("td", "td");
        HTML.put("label", "label");
        HTML.put("script", "script");
        HTML.put("abbr", "abbr");
        HTML.put("acronym", "acronym");
        HTML.put("address", "address");
        HTML.put("area", "area");
        HTML.put("base", "base");
        HTML.put("bdo", "bdo");
        HTML.put("big", "big");
        HTML.put("blockquote", "blockquote");
        HTML.put("cite", "cite");
        HTML.put("code", "code");
        HTML.put("del", "del");
        HTML.put("dfn", "dfn");
        HTML.put("frame", "frame");
        HTML.put("frameset", "frameset");
        HTML.put("hr", "hr");
        HTML.put("iframe", "iframe");
        HTML.put("kbd", "kbd");
        HTML.put("legend", "legend");
        HTML.put("ins", "ins");
        HTML.put("map", "map");
        HTML.put("meta", "meta");
        HTML.put("noframes", "noframes");
        HTML.put("noscript", "noscript");
        HTML.put("object", "object");
        HTML.put("optgroup", "optgroup");
        HTML.put("p", "p");
        HTML.put("param", "param");
        HTML.put("pre", "pre");
        HTML.put("q", "q");
        HTML.put("samp", "samp");
        HTML.put("small", "small");
        HTML.put("strong", "strong");
        HTML.put("style", "style");
        HTML.put("sub", "sub");
        HTML.put("textarea", "textarea");
        HTML.put("tt", "tt");
        HTML.put("var", "var");
        HTML.put("div", "div");
        HTML.put("span", "span");
        HTML.put("fieldset", "fieldset");
        HTML.put("option", "option");
        HTML.put("em", "em");
        HTML.put("input", "input");
        HTML.put("select", "select");
        HTML.put("button", "button");
        HTML.put("s", "s");
        HTML.put("strike", "strike");
        HTML.put("sup", "sup");
        HTML.put("u", "u");
    }

    private List<RenderableComponent> renderBeforeMe;

    private List<RenderableComponent> renderAfterMe;

    boolean renderId = true;

    boolean renderSelf = true;

    boolean useXhtml = true;

    boolean visible = true;

    private RenderableComponentConfiguration config;

    protected RenderableComponent(String componentId) {
        super(componentId);
    }

    protected RenderableComponent(Component parent) {
        super(parent);
        if (parent instanceof RenderableComponent) {
            RenderableComponent renderableComponent = (RenderableComponent) parent;
            if (!renderableComponent.useXhtml) useXhtml = false;
        }
    }

    protected RenderableComponent(Component parent, String componentId) {
        super(parent, componentId);
        if (parent instanceof RenderableComponent) {
            RenderableComponent renderableComponent = (RenderableComponent) parent;
            if (!renderableComponent.useXhtml) useXhtml = false;
        }
    }

    protected RenderableComponent(Component parent, String componentId, CharSequence renderHint) {
        super(parent, componentId, renderHint);
        if (parent instanceof RenderableComponent) {
            RenderableComponent renderableComponent = (RenderableComponent) parent;
            if (!renderableComponent.useXhtml) useXhtml = false;
        }
    }

    /**
     * All renderables added y calling this method will be rendered before the component itself but the renderables will not be added to the component tree.
     * @param renderable
     * @return
     */
    public Component addRenderBefore(Component renderable) {
        if (renderBeforeMe == null) renderBeforeMe = new LinkedList<RenderableComponent>();
        if (renderable instanceof RenderableComponent) renderBeforeMe.add((RenderableComponent) renderable); else throw new WheelException("addRenderBefore expects an instance of RenderableComponent.", this);
        return this;
    }

    /**
     * All renderables added y calling this method will be rendered after the component itself but the renderables will not be added to the component tree.
     * @param renderable
     * @return
     */
    public Component addRenderAfter(Component renderable) {
        if (renderAfterMe == null) renderAfterMe = new LinkedList<RenderableComponent>();
        if (renderable instanceof RenderableComponent) renderAfterMe.add((RenderableComponent) renderable); else throw new WheelException("addRenderAfter expects an instance of RenderableComponent.", this);
        return this;
    }

    public List<RenderableComponent> _getRenderBeforeMe() {
        return renderBeforeMe;
    }

    public List<RenderableComponent> _getRenderAfterMe() {
        return renderAfterMe;
    }

    /**
     * Default implementation. Handles render before/after logic. Will first figure out the xhtml tag to use from rendering hints, then delegates to renderComponent().
     * Subclasses should override renderComponent() rather than this method.
     * @param serializer
     * @throws IOException
     */
    public void _render(XmlSerializer serializer) throws IOException {
        if (!visible && !getEngine().isAjaxRequest()) return;
        if (renderBeforeMe != null) {
            for (RenderableComponent renderable : renderBeforeMe) {
                renderable._render(serializer);
            }
        }
        String usedTagName = "";
        if (renderSelf) {
            usedTagName = _renderTagStart(serializer);
            _renderActions(serializer);
        }
        renderComponent(serializer);
        if (renderSelf) serializer.endTag("", usedTagName);
        if (renderAfterMe != null) {
            for (RenderableComponent renderable : renderAfterMe) {
                renderable._render(serializer);
            }
        }
    }

    /**
     * Subclasses must implement this. If no xhtml tagname can be resolved from rendering hints, will default to value returned by this method.
     * @return
     */
    public abstract String defaultTagName();

    /**
     * Default implementation that will render all renderable child components that this component has. Subclasses should override this method if necessary.
     * @param serializer
     * @throws IOException
     */
    public void renderComponent(XmlSerializer serializer) throws IOException {
        for (RenderableComponent renderable : _getRenderableChildren()) {
            renderable._render(serializer);
        }
    }

    /**
     * Renders the tag start based on the rendering hints.
     * @param serializer
     * @return The tagname that was rendered.
     * @throws IOException
     */
    public String _renderTagStart(XmlSerializer serializer) throws IOException {
        String tagName = getTagName();
        if (tagName == null) throw new IllegalStateException("Component " + getComponentId() + " must have a defined tag name.");
        serializer.startTag("", tagName);
        if (renderId) serializer.attribute("", "id", getComponentId());
        _renderXhtmlAttributes(serializer);
        _renderClassReferenceFromHints(serializer);
        return tagName;
    }

    /**
     * Resolves the contents of the class-attribute for this tag from rendering hints.
     * @return
     */
    public String _classReferenceFromHints() {
        StringBuffer classRef = new StringBuffer();
        for (int i = 0; i < _getRenderHints().size(); i++) {
            String s = (String) _getRenderHints().get(i);
            if (i == 0 && HTML.containsKey(s)) {
                continue;
            }
            classRef.append(eval(s));
            if (i < (_getRenderHints().size() - 1)) classRef.append(" ");
        }
        if (_getRenderHints().size() == 0 && internalRenderHints != null) {
            for (int i = 0; i < internalRenderHints.size(); i++) {
                String hint = internalRenderHints.get(i);
                classRef.append(hint);
                if (i < (_getRenderHints().size() - 1)) classRef.append(" ");
            }
        }
        if (classRef.length() > 0) return classRef.toString();
        return null;
    }

    /**
     * Renders the class-attribute for this tag based on rendering hints.
     * @param serializer
     * @throws IOException
     */
    public void _renderClassReferenceFromHints(XmlSerializer serializer) throws IOException {
        if (!useXhtml) return;
        String classRef = _classReferenceFromHints();
        if (classRef != null) serializer.attribute("", "class", classRef);
    }

    /**
     * Resolves tag name for this component based on rendering hints.
     * @return
     */
    public String getTagName() {
        String tagName = defaultTagName();
        List<CharSequence> hints = _getRenderHints();
        if (hints != null) {
            if (useXhtml) {
                for (CharSequence hint : hints) {
                    if (HTML.containsKey(hint)) return hint.toString();
                }
            } else {
                if (!hints.isEmpty()) return hints.get(0).toString();
            }
        }
        return tagName;
    }

    /**
     * Renders all xhtml-attributes (given with calls to attribute() and attributes() methods).
     * @param serializer
     * @throws IOException
     */
    public void _renderXhtmlAttributes(XmlSerializer serializer) throws IOException {
        if (_getXhtmlAttributes() == null || _getXhtmlAttributes().isEmpty()) return;
        for (String name : _getXhtmlAttributes().keySet()) {
            String value = _getXhtmlAttributes().get(name);
            serializer.attribute("", name, value);
        }
    }

    public void _renderActions(XmlSerializer serializer) throws IOException {
        if (_getActions() == null) return;
        for (ActionExpression action : _getActions()) {
            String domEvent = action.getEvent();
            if (domEvent == null) domEvent = defaultDomEvent();
            if (domEvent.equals("onclick") && !(this instanceof FormElement)) serializer.attribute("", "style", "cursor:pointer");
            if (_getForm(false) != null) {
                if (action.getUpdateTargetId() != null) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("$('#" + action.getUpdateTarget().getComponentId() + "').load('" + action.getActionUrl() + "'");
                    if (action.isSubmitForm()) {
                        sb.append(", {");
                        Set<FormElement> formElements = _getForm(false).getFormElements();
                        for (Iterator formElementIterator = formElements.iterator(); formElementIterator.hasNext(); ) {
                            FormElement formElement = (FormElement) formElementIterator.next();
                            sb.append(formElement.getComponentId() + ": $('#" + formElement.getComponentId() + "').val()");
                            if (formElementIterator.hasNext()) sb.append(", ");
                        }
                        sb.append("});");
                    } else sb.append(");");
                    serializer.attribute("", domEvent, sb.toString());
                } else {
                    if (action.isSubmitForm()) {
                        if (!_getForm(false).renderSelf) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("document.getElementById('").append(_getVisibleForm(true).getComponentId()).append("').action='").append(action.getActionUrl()).append("';");
                            sb.append("document.getElementById('").append(_getVisibleForm(true).field("wheelSubmitId").getComponentId()).append("').value='").append(_getForm(false).getComponentId()).append("';");
                            if (!(this instanceof Submit)) sb.append("document.getElementById('").append(_getVisibleForm(true).getComponentId()).append("').submit();");
                            serializer.attribute("", domEvent, sb.toString());
                        } else {
                            if (!(this instanceof Submit)) serializer.attribute("", domEvent, "this.form.submit();");
                        }
                    } else {
                        serializer.attribute("", domEvent, "window.location.href='" + action.getActionUrl() + "';");
                    }
                }
                if (action.getJavascript() != null) {
                    serializer.attribute("", domEvent, action.getJavascript());
                }
            } else {
                if (action.getUpdateTargetId() != null) {
                    serializer.attribute("", domEvent, action.getUpdateComponentFunctionCall());
                } else {
                    serializer.attribute("", domEvent, "window.location.href='" + action.getActionUrl() + "';");
                }
            }
        }
    }

    protected String defaultDomEvent() {
        return "onclick";
    }

    @Override
    public synchronized void _clear() {
        super._clear();
        if (renderBeforeMe != null) renderBeforeMe.clear();
        if (renderAfterMe != null) renderAfterMe.clear();
    }

    public RenderableComponentConfiguration config() {
        if (config == null) config = new RenderableComponentConfiguration(this);
        return config;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        if (getTagName() != null) sb.append(getTagName()); else sb.append(defaultTagName());
        sb.append(" ");
        sb.append("id=\"").append(getComponentId()).append("\"");
        if (_getXhtmlAttributes() != null && !_getXhtmlAttributes().isEmpty()) {
            for (String name : _getXhtmlAttributes().keySet()) {
                String value = _getXhtmlAttributes().get(name);
                sb.append(name).append("=\"").append(value).append("\"");
                sb.append(" ");
            }
        }
        if (_classReferenceFromHints() != null) sb.append("class=\"").append(_classReferenceFromHints()).append("\"");
        sb.append("/>");
        return sb.toString();
    }
}
