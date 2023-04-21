package org.norecess.nolatte.primitives.group;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.ast.IIdentifier;
import org.norecess.nolatte.ast.IWhitespaceDatum;
import org.norecess.nolatte.ast.visitors.DefaultActionDatumVisitor;

public class AppendVisitor extends DefaultActionDatumVisitor<IGroupOfData> implements IGroupAppender {

    private static final long serialVersionUID = -2473341076115310577L;

    private final IGroupOfData myAccumulator;

    private final IGroupAppender myRecursion;

    public AppendVisitor(IGroupOfData accumulator) {
        myAccumulator = accumulator;
        myRecursion = this;
    }

    public AppendVisitor(IGroupOfData accumulator, IGroupAppender recursion) {
        myAccumulator = accumulator;
        myRecursion = recursion;
    }

    @Override
    public IGroupOfData doAction(Datum datum) {
        myAccumulator.add(datum);
        return myAccumulator;
    }

    @Override
    public IGroupOfData visitGroupOfData(IGroupOfData group) {
        myRecursion.appendElements(group);
        myRecursion.appendHashes(group);
        return myAccumulator;
    }

    public void appendHashes(IGroupOfData group) {
        for (IIdentifier identifier : group.getKeys()) {
            myAccumulator.hash(identifier, group.lookup(identifier));
        }
    }

    public void appendElements(IGroupOfData group) {
        for (Datum value : group) {
            myAccumulator.add(value);
        }
    }

    @Override
    public IGroupOfData visitWhitespaceDatum(IWhitespaceDatum datum) {
        return datum.getDatum().accept(this);
    }
}
