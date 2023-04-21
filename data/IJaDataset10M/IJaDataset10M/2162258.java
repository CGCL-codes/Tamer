package serl.equalschecker.alloydom;

import java.util.ArrayList;

/**
 *
 *
 * @author Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 *
 */
public class FieldDeclaration implements LogicElement {

    private Sig sig;

    private String name;

    private AlloyType type;

    private ArrayList<Composition> compositionList;

    protected boolean nullityPossible;

    private FieldDeclaration() {
        this.compositionList = new ArrayList<Composition>();
        nullityPossible = false;
    }

    /**
	 * Creates a new instance of the class FieldDeclaration
	 * 
	 */
    public FieldDeclaration(String name, AlloyType type, Sig sig) {
        this();
        this.name = name;
        this.type = type;
        this.sig = sig;
    }

    public Sig getSig() {
        return sig;
    }

    public void setSig(Sig sig) {
        this.sig = sig;
    }

    public void add(Composition c) {
        this.compositionList.add(c);
        c.setFieldDeclaration(this);
    }

    public void add(FieldDeclaration f) {
        for (Composition c : f.compositionList) {
            this.add(c);
        }
    }

    public void remove(Composition c) {
        this.compositionList.remove(c);
    }

    public ArrayList<Composition> getCompositionList() {
        return this.compositionList;
    }

    public boolean isNullityPossible() {
        return nullityPossible;
    }

    public void setNullityPossible(boolean nullityPossible) {
        this.nullityPossible = nullityPossible;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        if (SymbolTable.isAlloyKeyWord(name)) {
            return name + SymbolTable.SUFFIX;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the type
	 */
    public AlloyType getType() {
        return type;
    }

    public void setType(AlloyType type) {
        this.type = type;
    }

    public String getDefinition() {
        String name = this.name;
        if (SymbolTable.isAlloyKeyWord(name)) name += SymbolTable.SUFFIX;
        return SymbolTable.TAB + name + SymbolTable.SPACE + SymbolTable.OP_BELONGS_TO + SymbolTable.SPACE + this.type.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FieldDeclaration other = (FieldDeclaration) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    public String toString() {
        return this.sig.getAlloyName() + SymbolTable.OP_DOT + this.name;
    }
}
