package com.pinae.rafiki.task;

import com.pinae.rafiki.job.Job;
import com.pinae.rafiki.trigger.Trigger;

/**
 * ���񣬰�����Ҫִ�еĹ����Ͷ�ʱ������
 * 
 * @author ��ع��
 *
 */
public interface Task {

    /**
	 * �����������
	 * 
	 * @param name �������
	 */
    public void setName(String name);

    /**
	 * �����������
	 * 
	 * @return �������
	 */
    public String getName();

    /**
	 * ������Ҫִ�еĹ���
	 * 
	 * @param job ��Ҫִ�еĹ���
	 */
    public void setJob(Job job);

    /**
	 * ������Ҫִ�еĹ���
	 * 
	 * @return ��Ҫִ�еĹ���
	 */
    public Job getJob();

    /**
	 * ���ö�ʱ������
	 * 
	 * @param trigger ��ʱ������
	 */
    public void setTrigger(Trigger trigger);

    /**
	 * ���ض�ʱ������
	 * 
	 * @return ��ʱ������
	 */
    public Trigger getTrigger();
}
