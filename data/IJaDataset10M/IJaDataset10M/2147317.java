package org.jscsi.parser.datasegment;

import java.util.NoSuchElementException;
import java.lang.NumberFormatException;

/**
 * <h1>ResultFunctionFactory</h1> <p/> This factory creates a specific
 * <code>IResultFunction</code> instance for a given parameter.
 * 
 * @author Volker Wildi
 */
public final class ResultFunctionFactory {

    /**
   * The value of a result function to choose the and-value of the two
   * parameters.
   */
    private static final String RESULT_AND = "And";

    /** The value of a result function to choose the first two common values. */
    private static final String RESULT_CHOOSE = "Choose";

    /**
   * The value of a result function to choose the maximum value of the two
   * parameter values.
   */
    private static final String RESULT_MAX = "Max";

    /**
   * The value of a result function to choose the minimum value of the two
   * parameter values.
   */
    private static final String RESULT_MIN = "Min";

    /**
   * The value of a result function to choose none of the two parameter values.
   */
    private static final String RESULT_NONE = "None";

    /**
   * The value of a result function to choose the or-value of the two parameter
   * values.
   */
    private static final String RESULT_OR = "Or";

    /** This string represents the iSCSI boolean true. */
    private static final String BOOLEAN_YES = "Yes";

    /** This string represents the iSCSI boolean false. */
    private static final String BOOLEAN_NO = "No";

    /**
   * Creates an <code>IResultFunction</code> instance depending on the given
   * parameter.
   * 
   * @param result
   *          The type of the <code>IResultFunction</code>.
   * @return The <code>IResultFunction</code> instance.
   */
    public final IResultFunction create(final String result) {
        if (result.compareTo(RESULT_AND) == 0) {
            return new AndResultFunction();
        } else if (result.compareTo(RESULT_OR) == 0) {
            return new OrResultFunction();
        } else if (result.compareTo(RESULT_MIN) == 0) {
            return new MinResultFunction();
        } else if (result.compareTo(RESULT_MAX) == 0) {
            return new MaxResultFunction();
        } else if (result.compareTo(RESULT_CHOOSE) == 0) {
            return new ChooseResultFunction();
        } else if (result.compareTo(RESULT_NONE) == 0) {
            return new NoneResultFunction();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
   * <h1>NoneResultFunction</h1> <p/> The result of this class is nothing.
   * 
   * @author Volker Wildi
   */
    class NoneResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            return "";
        }
    }

    /**
   * <h1>MinResultFunction</h1> <p/> The result of this class is the minimum of
   * the input values.
   * 
   * @author Volker Wildi
   */
    class MinResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            try {
                final Integer ai = Integer.parseInt(a);
                final Integer bi = Integer.parseInt(b);
                if (ai <= bi) {
                    return a;
                } else {
                    return b;
                }
            } catch (NumberFormatException e) {
                return a;
            }
        }
    }

    /**
   * <h1>MaxResultFunction</h1> <p/> The result of this class is the maximum of
   * the input values.
   * 
   * @author Volker Wildi
   */
    class MaxResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            if (a.compareTo(b) >= 0) {
                return a;
            } else {
                return b;
            }
        }
    }

    /**
   * <h1>OrResultFunction</h1> <p/> The result of this class is the
   * <i>OR</i>-function of the input values.
   * 
   * @author Volker Wildi
   */
    class OrResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            if (a.compareTo(BOOLEAN_YES) == 0) {
                return BOOLEAN_YES;
            } else {
                if (b.compareTo(BOOLEAN_YES) == 0) {
                    return BOOLEAN_YES;
                } else {
                    return BOOLEAN_NO;
                }
            }
        }
    }

    /**
   * <h1>AndResultFunction</h1> <p/> The result of this class is the
   * <i>AND</i>-function of the input values.
   * 
   * @author Volker Wildi
   */
    class AndResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            if ((a.compareTo(BOOLEAN_YES) == 0) && (b.compareTo(BOOLEAN_YES) == 0)) {
                return BOOLEAN_YES;
            } else {
                return BOOLEAN_NO;
            }
        }
    }

    /**
   * <h1>ChooseResultFunction</h1> <p/> The result of this class is the first
   * common values of the list of the input values.
   * 
   * @author Volker Wildi
   */
    class ChooseResultFunction implements IResultFunction {

        /** {@inheritDoc} */
        public final String result(final String a, final String b) {
            final String[] aItems = a.split(",");
            final String[] bItems = b.split(",");
            for (String aItem : aItems) {
                for (String bItem : bItems) {
                    if (aItem.compareTo(bItem) == 0) {
                        return aItem;
                    }
                }
            }
            throw new NoSuchElementException();
        }
    }
}
