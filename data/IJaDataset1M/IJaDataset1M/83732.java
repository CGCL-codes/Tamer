package nz.org.venice.analyser.gp;

import java.util.Iterator;
import java.util.Random;
import nz.org.venice.analyser.GPGondolaSelection;
import nz.org.venice.parser.Expression;
import nz.org.venice.parser.ExpressionFactory;
import nz.org.venice.parser.expression.*;
import nz.org.venice.quote.Quote;

/**
 * The mutator can build random expressions and randomly mutate existing
 * expressions. This class is at the heart of the GP as it creates the
 * random buy and sell rules and combines the rules during "breeding".
 *
 * @author Andrew Leppard
 * @see Individual
 * @see GeneticProgramme
 */
public class Mutator {

    private static final int BRANCH_FACTOR = 80;

    private static final int FAVOUR_NUMBER_PERCENT = 85;

    private static final int MUTATION_PERCENT = 10;

    private static final int EXTRA_MUTATION_PERCENT = 10;

    private static final int INSERTION_MUTATION_PERCENT = 10;

    private static final int DELETION_MUTATION_PERCENT = 20;

    private static final int MODIFICATION_MUTATION_PERCENT = 70;

    private static final int NO_SUBTYPE = 0;

    private static final int POSITIVE_SHORT_INTEGER_SUBTYPE = 1;

    private static final int NEGATIVE_SHORT_INTEGER_SUBTYPE = 2;

    private static final int SMOOTHING_CONSTANT_SUBTYPE = 3;

    private Random random;

    private GPGondolaSelection GPGondolaSelection;

    private boolean allowHeld;

    private boolean allowOrder;

    /**
     * Create a new mutator.
     *
     * @param random use this random number generator
     * @param GPGondolaSelection UI containing user's desired expression probabilities
     * @param allowHeld allow the creation of the <code>held</code> variable
     * @param allowOrder allow the creation of the <code>order</code> variable
     */
    public Mutator(Random random, GPGondolaSelection GPGondolaSelection, boolean allowHeld, boolean allowOrder) {
        this.random = random;
        this.GPGondolaSelection = GPGondolaSelection;
        this.allowHeld = allowHeld;
        this.allowOrder = allowOrder;
    }

    /**
     * Create a new random expression of the given type.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @return a randomly generated expression
     */
    public Expression createRandom(int type) {
        return createRandom(null, type, this.NO_SUBTYPE, 1);
    }

    /**
     * Create a new random expression of the given type and subType.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @return a randomly generated expression
     */
    public Expression createRandom(int type, int subType) {
        return createRandom(null, type, subType, 1);
    }

    /**
     * Create a new random expression of the given type,subType at the given level. The
     * level parameter is used to vary the probability of the expression
     * being a non-terminal or a terminal expression. As the level of the expression
     * tree gets larger, the probability of creating a non-terminal child
     * expression decreases.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @param level the level in the tree
     * @return a randomly generated expression
     */
    public Expression createRandom(int type, int subType, int level) {
        return createRandom(null, type, subType, level);
    }

    /**
     * Create a new random expression based on mutating the given expression.
     * If <code>level < 1</code> then the top node of the created expression
     * will not be terminal.
     *
     * @param model initial expression to work with
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @param level the level in the tree
     * @return a randomly generated expression
     * @see #createRandom(int type, int subType, int level)
     */
    public Expression createRandom(Expression model, int type, int subType, int level) {
        boolean terminal = true;
        if (level < 1) {
            terminal = false;
        } else {
            double branchPercent = (double) BRANCH_FACTOR / (double) level;
            double percent = random.nextDouble() * 100;
            if (branchPercent > percent) {
                terminal = false;
            }
        }
        if (type == Expression.BOOLEAN_TYPE || !terminal) {
            return createRandomNonTerminal(model, type, subType, level + 1);
        } else {
            return createRandomTerminal(type, subType);
        }
    }

    /**
     * Create a  new random non-terminal expression of the given type.
     * A terminal expression is one that has children, e.g. an operator
     * such as plus. (Thus plus operator would have two children, e.g.
     * 1 and 1).
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @return a randomly generated non-terminal expression
     */
    public Expression createRandomNonTerminal(int type) {
        return createRandomNonTerminal(null, type, this.NO_SUBTYPE, 1);
    }

