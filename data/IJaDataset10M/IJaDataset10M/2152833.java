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
public class QueueItemFinishedExc extends FTraqExc {

    public QueueItemFinishedExc(String i_msgTextPattern, Object[] i_parameter) {
        super(i_msgTextPattern, i_parameter);
    }
}
