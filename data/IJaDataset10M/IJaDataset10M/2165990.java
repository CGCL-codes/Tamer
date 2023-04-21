package blms.userLeague;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import blms.dataBase.Factory;
import blms.dataBase.LeagueDao;
import blms.dataBase.UserDao;
import blms.dataBase.UserLeagueDao;
import blms.exceptions.DBException;
import blms.exceptions.InvalidParameterException;
import blms.league.League;
import blms.league.LeagueManager;
import blms.users.User;
import blms.users.UsersManager;
import blms.util.LeagueUtil;
import blms.util.Util;

/**
 * This class is responsible for all the operations that can be performed
 * in a relationship between a certain user and a certain league
 *
 */
public class UserLeagueManager {

    private UserLeagueDao userLeagueDao;

    private UsersManager usersManager = null;

    private LeagueManager leagueManager = null;

    public static UserLeagueManager userLeagueManager = null;

    /**
     * Constructor 
     * @throws blms.exceptions.DBException Exception thrown if an error occur while instantiating the DAO
     */
    private UserLeagueManager() throws DBException {
        userLeagueDao = Factory.createUserLeagueDao();
        usersManager = UsersManager.getInstance();
        leagueManager = LeagueManager.getInstance();
    }

    /**
     * users-league relationship
     * 
     * @throws DBException Exception thrown if an error occur while loading the
     * persistent data
     */
    public static UserLeagueManager getInstance() throws DBException {
        if (userLeagueManager == null) {
            userLeagueManager = new UserLeagueManager();
        }
        return userLeagueManager;
    }

    /**
     * 
     * This method is responsible for including a user in a 
     * particular league
     * @param user The id of the user
     * @param league The id of the league
     * @param initialHandicap The initial value for the user handicap
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     * @throws blms.exceptions.DBException Exception thrown if an error occur while searching in the leagues persistent information
     * 
     */
    public void joinLeague(Long user, Long league, String initialHandicap) throws InvalidParameterException, DBException {
        Long handicap = verifyHandicap(initialHandicap);
        userLeagueDao.saveUserLeague(new UserLeague(user, league, handicap));
    }

    /**
     * This method is responsible for finding the collection 
     * of leagues which a particular user is inserted
     * @param userId The id of the user
     * @return The leagues that the user is inserted
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     * @throws blms.exceptions.DBException Exception thrown if an error occur while searching in the leagues persistent information
     */
    public Collection<League> getPlayerLeagues(String userId) throws InvalidParameterException, DBException {
        Long id = Util.verifyId(userId, Util.USER_ERROR);
        usersManager.findUserById(id);
        List<Long> playerLeagues = userLeagueDao.findPlayerLeagues(id);
        Collection<League> leagues = new ArrayList<League>();
        LeagueDao leagueDao = Factory.createLeagueDao();
        for (Long leagueId : playerLeagues) {
            leagues.add(leagueDao.findLeagueByID(leagueId));
        }
        for (League league : leagueDao.getAllLeagues()) {
            if (league.getOperator().equals(id)) {
                leagues.add(league);
            }
        }
        return leagues;
    }

    /**
     * This method is responsible for finding the members of the league
     * @param leagueId The id of the league
     * @return The members of the league
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     * @throws blms.exceptions.DBException Exception thrown if an error occur while searching in the leagues persistent information
     */
    public Collection<User> getLeagueMembers(String leagueId) throws InvalidParameterException, DBException {
        Long id = Util.verifyId(leagueId, Util.LEAGUE_ERROR);
        UserDao userDao = Factory.createUserDao();
        Collection<User> users = new ArrayList<User>();
        League league = Factory.createLeagueDao().findLeagueByID(id);
        if (league == null) {
            throw new InvalidParameterException("Unknown League");
        }
        List<Long> members = userLeagueDao.findLeagueMembers(id);
        for (Long memId : members) {
            users.add(userDao.findUserByID(memId));
        }
        return users;
    }