    /**
     * Create a  new random non-terminal expression of the given type and subType.
     * A terminal expression is one that has children, e.g. an operator
     * such as plus. (Thus plus operator would have two children, e.g.
     * 1 and 1).
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @return a randomly generated non-terminal expression
     */
    public Expression createRandomNonTerminal(int type, int subType) {
        return createRandomNonTerminal(null, type, subType, 1);
    }

    /**
     * Create a new random non-terminal expression of the given type and subType
     * at the given level.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @param level the level in the tree
     * @return a randomly generated non-terminal expression
     */
    public Expression createRandomNonTerminal(int type, int subType, int level) {
        return createRandomNonTerminal(null, type, subType, level);
    }

    /**
     * Create a new random non-terminal expression based on mutating the given expression.
     *
     * @param model initial expression to work with
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @param level the level in the tree
     * @return a randomly generated non-terminal expression
     * @see #createRandom(int type, int subType, int level)
     */
    public Expression createRandomNonTerminal(Expression model, int type, int subType, int level) {
        Expression rv = null;
        if (type == Expression.BOOLEAN_TYPE) {
            rv = createRandomNonTerminalBoolean(model, level);
        } else if (type == Expression.FLOAT_TYPE) {
            rv = createRandomNonTerminalFloat(model, level);
        } else if ((type == Expression.INTEGER_TYPE) && (subType == this.POSITIVE_SHORT_INTEGER_SUBTYPE)) {
            rv = createRandomNonTerminalPositiveShortInteger(model, level);
        } else if ((type == Expression.INTEGER_TYPE) && (subType == this.NEGATIVE_SHORT_INTEGER_SUBTYPE)) {
            rv = createRandomNonTerminalNegativeShortInteger(model, level);
        } else if (type == Expression.INTEGER_TYPE) {
            rv = createRandomNonTerminalInteger(model, level);
        } else if (type == Expression.NUMERIC_TYPE) {
            rv = createRandomNonTerminalNumeric(model, level);
        } else {
            assert (type == Expression.FLOAT_QUOTE_TYPE || type == Expression.INTEGER_QUOTE_TYPE);
            rv = createRandomTerminal(type);
        }
        return rv;
    }

    /**
     * Creates a random terminal expression of the given type. A terminal
     * expression is one that does not have any children, e.g. a number
     * or a variable expression.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @return a randomly generated terminal expression
     */
    public Expression createRandomTerminal(int type) {
        return createRandomTerminal(type, this.NO_SUBTYPE);
    }

