package hu.e.compiler.internal.model.symbols.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import hu.e.compiler.internal.model.IProgramStep;
import hu.e.compiler.internal.model.ISymbolManager;
import hu.e.compiler.internal.model.symbols.IStructSymbol;
import hu.e.compiler.internal.model.symbols.ISymbol;
import hu.e.parser.eSyntax.StructTypeDefMember;
import hu.e.parser.eSyntax.Type;

/**
 * @author balazs.grill
 *
 */
public class StructLiteralSymbol implements IStructSymbol {

    private final Map<StructTypeDefMember, ISymbol> symbols;

    private final Type type;

    public StructLiteralSymbol(Map<StructTypeDefMember, ISymbol> symbols, Type type) {
        this.symbols = symbols;
        this.type = type;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public List<IProgramStep> getSteps() {
        return Collections.emptyList();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public ISymbol getMember(ISymbolManager sm, StructTypeDefMember member) {
        return symbols.get(member);
    }
}
