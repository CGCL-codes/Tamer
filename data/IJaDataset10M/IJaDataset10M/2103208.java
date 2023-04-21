package cn.ac.ntarl.umt.database;

import cn.ac.ntarl.umt.CLBException;

/**
 * ������ݿ��������쳣�ĸ���
 * @author xiejianjun
 *
 */
public class GeneralDataBaseException extends CLBException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6805187847129326293L;

    public GeneralDataBaseException(String message, Throwable e) {
        super(message, e);
    }

    public GeneralDataBaseException(String message) {
        super(message);
    }

    public GeneralDataBaseException(Throwable e) {
        super(e);
    }
}
