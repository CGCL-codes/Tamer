package ru.nsu.ccfit.pm.econ.common.engine.data;

import java.util.Collection;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 * Unmodifiable interface for company data objects.
 * @author dragonfly
 */
public interface IUCompany {

    /**
	 * Company identifier. This value should be unique amongst other companies
	 * within same game session and same scenario.
	 * @return Unique company identifier.
	 */
    long getId();

    /**
	 * Short company name.
	 * @return Company name.
	 */
    String getName();

    /**
	 * String, identifying company scope of activity. May be absent.
	 * @return Company type.
	 */
    @Nullable
    String getCompanyType();

    /**
	 * Company description (history, current position on the market, etc.). 
	 * May be lengthy and elaborate. May be absent as well.
	 * @return Company description.
	 */
    @Nullable
    String getDescription();

    /**
	 * Total amount of company shares on the market. 
	 * @return Total number of shares.
	 */
    @Nonnegative
    int getTotalSharesAmount();

    /**
	 * Current market value of a single company share. This value is based 
	 * on the latest transaction with the company shares.
	 * @return Share market value.
	 */
    @Nonnegative
    double getShareMarketValue();

    /**
	 * Expected profit of the company by the end of game period. 
	 * <p>This value is used by server and Teacher only. So, implementations
	 * may return 0 if called from client.</p>
	 * @return Expected profit by the end of the game period.
	 */
    double getExpectedProfit();

    /**
	 * Dividend payout ratio by the end of the previous game period. If game 
	 * has just started, corresponding value from game scenario is used.
	 * @return Dividend payout ratio by the end of the previous period.
	 */
    double getDividendPayoutRatio();

    /**
	 * Company profit just before the game started. This value is supplied 
	 * by game scenario.
	 * @return Company profit before the game started.
	 */
    double getProfitBeforeGameStart();

    /**
	 * Company profit by the end of the previous game period. If game has
	 * just started, returns the same value as {@link #getProfitBeforeGameStart()}.
	 * @return Company profit by the end of the previous period.
	 */
    double getProfitForPreviousPeriod();

    /**
	 * Company accounting period in game turns. This value should be more or
	 * equal to <tt>1</tt>.
	 * @return Accounting period in turns.
	 */
    @Nonnegative
    int getAccountingPeriod();

    /**
	 * Shows which part of company shares will be allocated to Teacher during
	 * initial share allocation. The rest of shares may then be distributed 
	 * among other players.
	 * <p>This value is used by server and scenario editor only so 
	 * implementations may return <tt>0</tt> when called from client.</p>
	 * @return Number in range [0; 1] showing which part of company shares 
	 * 			would be allocated to Teacher.
	 */
    @Nonnegative
    double getTeacherSharePart();

    /**
	 * List of published messages for this company. 
	 * <p>The collection should be ordered by publish time. All messages in this
	 * collection should also be in collection returned by 
	 * {@link #getAllMessages()}.</p>
	 * @return List of published messages.
	 */
    Collection<? extends IUTextOnlyCompanyMessage> getPublishedMessages();

    /**
	 * List of all available messages that apply to this company.
	 * @return List of all available messages for this company. Implementations
	 * 			may as well return <tt>null</tt> on client.
	 */
    @Nullable
    Collection<? extends IUCompanyMessage> getAllMessages();

    /**
	 * Set of all holdings of this company's shares.
	 * @return Set of company share holdings.
	 */
    Collection<? extends IUShareHolding> getShareHoldings();
}
