package de.fau.cs.dosis.util.board;

import java.net.URL;
import java.util.Map;

public interface BoardAdapter {

    /**
	 * Determines if topic with topicId exists
	 * 
	 * @param topicId
	 * @return
	 */
    public Boolean topicExists(String topicId) throws BoardException;

    /**
	 * Sets the topic title to the new name presented
	 * @param topicId
	 * @param newTitle
	 */
    public void renameTopic(String topicId, String newTitle) throws BoardException;

    /**
	 * Retrieves the TopicTitle from the topic with topicId
	 * @param topicId
	 * @return
	 */
    public String getTopicTitle(String topicId) throws BoardException;

    /**
	 * Creates a new Topic
	 *  
	 * @param title
	 * @param template
	 * @param templateData
	 * 
	 * @return the topicId of the new Topic
	 */
    public String createTopic(String title, String template, Map<String, String> templateData) throws BoardException;

    /**
	 * Formats a complete Url
	 * @param topicId
	 * @return
	 * @throws BoardException
	 */
    public URL formatLink(String topicId) throws BoardException;
}
