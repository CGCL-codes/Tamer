package net.sf.nbrosary;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.sf.jcarrierpigeon.WindowPosition;
import net.sf.jtelegraph.Telegraph;
import net.sf.jtelegraph.TelegraphQueue;
import net.sf.jtelegraph.TelegraphType;
import net.sf.nbrosary.utils.RosaryUtils;
import net.sf.nbrosary.utils.enumerations.RosaryMysteries;
import net.sf.nbrosary.utils.enumerations.RosaryMystery;
import net.sf.nbrosary.utils.enumerations.RosaryPrayers;
import org.openide.util.NbBundle;

/**
 * Acts like a real rosary, in which handles mysteries, prayers, images and
 * the beads themselves. In order to notify the user on the current prayer,
 * I decided to use another opensource project of mine - JTelegraph - to handle
 * such notifications, instead of the default NetBeans notification system.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class HolyRosary {

    private JLabel imgRosary;

    private JLabel imgMystery;

    private JLabel lblMysteryType;

    private JLabel lblMysteryTitle;

    private JLabel lblMysteryText;

    private JButton btnStart;

    private JButton btnNext;

    private JButton btnPrevious;

    private TelegraphQueue queue;

    private int counter = 0;

    private RosaryMysteries mysteriesType;

    /**
     * Setter for the next button.
     * @param btnNext The next button.
     */
    public void setButtonNext(JButton btnNext) {
        this.btnNext = btnNext;
    }

    /**
     * Setter for the previous button
     * @param btnPrevious The previous button.
     */
    public void setButtonPrevious(JButton btnPrevious) {
        this.btnPrevious = btnPrevious;
    }

    /**
     * Setter for the start button.
     * @param btnStart The start button.
     */
    public void setButtonStart(JButton btnStart) {
        this.btnStart = btnStart;
    }

    /**
     * Setter for the mystery image label.
     * @param imgMystery The mystery image label.
     */
    public void setImageMystery(JLabel imgMystery) {
        this.imgMystery = imgMystery;
    }

    /**
     * Setter for the rosary image label.
     * @param imgRosary The rosary image label.
     */
    public void setImageRosary(JLabel imgRosary) {
        this.imgRosary = imgRosary;
    }

    /**
     * Setter for the mystery description label.
     * @param lblMysteryText The mystery description label.
     */
    public void setLabelMysteryText(JLabel lblMysteryText) {
        this.lblMysteryText = lblMysteryText;
    }

    /**
     * Setter for the mystery title label.
     * @param lblMysteryTitle The mystery title label.
     */
    public void setLabelMysteryTitle(JLabel lblMysteryTitle) {
        this.lblMysteryTitle = lblMysteryTitle;
    }

    /**
     * Setter for the mystery type label.
     * @param lblMysteryType The mystery type label.
     */
    public void setLabelMysteryType(JLabel lblMysteryType) {
        this.lblMysteryType = lblMysteryType;
    }

    /**
     * Constructor method. Nothing new in here.
     */
    public HolyRosary() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_WEEK);
        switch(day) {
            case Calendar.MONDAY:
            case Calendar.SATURDAY:
                mysteriesType = RosaryMysteries.JOYFUL;
                break;
            case Calendar.TUESDAY:
            case Calendar.FRIDAY:
                mysteriesType = RosaryMysteries.SORROWFUL;
                break;
            case Calendar.THURSDAY:
                mysteriesType = RosaryMysteries.LUMINOUS;
                break;
            case Calendar.WEDNESDAY:
            case Calendar.SUNDAY:
                mysteriesType = RosaryMysteries.GLORIOUS;
        }
        queue = new TelegraphQueue();
    }

    /**
     * Interrupt the rosary.
     */
    public void stop() {
        setMysteryTexts(RosaryMystery.NONE, 11);
        setMysteryImage(RosaryMystery.NONE);
        RosaryUtils.addImageToLabel(imgRosary, "rosary0.png", "rosary");
        RosaryUtils.addIconsToButton(btnStart, "play.png", "play_hover.png");
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
    }

    public void pray() {
        RosaryUtils.addIconsToButton(btnStart, "stop.png", "stop_hover.png");
        btnNext.setEnabled(true);
        counter = 1;
        setRosaryState();
    }

    /**
     * Goes to the next step in the rosary.
     */
    public void next() {
        counter++;
        setRosaryState();
    }

    /**
     * Goes to the previous step in the rosary.
     */
    public void previous() {
        counter--;
        setRosaryState();
    }

    /**
     * Prays the current prayer through the JTelegraph notification system.
     * I decided to use it because NetBeans default notification system was not
     * able to handle long texts in balloon tooltips. It basically fails when
     * a line wrap is needed, and I really need that, since some prayers are
     * really long. Unfortunately, HTML was not supported, so instead of trying
     * to hack it, I decided to give a shot on my own library.
     * @param prayer The current prayer.
     */
    public void sayPrayer(RosaryPrayers prayer) {
        int timer = 0;
        String prayerName = "";
        switch(prayer) {
            case GLORYBETOTHEFATHER:
                prayerName = "prayers.glorybetothefather.";
                timer = 3000;
                break;
            case THEAPOSTLESCREED:
                prayerName = "prayers.theapostlescreed.";
                timer = 8000;
                break;
            case THEFATIMAPRAYER:
                prayerName = "prayers.thefatimaprayer.";
                timer = 3000;
                break;
            case THEHAILHOLYQUEEN:
                prayerName = "prayers.thehailholyqueen.";
                timer = 5000;
                break;
            case THEHAILMARY:
                prayerName = "prayers.thehailmary.";
                timer = 4000;
                break;
            case THEOURFATHER:
                prayerName = "prayers.theourfather.";
                timer = 4000;
                break;
            case THESIGNOFTHECROSS:
                prayerName = "prayers.thesignofthecross.";
                timer = 3000;
                break;
        }
        String pTitle = NbBundle.getBundle("net.sf.nbrosary.i18n.Rosary").getString(prayerName + "title");
        String pContent = NbBundle.getBundle("net.sf.nbrosary.i18n.Rosary").getString(prayerName + "prayer");
        Telegraph telegraph = new Telegraph(pTitle, pContent, TelegraphType.MESSAGE, WindowPosition.BOTTOMRIGHT, timer);
        queue.add(telegraph);
    }

    /**
     * Set the current mystery texts according to the day, and bead count.
     * @param mystery The current mystery.
     * @param value The bead count.
     */
    public void setMysteryTexts(RosaryMystery mystery, int value) {
        if (value == 11) {
            lblMysteryText.setText("");
            lblMysteryTitle.setText("");
            lblMysteryType.setText("");
            return;
        }
        String path = "";
        switch(mysteriesType) {
            case GLORIOUS:
                path = "glorious.";
                break;
            case JOYFUL:
                path = "joyful.";
                break;
            case LUMINOUS:
                path = "luminous.";
                break;
            case SORROWFUL:
                path = "sorrowful.";
                break;
        }
        String order = "";
        switch(mystery) {
            case NONE:
            case FIRST:
                order = "first.";
                break;
            case SECOND:
                order = "second.";
                break;
            case THIRD:
                order = "third.";
                break;
            case FOURTH:
                order = "fourth.";
                break;
            case FIFTH:
                order = "fifth.";
                break;
        }
        lblMysteryType.setText(NbBundle.getBundle("net.sf.nbrosary.i18n.Rosary").getString(path + "title"));
        lblMysteryTitle.setText(NbBundle.getBundle("net.sf.nbrosary.i18n.Rosary").getString(path + order + "title"));
        if (value != 0) {
            lblMysteryText.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 250, NbBundle.getBundle("net.sf.nbrosary.i18n.Rosary").getString(path + order + String.valueOf(value))));
        } else {
            lblMysteryText.setText("");
        }
    }

    /**
     * Set the mystery image according to the current mystery and type.
     * @param mystery The current mystery.
     */
    public void setMysteryImage(RosaryMystery mystery) {
        String filename = "";
        switch(mysteriesType) {
            case GLORIOUS:
                filename = "glorious";
                break;
            case JOYFUL:
                filename = "joy";
                break;
            case LUMINOUS:
                filename = "light";
                break;
            case SORROWFUL:
                filename = "sorrow";
                break;
        }
        switch(mystery) {
            case NONE:
                filename = "none.png";
                break;
            case FIRST:
                filename = filename + "1.png";
                break;
            case SECOND:
                filename = filename + "2.png";
                break;
            case THIRD:
                filename = filename + "3.png";
                break;
            case FOURTH:
                filename = filename + "4.png";
                break;
            case FIFTH:
                filename = filename + "5.png";
                break;
        }
        RosaryUtils.addImageToLabel(imgMystery, filename, "mysteries");
    }

    /**
     * Sets the rosary current state. This is by far the biggest method in
     * the whole project, as it handles every single rosary step. I decided to
     * work in here as a big integer switch, since the rosary has a well
     * defined order of praying. At least, I have full control over the whole
     * rosary at every single step, forward or even backwards.
     */
    private void setRosaryState() {
        switch(counter) {
            case 1:
                btnPrevious.setEnabled(false);
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary1.png", "rosary");
                sayPrayer(RosaryPrayers.THESIGNOFTHECROSS);
                break;
            case 2:
                btnPrevious.setEnabled(true);
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary1.png", "rosary");
                sayPrayer(RosaryPrayers.THEAPOSTLESCREED);
                break;
            case 3:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary2.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 4:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary3.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 5:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary4.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 6:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary5.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 7:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary5.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 8:
                setMysteryTexts(RosaryMystery.FIRST, 0);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary6.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 9:
                setMysteryTexts(RosaryMystery.FIRST, 1);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary7.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 10:
                setMysteryTexts(RosaryMystery.FIRST, 2);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary8.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 11:
                setMysteryTexts(RosaryMystery.FIRST, 3);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary9.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 12:
                setMysteryTexts(RosaryMystery.FIRST, 4);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary10.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 13:
                setMysteryTexts(RosaryMystery.FIRST, 5);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary11.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 14:
                setMysteryTexts(RosaryMystery.FIRST, 6);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary12.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 15:
                setMysteryTexts(RosaryMystery.FIRST, 7);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary13.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 16:
                setMysteryTexts(RosaryMystery.FIRST, 8);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary14.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 17:
                setMysteryTexts(RosaryMystery.FIRST, 9);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary15.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 18:
                setMysteryTexts(RosaryMystery.FIRST, 10);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary16.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 19:
                setMysteryTexts(RosaryMystery.FIRST, 0);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary16.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 20:
                setMysteryTexts(RosaryMystery.FIRST, 0);
                setMysteryImage(RosaryMystery.FIRST);
                RosaryUtils.addImageToLabel(imgRosary, "rosary16.png", "rosary");
                sayPrayer(RosaryPrayers.THEFATIMAPRAYER);
                break;
            case 21:
                setMysteryTexts(RosaryMystery.SECOND, 0);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary17.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 22:
                setMysteryTexts(RosaryMystery.SECOND, 1);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary18.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 23:
                setMysteryTexts(RosaryMystery.SECOND, 2);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary19.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 24:
                setMysteryTexts(RosaryMystery.SECOND, 3);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary20.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 25:
                setMysteryTexts(RosaryMystery.SECOND, 4);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary21.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 26:
                setMysteryTexts(RosaryMystery.SECOND, 5);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary22.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 27:
                setMysteryTexts(RosaryMystery.SECOND, 6);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary23.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 28:
                setMysteryTexts(RosaryMystery.SECOND, 7);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary24.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 29:
                setMysteryTexts(RosaryMystery.SECOND, 8);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary25.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 30:
                setMysteryTexts(RosaryMystery.SECOND, 9);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary26.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 31:
                setMysteryTexts(RosaryMystery.SECOND, 10);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary27.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 32:
                setMysteryTexts(RosaryMystery.SECOND, 0);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary27.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 33:
                setMysteryTexts(RosaryMystery.SECOND, 0);
                setMysteryImage(RosaryMystery.SECOND);
                RosaryUtils.addImageToLabel(imgRosary, "rosary27.png", "rosary");
                sayPrayer(RosaryPrayers.THEFATIMAPRAYER);
                break;
            case 34:
                setMysteryTexts(RosaryMystery.THIRD, 0);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary28.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 35:
                setMysteryTexts(RosaryMystery.THIRD, 1);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary29.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 36:
                setMysteryTexts(RosaryMystery.THIRD, 2);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary30.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 37:
                setMysteryTexts(RosaryMystery.THIRD, 3);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary31.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 38:
                setMysteryTexts(RosaryMystery.THIRD, 4);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary32.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 39:
                setMysteryTexts(RosaryMystery.THIRD, 5);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary33.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 40:
                setMysteryTexts(RosaryMystery.THIRD, 6);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary34.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 41:
                setMysteryTexts(RosaryMystery.THIRD, 7);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary35.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 42:
                setMysteryTexts(RosaryMystery.THIRD, 8);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary36.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 43:
                setMysteryTexts(RosaryMystery.THIRD, 9);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary37.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 44:
                setMysteryTexts(RosaryMystery.THIRD, 10);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary38.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 45:
                setMysteryImage(RosaryMystery.THIRD);
                setMysteryTexts(RosaryMystery.THIRD, 0);
                RosaryUtils.addImageToLabel(imgRosary, "rosary38.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 46:
                setMysteryTexts(RosaryMystery.THIRD, 0);
                setMysteryImage(RosaryMystery.THIRD);
                RosaryUtils.addImageToLabel(imgRosary, "rosary38.png", "rosary");
                sayPrayer(RosaryPrayers.THEFATIMAPRAYER);
                break;
            case 47:
                setMysteryTexts(RosaryMystery.FOURTH, 0);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary39.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 48:
                setMysteryTexts(RosaryMystery.FOURTH, 1);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary40.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 49:
                setMysteryTexts(RosaryMystery.FOURTH, 2);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary41.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 50:
                setMysteryTexts(RosaryMystery.FOURTH, 3);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary42.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 51:
                setMysteryTexts(RosaryMystery.FOURTH, 4);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary43.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 52:
                setMysteryTexts(RosaryMystery.FOURTH, 5);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary44.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 53:
                setMysteryTexts(RosaryMystery.FOURTH, 6);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary45.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 54:
                setMysteryTexts(RosaryMystery.FOURTH, 7);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary46.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 55:
                setMysteryTexts(RosaryMystery.FOURTH, 8);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary47.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 56:
                setMysteryTexts(RosaryMystery.FOURTH, 9);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary48.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 57:
                setMysteryTexts(RosaryMystery.FOURTH, 10);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary49.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 58:
                setMysteryTexts(RosaryMystery.FOURTH, 0);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary49.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 59:
                setMysteryTexts(RosaryMystery.FOURTH, 0);
                setMysteryImage(RosaryMystery.FOURTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary49.png", "rosary");
                sayPrayer(RosaryPrayers.THEFATIMAPRAYER);
                break;
            case 60:
                setMysteryTexts(RosaryMystery.FIFTH, 0);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary50.png", "rosary");
                sayPrayer(RosaryPrayers.THEOURFATHER);
                break;
            case 61:
                setMysteryTexts(RosaryMystery.FIFTH, 1);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary51.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 62:
                setMysteryTexts(RosaryMystery.FIFTH, 2);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary52.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 63:
                setMysteryTexts(RosaryMystery.FIFTH, 3);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary53.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 64:
                setMysteryTexts(RosaryMystery.FIFTH, 4);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary54.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 65:
                setMysteryTexts(RosaryMystery.FIFTH, 5);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary55.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 66:
                setMysteryTexts(RosaryMystery.FIFTH, 6);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary56.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 67:
                setMysteryTexts(RosaryMystery.FIFTH, 7);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary57.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 68:
                setMysteryTexts(RosaryMystery.FIFTH, 8);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary58.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 69:
                setMysteryTexts(RosaryMystery.FIFTH, 9);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary59.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 70:
                setMysteryTexts(RosaryMystery.FIFTH, 10);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary60.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILMARY);
                break;
            case 71:
                setMysteryTexts(RosaryMystery.FIFTH, 0);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary60.png", "rosary");
                sayPrayer(RosaryPrayers.GLORYBETOTHEFATHER);
                break;
            case 72:
                setMysteryTexts(RosaryMystery.FIFTH, 0);
                setMysteryImage(RosaryMystery.FIFTH);
                RosaryUtils.addImageToLabel(imgRosary, "rosary60.png", "rosary");
                sayPrayer(RosaryPrayers.THEFATIMAPRAYER);
                break;
            case 73:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary60.png", "rosary");
                sayPrayer(RosaryPrayers.THEHAILHOLYQUEEN);
                RosaryUtils.addIconsToButton(btnStart, "play.png", "play_hover.png");
                btnNext.setEnabled(false);
                btnPrevious.setEnabled(false);
                break;
            default:
                setMysteryTexts(RosaryMystery.NONE, 11);
                setMysteryImage(RosaryMystery.NONE);
                RosaryUtils.addImageToLabel(imgRosary, "rosary0.png", "rosary");
        }
    }
}
