package QuartzJob;

import java.util.Date;

/**
 * @author <a href="mailto:zhangting@taobao.com">��ͦ</a>
 * @since 10-10-27 ����3:41
 */
public class QuartzJob {

    public void work() {
        System.out.println("Quartz��������ȣ�����" + new Date());
    }
}
