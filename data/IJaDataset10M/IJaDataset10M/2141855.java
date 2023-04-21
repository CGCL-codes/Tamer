package net.sf.ofx4j.client;

import net.sf.ofx4j.OFXException;
import net.sf.ofx4j.domain.data.investment.accounts.InvestmentAccountDetails;
import net.sf.ofx4j.domain.data.investment.statements.InvestmentStatementResponse;
import net.sf.ofx4j.domain.data.seclist.SecurityList;
import net.sf.ofx4j.domain.data.seclist.SecurityListResponse;
import net.sf.ofx4j.domain.data.seclist.SecurityRequest;
import java.util.Date;
import java.util.List;

/**
 * @author Jon Perlow
 */
public interface InvestmentAccount extends FinancialInstitutionAccount {

    InvestmentStatementResponse readStatement(Date start, Date end) throws OFXException;

    /**
   * Reads a list of securities from the brokerage
   *
   * @param securities the securities to read
   * @return The security response containing the security infos
   * @throws OFXException if there's an error talking to the brokerage
   */
    SecurityList readSecurityList(List<SecurityRequest> securities) throws OFXException;

    /**
   * The details of the account.
   *
   * @return The details of the account.
   */
    InvestmentAccountDetails getDetails();
}
