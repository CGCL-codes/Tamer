package com.hack23.cia.service.impl.control.chartservice.api;

import java.util.List;
import com.hack23.cia.model.api.application.configuration.AgencyData;
import com.hack23.cia.model.api.application.content.BinaryContent;
import com.hack23.cia.model.api.application.content.LanguageData;
import com.hack23.cia.model.api.sweden.configuration.CommitteeData;
import com.hack23.cia.model.api.sweden.content.BallotData;
import com.hack23.cia.model.api.sweden.content.PoliticalPartyData;

/**
 * The Interface CommitteeChartService.
 */
public interface CommitteeChartService extends AbstractChartService {

    /**
	 * Generate committee charts.
	 * 
	 * @param agency the agency
	 * @param committee the committee
	 * @param allBallots the all ballots
	 * @param allPoliticalParties the all political parties
	 * @param language the language
	 * 
	 * @return the list< binary content>
	 */
    List<BinaryContent> generateCommitteeCharts(AgencyData agency, CommitteeData committee, List<BallotData> allBallots, List<PoliticalPartyData> allPoliticalParties, LanguageData language);
}
