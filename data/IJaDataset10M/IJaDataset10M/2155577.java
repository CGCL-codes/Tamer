package ftpfs.ftp;

import java.util.ListResourceBundle;

/**
 * This class is used to store the valid reply code for various ftp commands.
 */
class FtpReplyResourceBundle extends ListResourceBundle {

    public Object[][] getContents() {
        return cmdGrps;
    }

    static final Object[][] cmdGrps = { { FtpBean.FTP_INIT, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_ACCT, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_APPE, new String[] { FtpBean.REPLY_POS_PRE, FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_CDUP, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_CWD, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_DELE, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_LIST, new String[] { FtpBean.REPLY_POS_PRE, FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_MKD, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_PASV, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_PASS, new String[] { FtpBean.REPLY_POS_CMP, FtpBean.REPLY_POS_INT } }, { FtpBean.CMD_PORT, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_PWD, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_QUIT, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_RETR, new String[] { FtpBean.REPLY_POS_PRE, FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_RNFR, new String[] { FtpBean.REPLY_POS_INT } }, { FtpBean.CMD_RNTO, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_REST, new String[] { FtpBean.REPLY_POS_INT } }, { FtpBean.CMD_RMD, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_SITE, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_STOR, new String[] { FtpBean.REPLY_POS_PRE, FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_SYST, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_TYPE, new String[] { FtpBean.REPLY_POS_CMP } }, { FtpBean.CMD_USER, new String[] { FtpBean.REPLY_POS_INT, FtpBean.REPLY_POS_CMP } } };
}
