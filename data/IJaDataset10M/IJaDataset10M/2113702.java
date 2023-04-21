package de.boardgamesonline.bgo2.webserver.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Index;

/**
 * A <code>User</code> object represents a user of the boargamesonline webapp.
 * He has several personal data stored in the database.
 * 
 * @author Marc Lindenberg
 */
@Entity
public class User implements Serializable, SessionObject {

    /**
	 * Our serial version ID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The user's name.
	 */
    private String name;

    /**
	 * The user's password.
	 */
    private String password;

    /**
	 * The user's access code for accessing the site to enter a new password
	 * when he requested one via the
	 * {@link de.boardgamesonline.bgo2.webserver.wicket.RequestPasswordChangePage}.
	 */
    private String temporaryAccessCode;

    /**
	 * The user's email address.
	 */
    private String email;

    /**
	 * Whether the email address can be seen by other users.
	 */
    private boolean emailVisibile;

    /**
	 * The real name of the user.
	 */
    private String realName;

    /**
	 * The country the user lives in.
	 */
    private String country;

    /**
	 * The state the user lives in.
	 */
    private String state;

    /**
	 * The city the user lives in.
	 */
    private String city;

    /**
	 * The birth date of the user.
	 */
    private String birthDate;

    /**
	 * The gender of the user.
	 */
    private String sex;

    /**
	 * The favorite board game of the user.
	 */
    private String favoriteGame;

    /**
	 * The website of the user.
	 */
    private String homePage;

    /**
	 * Instant messaging contact information.
	 */
    private String instantMessaging;

    /**
	 * The number of wins of the player.
	 */
    private int wins;

    /**
	 * The number of games the user has played so far.
	 */
    private int playedGames;

    /**
	 * The total score sum of the player.
	 */
    private int score;

    /**
     * The user's session.
     */
    @Transient
    private String session;

    /**
	 * No-argument constructor as required by hibernate. It is private because
	 * generally a user must have a name, password and email.
	 */
    private User() {
    }

    /**
     * Creates a new user object.
	 * @param name
	 *            the nickname
	 * @param password
	 *            the password
	 * @param email
	 *            the emailadress
	 */
    public User(String name, String password, String email) {
        super();
        this.setName(name);
        this.setPasswordPublic(password);
        this.setEmail(email);
        this.setTemporaryAccessCode(null);
        this.setWins(0);
        this.setPlayedGames(0);
    }

    /**
     * Creates a new user object.
     * @param name
     *            the nickname
     * @param password
     *            the password
     * @param email
     *            the emailadress
     * @param session
     *            the session ID
     */
    public User(String name, String password, String email, String session) {
        this(name, password, email);
        setSession(session);
    }

    /**
     * Returns the user's name.
	 * @return the name
	 */
    @Id
    public String getName() {
        return this.name;
    }

    /**
     * Sets the user's name.
	 * @param name
	 *            the name to set
	 */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's password.
	 * @return the password
	 */
    public synchronized String getPassword() {
        return this.password;
    }

    /**
	 * This is used by hibernate to populate the password field with data from
	 * the database.
	 * 
	 * @param password
	 *            the password to set in already hashed form
	 */
    @SuppressWarnings("unused")
    private synchronized void setPassword(String password) {
        this.password = password;
    }

    /**
     * Hash and set the user's password.
	 * @param password
	 *            the password to set in non-hashed form (will be hashed before
	 *            assigned to the password field)
	 */
    public synchronized void setPasswordPublic(String password) {
        this.password = DataProvider.hashPassword(password);
    }

    /**
	 * @return the temporaryAccessCode
	 */
    public synchronized String getTemporaryAccessCode() {
        return this.temporaryAccessCode;
    }

    /**
	 * @param temporaryAccessCode
	 *            the temporaryAccessCode to set
	 */
    public synchronized void setTemporaryAccessCode(String temporaryAccessCode) {
        this.temporaryAccessCode = temporaryAccessCode;
    }

    /**
	 * Returns the user's email address.
	 * 
	 * @return The user's email address.
	 */
    public synchronized String getEmail() {
        return this.email;
    }

    /**
	 * Sets the user's new email address.
	 * 
	 * @param email
	 *            The email to set.
	 */
    public synchronized void setEmail(String email) {
        this.email = email;
    }

    /**
     * Indicates if the user's email address shall be visible.
	 * @return the emailVisibile
	 */
    public synchronized boolean isEmailVisibile() {
        return this.emailVisibile;
    }

    /**
     * Sets the user's email address visibility.
	 * @param emailVisibile
	 *            the emailVisibile to set
	 */
    public synchronized void setEmailVisibile(boolean emailVisibile) {
        this.emailVisibile = emailVisibile;
    }

    /**
     * Returns the user's birthdate.
	 * @return the birthDate
	 */
    public synchronized String getBirthDate() {
        return this.birthDate;
    }

