package com.hack23.cia.model.api.sweden.content;

import java.util.Date;
import java.util.List;
import com.hack23.cia.model.api.sweden.common.ParliamentImportedData;
import com.hack23.cia.model.api.sweden.events.ParliamentYearData;

/**
 * The Interface CommitteeReportData.
 */
public interface CommitteeReportData extends ParliamentImportedData {

    /**
	 * Adds the ballot data.
	 * 
	 * @param ballot the ballot
	 */
    void addBallotData(BallotData ballot);

    /**
	 * Gets the ballots data.
	 * 
	 * @return the ballots data
	 */
    List<BallotData> getBallotsData();

    /**
	 * Gets the decision date.
	 * 
	 * @return the decision date
	 */
    Date getDecisionDate();

    /**
	 * Gets the href.
	 * 
	 * @return the href
	 */
    String getHref();

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    String getName();

    /**
	 * Sets the decision date.
	 * 
	 * @param decidedDateIfAny the new decision date
	 */
    void setDecisionDate(Date decidedDateIfAny);

    /**
	 * Sets the href.
	 * 
	 * @param hrefAttribute the new href
	 */
    void setHref(String hrefAttribute);

    /**
	 * Sets the name.
	 * 
	 * @param asText the new name
	 */
    void setName(String asText);

    /**
	 * Sets the parliament year data.
	 * 
	 * @param parliamentYear the new parliament year data
	 */
    void setParliamentYearData(ParliamentYearData parliamentYear);

    /**
	 * Sets the short code.
	 * 
	 * @param shortCode the new short code
	 */
    void setShortCode(String shortCode);
}
