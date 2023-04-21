package shu.cms.colorformat;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �ഫ�{���ҥi�~�Ӧ����O
 * �o�����O�̤j���\�Φb��,�i�H�ΨӽT�w�ഫ�u�@�O�_����.
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public abstract class TransformTask {

    protected int lengthOfTask = 1000;

    protected boolean done = false;

    protected int current = 0;

    public int getLengthOfTask() {
        return lengthOfTask;
    }

    public boolean isDone() {
        return done;
    }

    public int getCurrent() {
        return current;
    }

    public abstract void transforming(String dir, String outputFilename) throws Exception;
}
