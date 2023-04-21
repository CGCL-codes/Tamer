package ftraq.transferqueue.exceptions;

import ftraq.FTraqExc;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class QueueNotRunningExc extends FTraqExc {

    public QueueNotRunningExc(String i_msgTextPattern, Object[] i_parameters) {
        super(i_msgTextPattern, i_parameters);
    }
}
