package com.botarena.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import com.botarena.server.model.Bot;
import com.botarena.server.model.Contest;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class BattleScheduler {

    private static final Logger log = Logger.getLogger(BattleScheduler.class.getName());

    public static void scheduleNextBattle() {
        final List<TaskOptions> newTasks = new ArrayList<TaskOptions>();
        List<Contest> activeContests = ContestService.getActiveContests();
        BotService bs = new BotService();
        for (Contest contest : activeContests) {
            Bot first = null;
            Collection<Bot> firstCandidates = bs.getNotRecentlyFighting(contest, 1);
            for (Bot b : firstCandidates) {
                first = b;
            }
            if (first == null) {
                continue;
            }
            log.info("SCHEDULER: first candidate=" + first.getName() + "(" + first.getRank() + ")");
            Collection<Bot> secondCandidates = bs.getNotRecentlyFighting(contest, 100);
            if (secondCandidates.size() == 1) {
                continue;
            }
            String sc = "";
            for (Bot b : secondCandidates) {
                sc += b.getName() + "(" + b.getRank() + ")[" + b.getLastFight() + "], ";
            }
            log.info("SCHEDULER: second candidates=" + sc);
            final Bot firstArg = first;
            List<Bot> candidateList = new ArrayList<Bot>(secondCandidates);
            Collections.sort(candidateList, new Comparator<Bot>() {

                @Override
                public int compare(Bot o1, Bot o2) {
                    return howFar(o1) - howFar(o2);
                }

                private int howFar(Bot o) {
                    return Math.abs(o.getRank() - firstArg.getRank());
                }
            });
            String ssc = "";
            for (Bot b : candidateList) {
                ssc += b.getName() + "(" + b.getRank() + "), ";
            }
            log.info("SCHEDULER: sorted second candidates=" + ssc);
            double favorization = 2.0;
            int random = candidateList.size() - (1 + (int) Math.pow((Math.random() * (Math.pow(candidateList.size() - 1, favorization))), 1.0 / favorization));
            log.info("SCHEDULER: random from 1 to " + (candidateList.size() - 1) + " is " + random);
            Bot second = candidateList.get(random);
            newTasks.add(TaskOptions.Builder.withUrl("/botarena/internal/processOneBattle").method(Method.GET).param("bot1", KeyFactory.keyToString(first.getKey())).param("bot2", KeyFactory.keyToString(second.getKey())).param("contest", KeyFactory.keyToString(contest.getKey())));
            newTasks.add(TaskOptions.Builder.withUrl("/botarena/internal/processOneBattle").method(Method.GET).param("bot1", KeyFactory.keyToString(second.getKey())).param("bot2", KeyFactory.keyToString(first.getKey())).param("contest", KeyFactory.keyToString(contest.getKey())));
        }
        QueueFactory.getDefaultQueue().add(newTasks);
    }
}
