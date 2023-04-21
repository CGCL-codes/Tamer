package org.peaseplate.domain.lang.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateRuntimeException;

public class MultiplyCommand extends AbstractNumericCommand implements ICommand {

    public MultiplyCommand(TemplateLocator locator, int line, int column, ICommand leftCommand, ICommand rightCommand) {
        super(locator, line, column, leftCommand, rightCommand);
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#getSign()
	 */
    @Override
    public String getSign() {
        return "*";
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Byte, java.lang.Byte)
	 */
    @Override
    public Byte calculate(Byte left, Byte right) throws TemplateRuntimeException {
        return new Byte((byte) (left.byteValue() * right.byteValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Short, java.lang.Short)
	 */
    @Override
    public Short calculate(Short left, Short right) throws TemplateRuntimeException {
        return new Short((short) (left.shortValue() * right.shortValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Character, java.lang.Character)
	 */
    @Override
    public Character calculate(Character left, Character right) throws TemplateRuntimeException {
        return new Character((char) (left.charValue() * right.charValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Integer, java.lang.Integer)
	 */
    @Override
    public Integer calculate(Integer left, Integer right) throws TemplateRuntimeException {
        return new Integer(left.intValue() * right.intValue());
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Long, java.lang.Long)
	 */
    @Override
    public Long calculate(Long left, Long right) throws TemplateRuntimeException {
        return new Long(left.longValue() * right.longValue());
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Float, java.lang.Float)
	 */
    @Override
    public Float calculate(Float left, Float right) throws TemplateRuntimeException {
        return new Float(left.floatValue() * right.floatValue());
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Double, java.lang.Double)
	 */
    @Override
    public Double calculate(Double left, Double right) throws TemplateRuntimeException {
        return new Double(left.doubleValue() * right.doubleValue());
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.math.BigDecimal, java.math.BigDecimal)
	 */
    @Override
    public BigDecimal calculate(BigDecimal left, BigDecimal right) throws TemplateRuntimeException {
        return left.multiply(right);
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.math.BigInteger, java.math.BigInteger)
	 */
    @Override
    public BigInteger calculate(BigInteger left, BigInteger right) throws TemplateRuntimeException {
        return left.multiply(right);
    }
}
