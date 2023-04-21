package org.jcvi.common.core.align.blast;

import java.io.File;
import java.io.IOException;
import org.jcvi.common.core.DirectedRange;
import org.jcvi.common.core.assembly.DefaultScaffold;
import org.jcvi.common.core.assembly.Scaffold;
import org.jcvi.common.core.assembly.ScaffoldBuilder;
import org.jcvi.common.core.util.Builder;

/**
 * @author dkatzel
 *
 *
 */
public final class BlastScaffoldBuilder implements BlastVisitor, Builder<Scaffold> {

    public static Scaffold createFromTabularBlastOutput(File tabularBlast, String referenceId) throws IOException {
        BlastScaffoldBuilder builder = new BlastScaffoldBuilder(referenceId);
        TabularBlastParser.parse(tabularBlast, builder);
        return builder.build();
    }

    public static Scaffold createFromXmlBlastOutput(File xmlBlast, String referenceId) {
        BlastScaffoldBuilder builder = new BlastScaffoldBuilder(referenceId);
        XmlBlastParser.parse(xmlBlast, builder);
        return builder.build();
    }

    private final ScaffoldBuilder scaffoldBuilder;

    private final String subjectId;

    private BlastScaffoldBuilder(String subjectId) {
        this.subjectId = subjectId;
        this.scaffoldBuilder = DefaultScaffold.createBuilder(subjectId);
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void visitLine(String line) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void visitFile() {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void visitEndOfFile() {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void visitHsp(Hsp blastHit) {
        if (subjectId.equals(blastHit.getSubjectId())) {
            DirectedRange directedRange = blastHit.getSubjectRange();
            String uniqueId = String.format("%s_%d_%d", blastHit.getQueryId(), directedRange.asRange().getBegin() + 1, directedRange.asRange().getEnd() + 1);
            scaffoldBuilder.add(uniqueId, directedRange.asRange(), directedRange.getDirection());
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public Scaffold build() {
        return scaffoldBuilder.build();
    }
}
