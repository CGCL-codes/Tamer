package ru.adv.web.statistic.stopwatch;

import ru.adv.util.timing.Stopwatch;

public interface RequestStatistic {

    public void start(Stopwatch stopwatch, String domain, String taskName);

    public void stop(Stopwatch stopwatch);

    public void destroy();
}
