package lang4j.parser.generated;

import lang4j.parser.ParsedEntity;

public interface Terminal extends ProductionFactor {

    public void accept(TerminalVisitor visitor);

    public <T> T transform(TerminalTransformer<T> visitor);
}
