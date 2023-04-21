package cn.aprilsoft.TinyAppServer.mailer;

import cn.aprilsoft.conf4j.Conf4j;
import cn.aprilsoft.mail.Mailer;

public class AppMailer {

    private static String smtp = Conf4j.getTrimProperty(AppMailer.class, "smtp");

    private static String from = Conf4j.getTrimProperty(AppMailer.class, "from");

    private static String username = Conf4j.getTrimProperty(AppMailer.class, "username");

    private static String password = Conf4j.getTrimProperty(AppMailer.class, "password");

    private static Mailer mailer = Mailer.create(smtp, from, username, password);

    private AppMailer() {
    }

    public static Mailer getMailer() {
        return mailer;
    }
}
