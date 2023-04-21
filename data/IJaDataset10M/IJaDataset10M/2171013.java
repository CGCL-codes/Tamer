package v4view.web;

public class Select extends ChildbearingPageElement implements IFormEvents<Select>, ICoreAttributes, ILanguageAttributes<Select>, IKeyboardEvents<Select>, IMouseEvents<Select> {

    private static final String SELECT_TAG = "select";

    {
        this.setTag(SELECT_TAG);
        this.setSupportsAttributes(true);
    }

    @Override
    public String getCssClass() {
        return this.getAttributeValue(CSS_CLASS_KEY);
    }

    @Override
    public String getId() {
        return this.getAttributeValue(ID_KEY);
    }

    @Override
    public String getStyle() {
        return this.getAttributeValue(STYLE_KEY);
    }

    @Override
    public String getTitle() {
        return this.getAttributeValue(TITLE_KEY);
    }

    @Override
    public Select setCssClass(final String _value) {
        return (Select) this.setAttribute(CSS_CLASS_KEY, _value);
    }

    @Override
    public Select setId(final String _value) {
        return (Select) this.setAttribute(ID_KEY, _value);
    }

    @Override
    public Select setStyle(final String _value) {
        return (Select) this.setAttribute(STYLE_KEY, _value);
    }

    @Override
    public Select setTitle(final String _value) {
        return (Select) this.setAttribute(TITLE_KEY, _value);
    }

    @Override
    public Select setOnKeyDown(final String _scriptlet) {
        return (Select) this.setAttribute(KEY_DOWN_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setOnKeyPress(final String _scriptlet) {
        return (Select) this.setAttribute(KEY_PRESS_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setOnKeyUp(final String _scriptlet) {
        return (Select) this.setAttribute(KEY_UP_EVENT_KEY, _scriptlet);
    }

    @Override
    public String getOnKeyDown() {
        return this.getAttributeValue(KEY_DOWN_EVENT_KEY);
    }

    @Override
    public String getOnKeyPress() {
        return this.getAttributeValue(KEY_PRESS_EVENT_KEY);
    }

    @Override
    public String getOnKeyUp() {
        return this.getAttributeValue(KEY_UP_EVENT_KEY);
    }

    @Override
    public Select setDir(final String _value) {
        return (Select) this.setAttribute(DIR_KEY, _value);
    }

    @Override
    public Select setLang(final String _lang) {
        return (Select) this.setAttribute(LANG_KEY, _lang);
    }

    @Override
    public Select setXmlLang(final String _xmlLang) {
        return (Select) this.setAttribute(XML_LANG_KEY, _xmlLang);
    }

    @Override
    public String getDir() {
        return this.getAttributeValue(DIR_KEY);
    }

    @Override
    public String getLang() {
        return this.getAttributeValue(LANG_KEY);
    }

    @Override
    public String getXmlLang() {
        return this.getAttributeValue(XML_LANG_KEY);
    }

    @Override
    public Select setOnClick(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSECLICK_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setOnDblClick(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEDBLCLICK_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setOnMouseDown(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEDOWN_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setOnMouseMove(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEMOVE_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setMouseOut(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEOUT_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setMouseOver(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEOVER_EVENT_KEY, _scriptlet);
    }

    @Override
    public Select setMouseUp(final String _scriptlet) {
        return (Select) this.setAttribute(ON_MOUSEUP_EVENT_KEY, _scriptlet);
    }

    @Override
    public String getOnClick() {
        return this.getAttributeValue(ON_MOUSECLICK_EVENT_KEY);
    }

    @Override
    public String getOnDblClick() {
        return this.getAttributeValue(ON_MOUSEDBLCLICK_EVENT_KEY);
    }

    @Override
    public String getOnMouseDown() {
        return this.getAttributeValue(ON_MOUSEDOWN_EVENT_KEY);
    }

    @Override
    public String getOnMouseMove() {
        return this.getAttributeValue(ON_MOUSEMOVE_EVENT_KEY);
    }

    @Override
    public String getMouseOut() {
        return this.getAttributeValue(ON_MOUSEOUT_EVENT_KEY);
    }

    @Override
    public String getMouseOver() {
        return this.getAttributeValue(ON_MOUSEOVER_EVENT_KEY);
    }

    @Override
    public String getMouseUp() {
        return this.getAttributeValue(ON_MOUSEUP_EVENT_KEY);
    }

    @Override
    public Select setOnBlur(final String _value) {
        return (Select) this.setAttribute(ON_BLUR_KEY, _value);
    }

    @Override
    public Select setOnChange(final String _value) {
        return (Select) this.setAttribute(ON_CHANGE_KEY, _value);
    }

    @Override
    public Select setOnFocus(final String _value) {
        return (Select) this.setAttribute(ON_FOCUS_KEY, _value);
    }

    @Override
    public Select setOnSelect(final String _value) {
        return (Select) this.setAttribute(ON_SELECT, _value);
    }

    @Override
    public String getOnBlur() {
        return this.getAttributeValue(ON_BLUR_KEY);
    }

    @Override
    public String getOnChange() {
        return this.getAttributeValue(ON_CHANGE_KEY);
    }

    @Override
    public String getOnFocus() {
        return this.getAttributeValue(ON_FOCUS_KEY);
    }

    @Override
    public String getOnSelect() {
        return this.getAttributeValue(ON_SELECT);
    }

    @Override
    public Select addPageElement(final PageElement _element) {
        if (_element instanceof Option || _element instanceof OptGroup) {
            super.addPageElement(_element);
        }
        return this;
    }
}
