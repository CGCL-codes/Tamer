package luaparser.LUA_PT;

public class concat extends SimpleNode {

    public concat(int id) {
        super(id);
    }

    public concat(LuaTreeParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(LuaTreeParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
