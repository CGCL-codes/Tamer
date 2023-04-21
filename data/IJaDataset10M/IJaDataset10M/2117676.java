package net.sf.mxlosgi.mxlosgixmppbundle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;

/**
 * Represents XMPP message packets.
 * 
 * @see http://www.ietf.org/rfc/rfc3920.txt
 * @author noah
 */
public class Message extends Packet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7381450097317664407L;

    private Type type = Type.normal;

    private String subject = null;

    private String thread = null;

    private String body;

    private Set<Body> bodies = new HashSet<Body>();

    private Set<Subject> subjects = new HashSet<Subject>();

    /**
	 * Creates a new, "normal" message.
	 */
    public Message() {
    }

    /**
	 * Creates a new "normal" message to the specified recipient.
	 * 
	 * @param to
	 *                  the recipient of the message.
	 */
    public Message(JID to) {
        setTo(to);
    }

    /**
	 * Creates a new message of the specified type to a recipient.
	 * 
	 * @param to
	 *                  the user to send the message to.
	 * @param type
	 *                  the message type.
	 */
    public Message(JID to, Type type) {
        setTo(to);
        this.type = type;
    }

    /**
	 * Returns the type of the message.
	 * 
	 * @return the type of the message.
	 */
    public Type getType() {
        return type;
    }

    /**
	 * Sets the type of the message.
	 * 
	 * @param type
	 *                  the type of the message.
	 * @throws IllegalArgumentException
	 *                   if null is passed in as the type
	 */
    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    /**
	 * Returns the subject of the message, or null if the subject has not
	 * been set.
	 * 
	 * @return the subject of the message.
	 */
    public String getSubject() {
        return subject;
    }

    /**
	 * Sets the subject of the message.
	 * 
	 * @param subject
	 *                  the subject of the message.
	 */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
	 * Returns the body of the message, or null if the body has not been
	 * set.
	 * 
	 * @return the body of the message.
	 */
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject(String language) {
        if (language != null) {
            for (Subject subj : subjects) {
                if (subj.getLang().equals(language)) {
                    return subj.getSubject();
                }
            }
        }
        return null;
    }

    public Collection<Subject> getSubjects() {
        return Collections.unmodifiableCollection(subjects);
    }

    public boolean addSubject(String language, String subject) {
        if (language == null || subject == null) {
            throw new NullPointerException("Language or body must not be null");
        }
        Subject subj = new Subject(subject, language);
        return subjects.add(subj);
    }

    public boolean removeSubject(Subject subj) {
        return subjects.remove(subj);
    }

    public boolean removeSubject(String language) {
        for (Subject subj : subjects) {
            if (subj.getLang().equals(language)) {
                return subjects.remove(subj);
            }
        }
        return false;
    }

    /**
	 * Returns the body corresponding to the language. If the language is
	 * null, the method result will be the same as {@link #getBody()}.
	 * Null will be returned if the language does not have a corresponding
	 * body.
	 * 
	 * @param language
	 *                  the language of the body to return.
	 * @return the body related to the passed in language.
	 */
    public String getBody(String language) {
        if (language != null) {
            for (Body body : bodies) {
                if (body.getLanguage().equals(language)) {
                    return body.getMessage();
                }
            }
        }
        return null;
    }

    /**
	 * Returns a set of all bodies in this Message, including the default
	 * message body accessible from {@link #getBody()}.
	 * 
	 * @return a collection of all bodies in this Message.
	 */
    public Collection<Body> getBodies() {
        return Collections.unmodifiableCollection(bodies);
    }

    /**
	 * Adds a body with a corresponding language.
	 * 
	 * @param language
	 *                  the language of the body being added.
	 * @param body
	 *                  the body being added to the message.
	 * @return the new Message.Body
	 * @throws NullPointerException
	 *                   if the body is null, a null pointer exception is
	 *                   thrown
	 */
    public boolean addBody(String language, String body) {
        if (language == null || body == null) {
            throw new NullPointerException("Language or body must not be null");
        }
        Body messageBody = new Body(language, body);
        return bodies.add(messageBody);
    }

    /**
	 * Removes the body with the given language from the message.
	 * 
	 * @param language
	 *                  the language of the body which is to be removed
	 * @return true if a body was removed and false if it was not.
	 */
    public boolean removeBody(String language) {
        for (Body body : bodies) {
            if (language.equals(body.langauge)) {
                return bodies.remove(body);
            }
        }
        return false;
    }

    /**
	 * Removes the body from the message and returns true if the body was
	 * removed.
	 * 
	 * @param body
	 *                  the body being removed from the message.
	 * @return true if the body was successfully removed and false if it
	 *         was not.
	 */
    public boolean removeBody(Body body) {
        return bodies.remove(body);
    }

    /**
	 * Returns the thread id of the message. If no thread id is set,
	 * <tt>null</tt> will be returned.
	 * 
	 * @return the thread id of the message, or <tt>null</tt> if it
	 *         doesn't exist.
	 */
    public String getThread() {
        return thread;
    }

    /**
	 * Sets the thread id of the message.
	 * 
	 * @param thread
	 *                  the thread id of the message.
	 */
    public void setThread(String thread) {
        this.thread = thread;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getLanguage() != null) {
            buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
        if (getStanzaID() != null) {
            buf.append(" id=\"").append(getStanzaID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo().toFullJID())).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom().toFullJID())).append("\"");
        }
        if (type != Type.normal) {
            buf.append(" type=\"").append(type).append("\"");
        }
        buf.append(">");
        if (subject != null) {
            buf.append("<subject>").append(StringUtils.escapeForXML(subject)).append("</subject>");
        }
        for (Subject subj : getSubjects()) {
            buf.append(subj.toXML());
        }
        if (getBody() != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(getBody())).append("</body>");
        }
        for (Body body : getBodies()) {
            buf.append(body.toXML());
        }
        if (thread != null) {
            buf.append("<thread>").append(thread).append("</thread>");
        }
        XMPPError error = getError();
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Message message = (Message) super.clone();
        message.type = this.type;
        message.subject = this.subject;
        message.thread = this.thread;
        message.body = this.body;
        message.bodies = new HashSet<Body>();
        message.subjects = new HashSet<Subject>();
        for (Body body : bodies) {
            message.bodies.add((Body) body.clone());
        }
        for (Subject subject : subjects) {
            message.subjects.add((Subject) subject.clone());
        }
        return message;
    }

    /**
	 * Represents a message body, its language and the content of the
	 * message.
	 */
    public static class Body implements XMLStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = -2032115197555337225L;

        private String message;

        private String langauge;

        private Body(String language, String message) {
            if (message == null) {
                throw new NullPointerException("Message cannot be null.");
            }
            if (language == null) {
                throw new NullPointerException("Language cannot be null.");
            }
            this.langauge = language;
            this.message = message;
        }

        /**
		 * Returns the language of this message body. If the
		 * language is null, then, no language was specified.
		 * 
		 * @return the language of this message body.
		 */
        public String getLanguage() {
            return langauge;
        }

        /**
		 * Returns the message content.
		 * 
		 * @return the content of the message.
		 */
        public String getMessage() {
            return message;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Body body = (Body) o;
            if (langauge != null ? !langauge.equals(body.langauge) : body.langauge != null) {
                return false;
            }
            return message.equals(body.message);
        }

        public int hashCode() {
            int result;
            result = message.hashCode();
            result = 31 * result + (langauge != null ? langauge.hashCode() : 0);
            return result;
        }

        @Override
        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<body xml:lang=\"").append(langauge).append("\">");
            buf.append(StringUtils.escapeForXML(message));
            buf.append("</body>");
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Body body = (Body) super.clone();
            body.message = this.message;
            body.langauge = this.langauge;
            return body;
        }
    }

    public static class Subject implements XMLStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = -7480568732535949117L;

        private String subject;

        private String lang;

        /**
		 * @param subject
		 * @param lang
		 */
        public Subject(String subject, String lang) {
            if (subject == null) {
                throw new NullPointerException("Message cannot be null.");
            }
            if (lang == null) {
                throw new NullPointerException("Language cannot be null.");
            }
            this.subject = subject;
            this.lang = lang;
        }

        public String getSubject() {
            return subject;
        }

        public String getLang() {
            return lang;
        }

        @Override
        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<subject xml:lang=\"").append(lang).append("\">");
            buf.append(StringUtils.escapeForXML(subject));
            buf.append("</subject>");
            return buf.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((lang == null) ? 0 : lang.hashCode());
            result = prime * result + ((subject == null) ? 0 : subject.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final Subject other = (Subject) obj;
            if (lang == null) {
                if (other.lang != null) return false;
            } else if (!lang.equals(other.lang)) return false;
            if (subject == null) {
                if (other.subject != null) return false;
            } else if (!subject.equals(other.subject)) return false;
            return true;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Subject subject = (Subject) super.clone();
            subject.subject = this.subject;
            subject.lang = this.lang;
            return subject;
        }
    }

    /**
	 * The type of a message.
	 */
    public enum Type {

        /**
		 * (Default) a normal text message used in email like
		 * interface.
		 */
        normal, /**
		 * Typically short text message used in line-by-line chat
		 * interfaces.
		 */
        chat, /**
		 * Chat message sent to a groupchat server for group chats.
		 */
        groupchat, /**
		 * Text message to be displayed in scrolling marquee
		 * displays.
		 */
        headline, /**
		 * indicates a messaging error.
		 */
        error;

        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                return normal;
            }
        }
    }
}
