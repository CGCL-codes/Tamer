package atp.reporter.items.form;

import atp.reporter.exception.RParseException;
import atp.reporter.items.*;
import atp.xml.Element;

/**
 * @author shteinke_ke
 * ������� ����� �����
 */
public abstract class RItemInput extends RItemTitled {

    /**
	 * ��� � XML-������������ ������
	 */
    public static final String TEG = "INPUT";

    private String name = null;

    public String getTeg() {
        return TEG;
    }

    protected final void advancedToXML(Element element) {
        if (name != null) element.setAttribute("NAME", name);
        inputToXML(element);
    }

    protected final void parseAdvancedProperties(Element element) throws RParseException {
        name = element.getValue("NAME");
        parseInputProperties(element);
    }

    public void checkCompleteItem() throws RParseException {
        if (name == null || name.trim().length() == 0) throw new RParseException("�� ������� ��� ����������");
    }

    protected void onClear() {
        name = null;
    }

    public final String getName() {
        return name;
    }

    /**
	 * ��������� �������� �������� �����
	 * @param element
	 */
    protected abstract void inputToXML(Element element);

    /**
	 * ��������� �������� �������� �����
	 * @param element
	 * @throws RParseException
	 */
    protected abstract void parseInputProperties(Element element) throws RParseException;
}
