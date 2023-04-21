package dk.knord.s3f09.sportsclub.contract;

import java.util.Collection;

public interface ManageSportAndTeamCase {

    Collection<Sport> listSports() throws ClubBackEndException;

    Collection<Team> listTeams() throws ClubBackEndException;

    Collection<Team> listTeams(Sport sport) throws ClubBackEndException, ClubInstanceException;

    Collection<Team> listTeams(Member coach) throws ClubBackEndException, ClubInstanceException;

    void setSport(Long id, Sport sport) throws ClubBackEndException, ClubIdException, ClubInstanceException;

    Sport getSport(Long id) throws ClubBackEndException, ClubIdException;

    void setTeam(Long id, Team team) throws ClubBackEndException, ClubIdException, ClubInstanceException;

    Team getTeam(Long id) throws ClubBackEndException, ClubIdException;
}
