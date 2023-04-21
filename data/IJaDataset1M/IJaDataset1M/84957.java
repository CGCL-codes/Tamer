package org.isakiev.xl.model.graph;

import java.util.Collection;
import org.isakiev.xl.model.BlockReference;
import org.isakiev.xl.model.CellReference;

public class BasicDependencyGraph extends AbstractDependencyGraph {

    public BasicDependencyGraph() {
        initialize(createCellToCellRelation(), createCellToBlockRelation(), createCellToBlockRelation());
    }

    private static ManyToManyRelation<CellReference, CellReference> createCellToCellRelation() {
        return createRelation();
    }

    private static ManyToManyRelation<CellReference, BlockReference> createCellToBlockRelation() {
        return createRelation();
    }

    private static <S, T> ManyToManyRelation<S, T> createRelation() {
        return new ManyToManyRelation<S, T>(new BasicMap<S, Collection<T>>(), new BasicMap<T, Collection<S>>());
    }
}
