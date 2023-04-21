package sample.gbase.cmdline;

import com.google.gdata.client.Service;

/**
 * Modifies the media entry (meta-data) describing a media attachment
 * of a Google Base item.
 * This command is called from {@link CustomerTool}.
 */
class UpdateMediaCommand extends Command {

    private String attachmentId;

    @Override
    public void execute() throws Exception {
        Service service = createService();
        Service.GDataRequest request = service.createUpdateRequest(fixEditUrl(attachmentId));
        inputRawRequest(request);
        request.execute();
        System.out.println("Item attachment updated successfully.");
    }

    /** Sets the Id of the media attachment to be updated. */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
