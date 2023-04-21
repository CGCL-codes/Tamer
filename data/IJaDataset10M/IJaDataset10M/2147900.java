package com.google.code.sagetvaddons.sjqc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class SJQThread extends Thread {

    private class ProcessKiller extends TimerTask {

        @Override
        public void run() {
            proc.destroy();
            setDidTimeout(true);
            clnt.log(task, "SJQ: Process killed due to timeout [MAXRUN = " + maxRunTime + "ms]");
        }
    }

    private JSONObject task;

    private boolean success;

    private Timer timer;

    private Process proc;

    private long maxRunTime;

    private int expectedRC;

    private TaskClient clnt;

    private boolean didTimeout;

    public SJQThread(JSONObject o) {
        task = o;
        success = true;
        timer = null;
        didTimeout = false;
    }

    private synchronized void setDidTimeout(boolean b) {
        didTimeout = b;
        return;
    }

    private synchronized boolean getDidTimeout() {
        return didTimeout;
    }

    @Override
    public void run() {
        clnt = new TaskClient();
        try {
            JSONArray cmds = null;
            synchronized (task) {
                cmds = task.getJSONArray("cmds");
                update("RUNNING");
                JSONObject opts = task.getJSONObject("options");
                expectedRC = opts.getInt("RETURNCODE");
                maxRunTime = opts.getLong("MAXRUN");
            }
            for (int i = 0; i < cmds.length(); ++i) {
                String cLine = null;
                synchronized (task) {
                    JSONArray cLineArray = cmds.getJSONArray(i);
                    cLine = new String("");
                    for (int j = 0; j < cLineArray.length(); ++j) cLine = cLine.concat(cLineArray.getString(j) + " ");
                    cLine = cLine.substring(0, cLine.length() - 1);
                    clnt.log(task, "Executing command line: " + cLine);
                }
                try {
                    proc = Runtime.getRuntime().exec(cLine);
                    Thread stdout = new IOMonster(proc.getInputStream(), task);
                    stdout.start();
                    Thread stderr = new IOMonster(proc.getErrorStream(), task);
                    stderr.start();
                    if (maxRunTime > 0) {
                        timer = new Timer(true);
                        timer.schedule(new ProcessKiller(), maxRunTime);
                    }
                    TaskMonitor monitor = new TaskMonitor(proc, task);
                    monitor.start();
                    if (proc.waitFor() != expectedRC || getDidTimeout()) success = false;
                    if (monitor != null && monitor.isAlive() == true) {
                        monitor.setKeepAlive(false);
                        monitor.interrupt();
                    }
                    if (!success) break;
                } catch (IOException e) {
                    synchronized (task) {
                        clnt.log(task, e.getMessage());
                        success = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    synchronized (task) {
                        clnt.log(task, e.getMessage());
                        success = false;
                        break;
                    }
                } finally {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
            }
            if (success) update("DONE"); else update("FAILED");
            TaskClient.wakeParent();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return;
    }

    private void update(String state) {
        synchronized (task) {
            try {
                task.put("state", state);
                task.put("priority", "0");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            clnt.update(task);
        }
        return;
    }
}
