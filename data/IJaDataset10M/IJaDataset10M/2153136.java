package com.bird.schedule;

import java.util.Timer;

/**
 * @author jiangzhiqiang
 * ���ǿ��Կ��������ź�����Timerֻ����ָ�����ִ��һ�Σ�������ָ���ھ����Ǹ�ʱ���ִ����������
 * ����˵������Ҫÿ����ҹ1��00ִ��ĳһ���񣬶�����QuartZ�����������õ�ʱ���ˡ�
 * ����QuartZ�ĸ����Ϣ���Է���http://www.opensymphony.com/quartz/����������������QuartZ����ô�����İɣ�
 * 2010-12-1
 */
public class TestTimeTask {

    /**
	 * schedule(TimerTask task, long delay, long period)��taskָ������Ҫִ�е�����
	   delayָ���ڵ�һ�����������Ķ��ٺ���ִ�д�����
	   period����ָ�����Ժ�Ķ��ٺ���ִ��һ������
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TimeTask(), 3000, 5000);
    }
}
