package de.mguennewig.pobjform;

import java.util.*;
import de.mguennewig.pobjects.metadata.ArrayEntry;
import de.mguennewig.pobjects.metadata.FormEntry;

/** Abstract array element.
 *
 * @author Michael G�nnewig
 */
public abstract class AbstractArrayElement extends AbstractValueElement implements ArrayElement, Iterable<FormElement> {

    private final List<FormElement> elements;

    /** Creates a new abstract array element. */
    public AbstractArrayElement(final PObjForm form, final ArrayEntry entry) {
        super(form, entry);
        this.elements = new ArrayList<FormElement>();
    }

    /** {@inheritDoc} */
    @Override
    public final void setProperty(final String property) {
        super.setProperty(property);
        final String childName = getChildEntry().getName();
        for (int i = 0; i < elements.size(); i++) getElement(i).setProperty(property + "[" + i + "]." + childName);
    }

    /** {@inheritDoc}
   *
   * @return always <code>false</code>;
   */
    public final boolean isMandatory() {
        return false;
    }

    /** {@inheritDoc}
   *
   * @return always <code>false</code>;
   */
    public boolean isVisible() {
        return false;
    }

    public void setVisible(final boolean visible) {
        for (final FormElement element : elements) element.setVisible(visible);
    }

    /** {@inheritDoc} */
    @Override
    public void appendToList(final List<FormElement> list) {
        super.appendToList(list);
        for (final FormElement element : elements) element.appendToList(list);
    }

    /** {@inheritDoc} */
    public final boolean hasChanged() {
        final Iterator<FormElement> i = iterator();
        while (i.hasNext()) {
            if (i.next().hasChanged()) return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    public final void validate() {
        final Iterator<FormElement> i = iterator();
        while (i.hasNext()) i.next().validate();
    }

    /** Returns the current object.
   *
   * @return A {@link List} of the values of the child elements.  The list is
   *   empty if the child elements are not {@link ValueElement}s.
   */
    public final List<Object> getObject() {
        final List<Object> current = new ArrayList<Object>(getNumElements());
        for (final FormElement element : this) {
            if (element instanceof ValueElement) current.add(((ValueElement) element).getObject()); else break;
        }
        return current;
    }

    /** Sets the current object to the new value.
   *
   * @param obj Must be a {@link List} that specifies the values for the child
   *   elements.
   * @see #setObject(List)
   */
    @SuppressWarnings("unchecked")
    public final void setObject(final Object obj) {
        setObject((List<Object>) obj);
    }

    /** Returns the initial object.
   *
   * @return A {@link List} of the values of the child elements.  The list is
   *   empty if the child elements are not {@link ValueElement}s.
   */
    public final List<Object> getInitialObject() {
        final List<Object> initial = new ArrayList<Object>(getNumElements());
        for (final FormElement element : this) {
            if (element instanceof ValueElement) initial.add(((ValueElement) element).getInitialObject()); else break;
        }
        return initial;
    }

    /** {@inheritDoc} */
    public final void setCurrentAsInitialState() {
        for (final FormElement element : this) {
            if (element instanceof ValueElement) ((ValueElement) element).setCurrentAsInitialState(); else break;
        }
    }

    /** {@inheritDoc} */
    public final String getInitialString() {
        throw new Error("Invalid call!");
    }

    /** {@inheritDoc} */
    public final String getCurrentString() {
        throw new Error("Invalid call!");
    }

    /** {@inheritDoc} */
    public final FormEntry getChildEntry() {
        return ((ArrayEntry) getEntry()).getChildEntry();
    }

    /** Sets the current objects to the new values from the list.
   *
   * <p>If the list contains more entries then element in the array, new
   * elements for these values will be appended to the array element.</p>
   *
   * @param list A {@link List} that specifies the values for the child
   *   elements.
   */
    protected void setObject(final List<Object> list) {
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            final FormElement element;
            if (i < getNumElements()) element = getElement(i); else element = addElement();
            if (element instanceof ValueElement) ((ValueElement) element).setObject(list.get(i));
        }
    }

    /** {@inheritDoc} */
    public final FormElement addElement() {
        return addElement(getNumElements());
    }

    /** {@inheritDoc} */
    public FormElement addElement(final int index) {
        final FormElement element = getForm().createFormElement(getChildEntry());
        elements.add(index, element);
        setProperty(getProperty());
        return element;
    }

    /** {@inheritDoc} */
    public void clear() {
        elements.clear();
    }

    /** {@inheritDoc} */
    public final FormElement getElement(final int i) {
        return elements.get(i);
    }

    /** {@inheritDoc} */
    public final int getNumElements() {
        return elements.size();
    }

    /** {@inheritDoc} */
    public final Iterator<FormElement> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    /** {@inheritDoc} */
    public void removeAllElements() {
        elements.clear();
    }

    /** {@inheritDoc} */
    public void removeElement(final int index) {
        elements.remove(index);
        setProperty(getProperty());
    }
}
