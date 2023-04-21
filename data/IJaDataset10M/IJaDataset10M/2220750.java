package jk.spider.core.task;

public interface Scheduler {

    /**
	 * ���һ���µ�����ƻ���Scheduler��
	 * @param task ����
	 */
    public void schedule(WorkerTask task);

    /**
	 * ����һ��Spider task������������
	 * @return 
	 */
    public WorkerTask getSpiderTask() throws InterruptedException;

    /**
	 * ����һ��Parser Task������������
	 * @return
	 */
    public WorkerTask getParserTask() throws InterruptedException;

    /**
	 * ����һ��Extract Task ���������ᴦ��
	 * @return
	 */
    public WorkerTask getExtractTask() throws InterruptedException;

    /**
	 * ͳ�Ʒ���
	 * @return �����������
	 */
    public int getJobCount();

    /**
	 * ͳ�Ʒ���
	 * @return ����Spider ��������
	 */
    public int getSpiderJobCount();

    public int getParserJobCount();

    public int getExtractJobCount();

    /**
	 * ͳ�Ʒ���
	 * @return �����Ѵ������������
	 */
    public int getJobDone();

    public int getSpiderJobDone();

    public int getParserJobDone();

    public int getExtractJobDone();

    public void scheduleForSeed(WorkerTask task);

    public WorkerTask getSeedTask() throws InterruptedException;

    public boolean isSpiderTaskEmpty();

    public boolean isSchedulerEmpty();

    public int getSpiderJob();

    public int getSeedJob();

    public int getDTSpiderJob();

    public int getPendingJob();
}
