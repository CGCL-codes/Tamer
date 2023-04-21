package com.kaixinff.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kaixinff.kaixin001.common.KaiXin001Client;
import com.kaixinff.net.HttpResponse;

public class TestLogin {

    private static KaiXin001Client client;

    private static String JOB_URL = "http://www.kaixin001.com/!spiderman/job.php";

    private static Job findJob(int jpid) throws Exception {
        String url = getJobURL(jpid);
        String cont = client.doGet(url, JOB_URL).getContent();
        Pattern p = Pattern.compile("<div class=\"l ml5\">���(\\d+?)%</div>");
        Matcher m = p.matcher(cont);
        String jobString = null;
        int index = 0;
        String jobName = null;
        while (m.find(index)) {
            index = m.start() + 1;
            jobName = cont.substring(cont.lastIndexOf("<b>", index) + 3, cont.lastIndexOf("</b>", index));
            if ("׷��Ĳ����Ŵ��뾯�ָ߲���ڹ�".equals(jobName)) {
                jobString = cont.substring(index, cont.indexOf("XW.doJob", index));
                break;
            }
        }
        if (jobString == null) {
            return null;
        }
        p = Pattern.compile("<div id=\"job_do_div_(\\d+?)\">");
        m = p.matcher(jobString);
        if (!m.find()) {
            return null;
        }
        String jobId = m.group(1);
        p = Pattern.compile("c_jpid=\"(.+?)\" jobname=\"(.+?)\"");
        m = p.matcher(jobString);
        if (!m.find()) {
            p = Pattern.compile("<b>(\\d+?)</b> ����");
            m = p.matcher(jobString);
            if (!m.find()) {
                throw new Exception("Cannot find <b>(\\d+?)</b> ����: " + jobString);
            }
            int energy = Integer.parseInt(m.group(1));
            System.out.println("�ҵ�δ�������" + jobName + " (" + jobId + ")");
            Job job = new Job(jobName, jobId);
            job.setEnergy(energy);
            return job;
        } else {
            int jpid2 = Integer.parseInt(m.group(1));
            jobName = m.group(2);
            if (jpid != jpid2) {
                url = getJobURL(jpid2);
                cont = client.doGet(url, JOB_URL).getContent();
            }
            index = cont.indexOf(jobName);
            int index2 = cont.indexOf("XW.doJob", index);
            int index3 = cont.indexOf(",", index2);
            jobId = cont.substring(index2 + 9, index3);
            System.out.println("�ҵ���������" + jobName + " (" + jobId + ")");
            Job job = new Job(jobName, jobId);
            p = Pattern.compile("<b>(\\d+?)</b> ����");
            m = p.matcher(cont);
            if (!m.find(index)) {
                throw new Exception("Cannot find <b>(\\d+?)</b> ����: " + cont);
            }
            int energy = Integer.parseInt(m.group(1));
            job.setEnergy(energy);
            return job;
        }
    }

    private static String getJobURL(int jpid) {
        return "http://www.kaixin001.com/!spiderman/!job.php?c_jpid=" + jpid + "&flag=phase&cid=2&tmp=" + Math.random();
    }

    public static void main(String[] args) throws Exception {
        UserInfo u = new UserInfo();
        if (u.getJob() != null && (u.getJob().getEnergy() - u.getEnergy()) * u.getEnergyIncRate() > System.currentTimeMillis() - u.getTimestamp()) {
            System.out.println("����δ�ָ����˳���");
            return;
        }
        client = new KaiXin001Client();
        HttpResponse resp = client.login("username", "password");
        String cont = resp.getContent();
        cont = client.doGet(JOB_URL, null).getContent();
        Pattern p = Pattern.compile("XW[.]processTimer[(]XW[.]ajaxNum,\"energy\",[-|\\d]+?,(\\d+?),(\\d+?),(\\d+?),1,");
        Matcher m = p.matcher(cont);
        if (!m.find()) {
            return;
        }
        int energy = Integer.parseInt(m.group(1));
        u.setEnergy(energy);
        if (u.getJob() == null) {
            System.out.println("����δ�������");
            p = Pattern.compile("XW[.]getJobListByPhase[(](\\d+?)[)]\">(.+?)<");
            m = p.matcher(cont);
            int index = 0;
            String jobId = null;
            while (m.find(index)) {
                index = m.start() + 1;
                int jpid = Integer.parseInt(m.group(1));
                if (jpid >= u.getJpid()) {
                    System.out.println("������" + m.group(2));
                    Job job = findJob(jpid);
                    if (job != null) {
                        u.setJpid(jpid);
                        u.setJob(job);
                        break;
                    }
                }
            }
        }
        if (u.getEnergy() > u.getJob().getEnergy()) {
            String url = "http://www.kaixin001.com/!spiderman/!ajax_dojob.php?c_jid=118&cid=2&tmp=0.97817307011139317" + Math.random();
            cont = client.doGet(url, JOB_URL).getContent();
            u.setJob(null);
            p = Pattern.compile("XW[.]processTimer[(]XW[.]ajaxNum,\"energy\",[-|\\d]+?,(\\d+?),(\\d+?),(\\d+?),1,");
            m = p.matcher(cont);
            if (!m.find()) {
                throw new Exception("Cannot find XW[.]processTimer[(]XW[.]ajaxNum,\"energy\",[-|\\d]+?,(\\d+?),(\\d+?),(\\d+?),1,: " + cont);
            }
            energy = Integer.parseInt(m.group(1));
            u.setEnergy(energy);
        }
    }

    private static class UserInfo {

        private long timestamp;

        private int energy;

        private long energyIncRate = 240000;

        private int jpid = 8;

        private Job job;

        public UserInfo() {
            timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public Job getJob() {
            return job;
        }

        public void setJob(Job job) {
            this.job = job;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getJpid() {
            return jpid;
        }

        public void setJpid(int jpid) {
            this.jpid = jpid;
        }

        public long getEnergyIncRate() {
            return energyIncRate;
        }

        public void setEnergyIncRate(long energyIncRate) {
            this.energyIncRate = energyIncRate;
        }
    }

    private static class Job {

        private String name;

        private String jobId;

        private int energy;

        public Job(String name, String jobId) {
            this.name = name;
            this.jobId = jobId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }
    }
}
