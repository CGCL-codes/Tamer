package corejava.util.concurrent;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ������
 * һ�����̣߳����ڽ�Ҫ������ļ�������ӵ���������У������ĸ�д�߳�����ȡ���ļ�����Ϊ��ģ��д������ʱ�����ص㣬
 * �����߳�˯��һ�����ȵ�ʱ�䡣
 * 
 * �������е�BlockingQueue��һ���࣬����˼�壬����������С�
 * ������Ҫ�ṩ����������put()��take()��ǰ�߽�һ������ŵ������У��������Ѿ����ˣ�
 * �͵ȴ�ֱ���п��нڵ㣻���ߴ�headȡһ���������û�ж��󣬾͵ȴ�ֱ���п�ȡ�Ķ���
 * 
 * ���⣬��DemoҲʹ�õ����̳߳غ�ԭ�����ͣ�AtomicInteger����
 * AtomicInteger�����ڲ�������´ﵽԭ�ӻ����£�����ʹ����synchronized���������ܷǳ��ߡ�
 * ����������е�put��take����������Ϊ��ʹ�߳��˳������ڶ����������һ������ʶ����
 * �㷨��Ҳ�С��ڱ�����������ڱ��д�߳̾��˳���
 * @author Administrator
 * @version 1.9, May 8, 2009
 * @since   JDK5.0
 */
public class TestBlockingQueue {

    static long randomTime() {
        return (long) (Math.random() * 1000);
    }

    public static void main(String[] args) {
        final BlockingQueue queue = new LinkedBlockingQueue(100);
        final ExecutorService exec = Executors.newFixedThreadPool(5);
        final File root = new File("f:\\JavaLib");
        final File exitFile = new File("");
        final AtomicInteger rc = new AtomicInteger();
        final AtomicInteger wc = new AtomicInteger();
        Runnable read = new Runnable() {

            public void run() {
            }
        };
    }

    public void scanFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles(new FileFilter() {

                public boolean accept(File pathName) {
                    return pathName.isDirectory() || pathName.getPath().endsWith(".java");
                }
            });
            for (File one : files) {
                scanFile(one);
            }
        } else {
        }
    }
}
