package org.nomicron.suber.rest;

import org.nomicron.suber.constants.Parameters;
import org.nomicron.suber.model.object.BallotItem;
import com.dreamlizard.miles.text.RegexPatterns;
import javax.servlet.http.HttpServletRequest;

/**
 * UrlAdapter for Proposals.
 */
public class ProposalUrlAdapter extends BaseUrlAdapter {

    /**
     * Translate the url for the specified request.
     *
     * @param request HttpServletRequest
     * @return translated url
     */
    public String translateUrl(HttpServletRequest request) {
        String translatedUrl = getNotFoundUrl();
        String parameter = lastUrlSection(request);
        if (RegexPatterns.POSITIVE_INTEGER.matcher(parameter).matches()) {
            Integer proposalNumber = new Integer(parameter);
            BallotItem proposal = getMetaFactory().getBallotItemFactory().getBallotItemByProposalNumber(proposalNumber);
            if (proposal != null) {
                StringBuilder url = new StringBuilder();
                url.append(request.getContextPath());
                url.append(getRedirectUrl());
                url.append("?");
                url.append(Parameters.BALLOT_ITEM_ID);
                url.append("=");
                url.append(proposal.getId());
                translatedUrl = url.toString();
            }
        }
        return translatedUrl;
    }
}
