package pl.edu.pw.DVDManiac.cronek;

public class CronekManager {

    public static void sendNewsletterMailing() {
        NewsletterMailSender mailer = new NewsletterMailSender();
        mailer.send();
    }

    public static void sendPenaltyMailing() {
        PenaltyMailSender mailer = new PenaltyMailSender();
        mailer.send();
    }

    public static void sendReminderMailing() {
        ReminderMailSender mailer = new ReminderMailSender();
        mailer.send();
    }
}
