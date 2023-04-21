package nl.burgerweeshuis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * An activity represents anything from a movie night or pop concert to eg a
 * meeting.
 */
@Entity
@Table(name = "bwh_activity")
public class Activity extends AbstractEntity {

    private String title;

    private String subtitle;

    private String description;

    private Date activityDate;

    private Integer opensOnHours;

    private Integer opensOnMinutes;

    private Integer beginsOnHours = Integer.valueOf(21);

    private Integer beginsOnMinutes = Integer.valueOf(30);

    private List<NavigationCategory> navigationCategories = new ArrayList<NavigationCategory>();

    private List<ActivityCategory> activityCategories = new ArrayList<ActivityCategory>();

    private List<Note> notes = new ArrayList<Note>();

    /** dups with ticketOptions, but is here to allow quick selection. */
    private TicketOption internetReservation;

    private List<TicketOption> ticketOptions;

    /** activity resources. */
    private Resources resources;

    private Venue venue;

    private Long id;

    /**
	 * Returns the unique identifier.
	 * 
	 * @return the unique identifier.
	 */
    @Id(generate = GeneratorType.AUTO)
    public Long getId() {
        return id;
    }

    /**
	 * Sets the unique identifier.
	 * 
	 * @param id
	 *            the unique identifier
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Gets the description.
	 * 
	 * @return description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description.
	 * 
	 * @param description
	 *            description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Gets the subtitle.
	 * 
	 * @return subtitle
	 */
    public String getSubtitle() {
        return subtitle;
    }

    /**
	 * Sets the subtitle.
	 * 
	 * @param subtitle
	 *            subtitle
	 */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
	 * Gets the title.
	 * 
	 * @return title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the title.
	 * 
	 * @param title
	 *            title
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Gets the navigation categories.
	 * 
	 * @return navigation categories
	 */
    @ManyToMany()
    @JoinTable(table = @Table(name = "bwh_activity_navigation_category"), joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = { @JoinColumn(name = "nav_category_id") })
    public List<NavigationCategory> getNavigationCategories() {
        return navigationCategories;
    }

    /**
	 * Sets the navigation categories.
	 * 
	 * @param categories
	 *            navigation categories
	 */
    public void setNavigationCategories(List<NavigationCategory> categories) {
        this.navigationCategories = categories;
    }

    /**
	 * Gets the activity categories.
	 * 
	 * @return activity categories
	 */
    @ManyToMany()
    @OrderBy("name")
    @JoinTable(table = @Table(name = "bwh_activity_activity_category"), joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = { @JoinColumn(name = "activity_category_id") })
    public List<ActivityCategory> getActivityCategories() {
        return activityCategories;
    }

    /**
	 * Sets the activity categories.
	 * 
	 * @param categories
	 *            activity categories
	 */
    public void setActivityCategories(List<ActivityCategory> categories) {
        this.activityCategories = categories;
    }

    /**
	 * Add activity category.
	 * 
	 * @param activityCategory
	 */
    public void add(ActivityCategory activityCategory) {
        if (activityCategories == null) {
            activityCategories = new ArrayList<ActivityCategory>();
        }
        activityCategories.add(activityCategory);
    }

    /**
	 * Whether this activity contains the provided category.
	 * @param activityCategory
	 * @return true if this contains the category
	 */
    public boolean contains(ActivityCategory activityCategory) {
        return (activityCategories != null && activityCategories.contains(activityCategory));
    }

    /**
	 * Remove activity category.
	 * 
	 * @param activityCategory
	 */
    public void remove(ActivityCategory activityCategory) {
        if (activityCategories != null) {
            activityCategories.remove(activityCategory);
        }
    }

    /**
	 * Gets the notes.
	 * 
	 * @return notes
	 */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    public List<Note> getNotes() {
        return notes;
    }

    /**
	 * Sets the notes.
	 * 
	 * @param notes
	 *            notes
	 */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /**
	 * Gets the activityDate.
	 * 
	 * @return activityDate
	 */
    public Date getActivityDate() {
        return activityDate;
    }

