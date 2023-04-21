package cn.nkjobsearch.html;

import java.io.BufferedReader;
import java.io.FileReader;
import cn.nkjobsearch.config.Config;
import cn.nkjobsearch.config.ConfigJob51;
import cn.nkjobsearch.publicClass.File;

public class Com51JobController {

    public void startGetID() {
        read51Job_PC();
        Com51JobSearchList job51SL = new Com51JobSearchList();
        job51SL.start();
        while (true) {
            if (!ConfigJob51.JOB51_SEARCHLIST_RUNNING) {
                write51Job_PC();
                break;
            }
            ConfigJob51.JOB51_SEARCHLIST_TIMER += 2;
            if (ConfigJob51.JOB51_SEARCHLIST_TIMER >= ConfigJob51.JOB51_SEARCHLIST_DELAY_SECONDS) {
                job51SL.close();
                job51SL.stop();
                ConfigJob51.JOB51_SEARCHLIST_P++;
                ConfigJob51.JOB51_SEARCHLIST_TIMER = 0;
                System.out.println("interrupt");
                job51SL = new Com51JobSearchList();
                job51SL.start();
                write51Job_PC();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            write51Job_PC();
        }
    }

    public void startGetHTML() {
        Com51JobContent job51C = new Com51JobContent();
        job51C.start();
        while (true) {
            if (!ConfigJob51.JOB51_CONTENT_RUNNING) {
                break;
            }
            ConfigJob51.JOB51_CONTENT_TIMER++;
            if (ConfigJob51.JOB51_CONTENT_TIMER >= ConfigJob51.JOB51_CONTENT_DELAY_SECONDS) {
                job51C.stop();
                ConfigJob51.JOB51_CONTENT_TIMER = 0;
                System.out.println("Interrupt: GET Content Of JOB51.");
                job51C = new Com51JobContent();
                job51C.start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void write51Job_PC() {
        String confStr = ConfigJob51.JOB51_SEARCHLIST_P + "\r\n" + ConfigJob51.JOB51_SEARCHLIST_C + "\r\n";
        File.writeConf(ConfigJob51.JOB51_SEARCHLIST_CONF_PATH, confStr);
    }

    private void read51Job_PC() {
        try {
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new FileReader(ConfigJob51.JOB51_SEARCHLIST_CONF_PATH));
            ConfigJob51.JOB51_SEARCHLIST_P = Integer.parseInt(bufferedReader.readLine().toString());
            ConfigJob51.JOB51_SEARCHLIST_C = Integer.parseInt(bufferedReader.readLine().toString());
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
