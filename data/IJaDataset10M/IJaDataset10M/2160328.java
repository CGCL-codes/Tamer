package atp.reporter.items.allocators;

/**
 * @author Shteinke_KE
 * �� 2-������� �������������
 */
public class RAllocatorTime2Hour extends RAllocatorTime {

    /**
	 * ��� ��������
	 */
    public static final String TYPE = "2HOUR";

    public static final long HOUR = 1000 * 60 * 60 * 2;

    public RAllocatorTime2Hour() {
        super((long) HOUR);
    }

    protected String getParameterColumnHeader(long date) {
        long l = date - date % HOUR;
        long d = date - date % DAY;
        int c = ((int) ((l - d) / HOUR)) * 2;
        return "" + (c < 10 ? "0" : "") + c + "-" + (c < 8 ? "0" : "") + (c + 2);
    }

    public String getDescription() {
        return "2-�������";
    }

    public String getType() {
        return TYPE;
    }
}
