package net.sf.doolin.app.sc.engine.support;

import java.util.Collections;
import java.util.List;
import net.sf.doolin.app.sc.engine.ClientID;
import net.sf.doolin.app.sc.engine.ClientState;
import net.sf.doolin.app.sc.engine.DownloadResponse;
import net.sf.doolin.app.sc.engine.DownloadStatus;
import net.sf.doolin.app.sc.engine.Message;
import net.sf.doolin.app.sc.engine.MessageType;
import net.sf.doolin.util.Strings;
import org.apache.commons.lang.Validate;

public class DefaultDownloadResponse<T extends ClientState> implements DownloadResponse<T> {

    private static final long serialVersionUID = 1L;

    public static <M extends ClientState> DownloadResponse<M> createError(ClientID cid, String key, Object... parameters) {
        return new DefaultDownloadResponse<M>(DownloadStatus.ERROR, Collections.singletonList(new Message(MessageType.ERROR, Strings.get(cid.getLocale(), key, parameters))));
    }

    private final List<Message> messages;

    private final DownloadStatus status;

    private final T state;

    /**
	 * Waiting or errors
	 */
    public DefaultDownloadResponse(DownloadStatus status, List<Message> messages) {
        Validate.isTrue(status != DownloadStatus.OK, "Download status must be Waiting or Error");
        this.messages = Collections.unmodifiableList(messages);
        this.status = status;
        this.state = null;
    }

    /**
	 * OK
	 */
    public DefaultDownloadResponse(T state) {
        this.messages = Collections.emptyList();
        this.status = DownloadStatus.OK;
        this.state = state;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public T getState() {
        Validate.notNull(this.state, "State has not been initialised");
        return this.state;
    }

    @Override
    public DownloadStatus getStatus() {
        return this.status;
    }
}
