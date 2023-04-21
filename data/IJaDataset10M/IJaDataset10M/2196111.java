package info.reflectionsofmind.vijual.core.expression;

import info.reflectionsofmind.vijual.core.lazy.ILazy;
import info.reflectionsofmind.vijual.core.lazy.LApply;
import info.reflectionsofmind.vijual.core.type.IType;
import info.reflectionsofmind.vijual.core.util.Types;
import info.reflectionsofmind.vijual.library.type.function.TFunctionConstructor;

public final class EApplication extends Expression {

    private final Expression function;

    private final Expression argument;

    private final IType<?> type;

    public EApplication(final Expression function, final Expression argument) {
        this.function = function;
        this.argument = argument;
        this.type = Types.resolve((TFunctionConstructor) function.getType(), argument.getType());
    }

    @Override
    public ILazy toLazy() {
        return new LApply(this.function.toLazy(), this.argument.toLazy());
    }

    @Override
    public Expression substitute(EVariable variable, Expression expression) {
        final Expression newFunction = getFunction().substitute(variable, expression);
        final Expression newArgument = getArgument().substitute(variable, expression);
        return (newFunction == this.function && newArgument == this.argument) ? this : new EApplication(newFunction, newArgument);
    }

    @Override
    public IType<?> getType() {
        return this.type;
    }

    public Expression getFunction() {
        return this.function;
    }

    public Expression getArgument() {
        return this.argument;
    }

    @Override
    public String toString() {
        return "(" + getFunction().toString() + " " + getArgument().toString() + ")";
    }
}
