package org.peaseplate.queryengine.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.peaseplate.typeconversion.TypeConversionException;
import org.peaseplate.typeconversion.TypeConversionService;
import org.peaseplate.utils.ReflectionUtils;
import org.peaseplate.utils.command.Command;
import org.peaseplate.utils.command.Scope;
import org.peaseplate.utils.exception.ExecuteException;
import org.peaseplate.utils.exception.UnsupportedExecuteException;

/**
 * The abstract implementation of equality command line == and != and compares like <, >, <= and >=
 * 
 * @author Manfred HANTSCHEL
 */
public abstract class AbstractCompareCommand extends AbstractDoubleParameterCommand {

    private final TypeConversionService typeConversionService;

    /**
	 * Creates a new instance of the command
	 * 
	 * @param typeConversionService the type conversion service
	 * @param line the line
	 * @param column the column
	 * @param leftCommand the left command
	 * @param rightCommand the right command
	 */
    public AbstractCompareCommand(final TypeConversionService typeConversionService, final int line, final int column, final Command leftCommand, final Command rightCommand) {
        super(line, column, leftCommand, rightCommand);
        this.typeConversionService = typeConversionService;
    }

    /**
	 * Returns the sign used for the command
	 * 
	 * @return the sign
	 */
    public abstract String getSign();

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object call(final Scope scope) throws ExecuteException {
        final Object left = callLeftCommand(scope);
        final Object right = callRightCommand(scope);
        if ((left == null) && (right == null)) {
            return evaluateNull();
        } else if ((left instanceof Comparable<?>) && (right instanceof Comparable<?>)) {
            return evaluatePossiblyComparable(scope, (Comparable<?>) left, right);
        }
        return evaluateNonComparable(left, right);
    }

    /**
	 * Evaluates the compare if it consists of two null values
	 * 
	 * @return the result of the evaluation
	 * @throws ExecuteException on occasion
	 */
    public Boolean evaluateNull() throws ExecuteException {
        throw new UnsupportedExecuteException(getSign(), null, null, getLine(), getColumn());
    }

    @SuppressWarnings("unchecked")
    protected <LEFT_TYPE extends Comparable<?>, RIGHT_TYPE> Boolean evaluatePossiblyComparable(final Scope scope, final LEFT_TYPE left, final RIGHT_TYPE right) throws ExecuteException {
        final Class<?> leftType = ReflectionUtils.getClass(left);
        final Class<?> rightType = ReflectionUtils.getClass(right);
        try {
            if ((leftType == StringBuilder.class) || (rightType == StringBuilder.class) || (leftType == StringBuffer.class) || (rightType == StringBuffer.class) || (leftType == String.class) || (rightType == String.class)) {
                return evaluateComparable(typeConversionService.convert(left, String.class), typeConversionService.convert(right, String.class));
            } else if ((leftType == BigDecimal.class) || (rightType == BigDecimal.class)) {
                return evaluateComparable(typeConversionService.convert(left, BigDecimal.class), typeConversionService.convert(right, BigDecimal.class));
            } else if ((leftType == BigInteger.class) || (rightType == BigInteger.class)) {
                return evaluateComparable(typeConversionService.convert(left, BigInteger.class), typeConversionService.convert(right, BigInteger.class));
            } else if ((leftType == double.class) || (leftType == Double.class) || (rightType == double.class) || (rightType == Double.class)) {
                return evaluateComparable(typeConversionService.convert(left, Double.class), typeConversionService.convert(right, Double.class));
            } else if ((leftType == float.class) || (leftType == Float.class) || (rightType == float.class) || (rightType == Float.class)) {
                return evaluateComparable(typeConversionService.convert(left, Float.class), typeConversionService.convert(right, Float.class));
            } else if ((leftType == long.class) || (leftType == Long.class) || (rightType == long.class) || (rightType == Long.class)) {
                return evaluateComparable(typeConversionService.convert(left, Long.class), typeConversionService.convert(right, Long.class));
            } else if ((leftType == int.class) || (leftType == Integer.class) || (rightType == int.class) || (rightType == Integer.class)) {
                return evaluateComparable(typeConversionService.convert(left, Integer.class), typeConversionService.convert(right, Integer.class));
            } else if ((leftType == char.class) || (leftType == Character.class) || (rightType == char.class) || (rightType == Character.class)) {
                return evaluateComparable(typeConversionService.convert(left, Character.class), typeConversionService.convert(right, Character.class));
            } else if ((leftType == short.class) || (leftType == Short.class) || (rightType == short.class) || (rightType == Short.class)) {
                return evaluateComparable(typeConversionService.convert(left, Short.class), typeConversionService.convert(right, Short.class));
            } else if ((leftType == byte.class) || (leftType == Byte.class) || (rightType == byte.class) || (rightType == Byte.class)) {
                return evaluateComparable(typeConversionService.convert(left, Byte.class), typeConversionService.convert(right, Byte.class));
            } else if ((leftType == boolean.class) || (leftType == Boolean.class) || (rightType == boolean.class) || (rightType == Boolean.class)) {
                return evaluateComparable(typeConversionService.convert(left, Boolean.class), typeConversionService.convert(right, Boolean.class));
            }
        } catch (final TypeConversionException e) {
        }
        try {
            return evaluateComparable((Comparable<RIGHT_TYPE>) left, right);
        } catch (final ClassCastException e) {
        }
        return evaluateNonComparable(left, right);
    }

    /**
	 * Evaluates the compare if it consists of two values that are comparable
	 * 
	 * @param <TYPE> the type of the right value that may be compared on the left
	 * @param left the left value
	 * @param right the right value
	 * @return the result of the compare
	 * @throws ExecuteException on occasion
	 */
    public <TYPE> Boolean evaluateComparable(final Comparable<TYPE> left, final TYPE right) throws ExecuteException {
        throw new UnsupportedExecuteException(getSign(), left, right, getLine(), getColumn());
    }

    /**
	 * Evaluates the compare for two values that are not comparable
	 * 
	 * @param left the left value
	 * @param right the right value
	 * @return the result of the compare
	 * @throws ExecuteException on occasion
	 */
    public Boolean evaluateNonComparable(final Object left, final Object right) throws ExecuteException {
        throw new UnsupportedExecuteException(getSign(), left, right, getLine(), getColumn());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        return "(" + getLeftCommand() + " " + getSign() + " " + getRightCommand() + ")";
    }
}
