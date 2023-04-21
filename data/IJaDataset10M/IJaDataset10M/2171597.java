package net.sf.ngrease.core.ast;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public interface ElementList {

    public static final ElementList EMPTY_LIST = new Builder.EmptyList();

    public static class Builder {

        private static class EmptyList extends AbstractList implements ElementList {

            private static class EmptyIterator implements Iterator, ElementIterator {

                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    return nextElement();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public Element nextElement() throws NoSuchElementException {
                    throw new NoSuchElementException("No next in empty iterator!");
                }
            }

            private static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

            public Object get(int index) {
                return getElement(index);
            }

            public int size() {
                return 0;
            }

            public Iterator iterator() {
                return EMPTY_ITERATOR;
            }

            public ElementIterator elementIterator() {
                return EMPTY_ITERATOR;
            }

            public Element getElement(int index) {
                throw new NoSuchElementException("No element at index " + index + " in an empty list");
            }

            public ElementList subElementList(int fromIndex, int toIndex) {
                throw new IndexOutOfBoundsException("No subElementList for an empty list");
            }
        }

        private static class ElementListSingletonImpl extends AbstractList implements ElementList {

            /**
			 * TODO if this isn't really faster than ElementArrayIterator, use
			 * it and remove this class
			 */
            private static class SingletonIterator implements ElementIterator {

                private Element nextElement;

                public SingletonIterator(Element element) {
                    this.nextElement = element;
                }

                public boolean hasNext() {
                    return nextElement != null;
                }

                public Element nextElement() throws NoSuchElementException {
                    Element retval = nextElement;
                    if (retval == null) {
                        throw new NoSuchElementException();
                    }
                    nextElement = null;
                    return retval;
                }
            }

            private final Element element;

            public ElementListSingletonImpl(Element element) {
                this.element = element;
            }

            public Object get(int index) {
                return getElement(index);
            }

            public int size() {
                return 1;
            }

            public ElementIterator elementIterator() {
                return new SingletonIterator(element);
            }

            public Element getElement(int index) {
                if (index != 0) {
                    throw new NoSuchElementException("A singleton list has no element at index " + index);
                }
                return element;
            }

            public ElementList subElementList(int fromIndex, int toIndex) {
                if (fromIndex == 0) {
                    switch(toIndex) {
                        case 0:
                            return EMPTY_LIST;
                        case 1:
                            return this;
                        default:
                            break;
                    }
                }
                throw new ArrayIndexOutOfBoundsException("");
            }
        }

        private static class ElementListArrayImpl extends AbstractList implements ElementList {

            private class ElementArrayIterator implements ElementIterator {

                private int nextIndex = 0;

                public boolean hasNext() {
                    return nextIndex < size();
                }

                public Element nextElement() throws NoSuchElementException {
                    return getElement(nextIndex++);
                }
            }

            private final Element[] elements;

            private final int size;

            public ElementListArrayImpl(Element[] elements, int size) {
                this.elements = elements;
                this.size = size;
            }

            public Object get(int index) {
                return getElement(index);
            }

            public int size() {
                return size;
            }

            public ElementIterator elementIterator() {
                return new ElementArrayIterator();
            }

            public Element getElement(int index) {
                if (index < 0 || size <= index) {
                    throw new NoSuchElementException("No element at " + index);
                }
                return elements[index];
            }

            public ElementList subElementList(int fromIndex, int toIndex) {
                int length = toIndex - fromIndex;
                if (fromIndex < 0 || fromIndex >= size || toIndex > size || length < 0) {
                    throw new IndexOutOfBoundsException("");
                }
                Element[] copy = new Element[length];
                System.arraycopy(elements, fromIndex, copy, 0, length);
                return new ElementListArrayImpl(copy, length);
            }
        }

        private Element[] elements;

        private int size = 0;

        private Builder() {
            elements = new Element[10];
        }

        private Builder(int estimatedSize) {
            elements = new Element[estimatedSize];
        }

        public static Builder with() {
            return new Builder();
        }

        public static Builder with(int estimatedSize) {
            return new Builder(estimatedSize);
        }

        public static ElementList buildSingleton(Element element) {
            return new ElementListSingletonImpl(element);
        }

        public ElementList build() {
            if (size == 0) {
                return EMPTY_LIST;
            }
            return new ElementListArrayImpl(elements, size);
        }

        public Builder element(Element element) {
            ensureCapacity(1);
            elements[size++] = element;
            return this;
        }

        public Builder elements(List elements) {
            ensureCapacity(elements.size());
            for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                this.elements[size++] = element;
            }
            return this;
        }

        public Builder elements(ElementList elements) {
            ensureCapacity(elements.size());
            for (ElementIterator iter = elements.elementIterator(); iter.hasNext(); ) {
                Element element = iter.nextElement();
                this.elements[size++] = element;
            }
            return this;
        }

        private void ensureCapacity(int numberOfNewElements) {
            int needed = size + numberOfNewElements;
            if (elements.length < needed) {
                Element[] newElements = new Element[(needed * 3) / 2 + 1];
                System.arraycopy(elements, 0, newElements, 0, size);
                elements = newElements;
            }
        }
    }

    ElementIterator elementIterator();

    Element getElement(int index);

    ElementList subElementList(int fromIndex, int toIndex);

    int size();

    boolean isEmpty();
}
