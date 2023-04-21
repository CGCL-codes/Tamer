package bot;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import util.Logger;
import bot.attack.AttackManager;
import bot.train.TrainManager;
import bot.upgrade.UpgradeManager;

public class Bot extends Thread {

    private Map<Integer, String> tasksOrder = new HashMap<Integer, String>();

    private Vector<Integer> randomRow(int max) {
        Vector<Integer> randomRow = new Vector<Integer>();
        Random r = new Random();
        int random = r.nextInt(max);
        while (randomRow.size() != max) {
            random = r.nextInt(max);
            if (!randomRow.contains(random)) randomRow.addElement(random);
        }
        return randomRow;
    }

    private void orderTasks() {
        Vector<Integer> row = new Vector<Integer>();
        row = randomRow(9);
        tasksOrder.put(row.get(0), "Upgrade");
        tasksOrder.put(row.get(1), "Train");
        tasksOrder.put(row.get(2), "Destroy");
        tasksOrder.put(row.get(3), "Celebration");
        tasksOrder.put(row.get(4), "Market");
        tasksOrder.put(row.get(5), "Armoury");
        tasksOrder.put(row.get(6), "Blacksmith");
        tasksOrder.put(row.get(7), "AttackCheck");
        tasksOrder.put(row.get(8), "Attack");
    }

    private void executeTasks() throws UnknownHostException, IOException {
        for (int i = 0; i < tasksOrder.size(); i++) {
            if (tasksOrder.get(i).equals("Upgrade")) UpgradeManager.check(); else if (tasksOrder.get(i).equals("Attack")) AttackManager.check(); else if (tasksOrder.get(i).equals("Train")) TrainManager.check();
        }
    }

    private boolean canRun() {
        return !BotController.shouldStop();
    }

    private boolean canPause() {
        return BotController.shouldPause();
    }

    public void run() {
        try {
            while (canPause() && canRun()) {
                BotController.setPaused(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException i) {
                }
            }
            BotController.setPaused(false);
            while (canRun()) {
                while ((canPause() && canRun())) {
                    BotController.setPaused(true);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException i) {
                    }
                }
                BotController.setPaused(false);
                if (!canRun()) break;
                orderTasks();
                executeTasks();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException i) {
                }
            }
        } catch (Exception e) {
            Logger.log(e.toString());
            e.printStackTrace();
            try {
                Logger.toGui("Restarting bot services...");
            } catch (Exception i) {
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException i) {
            }
            Bot handler = new Bot();
            Thread tasks = new Thread(handler);
            tasks.start();
        }
    }
}
