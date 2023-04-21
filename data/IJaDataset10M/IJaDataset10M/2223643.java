package com.sefer.dragonfly.client.core.domain;

import javax.management.NotificationListener;
import com.sefer.dragonfly.client.core.domain.lifecycle.Lifecycle;
import com.sefer.dragonfly.client.core.domain.scheduler.Job;

/**
 * 监控客户端插件接口
 * 
 * @author xiaofeng 2011-11-1 下午04:40:35
 */
public interface CmConsumer<K, V> extends Job<K, V>, Lifecycle, Comparable<CmConsumer>, NotificationListener {

    NameSpace getNameSpace();

    /**
	 * 插件ID，必须保证相同nameSpace中唯一
	 */
    String getConsumerId();

    /**
	 * 获取插件版本号
	 * 
	 * @return
	 */
    String getVersion();

    /**
	 * 是否要通知服务端,本地配置了该插件，启动时通知，或者动态修改状态时通知
	 */
    boolean isNotifyServer();

    /**
	 * 插件优先级
	 * 
	 * @return
	 */
    int getPriority();

    /**
	 * <table cellspacing="8">
	 * <tr>
	 * <th align="left">Field Name</th>
	 * <th align="left">&nbsp;</th>
	 * <th align="left">Allowed Values</th>
	 * <th align="left">&nbsp;</th>
	 * <th align="left">Allowed Special Characters</th>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Seconds</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>0-59</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * /</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Minutes</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>0-59</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * /</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Hours</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>0-23</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * /</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Day-of-month</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>1-31</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * ? / L W</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Month</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>1-12 or JAN-DEC</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * /</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Day-of-Week</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>1-7 or SUN-SAT</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * ? / L #</code></td>
	 * </tr>
	 * <tr>
	 * <td align="left"><code>Year (Optional)</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>empty, 1970-2099</code></td>
	 * <td align="left">&nbsp;</th>
	 * <td align="left"><code>, - * /</code></td>
	 * </tr>
	 * </table>
	 * <P>
	 * The '*' character is used to specify all values. For example,
	 * &quot;*&quot; in the minute field means &quot;every minute&quot;.
	 * <P>
	 * The '?' character is allowed for the day-of-month and day-of-week fields.
	 * It is used to specify 'no specific value'. This is useful when you need
	 * to specify something in one of the two fileds, but not the other.
	 * <P>
	 * The '-' character is used to specify ranges For example &quot;10-12&quot;
	 * in the hour field means &quot;the hours 10, 11 and 12&quot;.
	 * <P>
	 * The ',' character is used to specify additional values. For example
	 * &quot;MON,WED,FRI&quot; in the day-of-week field means &quot;the days
	 * Monday, Wednesday, and Friday&quot;.
	 * <P>
	 * The '/' character is used to specify increments. For example
	 * &quot;0/15&quot; in the seconds field means &quot;the seconds 0, 15, 30,
	 * and 45&quot;. And &quot;5/15&quot; in the seconds field means &quot;the
	 * seconds 5, 20, 35, and 50&quot;. Specifying '*' before the '/' is
	 * equivalent to specifying 0 is the value to start with. Essentially, for
	 * each field in the expression, there is a set of numbers that can be
	 * turned on or off. For seconds and minutes, the numbers range from 0 to
	 * 59. For hours 0 to 23, for days of the month 0 to 31, and for months 1 to
	 * 12. The &quot;/&quot; character simply helps you turn on every
	 * &quot;nth&quot; value in the given set. Thus &quot;7/6&quot; in the month
	 * field only turns on month &quot;7&quot;, it does NOT mean every 6th
	 * month, please note that subtlety.
	 * <P>
	 * The 'L' character is allowed for the day-of-month and day-of-week fields.
	 * This character is short-hand for &quot;last&quot;, but it has different
	 * meaning in each of the two fields. For example, the value &quot;L&quot;
	 * in the day-of-month field means &quot;the last day of the month&quot; -
	 * day 31 for January, day 28 for February on non-leap years. If used in the
	 * day-of-week field by itself, it simply means &quot;7&quot; or
	 * &quot;SAT&quot;. But if used in the day-of-week field after another
	 * value, it means &quot;the last xxx day of the month&quot; - for example
	 * &quot;6L&quot; means &quot;the last friday of the month&quot;. When using
	 * the 'L' option, it is important not to specify lists, or ranges of
	 * values, as you'll get confusing results.
	 * <P>
	 * The 'W' character is allowed for the day-of-month field. This character
	 * is used to specify the weekday (Monday-Friday) nearest the given day. As
	 * an example, if you were to specify &quot;15W&quot; as the value for the
	 * day-of-month field, the meaning is: &quot;the nearest weekday to the 15th
	 * of the month&quot;. So if the 15th is a Saturday, the trigger will fire
	 * on Friday the 14th. If the 15th is a Sunday, the trigger will fire on
	 * Monday the 16th. If the 15th is a Tuesday, then it will fire on Tuesday
	 * the 15th. However if you specify &quot;1W&quot; as the value for
	 * day-of-month, and the 1st is a Saturday, the trigger will fire on Monday
	 * the 3rd, as it will not 'jump' over the boundary of a month's days. The
	 * 'W' character can only be specified when the day-of-month is a single
	 * day, not a range or list of days.
	 * <P>
	 * The 'L' and 'W' characters can also be combined for the day-of-month
	 * expression to yield 'LW', which translates to &quot;last weekday of the
	 * month&quot;.
	 * <P>
	 * The '#' character is allowed for the day-of-week field. This character is
	 * used to specify &quot;the nth&quot; XXX day of the month. For example,
	 * the value of &quot;6#3&quot; in the day-of-week field means the third
	 * Friday of the month (day 6 = Friday and &quot;#3&quot; = the 3rd one in
	 * the month). Other examples: &quot;2#1&quot; = the first Monday of the
	 * month and &quot;4#5&quot; = the fifth Wednesday of the month. Note that
	 * if you specify &quot;#5&quot; and there is not 5 of the given day-of-week
	 * in the month, then no firing will occur that month.
	 * <P>
	 * <!--The 'C' character is allowed for the day-of-month and day-of-week
	 * fields. This character is short-hand for "calendar". This means values
	 * are calculated against the associated calendar, if any. If no calendar is
	 * associated, then it is equivalent to having an all-inclusive calendar. A
	 * value of "5C" in the day-of-month field means "the first day included by
	 * the calendar on or after the 5th". A value of "1C" in the day-of-week
	 * field means
	 * "the first day included by the calendar on or after sunday".-->
	 * <P>
	 * The legal characters and the names of months and days of the week are not
	 * case sensitive.
	 * 
	 * <p>
	 * <b>NOTES:</b>
	 * <ul>
	 * <li>Support for specifying both a day-of-week and a day-of-month value is
	 * not complete (you'll need to use the '?' character in on of these
	 * fields).</li>
	 * </ul>
	 * </p>
	 * 
	 * @see org.quartz.CronExpression
	 * @return
	 */
    String getCronExpression();

    String getCacheId();

    boolean isNeedCache();

    void setNameSpace(String namespace);

    void setConsumerId(String id);

    void setVersion(String version);

    void setNotifyServer(boolean needNotify);

    void setPriority(int priority);

    void setCronExpression(String cronExpression);

    void setCacheId(String id);

    void setNeedCache(boolean needCache);
}
