package cn.imgdpu.fetion;

public class Fetion extends Thread {

    String uid, pwd, sms;

    FetionSocket fetionRun;

    public void run() {
        new Fetion(uid, pwd, uid, sms);
    }

    public Fetion(String phone, String passwd, String msg) {
        uid = phone;
        pwd = passwd;
        sms = msg;
    }

    public Fetion(String phone, String passwd, String tophone, String msg) {
        fetionRun = new FetionSocket();
        fetionRun.phone = phone;
        fetionRun.passwd = passwd;
        fetionRun.tophone = tophone;
        fetionRun.msg = msg;
        Thread thread = new Thread(fetionRun);
        fetionRun.thread = thread;
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                cn.imgdpu.util.CatException.getMethod().catException(e, "线程中断异常");
            }
        }
        String result = "";
        if (fetionRun.errCount > 0) {
            result = "发送失败，请检查密码是否正确。错误信息：" + fetionRun.errMsg;
        } else {
            result = "飞信发送成功!";
        }
        cn.imgdpu.GSAGUI.setStatusAsyn(result);
        cn.imgdpu.dialog.SendFetionDialog.setStatusAsyn(result);
    }
}
