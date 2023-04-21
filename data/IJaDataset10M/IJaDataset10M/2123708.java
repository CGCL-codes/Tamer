package com.rb.lottery.analysis.schdule;

import sun.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.TimerTask;
import com.rb.lottery.analysis.client.UI.MainFrame;
import com.rb.lottery.analysis.common.SystemConstants;
import com.rb.lottery.analysis.system.SystemProcessor;

/**
 * @类功能说明: 空闲内存检测线程
 * @类修改者:
 * @修改日期:
 * @修改说明:
 * @作者: robin
 * @创建时间: 2011-11-8 下午04:53:12
 * @版本: 1.0.0
 */
public class IdleMemoryTimer extends TimerTask {

    @Override
    public void run() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        String free = Math.round(osmxb.getFreePhysicalMemorySize() / 1048576.0) + SystemConstants.MB;
        SystemProcessor.setIdleMemory(free);
    }
}
