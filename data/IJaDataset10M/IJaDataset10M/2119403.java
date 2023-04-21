package barde.log.view;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import cbonar.memento.Memento;

/**
 * This class represents a view of a log, which is basically a collection of {@link MessageRef}.<br>
 * Remember this is a view, as in MVC : it doesn't contain any object, it just makes reference to them.<br/>
 * Because it acts as an interface between the data and the way they are viewed/accessed,
 * it has methods to define which content to keep from the original messages, and in which way to deliver them.<br/>
 * 
 * memento design pattern : All classes that implement this interface are able to give their state
 * at any moment, and to restore it later.
 * 
 * TODO : add more selection methods (see the common usage)
 * @author cbonar@free.fr
 */
public interface LogView extends List {

    /** select only those channels */
    public LogView retainChannels(String regex);

    /** exclude the matching channels */
    public LogView removeChannels(String regex);

    /** select only those avatars in those channels */
    public LogView retainAvatars(String channels, String regex);

    /** exclude the matching avatars in those channels */
    public LogView removeAvatars(String channels, String regex);

    /** select only those matching messages */
    public LogView retainMessages(String channels, String avatars, String regex);

    /** exclude those matching messages */
    public LogView removeMessages(String channels, String avatars, String regex);
}
