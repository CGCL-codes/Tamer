package hoplugins.tsforecast;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.awt.Color;
import plugins.IHOMiniModel;
import plugins.IJDBCAdapter;
import plugins.IMatchDetails;

public class Curve {

    public static final int WEEKS_BACK = 26;

    public static final int RESET = -1;

    public static final int UNKNOWN_MATCH = 0;

    public static final int LEAGUE_MATCH = 1;

    public static final int RELEGATION_MATCH = 2;

    public static final int CUP_MATCH = 3;

    public static final int NATIONAL_FRIENDLY_MATCH = 4;

    public static final int NATIONAL_FRIENDLY_CUP_MATCH = 5;

    public static final int RESERVED_MATCH = 6;

    public static final int MASTERS_MATCH = 7;

    public static final int INTERN_FRIENDLY_MATCH = 8;

    public static final int INTERN_FRIENDLY_CUP_MATCH = 9;

    public static final int NATIONAL_TEAM_COMPETITION_MATCH = 10;

    public static final int NATIONAL_TEAM_COMPETITION_CUP_MATCH = 11;

    public static final int NATIONAL_TEAM_FRIENDLY_MATCH = 12;

    protected static final double TEAM_SPIRIT_UNKNOWN = -1D;

    protected static final double TEAM_SPIRIT_RESET = 4.5D;

    public static final int STANDARD_PT = 0;

    public static final int RESET_PT = 1;

    public static final int NEW_TRAINER_PT = 10;

    public static final int TRAINER_DOWN_PT = 11;

    public static final int START_TRAINER_PT = 12;

    protected IJDBCAdapter m_clJDBC = null;

    protected IHOMiniModel m_clModel = null;

    protected ArrayList<Point> m_clPoints = new ArrayList();

    private Iterator<Point> m_clIterator = null;

    private Point m_currentPoint = null;

    private Color m_Color = null;

    public class Point implements Comparable {

        double m_dSpirit = TEAM_SPIRIT_UNKNOWN;

        int m_iAttitude = plugins.IMatchDetails.EINSTELLUNG_UNBEKANNT;

        Date m_dDate = null;

        int m_iMatchDay = 0;

        int m_iMatchType = UNKNOWN_MATCH;

        int m_iPointType = STANDARD_PT;

        String m_strTooltip = null;

        Point(Point point) {
            m_dDate = new Date(point.m_dDate.getTime());
            m_dSpirit = point.m_dSpirit;
            m_iAttitude = point.m_iAttitude;
            m_iMatchDay = point.m_iMatchDay;
            m_iMatchType = point.m_iMatchType;
            m_iPointType = point.m_iPointType;
        }

        Point(Date date, double dSpirit, int iAttitude, int iMatchDay, int iMatchType, int iPointType) {
            m_dDate = new Date(date.getTime());
            m_dSpirit = dSpirit;
            m_iAttitude = iAttitude;
            m_iMatchDay = iMatchDay;
            m_iMatchType = iMatchType;
            m_iPointType = iPointType;
        }

        Point(Date date, int iAttitude, int iMatchDay, int iMatchType) {
            this(date, TEAM_SPIRIT_UNKNOWN, iAttitude, iMatchDay, iMatchType, STANDARD_PT);
        }

        Point(Date date, double dSpirit) {
            this(date, dSpirit, IMatchDetails.EINSTELLUNG_UNBEKANNT, 0, UNKNOWN_MATCH, STANDARD_PT);
        }

        Point(Date date, double dSpirit, int iPointType) {
            this(date, dSpirit, IMatchDetails.EINSTELLUNG_UNBEKANNT, 0, UNKNOWN_MATCH, iPointType);
        }

        public int compareTo(Object obj) {
            return m_dDate.compareTo(((Point) obj).m_dDate);
        }
    }

    public Curve(IHOMiniModel ihominimodel) {
        m_clModel = ihominimodel;
        m_clJDBC = m_clModel.getAdapter();
    }

    public boolean first() {
        m_clIterator = m_clPoints.iterator();
        return !m_clPoints.isEmpty();
    }

    public boolean last() {
        for (m_clIterator = m_clPoints.iterator(); m_clIterator.hasNext(); ) m_currentPoint = m_clIterator.next();
        return !m_clPoints.isEmpty();
    }

    public boolean next() {
        if (m_clIterator.hasNext()) {
            m_currentPoint = m_clIterator.next();
            return true;
        } else {
            return false;
        }
    }

    public Date getDate() {
        return m_currentPoint.m_dDate;
    }

    public double getSpirit() {
        return m_currentPoint.m_dSpirit;
    }

    public int getAttitude() {
        return m_currentPoint.m_iAttitude;
    }

    public int getMatchType() {
        return m_currentPoint.m_iMatchType;
    }

    public int getMatchDay() {
        return m_currentPoint.m_iMatchDay;
    }

    public String getTooltip() {
        return m_currentPoint.m_strTooltip;
    }

    public Color getColor() {
        return m_Color;
    }

    public int getPointType() {
        return m_currentPoint.m_iPointType;
    }

    public void setColor(Color color) {
        m_Color = color;
    }

    public Point getFirstPoint() {
        return m_clPoints.get(0);
    }

    public Point getLastPoint() {
        return m_clPoints.get(m_clPoints.size() - 1);
    }

    public void addPoint(int i, Point point) {
        m_clPoints.add(i, new Point(point));
    }

    protected static int getDiffDays(Point point1, Point point2) {
        GregorianCalendar gregoriancalendar1 = new GregorianCalendar();
        gregoriancalendar1.setTime(point1.m_dDate);
        GregorianCalendar gregoriancalendar2 = new GregorianCalendar();
        gregoriancalendar2.setTime(point2.m_dDate);
        int iRet = 0;
        if (gregoriancalendar1.get(Calendar.YEAR) != gregoriancalendar2.get(Calendar.YEAR)) {
            iRet += 365;
            if (gregoriancalendar1.isLeapYear(gregoriancalendar1.get(Calendar.YEAR))) iRet++;
        }
        iRet += gregoriancalendar2.get(Calendar.DAY_OF_YEAR) - gregoriancalendar1.get(Calendar.DAY_OF_YEAR);
        return iRet;
    }

    static String matchTypeToString(int iMatchType) {
        switch(iMatchType) {
            case 1:
                return new String("League");
            case 2:
                return new String("Relegation");
            case 3:
                return new String("Cup");
            case 4:
                return new String("national friendly with normal rules");
            case 5:
                return new String("national friendly with cup rules");
            case 6:
                return new String("reserved for international compettition");
            case 7:
                return new String("Hattrick masters");
            case 8:
                return new String("international friendly with normal rules");
            case 9:
                return new String("international friendly with cup rules");
            case 10:
                return new String("national team competition with normal rules");
            case 11:
                return new String("national team competition with cup rules");
            case 12:
                return new String("national team friendly");
            case -1:
                return new String("Reset");
            case 0:
            default:
                return new String("Unknown " + iMatchType);
        }
    }
}