    /**
     * Creates a random terminal expression of the given type and subType. A terminal
     * expression is one that does not have any children, e.g. a number
     * or a variable expression.
     *
     * @param type the type of the expression, e.g. {@link Expression#BOOLEAN_TYPE}
     * @param subType the subType of the expression.
     * @return a randomly generated terminal expression
     */
    public Expression createRandomTerminal(int type, int subType) {
        int randomNumber = 0;
        switch(type) {
            case Expression.BOOLEAN_TYPE:
                randomNumber = random.nextInt(2);
                if (randomNumber == 0) {
                    return new NumberExpression(true);
                } else {
                    assert randomNumber == 1;
                    return new NumberExpression(false);
                }
            case Expression.FLOAT_TYPE:
                if (subType == this.SMOOTHING_CONSTANT_SUBTYPE) {
                    return new NumberExpression(0.01D + random.nextDouble() * (1.0D - 0.01D));
                }
                randomNumber = GPGondolaSelection.getRandomToGenerateTerminalFloat(allowHeld);
                if (randomNumber == 0) {
                    return new NumberExpression(50 - random.nextDouble() * 100);
                } else if (randomNumber == 1) {
                    return new GetVariableExpression("capital", Expression.FLOAT_TYPE);
                } else if (randomNumber == 2) {
                    assert allowHeld;
                    return new GetVariableExpression("stockcapital", Expression.FLOAT_TYPE);
                }
            case Expression.INTEGER_TYPE:
                if (subType == this.NEGATIVE_SHORT_INTEGER_SUBTYPE) {
                    return new NumberExpression(0 - random.nextInt(50));
                }
                randomNumber = GPGondolaSelection.getRandomToGenerateTerminalInteger(allowHeld, allowOrder);
                if (randomNumber == 0) {
                    if (subType == this.POSITIVE_SHORT_INTEGER_SUBTYPE) {
                        return new NumberExpression(50 - random.nextInt(50));
                    }
                    return new NumberExpression(50 - random.nextInt(100));
                } else if (randomNumber == 1) {
                    return new DayOfYearExpression();
                } else if (randomNumber == 2) {
                    return new MonthExpression();
                } else if (randomNumber == 3) {
                    return new DayExpression();
                } else if (randomNumber == 4) {
                    return new DayOfWeekExpression();
                } else if (randomNumber == 5) {
                    return new GetVariableExpression("daysfromstart", Expression.INTEGER_TYPE);
                } else if (randomNumber == 6) {
                    return new GetVariableExpression("transactions", Expression.INTEGER_TYPE);
                } else {
                    if (allowOrder && allowHeld) {
                        if (randomNumber == 7) {
                            return new GetVariableExpression("held", Expression.INTEGER_TYPE);
                        } else {
                            return new GetVariableExpression("order", Expression.INTEGER_TYPE);
                        }
                    } else if (allowHeld) {
                        return new GetVariableExpression("held", Expression.INTEGER_TYPE);
                    } else {
                        assert allowOrder;
                        return new GetVariableExpression("order", Expression.INTEGER_TYPE);
                    }
                }
            case Expression.FLOAT_QUOTE_TYPE:
                randomNumber = GPGondolaSelection.getRandomToGenerateFloatQuote();
                if (randomNumber == 0) {
                    return new QuoteExpression(Quote.DAY_OPEN);
                } else if (randomNumber == 1) {
                    return new QuoteExpression(Quote.DAY_HIGH);
                } else if (randomNumber == 2) {
                    return new QuoteExpression(Quote.DAY_LOW);
                } else {
                    assert randomNumber == 3;
                    return new QuoteExpression(Quote.DAY_CLOSE);
                }
            case Expression.INTEGER_QUOTE_TYPE:
                return new QuoteExpression(Quote.DAY_VOLUME);
            case Expression.NUMERIC_TYPE:
                randomNumber = GPGondolaSelection.getRandomToGenerateTerminalInteger(allowHeld, allowOrder);
                if (randomNumber % 2 == 0) {
                    randomNumber = GPGondolaSelection.getRandomToGenerateTerminalInteger(allowHeld, allowOrder);
                    if (randomNumber % 2 != 0) {
                        return new NumberExpression(0 - random.nextInt(50));
                    } else {
                        return new NumberExpression(50 - random.nextInt(100));
                    }
                } else {
                    if (subType == this.SMOOTHING_CONSTANT_SUBTYPE) {
                        return new NumberExpression(0.01D + random.nextDouble() * (1.0D - 0.01D));
                    }
                    randomNumber = GPGondolaSelection.getRandomToGenerateTerminalFloat(allowHeld);
                    if (randomNumber == 0) {
                        return new NumberExpression(50 - random.nextDouble() * 100);
                    } else if (randomNumber == 1) {
                        return new GetVariableExpression("capital", Expression.FLOAT_TYPE);
                    } else if (randomNumber == 2) {
                        assert allowHeld;
                        return new GetVariableExpression("stockcapital", Expression.FLOAT_TYPE);
                    }
                }
            default:
                System.out.println("Shouldnt get here: " + type);
                System.exit(0);
                assert false;
                return null;
        }
    }

