package ftraq.fs.exceptions;

import ftraq.FTraqExc;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class FileSystemExc extends FTraqExc {

    protected FileSystemExc(String i_msgTextPattern, Object[] i_parameters) {
        super(i_msgTextPattern, i_parameters);
    }

    protected FileSystemExc(String i_msgTextPattern, Object i_parameter) {
        super(i_msgTextPattern, i_parameter);
    }

    protected FileSystemExc(String i_msgTextPattern, Object i_parameter1, Object i_parameter2) {
        super(i_msgTextPattern, i_parameter1, i_parameter2);
    }

    protected FileSystemExc(String i_msgTextPattern, Object i_parameter1, Object i_parameter2, Object i_parameter3) {
        super(i_msgTextPattern, i_parameter1, i_parameter2, i_parameter3);
    }
}
