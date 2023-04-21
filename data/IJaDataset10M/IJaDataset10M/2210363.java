package gloodb.operators;

import gloodb.memory.MemoryBasedRepositoryFactory;

public class MemoryBasedExpressionTest extends ExpressionTestBase {

    public MemoryBasedExpressionTest() {
        super(new MemoryBasedRepositoryFactory().buildRepository("target/UnitTests/Expressions"));
    }
}
