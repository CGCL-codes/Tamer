package org.jcvi.common.core.assembly.asm.atac;

import org.jcvi.common.core.DirectedRange;
import org.jcvi.common.core.Range;

public class DefaultAtacMatch implements AtacMatch {

    private final MatchType type;

    private final String instanceIndex;

    private final long parentIndex;

    private final String firstAssemblyId, secondAssemblyId;

    private final Range firstAssemblyRange;

    private final DirectedRange secondAsssemblyRange;

    public DefaultAtacMatch(MatchType type, String instanceIndex, long parentIndex, String firstAssemblyId, Range firstAssemblyRange, String secondAssemblyId, DirectedRange secondAsssemblyRange) {
        super();
        this.type = type;
        this.instanceIndex = instanceIndex;
        this.parentIndex = parentIndex;
        this.firstAssemblyId = firstAssemblyId;
        this.firstAssemblyRange = firstAssemblyRange;
        this.secondAssemblyId = secondAssemblyId;
        this.secondAsssemblyRange = secondAsssemblyRange;
    }

    @Override
    public MatchType getMatchType() {
        return type;
    }

    @Override
    public String getInstanceIndex() {
        return instanceIndex;
    }

    @Override
    public long getParentIndex() {
        return parentIndex;
    }

    @Override
    public String getFirstAssemblyId() {
        return firstAssemblyId;
    }

    @Override
    public Range getFirstAssemblyRange() {
        return firstAssemblyRange;
    }

    @Override
    public String getSecondAssemblyId() {
        return secondAssemblyId;
    }

    @Override
    public DirectedRange getSecondAssemblyDirectedRange() {
        return secondAsssemblyRange;
    }

    @Override
    public String toString() {
        return "DefaultAtacMatch [type=" + type + ", instanceIndex=" + instanceIndex + ", parentIndex=" + parentIndex + ", firstAssemblyId=" + firstAssemblyId + ", firstAssemblyRange=" + firstAssemblyRange + ", secondAssemblyId=" + secondAssemblyId + ", secondAsssemblyRange=" + secondAsssemblyRange + "]";
    }
}
