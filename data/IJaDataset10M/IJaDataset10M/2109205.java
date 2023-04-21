package br.ucam.kuabaSubsystem.mofParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import javax.jmi.model.Attribute;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import br.ucam.kuabaSubsystem.util.MofHelper;
import br.ucam.kuabaSubsystem.util.kuabaGraph.AbstractCompositeDFSIterator;

@SuppressWarnings("unchecked")
public class MOFCompositeIterator extends AbstractCompositeDFSIterator {

    public MOFCompositeIterator(Iterator pack) {
        super(pack);
    }

    @Override
    protected Iterator getElementIterator(Object element) {
        if (element instanceof MofClass) {
            return MofHelper.structuralFeaturesOfClass((MofClass) element, true).iterator();
        }
        if (element instanceof Attribute) {
            Attribute metaAttribute = (Attribute) element;
            if (metaAttribute.getType() instanceof EnumerationType) {
                EnumerationType enumType = (EnumerationType) metaAttribute.getType();
                return enumType.getLabels().iterator();
            }
        }
        return null;
    }

    @Override
    protected boolean isComposite(Object element) {
        if (element instanceof MofClass) {
            MofClass metaclass = (MofClass) element;
            if (metaclass.getContents().isEmpty() || (metaclass.isAbstract())) {
                return false;
            }
            return true;
        }
        if (element instanceof Attribute) {
            Attribute metaAttribute = (Attribute) element;
            if (metaAttribute.getType() instanceof EnumerationType) {
                EnumerationType enumType = (EnumerationType) metaAttribute.getType();
                if (enumType.getLabels().isEmpty()) {
                    return false;
                }
                return true;
            }
        }
        if (element instanceof Reference) {
            Reference ref = (Reference) element;
            if (!(ref.getContainer().getContainer().equals(ref.getType().getContainer()))) if (ref.getType().isAbstract()) {
                List<MofClass> l = MofHelper.getConcreteSubClasses(ref.getType());
                for (MofClass mofClass : l) insertElement(mofClass);
            } else {
                insertElement(ref.getType());
            }
        }
        return false;
    }

    private void insertElement(ModelElement mofClass) {
        if (((ListIterator) this.target).hasNext()) {
            this.target.next();
            ((ListIterator) this.target).add(mofClass);
            ((ListIterator) this.target).previous();
        } else {
            Object previous = ((ListIterator) this.target).previous();
            ((ListIterator) this.target).set(mofClass);
            ((ListIterator) this.target).add(previous);
        }
    }
}
