package workflowSim.resource;

/**
 * ��Դ��������쳣
 * @version 0.1
 * @author Guoshen Kuang
 */
public class ResDefErrException extends Exception {

    /**
	 * ���캯��
	 */
    ResDefErrException() {
    }

    /**
	 * ���캯��
	 * @param fileName ���ֶ���������Դ�����ļ���
	 */
    ResDefErrException(String fileName) {
        super("There exist(s) resource definition error(s) in file \"" + fileName + "\".");
    }
}
