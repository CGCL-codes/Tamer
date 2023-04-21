package com.google.gwt.dom.client;

/**
 * The select element allows the selection of an option.
 * 
 * The contained options can be directly accessed through the select element as
 * a collection.
 * 
 * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#edef-SELECT">W3C HTML Specification</a>
 */
@TagName(SelectElement.TAG)
public class SelectElement extends Element {

    public static final String TAG = "select";

    /**
   * Assert that the given {@link Element} is compatible with this class and
   * automatically typecast it.
   */
    public static SelectElement as(Element elem) {
        assert elem.getTagName().equalsIgnoreCase(TAG);
        return (SelectElement) elem;
    }

    protected SelectElement() {
    }

    /**
   * Add a new element to the collection of OPTION elements for this SELECT.
   * This method is the equivalent of the appendChild method of the Node
   * interface if the before parameter is null. It is equivalent to the
   * insertBefore method on the parent of before in all other cases. This method
   * may have no effect if the new element is not an OPTION or an OPTGROUP.
   * 
   * @param option The element to add
   * @param before The element to insert before, or null for the tail of the
   *          list
   */
    public final void add(OptionElement option, OptionElement before) {
        DOMImpl.impl.selectAdd(this, option, before);
    }

    /**
   * Removes all OPTION elements from this SELECT.
   */
    public final void clear() {
        DOMImpl.impl.selectClear(this);
    }

    /**
   * The control is unavailable in this context.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-disabled">W3C HTML Specification</a>
   * @deprecated use {@link #isDisabled()} instead.
   */
    @Deprecated
    public final native String getDisabled();

    /**
   * Returns the FORM element containing this control. Returns null if this
   * control is not within the context of a form.
   */
    public final native FormElement getForm();

    /**
   * The number of options in this SELECT.
   */
    public final int getLength() {
        return DOMImpl.impl.selectGetLength(this);
    }

    /**
   * If true, multiple OPTION elements may be selected in this SELECT.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-multiple">W3C HTML Specification</a>
   */
    public final native String getMultiple();

    /**
   * Form control or object name when submitted with a form.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-name-SELECT">W3C HTML Specification</a>
   */
    public final native String getName();

    /**
   * The collection of OPTION elements contained by this element.
   */
    public final NodeList<OptionElement> getOptions() {
        return DOMImpl.impl.selectGetOptions(this);
    }

    /**
   * The ordinal index of the selected option, starting from 0. The value -1 is
   * returned if no element is selected. If multiple options are selected, the
   * index of the first selected option is returned.
   */
    public final native int getSelectedIndex();

    /**
   * Number of visible rows.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-size-SELECT">W3C HTML Specification</a>
   */
    public final native int getSize();

    /**
   * The type of this form control. This is the string "select-multiple" when
   * the multiple attribute is true and the string "select-one" when false.
   */
    public final native String getType();

    /**
   * The current form control value (i.e., the value of the currently
   * selected option), if multiple options are selected this is the value of the
   * first selected option.
   */
    public final native String getValue();

    /**
   * The control is unavailable in this context.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-disabled">W3C HTML Specification</a>
   */
    public final native boolean isDisabled();

    /**
   * If true, multiple OPTION elements may be selected in this SELECT.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-multiple">W3C HTML Specification</a>
   */
    public final native boolean isMultiple();

    /**
   * Remove an element from the collection of OPTION elements for this SELECT.
   * Does nothing if no element has the given index.
   * 
   * @param index The index of the item to remove, starting from 0.
   */
    public final void remove(int index) {
        DOMImpl.impl.selectRemoveOption(this, index);
    }

    /**
   * The control is unavailable in this context.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-disabled">W3C HTML Specification</a>
   */
    public final native void setDisabled(boolean disabled);

    /**
   * The control is unavailable in this context.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-disabled">W3C HTML Specification</a>
   */
    public final native void setDisabled(String disabled);

    /**
   * If true, multiple OPTION elements may be selected in this SELECT.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-multiple">W3C HTML Specification</a>
   */
    public final native void setMultiple(boolean multiple);

    /**
   * Form control or object name when submitted with a form.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-name-SELECT">W3C HTML Specification</a>
   */
    public final native void setName(String name);

    /**
   * The ordinal index of the selected option, starting from 0. The value -1 is
   * returned if no element is selected. If multiple options are selected, the
   * index of the first selected option is returned.
   */
    public final native void setSelectedIndex(int index);

    /**
   * Number of visible rows.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-size-SELECT">W3C HTML Specification</a>
   */
    public final native void setSize(int size);

    /**
   * The type of this form control. This is the string "select-multiple" when
   * the multiple attribute is true and the string "select-one" when false.
   */
    public final native void setType(String type);

    /**
   * The current form control value (i.e., the value of the currently
   * selected option), if multiple options are selected this is the value of the
   * first selected option.
   */
    public final native void setValue(String value);
}
