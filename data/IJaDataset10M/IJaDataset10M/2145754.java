package sample.gbase.cmdline;

import com.google.gdata.client.Service;
import java.net.URL;

/**
 * Displays the media entry (meta-data) of one attachment.
 * This command is called from {@link CustomerTool}.
 */
public class GetMediaCommand extends Command {

    private String attachmentId;

    @Override
    public void execute() throws Exception {
        Service service = createService();
        Service.GDataRequest request = service.createEntryRequest(new URL(attachmentId));
        request.execute();
        outputRawResponse(request);
    }

    /** Sets the id/URL of the media attachment to display. */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
