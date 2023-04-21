package studivz.mailtool.model.core.execptions;

public abstract class ModelCoreException extends Exception {

    private String msgId;

    public ModelCoreException(String msgId, Throwable cause) {
        super(msgId, cause);
        this.msgId = msgId;
    }

    public ModelCoreException(String msgId) {
        super(msgId);
        this.msgId = msgId;
    }

    public ModelCoreException(Throwable cause) {
        super(cause);
        this.msgId = null;
    }

    @Override
    public String getLocalizedMessage() {
        return "msg(" + msgId + ")";
    }
}
