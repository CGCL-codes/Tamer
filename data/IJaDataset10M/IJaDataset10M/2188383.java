package com.modelmetrics.cloudconverter.importxls.struts2;

/**
 * For advanced options, with multiple objects, this action managed the flow
 * to either next next object to be imported OR the finish screen.
 * 
 * @author reidcarlberg
 * @since 2009-05-24
 */
public class AdvancedImportLoopManagerAction extends AbstractUploadContextAware {

    private static final String SUCCESS_NEXT = "success_next";

    private static final String SUCCESS_FINISH = "success_finish";

    public String execute() throws Exception {
        if (this.getUploadContext().getCurrentCloudConverterObjectIndex() == -1) {
            this.getUploadContext().setCurrentCloudConverterObjectIndex(0);
            return SUCCESS_NEXT;
        }
        if (this.getUploadContext().isNextCloudConverterObjectPresent()) {
            this.getUploadContext().setCurrentCloudConverterObjectIndex(this.getUploadContext().getCurrentCloudConverterObjectIndex() + 1);
            return SUCCESS_NEXT;
        }
        return SUCCESS_FINISH;
    }
}
