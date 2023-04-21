package org.wcb.model.dao;

import org.wcb.model.vo.hibernate.Logbook;
import org.wcb.exception.InfrastructureException;
import org.springframework.dao.DataAccessException;
import java.util.List;
import java.util.Date;

/**
 * <small>
 * Copyright (c)  2006  wbogaardt.
 * Permission is granted to copy, distribute and/or modify this document
 * under the terms of the GNU Free Documentation License, Version 1.2
 * or any later version published by the Free Software Foundation;
 * with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
 * Texts.  A copy of the license is included in the section entitled "GNU
 * Free Documentation License".
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Mar 24, 2006 10:32:28 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Mar 24, 2006
 *          Time: 10:32:28 AM
 */
public interface ILogbookDAO extends IDao {

    List<Logbook> findAll() throws InfrastructureException, DataAccessException;

    List<Logbook> findByRegistration(String registration) throws InfrastructureException;

    List<Logbook> findAllCrossCountry() throws InfrastructureException;

    List<Logbook> findNightTime() throws InfrastructureException;

    List<Logbook> findNightTimeCurrent(Date sDate) throws InfrastructureException;

    List<Logbook> findIFRApproaches() throws InfrastructureException;

    List<Logbook> findIFRApproaches(Date sDate) throws InfrastructureException;

    Logbook findEntry(Integer id) throws InfrastructureException;

    List<Logbook> findByDateRange(Date sDate, Date eDate) throws InfrastructureException;

    void deleteEntry(Logbook oEntry) throws InfrastructureException;
}
