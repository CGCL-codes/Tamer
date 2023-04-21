package tm.javaLang.parser;

public interface JavaParserVisitor {

    public Object visit(SimpleNode node, Object data);
}
