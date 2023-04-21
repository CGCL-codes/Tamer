package com.hack23.cia.service.impl.admin.agent.sweden.api;

import com.hack23.cia.model.impl.sweden.ParliamentMember;
import com.hack23.cia.model.impl.sweden.RegisterInformation;

/**
 * The Interface ParliamentMemberRegisterAgent.
 */
public interface ParliamentMemberRegisterAgent extends GenericParliamentDataAgent<RegisterInformation> {

    /**
     * Inits the.
     * 
     * @throws Exception the exception
     */
    void init() throws Exception;

    /**
     * Lookup register information.
     * 
     * @param member the member
     * 
     * @return the register information
     */
    RegisterInformation lookupRegisterInformation(ParliamentMember member);
}