    /**
     * Create a random non-terminal {@link Expression#BOOLEAN_TYPE} expression.
     *
     * @param model model expression
     * @param level tree level
     * @return randomly generated non-terminal boolean expression
     */
    private Expression createRandomNonTerminalBoolean(Expression model, int level) {
        int randomNumber = GPGondolaSelection.getRandomToGenerateBoolean();
        if (randomNumber == 0) {
            return new NotExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE));
        } else if (randomNumber == 1) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            return new EqualThanExpression(first, getChild(model, level, 1, first.getType()));
        } else if (randomNumber == 2) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            return new GreaterThanEqualExpression(first, getChild(model, level, 1, first.getType()));
        } else if (randomNumber == 3) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            Expression next = getChild(model, level, 1, first.getType());
            Expression retExp = new GreaterThanExpression(first, next);
            return retExp;
        } else if (randomNumber == 4) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            Expression next = getChild(model, level, 1, first.getType());
            Expression retExp = new LessThanEqualExpression(first, next);
            return retExp;
        } else if (randomNumber == 5) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            return new LessThanExpression(first, getChild(model, level, 1, first.getType()));
        } else if (randomNumber == 6) {
            Expression first = getChild(model, level, 0, Expression.NUMERIC_TYPE);
            return new NotEqualExpression(first, getChild(model, level, 1, first.getType()));
        } else if (randomNumber == 7) {
            return new AndExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE), getChild(model, level, 1, Expression.BOOLEAN_TYPE));
        } else {
            assert randomNumber == 8;
            Expression first = getChild(model, level, 0, Expression.BOOLEAN_TYPE);
            Expression next = getChild(model, level, 1, Expression.BOOLEAN_TYPE);
            Expression retExp = new OrExpression(first, next);
            return retExp;
        }
    }

    /**
     * Create a random non-terminal {@link Expression#FLOAT_TYPE} expression.
     *
     * @param model model expression
     * @param level tree level
     * @return randomly generated non-terminal float expression
     */
    private Expression createRandomNonTerminalFloat(Expression model, int level) {
        if (model != null && model instanceof NumberExpression && FAVOUR_NUMBER_PERCENT > random.nextInt(100)) {
            NumberExpression numberExpression = (NumberExpression) model;
            double step = random.nextDouble() * 6.0D;
            double value = Math.pow(10.0D, step);
            if (random.nextBoolean()) value = -value;
            numberExpression.setValue(numberExpression.getValue() + value);
            return numberExpression;
        }
        int randomNumber = GPGondolaSelection.getRandomToGenerateFloat();
        if (randomNumber == 0) {
            return createRandomTerminal(Expression.FLOAT_TYPE);
        } else if (randomNumber == 1) {
            return new AddExpression(getChild(model, level, 0, Expression.FLOAT_TYPE), getChild(model, level, 1, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 2) {
            return new SubtractExpression(getChild(model, level, 0, Expression.FLOAT_TYPE), getChild(model, level, 1, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 3) {
            return new MultiplyExpression(getChild(model, level, 0, Expression.FLOAT_TYPE), getChild(model, level, 1, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 4) {
            return new DivideExpression(getChild(model, level, 0, Expression.FLOAT_TYPE), getChild(model, level, 1, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 5) {
            return new PercentExpression(getChild(model, level, 0, Expression.FLOAT_TYPE), getChild(model, level, 1, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 6) {
            return new IfExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE), getChild(model, level, 1, Expression.FLOAT_TYPE), getChild(model, level, 2, Expression.FLOAT_TYPE));
        } else if (randomNumber == 7) {
            return new LagExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 8) {
            return new MinExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 9) {
            return new MaxExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 10) {
            return new SumExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 11) {
            return new SqrtExpression(getChild(model, level, 0, Expression.FLOAT_TYPE));
        } else if (randomNumber == 12) {
            return new AbsExpression(getChild(model, level, 0, Expression.FLOAT_TYPE));
        } else if (randomNumber == 13) {
            return new CosineExpression(getChild(model, level, 0, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 14) {
            return new SineExpression(getChild(model, level, 0, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 15) {
            return new LogarithmExpression(getChild(model, level, 0, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 16) {
            return new ExponentialExpression(getChild(model, level, 0, Expression.NUMERIC_TYPE));
        } else if (randomNumber == 17) {
            return new AvgExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 18) {
            return new EMAExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), createRandomTerminal(Expression.FLOAT_TYPE, this.SMOOTHING_CONSTANT_SUBTYPE));
        } else if (randomNumber == 19) {
            return new MACDExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 20) {
            return new MomentumExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 21) {
            return new RSIExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 22) {
            return new StandardDeviationExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 23) {
            return new BBLExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else {
            assert randomNumber == 24;
            return new BBUExpression(createRandomTerminal(Expression.FLOAT_QUOTE_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        }
    }

    private Expression createRandomNonTerminalNumeric(Expression model, int level) {
        int randomNumber = GPGondolaSelection.getRandomToGenerateInteger();
        if (randomNumber % 2 == 0) {
            return createRandomNonTerminalInteger(model, level);
        } else {
            return createRandomNonTerminalFloat(model, level);
        }
    }

    /**
     * Create a random non-terminal {@link Expression#INTEGER_TYPE} expression.
     *
     * @param model model expression
     * @param level tree level
     * @return randomly generated non-terminal integer expression
     */
    private Expression createRandomNonTerminalInteger(Expression model, int level) {
        Expression rv = null;
        if (model != null && model instanceof NumberExpression && FAVOUR_NUMBER_PERCENT > random.nextInt(100)) {
            NumberExpression numberExpression = (NumberExpression) model;
            double step = random.nextDouble() * 6.0D;
            double value = Math.pow(10.0D, step);
            if (random.nextBoolean()) value = -value;
            numberExpression.setValue(numberExpression.getValue() + value);
            return numberExpression;
        }
        int randomNumber = GPGondolaSelection.getRandomToGenerateInteger();
        if (randomNumber == 0) {
            rv = createRandomTerminal(Expression.INTEGER_TYPE);
        } else if (randomNumber == 1) {
            rv = new AddExpression(getChild(model, level, 0, Expression.INTEGER_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE));
        } else if (randomNumber == 2) {
            rv = new SubtractExpression(getChild(model, level, 0, Expression.INTEGER_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE));
        } else if (randomNumber == 3) {
            rv = new MultiplyExpression(getChild(model, level, 0, Expression.INTEGER_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE));
        } else if (randomNumber == 4) {
            rv = new DivideExpression(getChild(model, level, 0, Expression.INTEGER_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE));
        } else if (randomNumber == 5) {
            rv = new PercentExpression(getChild(model, level, 0, Expression.INTEGER_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE));
        } else if (randomNumber == 6) {
            rv = new IfExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE), getChild(model, level, 2, Expression.INTEGER_TYPE));
        } else if (randomNumber == 7) {
            rv = new LagExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 8) {
            rv = new MinExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 9) {
            rv = new MaxExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 10) {
            rv = new SumExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 11) {
            rv = new SqrtExpression(getChild(model, level, 0, Expression.INTEGER_TYPE));
        } else if (randomNumber == 12) {
            rv = new AbsExpression(getChild(model, level, 0, Expression.INTEGER_TYPE));
        } else if (randomNumber == 13) {
            rv = new AvgExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 14) {
            rv = new EMAExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), createRandomTerminal(Expression.FLOAT_TYPE, this.SMOOTHING_CONSTANT_SUBTYPE));
        } else if (randomNumber == 15) {
            rv = new MACDExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 16) {
            rv = new MomentumExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 17) {
            rv = new StandardDeviationExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 18) {
            rv = new BBLExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 19) {
            rv = new BBUExpression(new QuoteExpression(Quote.DAY_VOLUME), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else {
            assert randomNumber == 20;
            Expression child1 = getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE);
            Expression child2 = getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE);
            Expression child3 = getChild(model, level, 0, Expression.INTEGER_TYPE);
            rv = new OBVExpression(child1, child2, child3);
        }
        return rv;
    }

    /**
     * Create a random non-terminal {@link Expression#INTEGER_TYPE} expression.
     * The number should be a positive short integer
     * {@link Mutator#POSITIVE_SHORT_INTEGER_SUBTYPE}.
     *
     * @param model model expression
     * @param level tree level
     * @return randomly generated non-terminal integer expression
     */
    private Expression createRandomNonTerminalPositiveShortInteger(Expression model, int level) {
        if (model != null && model instanceof NumberExpression && FAVOUR_NUMBER_PERCENT > random.nextInt(100)) {
            NumberExpression numberExpression = (NumberExpression) model;
            double step = random.nextDouble() * 4.0D;
            double value = Math.pow(2.0D, step);
            numberExpression.setValue(numberExpression.getValue() + value);
            return numberExpression;
        }
        int randomNumber = GPGondolaSelection.getRandomToGeneratePositiveShortInteger();
        if (randomNumber == 0) {
            return createRandomTerminal(Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE);
        } else if (randomNumber == 1) {
            return new AddExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 2) {
            return new SubtractExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 3) {
            return new MultiplyExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 4) {
            return new DivideExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 5) {
            return new PercentExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 6) {
            return new IfExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 7) {
            return new SqrtExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 8) {
            return new AbsExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 9) {
            assert randomNumber == 10;
            return new MultiplyExpression(new NumberExpression(1), getChild(model, level, 1, Expression.FLOAT_TYPE));
        } else {
            assert randomNumber == 10;
            return createRandomNonTerminal(model, Expression.INTEGER_TYPE, this.NO_SUBTYPE, level);
        }
    }

    /**
     * Create a random non-terminal {@link Expression#INTEGER_TYPE} expression.
     * The number should be a negative small integer
     * {@link Mutator#NEGATIVE_SHORT_INTEGER_SUBTYPE}.
     *
     * @param model model expression
     * @param level tree level
     * @return randomly generated non-terminal integer expression
     */
    private Expression createRandomNonTerminalNegativeShortInteger(Expression model, int level) {
        if (model != null && model instanceof NumberExpression && FAVOUR_NUMBER_PERCENT > random.nextInt(100)) {
            NumberExpression numberExpression = (NumberExpression) model;
            double step = random.nextDouble() * 4.0D;
            double value = Math.pow(2.0D, step);
            numberExpression.setValue(numberExpression.getValue() - value);
            return numberExpression;
        }
        int randomNumber = GPGondolaSelection.getRandomToGenerateNegativeShortInteger();
        if (randomNumber == 0) {
            return createRandomTerminal(Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE);
        } else if (randomNumber == 1) {
            return new AddExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 2) {
            return new SubtractExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 3) {
            return new MultiplyExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 4) {
            return new DivideExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 5) {
            return new PercentExpression(getChild(model, level, 0, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.POSITIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 6) {
            return new IfExpression(getChild(model, level, 0, Expression.BOOLEAN_TYPE), getChild(model, level, 1, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE), getChild(model, level, 2, Expression.INTEGER_TYPE, this.NEGATIVE_SHORT_INTEGER_SUBTYPE));
        } else if (randomNumber == 7) {
            return new MultiplyExpression(new NumberExpression(-1), getChild(model, level, 1, Expression.FLOAT_TYPE));
        } else {
            assert randomNumber == 8;
            return new MultiplyExpression(new NumberExpression(-1), getChild(model, level, 1, Expression.INTEGER_TYPE));
        }
    }

    private Expression getChild(Expression model, int level, int arg) {
        if (model == null || arg >= model.getChildCount() || (model.getChild(arg).getType() != Expression.FLOAT_TYPE && model.getChild(arg).getType() != Expression.INTEGER_TYPE)) {
            int randomNumber = GPGondolaSelection.getRandomToGenerateFloatInteger();
            if (randomNumber == 0) {
                return createRandom(null, Expression.FLOAT_TYPE, this.NO_SUBTYPE, level);
            } else {
                assert randomNumber == 1;
                return createRandom(null, Expression.INTEGER_TYPE, this.NO_SUBTYPE, level);
            }
        } else {
            return model.getChild(arg);
        }
    }

    private Expression getChild(Expression model, int level, int arg, int type) {
        return getChild(model, level, arg, type, this.NO_SUBTYPE);
    }

    private Expression getChild(Expression model, int level, int arg, int type, int subType) {
        Expression rv;
        if (model == null || arg >= model.getChildCount() || model.getChild(arg).getType() != type) {
            if (model != null && arg < model.getChildCount() && type == Expression.NUMERIC_TYPE && (model.getChild(arg).getType() == Expression.INTEGER_TYPE || model.getChild(arg).getType() == Expression.FLOAT_TYPE)) {
                rv = model.getChild(arg);
            }
            rv = createRandom(null, type, subType, level);
        } else {
            rv = model.getChild(arg);
        }
        return rv;
    }

    /**
     * Randomly pick a node in the given expression.
     *
     * @param expression the expression to search
     * @return expression node
     */
    public Expression findRandomSite(Expression expression) {
        int randomNumber = random.nextInt(expression.size());
        Expression randomSite = null;
        for (Iterator iterator = expression.iterator(); iterator.hasNext(); ) {
            randomSite = (Expression) iterator.next();
            if (randomNumber-- <= 0) break;
        }
        assert randomSite != null;
        return randomSite;
    }

    /**
     * Randomly pick a node of the given type in the given expression.
     *
     * @param expression the expression node
     * @param type the type of the node, e.g. {@link Expression#BOOLEAN_TYPE}
     * @return expression node or <code>null</code> if one could not be found
     */
    public Expression findRandomSite(Expression expression, int type) {
        Expression randomSite = null;
        int possibleSites = expression.size(type);
        if (possibleSites > 0) {
            int randomNumber = random.nextInt(possibleSites);
            for (Iterator iterator = expression.iterator(); iterator.hasNext(); ) {
                randomSite = (Expression) iterator.next();
                if (randomSite.getType() == type) if (randomNumber-- <= 0) break;
            }
            assert randomSite != null;
        }
        return randomSite;
    }

    /**
     * Perform a deletion mutation on the given expression. Since the mutation
     * might chance the root of the expression, the updated root is
     * returned. The returned root may be the same as the one passed in.
     *
     * @param root the root of the expression being mutated
     * @param destination the destination site for the deletion
     * @return the new root of the expression
     */
    public Expression delete(Expression root, Expression destination) {
        return insert(root, destination, createRandomTerminal(destination.getType()));
    }

    /**
     * Perform an insertion mutation on the given expression.
     *
     * @param root the root of the expression being mutated
     * @param destination the destination site for the insertion
     * @param source the expression to insert
     * @return the new root of the expression
     * @see #delete(Expression root, Expression destination)
     */
    public Expression insert(Expression root, Expression destination, Expression source) {
        Expression parent = (Expression) destination.getParent();
        if (parent == null) {
            assert root == destination;
            return source;
        } else {
            int childNumber = parent.getIndex(destination);
            parent = ExpressionFactory.setChild(parent, source, childNumber);
            return root;
        }
    }

    /**
     * Perform a modification mutation on the given expression.
     *
     * @param root the root of the expression being mutated
     * @param destination the destination site for the modification
     * @return the new root of the expression
     * @see #delete(Expression root, Expression destination)
     */
    public Expression modify(Expression root, Expression destination) {
        if (destination == root) {
            Expression newExpression = createRandom(destination.getType(), this.NO_SUBTYPE, 1);
            newExpression = newExpression.simplify();
            return newExpression;
        } else {
            Expression newExpression = createRandom(destination, destination.getType(), this.NO_SUBTYPE, 1);
            newExpression = newExpression.simplify();
            return insert(root, destination, newExpression);
        }
    }

    /**
     * Possibly mutate the given expression.
     *
     * @param expression the root of the expression to modify
     * @return the new root of the expression
     */
    public Expression mutate(Expression expression) {
        return mutate(expression, MUTATION_PERCENT);
    }

    /**
     * Possibly mutate the given expression
     *
     * @param expression the root of the expression to modify
     * @param percent percent change of mutation
     * @return the new root of the expression
     */
    public Expression mutate(Expression expression, int percent) {
        int randomPercent = random.nextInt(100);
        if (percent < randomPercent) {
            return expression;
        }
        int randomTypePercent = random.nextInt(100);
        if (INSERTION_MUTATION_PERCENT > randomTypePercent) {
            expression = mutateByInsertion(expression);
        } else {
            randomTypePercent -= INSERTION_MUTATION_PERCENT;
            if (DELETION_MUTATION_PERCENT > randomTypePercent) {
                expression = mutateByDeletion(expression);
            } else {
                randomTypePercent -= DELETION_MUTATION_PERCENT;
                expression = mutateByModification(expression);
            }
        }
        return mutate(expression, EXTRA_MUTATION_PERCENT);
    }

    /**
     * Mutate the given expression by modification.
     *
     * @param expression the root of the expression to modify
     * @return the new root of the expression
     */
    private Expression mutateByModification(Expression expression) {
        Expression destination = findRandomSite(expression);
        return modify(expression, destination);
    }

    /**
     * Mutate the given expression by insertion.
     *
     * @param expression the root of the expression to modify
     * @return the new root of the expression
     */
    private Expression mutateByInsertion(Expression expression) {
        Expression destination = findRandomSite(expression);
        Expression insertSubTree = createRandom(destination.getType());
        return insert(expression, destination, insertSubTree);
    }

    /**
     * Mutate the given expression by deletion.
     *
     * @param expression the root of the expression to modify
     * @return the new root of the expression
     */
    private Expression mutateByDeletion(Expression expression) {
        Expression destination = findRandomSite(expression);
        if (destination.isRoot() || destination.getChildCount() == 0) {
            return mutateByModification(expression);
        } else {
            return delete(expression, destination);
        }
    }
}
