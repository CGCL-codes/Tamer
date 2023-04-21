package net.sourceforge.velocidoc.parser;

public class DocMacroArgument extends SimpleNode {

    String name;

    public void setArgumentName(String n) {
        this.name = n;
    }

    public String getName() {
        System.out.println("Argument name:" + name);
        return this.name;
    }

    public DocMacroArgument(int id) {
        super(id);
    }

    public DocMacroArgument(MacroParser p, int id) {
        super(p, id);
    }
}
