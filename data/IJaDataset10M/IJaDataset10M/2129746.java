package org.helianto.inventory.filter.classic;

import javax.persistence.Transient;
import org.helianto.core.Entity;
import org.helianto.core.User;
import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.core.filter.classic.AbstractUserBackedCriteriaFilter;
import org.helianto.process.Process;

/**
 * Card set filter.
 * 
 * @author Mauricio Fernandes de Castro
 */
public class CardSetFilter extends AbstractUserBackedCriteriaFilter {

    /**
	 * Factory method.
	 * 
	 * @param user
	 */
    public static CardSetFilter cardSetFilterFactory(User user) {
        CardSetFilter filter = new CardSetFilter();
        filter.setUser(user);
        return filter;
    }

    private static final long serialVersionUID = 1L;

    private long internalNumber;

    private Process process;

    private char cardType;

    /**
     * Default constructor.
     */
    public CardSetFilter() {
        super();
        setCardType(' ');
    }

    public void reset() {
        setCardType(' ');
    }

    public boolean isSelection() {
        return getInternalNumber() > 0;
    }

    @Override
    protected void doSelect(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendEqualFilter("internalNumber", getInternalNumber(), mainCriteriaBuilder);
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendEqualFilter("cardType", getCardType(), mainCriteriaBuilder);
        if (getProcess() != null) {
            appendEqualFilter("processDocument.id", getProcess().getId(), mainCriteriaBuilder);
        }
        appendOrderBy("internalNumber", mainCriteriaBuilder);
    }

    @Override
    protected final void appendEntityFilter(Entity entity, OrmCriteriaBuilder mainCriteriaBuilder) {
        mainCriteriaBuilder.appendSegment("entity.id", "=").append(entity.getId());
    }

    /**
	 * Internal number filter
	 */
    public long getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(long internalNumber) {
        this.internalNumber = internalNumber;
    }

    /**
	 * Process filter
	 */
    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    /**
	 * Process code filter.
	 */
    @Transient
    public String getProcessCode() {
        if (process != null) {
            return process.getDocCode();
        }
        return "";
    }

    /**
	 * Card type filter.
	 */
    public char getCardType() {
        return this.cardType;
    }

    public void setCardType(char cardType) {
        this.cardType = cardType;
    }
}
