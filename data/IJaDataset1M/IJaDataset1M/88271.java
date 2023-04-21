package org.databene.benerator.composite;

import org.databene.benerator.Generator;
import org.databene.commons.Mutator;
import org.databene.commons.UpdateFailedException;

/**
 * {@link ComponentBuilder} implementation which builds array elements.<br/><br/>
 * Created: 30.04.2010 09:57:50
 * @since 0.6.1
 * @author Volker Bergmann
 */
public class ArrayElementBuilder extends AbstractComponentBuilder<Object[]> {

    public ArrayElementBuilder(int index, Generator<?> source, String scope) {
        super(source, new Mutator_(index), scope);
    }

    private static class Mutator_ implements Mutator {

        int index;

        public Mutator_(int index) {
            this.index = index;
        }

        public void setValue(Object target, Object value) throws UpdateFailedException {
            ((Object[]) target)[index] = value;
        }
    }
}
