package org.monet.bpi.java;

import java.util.Iterator;
import org.monet.bpi.BPIExpression;
import org.monet.bpi.BPIReference;
import org.monet.kernel.components.ComponentPersistence;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Node;
import org.monet.kernel.model.Reference;
import org.monet.kernel.model.ReferenceList;
import org.monet.kernel.model.definition.CollectionDefinition;
import org.monet.kernel.model.definition.ReferenceDefinition;

public class BPICollectionIterableImpl implements Iterable<BPIReference> {

    private final ReferenceDefinition referenceDefinition;

    private final BPIExpression where;

    private ComponentPersistence componentPersistence = ComponentPersistence.getInstance();

    public BPICollectionIterableImpl(Node node, BPIExpression where) {
        this.where = where;
        String referenceName = null;
        if (node.isCollection()) {
            referenceName = ((CollectionDefinition) node.getDefinition()).getUse().getReference();
        } else {
            throw new RuntimeException("Node is not a Collection.");
        }
        this.referenceDefinition = BusinessUnit.getInstance().getBusinessModel().getDictionary().getReferenceDefinition(referenceName);
    }

    @Override
    public Iterator<BPIReference> iterator() {
        return new BPICollectionIteratorImpl();
    }

    private class BPICollectionIteratorImpl implements Iterator<BPIReference> {

        private static final int pageSize = 10;

        private int currentOffset = -pageSize;

        private int totalCount;

        private Iterator<Reference> currentPage;

        private Reference currentReference;

        public BPICollectionIteratorImpl() {
            getNextPage();
            this.totalCount = componentPersistence.getReferencesCount(referenceDefinition.getCode(), where.toString());
        }

        private void getNextPage() {
            this.currentOffset += pageSize;
            ReferenceList referenceList = componentPersistence.getReferences(referenceDefinition.getCode(), where.toString(), currentOffset, pageSize);
            this.currentPage = referenceList.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.currentPage.hasNext() || (currentOffset + pageSize) < totalCount;
        }

        @Override
        public BPIReference next() {
            if (!this.hasNext()) return null;
            BPIReferenceImpl bpiReference = new BPIReferenceImpl();
            if (!currentPage.hasNext()) this.getNextPage();
            currentReference = currentPage.next();
            bpiReference.injectReference(currentReference);
            return bpiReference;
        }

        @Override
        public void remove() {
            componentPersistence.deleteNode(this.currentReference.getIdNode());
        }
    }
}
