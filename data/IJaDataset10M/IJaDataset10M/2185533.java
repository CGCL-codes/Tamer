package ch.iserver.ace.net.impl.protocol;

import org.beepcore.beep.core.MessageMSG;

/**
 *
 */
public class RequestImpl implements Request {

    private int type;

    private Object payload;

    private String userid;

    private MessageMSG message;

    public RequestImpl(int type, String userid, Object payload) {
        this.type = type;
        this.userid = userid;
        this.payload = payload;
    }

    public int getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setMessage(MessageMSG message) {
        this.message = message;
    }

    public MessageMSG getMessage() {
        return message;
    }

    public String getUserId() {
        return userid;
    }

    public String toString() {
        return "RequestImpl(" + type + ", " + userid + ", " + payload + ")";
    }

    /**
	 * Helper class to wrap document specific information in the
	 * Request.
	 *
	 * @see Request
	 */
    static class DocumentInfo {

        private String docId, name, userId;

        public DocumentInfo(String docId, String name, String userId) {
            this.docId = docId;
            this.name = name;
            this.userId = userId;
        }

        public String getDocId() {
            return docId;
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

        public String toString() {
            return "DocumentInfo(" + docId + ", '" + name + "', " + userId + ")";
        }
    }
}
