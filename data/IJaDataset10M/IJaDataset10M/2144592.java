package com.catarak.uwhscoretime.model;

import junit.framework.TestCase;

/**
 * Base class to setup common match properties
 *
 * @author sfbouchard
 */
public abstract class AbstractMatchTestUtil extends TestCase {

    protected Team whiteTeam;

    protected Team blackTeam;

    protected TeamMatch whiteTeamMatch;

    protected TeamMatch blackTeamMatch;

    protected MatchTemplate shortMatchTemplate;

    protected Period fh;

    protected Period mb;

    protected Period sh;

    @Override
    protected void setUp() throws Exception {
        whiteTeam = new Team();
        blackTeam = new Team();
        whiteTeamMatch = new TeamMatch(whiteTeam);
        blackTeamMatch = new TeamMatch(blackTeam);
        shortMatchTemplate = new MatchTemplate();
        shortMatchTemplate.setName("Short Match Period");
        shortMatchTemplate.addPeriod(new Period("1st Half", new Time(0, 2), Period.PeriodType.HALF));
        shortMatchTemplate.addPeriod(new Period("Half pause", new Time(0, 2), Period.PeriodType.BREAK));
        shortMatchTemplate.addPeriod(new Period("2nd Half", new Time(0, 3), Period.PeriodType.POSSIBLE_END_OF_MATCH_HALF));
        shortMatchTemplate.addPeriod(new Period("Overtime break", new Time(0, 2), Period.PeriodType.BREAK));
        shortMatchTemplate.addPeriod(new Period("1st overtime", new Time(0, 2), Period.PeriodType.HALF));
        shortMatchTemplate.addPeriod(new Period("Overtime switch", new Time(0, 2), Period.PeriodType.BREAK));
        shortMatchTemplate.addPeriod(new Period("2nd overtime", new Time(0, 2), Period.PeriodType.POSSIBLE_END_OF_MATCH_HALF));
    }
}
