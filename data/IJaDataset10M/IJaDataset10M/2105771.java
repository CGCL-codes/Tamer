package com.jxva.mvc.upload;

/**
 * 文件上传返回的数据
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-11-27 10:23:21 by Jxva
 */
public class UploadMsg {

    public static final int RESULT_SUCCESS = 0;

    public static final int RESULT_TOO_LARGE = 1;

    public static final int RESULT_EMPTY = 2;

    public static final int RESULT_BAD_FORMAT = 3;

    public static final int RESULT_EXCEPTION = 4;

    public boolean isSuccessful;

    public int result;

    public Exception e;

    public int allowMega;
}
