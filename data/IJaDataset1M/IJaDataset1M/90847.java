package util.dateCountDown;

import java.util.Date;

public class DateCountDown {

    private Date _date;

    public DateCountDown(Date date) {
        _date = date;
    }

    public int compareToNow() {
        return _date.compareTo(getNow());
    }

    public int compareTo(Date date) {
        return _date.compareTo(date);
    }

    public Date getNow() {
        return new Date();
    }

    public String getDiffToNow() {
        StringBuilder sb = new StringBuilder();
        if (_date.after(getNow())) {
            sb.append("离 ");
            sb.append(_date);
            sb.append(" 还有 ");
        } else if (_date.before(getNow())) {
            sb.append(_date);
            sb.append(" 已过 ");
        }
        boolean show = false;
        int diff = _date.getYear() - getNow().getYear();
        if (diff != 0) {
            processDiff(sb, " 年", diff);
            show = true;
        }
        diff = _date.getMonth() - getNow().getMonth();
        if (show || diff != 0) {
            processDiff(sb, " 月", diff);
            show = true;
        }
        diff = _date.getDate() - getNow().getDate();
        if (show || diff != 0) {
            processDiff(sb, " 天", diff);
            show = true;
        }
        diff = _date.getHours() - getNow().getHours();
        if (show || diff != 0) {
            processDiff(sb, " 小时", diff);
            show = true;
        }
        diff = _date.getMinutes() - getNow().getMinutes();
        if (show || diff != 0) {
            processDiff(sb, " 分", diff);
            show = true;
        }
        diff = _date.getSeconds() - getNow().getSeconds();
        if (show || diff != 0) {
            processDiff(sb, " 秒", diff);
            show = true;
        }
        return sb.toString();
    }

    private StringBuilder processDiff(StringBuilder sb, String v, int diff) {
        sb.append(Math.abs(diff));
        sb.append(v);
        return sb;
    }

    /**
	 * @param args
	 * @throws InterruptedException
	 */
    public static void main(String[] args) throws InterruptedException {
        DateCountDown now = new DateCountDown(new Date("2010/11/5 20:21:00"));
        System.out.println(now.getDiffToNow());
    }
}
