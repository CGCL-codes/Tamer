package org.peaseplate.domain.lang.command;

import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateRuntimeException;

public class BitwiseExclusiveOrCommand extends AbstractNumericCommand implements ICommand {

    public BitwiseExclusiveOrCommand(TemplateLocator locator, int line, int column, ICommand leftCommand, ICommand rightCommand) {
        super(locator, line, column, leftCommand, rightCommand);
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#getSign()
	 */
    @Override
    public String getSign() {
        return "^";
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Byte, java.lang.Byte)
	 */
    @Override
    public Byte calculate(Byte left, Byte right) throws TemplateRuntimeException {
        return new Byte((byte) (left.byteValue() ^ right.byteValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Short, java.lang.Short)
	 */
    @Override
    public Short calculate(Short left, Short right) throws TemplateRuntimeException {
        return new Short((short) (left.shortValue() ^ right.shortValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Character, java.lang.Character)
	 */
    @Override
    public Character calculate(Character left, Character right) throws TemplateRuntimeException {
        return new Character((char) (left.charValue() ^ right.charValue()));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Integer, java.lang.Integer)
	 */
    @Override
    public Integer calculate(Integer left, Integer right) throws TemplateRuntimeException {
        return new Integer(left.intValue() ^ right.intValue());
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNumericCommand#calculate(java.lang.Long, java.lang.Long)
	 */
    @Override
    public Long calculate(Long left, Long right) throws TemplateRuntimeException {
        return new Long(left.longValue() ^ right.longValue());
    }
}
