package com.misyshealthcare.connect.doc.ccd;

import java.io.ByteArrayInputStream;
import javax.mail.util.ByteArrayDataSource;

/**
 *  
 *
 * @author Wenzhi Li
 * @version 3.0, Nov 20, 2007
 */
public class CCDDocument {

    private String content = null;

    private MetaData metadata = null;

    private Error[] errors = null;

    /**
	 * @return the content
	 */
    public String getContent() {
        return content;
    }

    /**
	 * @param content the content to set
	 */
    public void setContent(String content) {
        this.content = content;
    }

    /**
	 * @return the metadata
	 */
    public MetaData getMetadata() {
        return metadata;
    }

    /**
	 * @param metadata the metadata to set
	 */
    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

    /**
	 * @return the errors
	 */
    public Error[] getErrors() {
        return errors;
    }

    /**
	 * @param errors the errors to set
	 */
    public void setErrors(Error[] errors) {
        this.errors = errors;
    }
}
