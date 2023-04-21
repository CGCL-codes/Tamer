package cat.quadriga.parsers.code.statements;

import cat.quadriga.parsers.Token;
import cat.quadriga.parsers.code.CodeZoneClass;
import cat.quadriga.parsers.code.ErrorLog;
import cat.quadriga.parsers.code.SymbolTable;
import cat.quadriga.parsers.code.symbols.BaseSymbol;

public class LabeledStatementNode extends StatementNodeClass {

    public final LabelSymbol label;

    public final StatementNode statement;

    public LabeledStatementNode(Token label, StatementNode statement, SymbolTable st, ErrorLog errorLog) {
        super(new CodeZoneClass(label, statement));
        if (st.findSymbolOnTop(label.image) != null) {
            errorLog.insertError("Identificador \"" + label.image + "\" repetit.", this);
        }
        this.label = new LabelSymbol(label.image);
        this.statement = statement;
    }

    private LabeledStatementNode(LabeledStatementNode other, SymbolTable symbolTable, ErrorLog errorLog) {
        super(other);
        symbolTable.newContext();
        linked = true;
        linkedVersion = this;
        label = this.new LabelSymbol(other.label.name);
        symbolTable.addSymbol(label);
        if (other.statement.isCorrectlyLinked()) {
            statement = other.statement;
        } else {
            statement = other.statement.getLinkedVersion(symbolTable, errorLog);
            if (statement == null) {
                linked = false;
            }
        }
        symbolTable.deleteContext();
    }

    private boolean linked = false;

    private LabeledStatementNode linkedVersion = null;

    @Override
    public LabeledStatementNode getLinkedVersion(SymbolTable symbolTable, ErrorLog errorLog) {
        if (linked) {
            return this;
        } else if (linkedVersion == null) {
            linkedVersion = new LabeledStatementNode(this, symbolTable, errorLog);
            if (!linkedVersion.isCorrectlyLinked()) {
                linkedVersion = null;
            }
        }
        return linkedVersion;
    }

    @Override
    public boolean isCorrectlyLinked() {
        return linked;
    }

    @Override
    public String[] getOperands() {
        String[] aux = { statement.treeStringRepresentation() };
        return aux;
    }

    @Override
    public String getOperation() {
        return "LABEL: " + label.name;
    }

    public class LabelSymbol extends BaseSymbol {

        public LabelSymbol(String label) {
            super(label);
        }

        @Override
        public String createTreeStringRepresentation() {
            return "Symbol Label [" + name + "]";
        }

        public LabeledStatementNode getOuter() {
            return LabeledStatementNode.this;
        }
    }
}
