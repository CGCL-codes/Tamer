package ftraq.fs.ftp.exceptions;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class FtpConnectionNotIdleExc extends FtpExc {

    private static final String msgPattern = "{0} is not idle, it's current status is {1}";

    public FtpConnectionNotIdleExc(ftraq.fs.ftp.service.FtpConnection i_connection, String i_status) {
        super(msgPattern, i_connection, i_status);
    }
}
