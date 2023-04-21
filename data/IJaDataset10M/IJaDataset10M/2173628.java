package atp.reporter.items.outformat;

import atp.reporter.data.*;
import atp.reporter.exception.RParseException;
import atp.reporter.items.ROutFormat;
import atp.reporter.product.ROutMethod;
import atp.reporter.product.RUtil;
import atp.xml.Element;

/**
 * @author Shteinke_KE
 * ����� �������� ��� ���������� �����
 */
public class ROutFormatMinutes extends ROutFormat {

    /**
	 * ��� ��������
	 */
    public static final String TYPE = "MINUTES";

    public String getDescription() {
        return "���������� �����";
    }

    public String getType() {
        return TYPE;
    }

    protected void toXML(Element element) {
    }

    protected void parseProperties(Element element) throws RParseException {
    }

    public ROutMethod getOutMethod(RDecryptionSource decryptionSource) {
        return new ROutMethod() {

            public String getText(RDataItem value) {
                int d = (int) RUtil.getDouble(value.value.value);
                int h = d / 60;
                int m = d - h * 60;
                String H = "" + h;
                if (h < 10) H = "0" + H;
                String M = "" + m;
                if (m < 10) M = "0" + M;
                return H + ":" + M;
            }
        };
    }

    public int getOutType() {
        return T_TIME;
    }
}
