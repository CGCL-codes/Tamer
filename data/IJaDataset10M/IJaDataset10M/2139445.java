package lang4j.parser.generated;

public interface FlatAttributeTransformer<T> {

    public T transformIntLiteralAttribute(IntLiteralAttribute intLiteralAttribute);

    public T transformDoubleLiteralAttribute(DoubleLiteralAttribute doubleLiteralAttribute);

    public T transformCharLiteralAttribute(CharLiteralAttribute charLiteralAttribute);

    public T transformBooleanLiteralAttribute(BooleanLiteralAttribute booleanLiteralAttribute);

    public T transformStringLiteralAttribute(StringLiteralAttribute stringLiteralAttribute);

    public T transformIdentifierAttribute(IdentifierAttribute identifierAttribute);

    public T transformReferenceAttribute(ReferenceAttribute referenceAttribute);

    public T transformMultiLineStringAttribute(MultiLineStringAttribute multiLineStringAttribute);
}
