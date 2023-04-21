package org.jsresources.apps.chat;

/** Holds pointers to all other model classes.
 */
public class MasterModel {

    private ChatModel m_chatModel;

    private ConnectionSettings m_connectionSettings;

    private AudioSettings m_audioSettings;

    public MasterModel() {
        m_connectionSettings = new ConnectionSettings(this);
        m_audioSettings = new AudioSettings(this);
        m_chatModel = new ChatModel(this);
    }

    public ChatModel getChatModel() {
        return m_chatModel;
    }

    public ConnectionSettings getConnectionSettings() {
        return m_connectionSettings;
    }

    public AudioSettings getAudioSettings() {
        return m_audioSettings;
    }
}
