package kwikserver.request;

import java.util.HashMap;
import kwikserver.exceptions.BadRequestException;
import kwikserver.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ������� �����, ������������� ������ � ���� Document
 *
 * ��� ���� ����� �������� ������ ���� ����������� ��������������� ����������
 * ����� ������ � ��������, ������������� ����������� ��� ����� ������� ������
 *
 * ��� ����� Request ��� ����� ������ �������� ��� ������������ � ����� �������
 * ��������:
 *  - id
 *  - type
 */
public class Request {

    protected Document document = null;

    protected HashMap<String, String> cachedValues = new HashMap<String, String>();

    public Request(Document document) {
        this.document = document;
    }

    public String getElementText(Element parent, String name, int index) throws BadRequestException {
        String text = cachedValues.get(name);
        if (text == null) {
            text = getElement(parent, name, index).getTextContent();
            cachedValues.put(name, text);
        }
        if (text == null) {
            throw new BadRequestException();
        }
        return text;
    }

    public Element getElement(Element parent, String name, int index) {
        return Utils.getElement(parent, name, index);
    }

    public Element getRoot() {
        return document.getDocumentElement();
    }

    public String getType() throws BadRequestException {
        return getElementText(getRoot(), "type", 0);
    }

    public String getId() throws BadRequestException {
        return getElementText(getRoot(), "id", 0);
    }
}
