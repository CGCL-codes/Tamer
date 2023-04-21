package org.norecess.citkit.tir.declarations;

import org.norecess.citkit.ISymbol;
import org.norecess.citkit.tir.ExpressionTIR;
import org.norecess.citkit.tir.IPosition;
import org.norecess.citkit.tir.types.INameTTIR;
import org.norecess.citkit.tir.types.NameTTIR;
import org.norecess.citkit.visitors.DeclarationTIRVisitor;

/**
 * The TIR for a variable declaration. The name of the variable being declared
 * is specified as a {@link String}. The expression used to initialize the
 * variable is an {@link ExpressionTIR}. You must provide both of these; if your
 * language does not require an initialization expression, then provide a
 * default initialization expression. Optionally, you can specify a data type
 * for the variable as a {@link NameTTIR}.
 * 
 * @author Jeremy D. Frens
 * 
 */
public class VariableDTIR implements IVariableDTIR {

    /**
	 * The position of code that lead to the TIR.
	 */
    private final IPosition myPosition;

    /**
	 * The name of the variable.
	 */
    private final ISymbol mySymbol;

    /**
	 * The type of the variable.
	 */
    private final INameTTIR myType;

    /**
	 * The initializing expression for the variable.
	 */
    private final ExpressionTIR myInititialization;

    /**
	 * Constructs a variable declaration.
	 * 
	 * @param position
	 *            the position of the variable declaration in the source code.
	 * @param symbol
	 *            the name of the variable.
	 * @param type
	 *            the type of the variable (could be <code>null</code>)
	 * @param initialization
	 *            the initialization of the variable.
	 */
    public VariableDTIR(IPosition position, ISymbol symbol, INameTTIR type, ExpressionTIR initialization) {
        if (symbol == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (initialization == null) {
            throw new IllegalArgumentException("init cannot be null");
        }
        myPosition = position;
        mySymbol = symbol;
        myType = type;
        myInititialization = initialization;
    }

    /**
	 * Basic constructor.
	 * 
	 * @param symbol
	 *            name of the variable.
	 * @param type
	 *            type of the variable (could be <code>null</code>)
	 * @param initialization
	 *            the initialization expression.
	 */
    public VariableDTIR(ISymbol symbol, INameTTIR type, ExpressionTIR initialization) {
        this(null, symbol, type, initialization);
    }

    /**
	 * Constructs a variable declaration without a known type.
	 * 
	 * @param symbol
	 *            the name of the variable.
	 * @param initialization
	 *            the initialization of the variable.
	 */
    public VariableDTIR(ISymbol symbol, ExpressionTIR initialization) {
        this(null, symbol, null, initialization);
    }

    public IPosition getPosition() {
        return myPosition;
    }

    /**
	 * Returns the initialization expression.
	 * 
	 * @return the initialization expression.
	 */
    public ExpressionTIR getInititialization() {
        return myInititialization;
    }

    public ISymbol getSymbol() {
        return mySymbol;
    }

    /**
	 * Returns the type of the variable.
	 * 
	 * @return the type of the variable.
	 */
    public INameTTIR getType() {
        return myType;
    }

    public <T> T accept(DeclarationTIRVisitor<T> visitor) {
        return visitor.visitVariableDTIR(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((myInititialization == null) ? 0 : myInititialization.hashCode());
        result = prime * result + ((mySymbol == null) ? 0 : mySymbol.hashCode());
        result = prime * result + ((myType == null) ? 0 : myType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VariableDTIR other = (VariableDTIR) obj;
        if (myInititialization == null) {
            if (other.myInititialization != null) {
                return false;
            }
        } else if (!myInititialization.equals(other.myInititialization)) {
            return false;
        }
        if (mySymbol == null) {
            if (other.mySymbol != null) {
                return false;
            }
        } else if (!mySymbol.equals(other.mySymbol)) {
            return false;
        }
        if (myType == null) {
            if (other.myType != null) {
                return false;
            }
        } else if (!myType.equals(other.myType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getSymbol() + " : " + getType() + " := " + getInititialization();
    }
}
