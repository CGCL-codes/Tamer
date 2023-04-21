package vh.data.club;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import net.virtualhockey.data.UserClubAffiliation;
import net.virtualhockey.infrastructure.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;
import vh.data.contest.VHLeague;
import vh.data.user.VHUser;
import vh.error.VHException;

/**
 * Represents a manageable hockey team.
 * 
 * @version $Id: VHClub.java 48 2006-10-31 20:49:34Z janjanke $
 * @author jankejan
 */
@Entity
@Table(name = "club")
public class VHClub implements Comparable<VHClub> {

    /**
   * Unique 6 characater club code in the form CC-AAA where CC is the official ISO two
   * letter country code and AAA the abbreviation of the club.
   */
    @Id
    @Column(name = "id")
    private String d_strId;

    /**
   * Two letter ISO country code of the club.
   */
    @Column(name = "country")
    private String d_strCountry;

    /**
   * Official club name
   */
    @Column(name = "name")
    private String d_strName;

    /**
   * The Id of 32x32 pixel club logo.
   */
    @Column(name = "image_id")
    private int d_nImageId;

    /**
   * The national league, the club is currently a member of.
   */
    @ManyToOne
    @JoinColumn(name = "current_league_id")
    private VHLeague d_currentLeague;

    /**
   * The user that currently manages this club.
   */
    @ManyToOne
    @JoinColumn(name = "current_user_id")
    private VHUser d_currentUser;

    /** Default no argument constructor. */
    protected VHClub() {
    }

    /**
   * Returns the national league, this club is currently attached to.
   */
    public VHLeague getCurrentLeague() {
        return d_currentLeague;
    }

    /**
   * Returns the user that is currently managing the club.
   */
    public VHUser getCurrentUser() {
        return d_currentUser;
    }

    /**
   * Sets the user that is currently managing the club.
   * 
   * @param user the new manager for this club
   */
    public void setCurrentUser(VHUser user) {
        d_currentUser = user;
    }

    /**
   * Returns the currently active UserClubAffiliation object related to this VHClub.
   * 
   * @throws VHException if either no {@link UserClubAffiliation} exists or more than one
   *         active affiliation was found
   */
    public UserClubAffiliation getActiveUserClubAffiliation() throws VHException {
        Criteria criteria = HibernateUtil.getSession().createCriteria(UserClubAffiliation.class);
        criteria.add(Expression.eq("d_currentUser", getCurrentUser()));
        criteria.add(Expression.eq("d_club", this));
        criteria.add(Expression.eq("d_active", true));
        try {
            UserClubAffiliation uca = (UserClubAffiliation) criteria.uniqueResult();
            if (uca == null) throw new VHException("No active UserClubAffiliation found for club " + getId());
            return uca;
        } catch (HibernateException ex) {
            throw new VHException("More than one active UserClubAffiliation found for club " + getId());
        }
    }

    /**
   * Returns the official unique club code. The club code has the form CC-AAA where CC is
   * the official ISO two letter country code and AAA the abbreviation of the club.
   */
    public String getId() {
        return d_strId;
    }

    /**
   * Returns the code of the country the club is registered in. The returned code
   * corresponds to the official two letter ISO country code.
   */
    public String getCountry() {
        return d_strCountry;
    }

    /**
   * Returns the club's full name.
   */
    public String getName() {
        return d_strName;
    }

    /**
   * Returns the ID of the 32 x 32 pixel club logo image.
   */
    public int getImageId() {
        return d_nImageId;
    }

    /**
   * The natural ordering of VHClub's is alphabetically by their respective names.
   * 
   * @see java.lang.Comparable#compareTo(T)
   */
    public int compareTo(VHClub club) {
        return d_strName.compareToIgnoreCase(club.getName());
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return d_strId.equals(((VHClub) obj).d_strId);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return d_strId.hashCode();
    }
}
