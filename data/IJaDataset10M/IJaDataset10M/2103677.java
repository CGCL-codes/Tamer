package sketch.generator.examples;

import sketch.generator.CompositeGenerator;
import sketch.generator.IGenerator;
import sketch.generator.IIterationLink;
import sketch.generator.LinkCreator;

public class PairGenerator<LeftType, RightType> extends CompositeGenerator<Pair<LeftType, RightType>> {

    private final IGenerator<LeftType> leftGen;

    private final IGenerator<RightType> rightGen;

    public PairGenerator(IGenerator<LeftType> leftGen, IGenerator<RightType> rightGen) {
        super();
        this.leftGen = leftGen;
        this.rightGen = rightGen;
    }

    @Override
    public Pair<LeftType, RightType> generateCurrent() {
        return new Pair<LeftType, RightType>(leftGen.current(), rightGen.current());
    }

    @Override
    public IIterationLink getInitializedIterator() {
        return LinkCreator.createCombinatoricLink(leftGen, rightGen);
    }
}
