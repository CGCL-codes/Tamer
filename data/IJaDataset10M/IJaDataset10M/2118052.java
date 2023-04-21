package cz.muni.fi.pb138.jaro2011.timetrack.dao;

import cz.muni.fi.pb138.jaro2011.timetrack.domain.Activity;
import cz.muni.fi.pb138.jaro2011.timetrack.domain.Person;
import cz.muni.fi.pb138.jaro2011.timetrack.domain.Type;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 * Interface ActivityDAO - used to manipulate with activity elements (see ActivityDAOImpl for its implementation)
 *
 * @param path - variable used to store path to the XML we are manipulating with
 */
public interface ActivityDAO {

    /**
     * method addActivity - to add new activity elements
     *
     * @param activity - values for the activity we are about to insert
     *
     * @param root - stores the root node for Events (cause activity is inserted under it)
     * @param listOfEvents - nodelist of all events
     * @param totalEvents - length of listOfEvents nodelist
     * @param i - integer to iterate over events in nodelist
     * @param aNode - one node from listOfEvents
     * @param sId - value of attribute for Event
     * @param attributes - namednodemap to store all possible attributes of event
     * @param theAttribute - one attribute from all possible attributes of event
     * @param element - to store the distinct event under which we are adding the activity
     * @param act - element Activity (used to step into the tags)
     * @param attr - attribute for activity
     * @param sAjd - value of attribute for activity
     * @param pers - element for the person refference
     * @param attr_t - attribute for the person refference
     * @param P_id - value of attribute for the person refference
     * @param hours  - element for the hours value
     * @param com  - element for the comment value
     * @param PPH - element for the PayPerHour value
     * @param date - element for the date value
     * @param d - value of time inserted
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void addActivity(Activity activity) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method updateActivity - to change already existing activity
     *
     * @param activity - given activity to change
     *
     * @param root - stores the root node for Events (cause activity is inserted under it)
     * @param listOfEvents - nodelist of all events
     * @param totalEvents - length of listOfEvents nodelist
     * @param i - integer to iterate over events in nodelist
     * @param aNode - one node from listOfEvents
     * @param sId - value of attribute for Event
     * @param attributes - namednodemap to store all possible attributes of event
     * @param theAttribute - one attribute from all possible attributes of event
     * @param element - to store the distinct event under which we are adding the activity
     * @param listOfActivities - nodelist of all activities
     * @param totalActivities - length of listOfActivities nodelist
     * @param j - integer to iterate over activities in nodelist
     * @param aaNode - one node from listOfActivities
     * @param attributes_a - namednodemap to store all possible attributes of activity
     * @param theAttribute_a - one attribute from all possible attributes of activity
     * @param sIid - id of activity we want to change
     * @param activitaa - element to get out our activity we want to change
     * @param activitaalist - list of all childnodes of activity
     * @param hours - used to get out the hours tag
     * @param nch_hours - value of unchanged hours value
     * @param commento - used to get out the comment tag
     * @param nch_commento - value of unchanged comment
     * @param PPHo - used to get out the PayPerHour tag
     * @param nch_PPHo -  value of unchanged PPHo
     * @param dateo - used to get out the date tag
     * @param nch_dateo - value of unchanged PPHo
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void updateActivity(Activity activity) throws IOException, SAXException, ParserConfigurationException, TransformerException;

    /**
     * method removeActivity - to delete one of the existing activities
     *
     * @param id - id of activity we want to delete
     *
     * @param sId - need to work with Strings so therefore the conversion
     * @param listOfActivities - nodelist of all activities
     * @param totalActivities - length of listOfActivities nadelist
     * @param i - integer to iterate over activities in nodelist
     * @param aNode - one node from listOfActivities
     * @param attributes - namednodemap to store all possible attributes of activity
     * @param theAttribute - one attribute from all possible attributes of activity
     * @param element_rem - our activity to remove
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void removeActivity(long id) throws IOException, SAXException, ParserConfigurationException, TransformerException;

    /**
     * method listActivities - to store all activities from file into List, for easier work with statistics
     *
    * @param listOfActivities - nodelist of all activities
     * @param totalActivities - length of listOfActivities nadelist
     * @param s - integer to iterate over activities in nodelist
     * @param result - list of activities to store the result
     * @param id - used to find the id of actual activity, so we can use get and get out the whole activity
     * @param activity - (see getActivity) used to get out the activity person and store it into result
     *
     * @return result - list of all activities
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> listActivities() throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivity - to get out one activity with all his elements/attributes using Xpath
     *
     * @param id - id of person we want to get
     *
     * @param root - the root element of the document
     * @param activityNode - we take out our wanted activity into this nodelist
     * @param tags - nodelist to take all activities elements out
     * @param personId - to store person id of our wanted activity
     * @param hours - to store hours of our wanted activity
     * @param comment - to store comment of our wanted person
     * @param payPerHour - to store payPerHour of our wanted person
     * @param dateString - to store date of our wanted person
     * @param date - to store and convert date to deserved format
     * @param personMngr - to get out the person refference id
     * @param eventMngr - to get out the event refference id
     *
     * @return Activity - our one deserved activity
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public Activity getActivity(long id) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivitiesByPerson - to store wanted activities from file into List, for easier work with statistics
     *
     * @param person  - person we are looking for
     * @param type - type of activity
     *
     * @return result - list of activities of our deserved person
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> getActivitiesByPerson(Person person, Type type) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivitiesByType - to store wanted activities from file into List, for easier work with statistics
     *
     * @param type - type of activity
     *
     * @return result - list of activities of our deserved type
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> getActivitiesByType(Type type) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivitiesByPerson - to store wanted activities from file into List, for easier work with statistics
     *
     * @param person  - person we are looking for
     *
     * @return result - list of activities of our deserved person
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> getActivitiesByPerson(Person person) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivitiesByDate - to store wanted activities from file into List, for easier work with statistics
     *
     * @param dateFrom   - date when the activity started
     * @param dateTo - date when it ended
     * @param type - tape of activity
     * 
     * @return result - list of activities of our deserved time period
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> getActivitiesByDate(Date dateFrom, Date dateTo, Type type) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;

    /**
     * method getActivitiesByPersonDate - to store wanted activities from file into List, for easier work with statistics
     *
     * @param person - person we are looking for
     * @param dateFrom   - date when the activity started
     * @param dateTo - date when it ended
     * @param type - tape of activity
     * 
     * @return result - list of activities of our deserved time period
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public List<Activity> getActivitiesByPersonDate(Person person, Date dateFrom, Date dateTo, Type type) throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException;
}