    /**
	 * Sets the activityDate.
	 * 
	 * @param activityDate
	 *            activityDate
	 */
    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    /**
	 * Gets the beginsOnHours.
	 * 
	 * @return beginsOnHours
	 */
    public Integer getBeginsOnHours() {
        return beginsOnHours;
    }

    /**
	 * Sets the beginsOnHours.
	 * 
	 * @param beginsOnHours
	 *            beginsOnHours
	 */
    public void setBeginsOnHours(Integer beginsOnHours) {
        this.beginsOnHours = beginsOnHours;
    }

    /**
	 * Gets the beginsOnMinutes.
	 * 
	 * @return beginsOnMinutes
	 */
    public Integer getBeginsOnMinutes() {
        return beginsOnMinutes;
    }

    /**
	 * Sets the beginsOnMinutes.
	 * 
	 * @param beginsOnMinutes
	 *            beginsOnMinutes
	 */
    public void setBeginsOnMinutes(Integer beginsOnMinutes) {
        this.beginsOnMinutes = beginsOnMinutes;
    }

    /**
	 * Gets the opensOnHours.
	 * 
	 * @return opensOnHours
	 */
    public Integer getOpensOnHours() {
        return opensOnHours;
    }

    /**
	 * Sets the opensOnHours.
	 * 
	 * @param opensOnHours
	 *            opensOnHours
	 */
    public void setOpensOnHours(Integer opensOnHours) {
        this.opensOnHours = opensOnHours;
    }

    /**
	 * Gets the opensOnMinutes.
	 * 
	 * @return opensOnMinutes
	 */
    public Integer getOpensOnMinutes() {
        return opensOnMinutes;
    }

    /**
	 * Sets the opensOnMinutes.
	 * 
	 * @param opensOnMinutes
	 *            opensOnMinutes
	 */
    public void setOpensOnMinutes(Integer opensOnMinutes) {
        this.opensOnMinutes = opensOnMinutes;
    }

    /**
	 * Gets the internetReservation.
	 * 
	 * @return internetReservation
	 */
    @ManyToOne()
    @JoinColumn(name = "internet_reservation_id")
    public TicketOption getInternetReservation() {
        return internetReservation;
    }

    /**
	 * Sets the internetReservation.
	 * 
	 * @param internetReservation
	 *            internetReservation
	 */
    public void setInternetReservation(TicketOption internetReservation) {
        this.internetReservation = internetReservation;
    }

    /**
	 * Gets the ticketOptions.
	 * 
	 * @return ticketOptions
	 */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_options_id")
    public List<TicketOption> getTicketOptions() {
        return ticketOptions;
    }

    /**
	 * Sets the ticketOptions.
	 * 
	 * @param ticketOptions
	 *            ticketOptions
	 */
    public void setTicketOptions(List<TicketOption> ticketOptions) {
        this.ticketOptions = ticketOptions;
    }

    /**
	 * Adds a ticket option.
	 * 
	 * @param ticketOption
	 */
    public void addTicketOption(TicketOption ticketOption) {
        if (ticketOptions == null) {
            ticketOptions = new ArrayList<TicketOption>();
        }
        ticketOptions.add(ticketOption);
    }

    /**
	 * Removes a ticket option.
	 * 
	 * @param ticketOption
	 */
    public void removeTicketOption(TicketOption ticketOption) {
        if (ticketOptions != null) {
            ticketOptions.remove(ticketOption);
        }
    }

    /**
	 * Gets the venue.
	 * 
	 * @return venue
	 */
    @ManyToOne()
    @JoinColumn(name = "venue_id")
    public Venue getVenue() {
        return venue;
    }

    /**
	 * Sets the venue.
	 * 
	 * @param venue
	 *            venue
	 */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
	 * Gets resources.
	 * @return resources
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id")
    public Resources getResources() {
        return resources;
    }

    /**
	 * Sets resources.
	 * @param resources resources
	 */
    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
