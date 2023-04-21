package saf.compiler.types;

public abstract class BinCondition extends ICondition {

    protected ICondition m_Left;

    protected ICondition m_Right;

    public BinCondition(ICondition left, ICondition right) {
        m_Left = left;
        m_Right = right;
    }
}
