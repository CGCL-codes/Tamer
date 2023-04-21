package DL.Ontology.Expression;

/**
 * Expression factory interface
 */
public interface IExpressionFactory {

    /** Instantiate expression from binary blob */
    IExpression Create(byte[] blob) throws Exception;
}
