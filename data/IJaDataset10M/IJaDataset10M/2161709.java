package net.sf.oreka.messages;

import net.sf.oreka.OrkException;
import net.sf.oreka.serializers.OrkSerializer;

public class SimpleResponseMessage extends AsyncMessage {

    boolean success = false;

    String comment = "";

    public void define(OrkSerializer serializer) throws OrkException {
        success = serializer.booleanValue("success", success, true);
        comment = serializer.stringValue("comment", comment, false);
    }

    public String getOrkClassName() {
        return "simpleresponse";
    }

    public void validate() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