    /**
     * This method is responsible for verifying whether a 
     * user is a member of a league
     * @param userId The id of the user
     * @param leagueId The id of the user
     * @return true if is a member, false otherwise
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     * @throws blms.exceptions.DBException Exception thrown if an error occur while searching in the leagues persistent information
     */
    public boolean isLeagueMember(String userId, String leagueId) throws InvalidParameterException, DBException {
        Long usId = Util.verifyId(userId, Util.USER_ERROR);
        Long leId = Util.verifyId(leagueId, Util.LEAGUE_ERROR);
        usersManager.findUserById(usId);
        leagueManager.getLeagueAttribute(leId + "", LeagueUtil.NAME);
        List<Long> members = userLeagueDao.findLeagueMembers(leId);
        Long opId = Factory.createLeagueDao().findLeagueByID(leId).getOperator();
        return members.contains(usId) || opId.equals(usId);
    }

    /**
     * This method will look for the value of a certain attribute of the 
     * referenced user of the league
     * @param user The id of the user
     * @param league The id of the league
     * @param attribute name of the attribute
     * @return the value of the attribute sought
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     * @throws DBException Exception thrown if an error occur while searching for the persisten information 
     */
    public Object getUserLeagueAttribute(Long user, Long league, String attribute) throws InvalidParameterException, DBException {
        UserLeague userLeague = verifyUserLeague(user, league);
        return userLeague.getAttribute(attribute);
    }

    /**
     * This method is responsible for the exclusion of a 
     * specific user from a league
     * @param userId The id of the user
     * @param leagueId The id of the league
     * @throws blms.exceptions.DBException Exception thrown if an error occur while searching in the leagues persistent information
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     */
    public void leaveLeague(String userId, String leagueId) throws DBException, InvalidParameterException {
        Long league = Util.verifyId(leagueId, Util.LEAGUE_ERROR);
        Long user = Util.verifyId(userId, Util.USER_ERROR);
        usersManager.findUserById(user);
        leagueManager.getLeagueAttribute(league + "", LeagueUtil.NAME);
        if (Factory.createLeagueDao().isLeagueOperator(user, league)) {
            throw new InvalidParameterException("Operator cannot leave league");
        }
        verifyUserLeague(user, league);
        userLeagueDao.leaveLeague(user, league);
    }

    /**
     * This method is responsible for changing the database being used to
     * manipulate the relationships
     * @param databaseName The new database to be used
     * @throws DBException Exception thrown if an error occur while changing the databases
     */
    public void setDataBase(String databaseName) throws DBException {
        userLeagueDao.setDataBase(databaseName);
    }

    /**
     * This method is responsible for verifying if the handicap is valid
     * @param initialHandicap The value of handicap
     * @return True if is valid, false otherwise
     * @throws blms.exceptions.InvalidParameterException Exception thrown if an invalid id is passed as a parameter
     */
    private Long verifyHandicap(String initialHandicap) throws InvalidParameterException {
        try {
            Long handicap = Long.valueOf(initialHandicap);
            if (handicap.compareTo(new Long(0)) < 0) {
                throw new InvalidParameterException("Handicap cant be negative");
            }
            return handicap;
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Must provide initial player handicap");
        }
    }

    /**
     * This method is responsible for verifying if already exists a relationship
     * between a given user and a given league
     * @param userId The id of the user
     * @param leagueId The id of the league
     * @return The relationship information between the user and the league
     * @throws InvalidParameterException Exception thrown if the user has not previously joined the league
     */
    public UserLeague verifyUserLeague(Long userId, Long leagueId) throws InvalidParameterException {
        UserLeague userLeague = userLeagueDao.findUserLeague(userId, leagueId);
        if (userLeague == null) {
            throw new InvalidParameterException("User is not a league member");
        }
        return userLeague;
    }

    /**
     * This method cleans all relationships between users and leagues
     * that exists in the database
     */
    public void removeAllRelationships() throws DBException {
        userLeagueDao.clearDB();
    }
}
