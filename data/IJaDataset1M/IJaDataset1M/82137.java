package com.rb.lottery.analysis.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.rb.lottery.analysis.common.SystemConstants;
import com.rb.lottery.analysis.common.SystemFunctions;
import com.rb.lottery.analysis.system.SystemCache;
import com.rb.threadPool.Task;

/**
 * @类功能说明:
 * @类修改者:
 * @修改日期:
 * @修改说明:
 * @作者: robin
 * @创建时间: 2011-11-25 下午01:07:11
 * @版本: 1.0.0
 */
public class ScoreFilterTask extends Task {

    private float least;

    private float most;

    public ScoreFilterTask() {
        least = 0;
        most = 42;
    }

    public ScoreFilterTask(float least, float most) {
        this.least = least;
        this.most = most;
    }

    @Override
    public String info() {
        String taskInfo = "积分和过滤任务处理";
        return taskInfo;
    }

    @Override
    protected boolean needExecuteImmediate() {
        return false;
    }

    @Override
    public Task[] taskCore() throws Exception {
        return null;
    }

    @Override
    protected boolean useDb() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        System.out.println(info());
        Map<String, List<String>> singleBets = SystemCache.getBasket(SystemCache.currentBasketId).getSingleBets();
        Iterator it = singleBets.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            List<String> bets = singleBets.get(key);
            if (bets == null) {
                continue;
            }
            Iterator vit = bets.iterator();
            while (vit.hasNext()) {
                Object obj = vit.next();
                if (obj == null) {
                    continue;
                }
                String bet = (String) obj;
                int count = SystemFunctions.getScoCount(bet);
                if (count > most || count < least) {
                    vit.remove();
                }
            }
            if (bets.size() == 0) {
                it.remove();
            }
        }
    }
}
