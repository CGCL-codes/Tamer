package net.sf.gilead.examples.rpc;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Vincent Legendre
 *
 */
public class ChatMessage implements IsSerializable {

    static final long serialVersionUID = 201;

    private String m_text = "";

    private String m_fromLogin = "";

    private Date m_date = new Date();

    public ChatMessage() {
    }

    public ChatMessage(long p_gameId, String p_from, String p_text) {
        m_fromLogin = p_from;
        m_text = p_text;
    }

    /**
   * @return the text
   */
    public String getText() {
        return m_text;
    }

    /**
   * @param p_text the text to set
   */
    public void setText(String p_text) {
        m_text = p_text;
    }

    /**
   * @return the fromLogin
   */
    public String getFromLogin() {
        return m_fromLogin;
    }

    /**
   * @param p_fromLogin the fromLogin to set
   */
    public void setFromLogin(String p_fromLogin) {
        m_fromLogin = p_fromLogin;
    }

    /**
   * @return the date
   */
    public Date getDate() {
        return m_date;
    }

    /**
   * @param p_date the date to set
   */
    public void setDate(Date p_date) {
        m_date = p_date;
    }
}
