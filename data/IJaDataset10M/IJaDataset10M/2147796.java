package travel.impression.note;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;
import travel.impression.message.Message;
import travel.impression.place.Place;

/**
 * Note specific entity.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-09-18
 */
public class Note extends GenNote {

    private static final long serialVersionUID = 1189698214192L;

    private static Log log = LogFactory.getLog(Note.class);

    /**
	 * Constructs note within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public Note(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs note within its parent(s).
	 * 
	 * @param Message
	 *            message
	 * @param Place
	 *            place
	 */
    public Note(Message message, Place place) {
        super(message, place);
    }
}
