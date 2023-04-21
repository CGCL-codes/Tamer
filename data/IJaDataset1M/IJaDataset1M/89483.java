package MScheme.values;

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import MScheme.Value;
import MScheme.List;
import MScheme.Translator;
import MScheme.Code;
import MScheme.code.Literal;
import MScheme.environment.Environment;
import MScheme.environment.StaticEnvironment;
import MScheme.exceptions.*;

public abstract class ValueDefaultImplementations implements Value {

    /** The CVS id of the file containing this class. */
    public static final String id = "$Id: ValueDefaultImplementations.java 409 2001-09-30 14:22:10Z sielenk $";

    /** The default constructor. */
    protected ValueDefaultImplementations() {
    }

    /**
     * @return <code>this</code>.
     */
    public Value getConst() {
        return this;
    }

    /**
     * @return <code>true</code>
     */
    public boolean isTrue() {
        return true;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isList() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isScmBoolean() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isPair() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isSymbol() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isScmNumber() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isScmChar() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isScmString() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isScmVector() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isPort() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isFunction() {
        return false;
    }

    /**
     * @throws ListExpected
     */
    public List toList() throws ListExpected {
        throw new ListExpected(this);
    }

    /**
     * @throws PairExpected
     */
    public Pair toPair() throws PairExpected {
        throw new PairExpected(this);
    }

    /**
     * @throws SymbolExpected
     */
    public Symbol toSymbol() throws SymbolExpected {
        throw new SymbolExpected(this);
    }

    /**
     * @throws NumberExpected
     */
    public ScmNumber toScmNumber() throws NumberExpected {
        throw new NumberExpected(this);
    }

    /**
     * @throws CharExpected
     */
    public ScmChar toScmChar() throws CharExpected {
        throw new CharExpected(this);
    }

    /**
     * @throws StringExpected
     */
    public ScmString toScmString() throws StringExpected {
        throw new StringExpected(this);
    }

    /**
     * @throws VectorExpected
     */
    public ScmVector toScmVector() throws VectorExpected {
        throw new VectorExpected(this);
    }

    /**
     * @throws InputPortExpected
     */
    public InputPort toInputPort() throws InputPortExpected {
        throw new InputPortExpected(this);
    }

    /**
     * @throws OutputPortExpected
     */
    public OutputPort toOutputPort() throws OutputPortExpected {
        throw new OutputPortExpected(this);
    }

    /**
     * @throws FunctionExpected
     */
    public Function toFunction() throws FunctionExpected {
        throw new FunctionExpected(this);
    }

    /**
     * @throws EnvironmentExpected
     */
    public Environment toEnvironment() throws EnvironmentExpected {
        throw new EnvironmentExpected(this);
    }

    /**
     * @throws EnvironmentExpected
     */
    public StaticEnvironment toStaticEnvironment() throws EnvironmentExpected {
        throw new EnvironmentExpected(this);
    }

    /**
     * @return <code>this == other</code>
     */
    public boolean eq(Value other) {
        return this == other;
    }

    /**
     * @return <code>eq(other)</code>
     */
    public boolean eqv(Value other) {
        return eq(other);
    }

    /**
     * @return <code>eqv(other)</code>
     */
    public boolean equal(Value other) {
        return eqv(other);
    }

    /**
     * @return <code>(other instanceof Value) && equal((Value)other)</code>
     */
    public final boolean equals(Object other) {
        return (other instanceof Value) && equal((Value) other);
    }

    /**
     * Calls {@link #write write(destination)}.
     */
    public void display(Writer destination) throws IOException {
        write(destination);
    }

    /**
     * Implements Java's <code>toString</code> in terms of
     * Scheme's <code>write</code>.
     * <p>
     * @return a java string representation of the value.
     */
    public final String toString() {
        StringWriter out = new StringWriter();
        try {
            write(out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * @return a newly created {@link Literal}.
     */
    public final Literal getLiteral() {
        return Literal.create(this);
    }

    /**
     * Assumes the value to be a constant.
     * <p>
     * @param  compilationEnv ignored by this implementation
     * @return {@link #getLiteral()}
     */
    public Code getCode(StaticEnvironment compilationEnv) throws SchemeException {
        return getLiteral();
    }

    /**
     * Compiles as normal code.
     * <p>
     * @return <code>getCode(compilationEnv)</code>
     */
    public Translator getTranslator(StaticEnvironment compilationEnv) throws SchemeException {
        return getCode(compilationEnv);
    }
}
