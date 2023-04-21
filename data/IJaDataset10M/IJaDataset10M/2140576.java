package com.google.api.data.gdata.v2.model.batch;

import com.google.api.client.util.Key;

/**
 * Present in the response entries of a batch request.
 * 
 * @since 2.2
 * @author Nicolas Garnier (nivco@google.com)
 */
public class BatchResponseStatus {

    /** HTTP status code of the operation result. */
    @Key("@code")
    public String code;

    /**
   * Reason of the operation result. A textual representation of the statusCode.
   * Can be the explanation of what went wrong in case of errors.
   */
    @Key("@reason")
    public String reason;

    /** The content-type of the operation result. */
    @Key("@content-type")
    public String contentType;

    /**
   * The body of the response in certain case, for example in the case of errors
   */
    @Key("text()")
    public String content;
}