    /**
     * Sets the user's birthdate.
	 * @param birthDate
	 *            the birthDate to set
	 */
    public synchronized void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the user's city.
	 * @return the city
	 */
    public synchronized String getCity() {
        return this.city;
    }

    /**
     * Sets the user's city.
	 * @param city
	 *            the city to set
	 */
    public synchronized void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the user's country.
	 * @return the country
	 */
    public synchronized String getCountry() {
        return this.country;
    }

    /**
     * Sets the user's country.
	 * @param country
	 *            the country to set
	 */
    public synchronized void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the user's gender.
	 * @return the sex
	 */
    public synchronized String getSex() {
        return sex;
    }

    /**
     * Sets the user's gender.
	 * @param sex
	 *            the sex to set
	 */
    public synchronized void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Returns the user's favorite game.
	 * @return the favoriteGame
	 */
    public synchronized String getFavoriteGame() {
        return this.favoriteGame;
    }

    /**
     * Sets the user's favorite game.
	 * @param favoriteGame
	 *            the favoriteGame to set
	 */
    public synchronized void setFavoriteGame(String favoriteGame) {
        this.favoriteGame = favoriteGame;
    }

    /**
     * Returns the user's homepage.
	 * @return the homePage
	 */
    public synchronized String getHomePage() {
        return this.homePage;
    }

    /**
     * Sets the user's homepage.
	 * @param homePage
	 *            the homePage to set
	 */
    public synchronized void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
     * Returns the user's instant messaging addresses.
	 * @return the instantMessaging
	 */
    public synchronized String getInstantMessaging() {
        return this.instantMessaging;
    }

    /**
     * Sets the user's instant messaging addresses.
	 * @param instantMessaging
	 *            the instantMessaging to set
	 */
    public synchronized void setInstantMessaging(String instantMessaging) {
        this.instantMessaging = instantMessaging;
    }

    /**
     * Returns the user's real name.
	 * @return the realName
	 */
    public synchronized String getRealName() {
        return this.realName;
    }

    /**
     * Sets the user's real name.
	 * @param realName
	 *            the realName to set
	 */
    public synchronized void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * Returns the user's state.
	 * @return the state
	 */
    public synchronized String getState() {
        return this.state;
    }

    /**
     * Sets the user's state.
	 * @param state
	 *            the state to set
	 */
    public synchronized void setState(String state) {
        this.state = state;
    }

    /**
	 * @return the playedGames
	 */
    @Index(name = "playedGamesIndex")
    public synchronized int getPlayedGames() {
        return this.playedGames;
    }

    /**
	 * @param playedGames the playedGames to set
	 */
    public synchronized void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    /**
	 * Convenience method to increase number of played games.
	 */
    public synchronized void incPlayedGames() {
        this.setPlayedGames(this.getPlayedGames() + 1);
    }

    /**
	 * @return the wins
	 */
    @Index(name = "winsIndex")
    public synchronized int getWins() {
        return this.wins;
    }

    /**
	 * @param wins the wins to set
	 */
    public synchronized void setWins(int wins) {
        this.wins = wins;
    }

    /**
	 * Convenience method to increase number of wins.
	 */
    public synchronized void incWin() {
        this.setWins(this.getWins() + 1);
    }

    /**
	 * @return the score
	 */
    @Index(name = "scoreIndex")
    public synchronized int getScore() {
        return this.score;
    }

    /**
	 * @param score the score to set
	 */
    public synchronized void setScore(int score) {
        this.score = score;
    }

    /**
	 * Convenience method to increase number of wins.
	 * @param toAdd The score to add to the user's total score.
	 */
    public synchronized void addToScore(int toAdd) {
        this.setScore(this.getScore() + toAdd);
    }

    /**
	 * Compares this user object with a given object. Comparison is based on
	 * user name and session.
	 * 
	 * @param obj
	 *            The object to compare with.
	 * @return <em>True</em> iff <code>obj</code> is of type {@link User}
	 *         and equal to <em>this</em>.
	 */
    public boolean equals(Object obj) {
        return (obj instanceof User) && (this.getName().equalsIgnoreCase(((User) obj).getName()));
    }

    /**
	 * @return The hashcode of this user object.
	 */
    public int hashCode() {
        return User.class.hashCode() + this.getName().hashCode();
    }

    /**
	 * Saves the actual state of this <code>User</code> object to the
	 * database.
	 * 
	 * @return <em>True</em> iff <em>this</em> was successfully saved
	 * 			to the database.
	 */
    public synchronized boolean save() {
        Session hibernateSession = DataProvider.getInstance().getHibernateSessionFactory().getCurrentSession();
        hibernateSession.beginTransaction();
        try {
            hibernateSession.update(this);
            hibernateSession.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            hibernateSession.getTransaction().rollback();
            return false;
        }
    }

    /**
     * Returns the user's session ID.
     * @return The user's session ID.
     */
    @Transient
    public String getSession() {
        return session;
    }

    /**
     * Sets the user's session ID.
     * @param session The user's sessoin ID:
     */
    public void setSession(String session) {
        this.session = session;
    }
}
