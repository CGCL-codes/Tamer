package tuxiazi.webapi.form;

import halo.util.HaloValidate;
import tuxiazi.util.Err;

public class PhotoCmtForm {

    private String content;

    public PhotoCmtForm(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int validate() {
        if (!HaloValidate.validateEmptyAndLength(this.content, true, 200)) {
            return Err.PHOTOCMT_CONTENT_ERROR;
        }
        return Err.SUCCESS;
    }
}
