package net.sf.adatagenerator.ex.cdc1.groups;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;
import net.sf.adatagenerator.api.GeneratedPair.GeneratedDecision;
import net.sf.adatagenerator.api.ModifiablePair;
import net.sf.adatagenerator.api.ProcessingException;
import net.sf.adatagenerator.core.DefaultGroupSource;
import net.sf.adatagenerator.ex.cdc1.api.Cdc1Record;
import net.sf.adatagenerator.ex.cdc1.api.Cdc1Record.RecordField;
import net.sf.adatagenerator.ex.cdc1.api.GeneratedCdc1Record;
import net.sf.adatagenerator.ex.cdc1.bean.Cdc1GeneratorMap;
import net.sf.adatagenerator.ex.cdc1.bean.GeneratedCdc1RecordBean;
import com.choicemaker.shared.util.CMEqual;
import com.choicemaker.shared.util.CMReflectionUtils;
import com.choicemaker.shared.util.CMReflectionUtils.ACCESSOR_PREFIX;

/**
 * A source of pairs that represent the same child, in which the records have
 * identical last name, address and date of birth fields, and have first name
 * fields that have a nickname relationship.
 * 
 * @see NicknamedChild
 * @author rphall
 * 
 */
public class NicknamedChildSource extends DefaultGroupSource<GeneratedCdc1Record> {

    private static Logger logger = Logger.getLogger(IdenticalChildSource.class.getName());

    public static final int DEFAULT_MIN = 4;

    public static final int DEFAULT_MAX = 4;

    public static final GeneratedDecision DEFAULT_DECISION = GeneratedDecision.MATCH;

    /**
	 * Creates a source of pairs that uses default group settings.
	 * 
	 * @see #DEFAULT_MIN
	 * @see #DEFAULT_MAX
	 * @see #DEFAULT_TYPE
	 */
    public NicknamedChildSource() throws ProcessingException {
        this(createGroupParameters(DEFAULT_MIN, DEFAULT_MAX, DEFAULT_DISTRIBUTION));
    }

    /**
	 * Creates a source of pairs that uses the specified group settings; i.e.
	 * these setting are used to create a Properties instance which is passed to
	 * the {@link #NicknamedChildSource(Properties)} constructor.
	 * 
	 * @param min
	 *            the value for {@link #PN_MIN}; must be equal to or greater
	 *            than {@link #MINIMUM_MIN}.
	 * @param max
	 *            the value for {@link #PN_MAX}; must be equal to or greater
	 *            then <code>min</code>
	 * @param distribution
	 *            the value for {@link #PN_DISTRIBUTION}. If null or blank, the
	 *            value will be set to {@link #DEFAULT_DISTRIBUTION}.
	 * @see #createGroupParameters(int, int, String)
	 */
    public NicknamedChildSource(int min, int max, String distribution) throws ProcessingException {
        this(createGroupParameters(min, max, distribution));
    }

    /**
	 * Creates a source of pairs that uses the specified group settings.
	 * 
	 * @param params
	 *            properties that will be passed to the group creator; if null,
	 *            default properties will be used. If optional properties are
	 *            missing, default property values will be used.
	 */
    public NicknamedChildSource(Properties params) throws ProcessingException {
        super(new NicknamedChild(), GeneratedCdc1RecordBean.class, new Cdc1GeneratorMap(), params, DEFAULT_DECISION);
    }

    public boolean groupInvariant(ModifiablePair<? extends Cdc1Record> pair) throws IllegalStateException {
        final boolean ARE_NULLS_EQUAL = true;
        String msg = "";
        GeneratedCdc1Record left = (GeneratedCdc1Record) pair.getLeft();
        GeneratedCdc1Record right = (GeneratedCdc1Record) pair.getRight();
        if (!CMEqual.equal(left.getSiblingRole(), right.getSiblingRole(), ARE_NULLS_EQUAL)) {
            msg += "Sibling status differs/";
        }
        for (RecordField field : RecordField.values()) {
            if (field == RecordField.VacCode || field == RecordField.VacDate || field == RecordField.VacMfr || field == RecordField.VacName) {
                continue;
            }
            Method m = CMReflectionUtils.getFieldAccessor(Cdc1Record.class, field.name(), ACCESSOR_PREFIX.GET);
            try {
                Object leftValue = m.invoke(left, (Object[]) null);
                Object rightValue = m.invoke(left, (Object[]) null);
                if (!CMEqual.equal(leftValue, rightValue, ARE_NULLS_EQUAL)) {
                    msg += field.name() + " differs/";
                }
            } catch (Exception e) {
                String s = e.toString();
                if (e.getCause() != null) {
                    s += ": " + e.getCause().toString();
                }
                logger.severe(s);
                msg += s + "/";
            }
        }
        if (!msg.isEmpty()) {
            throw new IllegalStateException(msg);
        }
        return true;
    }
}
