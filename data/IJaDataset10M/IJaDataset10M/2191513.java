package org.codingwithpassion.patterns.templateMethod;

/**
 * Abstract implementation -- class that control flow.
 * Describe algorithm for generic object creation.
 *
 */
abstract class CreateObject {

    protected Object[] datas = { "Kimmi", "Zuco", 1, 21 };

    public void setData(Object[] data) {
        CreateObject.this.datas = data;
    }

    /**
	 * Algorithm that control flow (IOC - Inversion of Control).
	 * @return Object String representation. 
	 */
    public String decorate() {
        StringBuilder sb = new StringBuilder();
        objectStart(sb);
        for (int i = 0; i < datas.length; i++) {
            Object data = datas[i];
            if (data instanceof String) {
                stringValue(sb, data, i);
            } else if (data instanceof Integer) {
                numberValue(sb, data, i);
            }
        }
        objectEnd(sb);
        return sb.toString();
    }

    abstract void objectStart(StringBuilder sb);

    abstract void objectEnd(StringBuilder sb);

    abstract void stringValue(StringBuilder sb, Object value, int indx);

    abstract void numberValue(StringBuilder sb, Object value, int indx);
}

/**
 * Object creation for JSON objects;
 *
 */
class JSONObject extends CreateObject {

    protected void objectStart(StringBuilder sb) {
        sb.append("\"Object\":").append("\n{");
    }

    protected void objectEnd(StringBuilder sb) {
        sb.append("\n}");
    }

    protected void stringValue(StringBuilder sb, Object value, int indx) {
        sb.append("prop").append("\"").append(indx).append("\":").append("\"").append(value).append("\",").append("\n");
    }

    protected void numberValue(StringBuilder sb, Object value, int indx) {
        sb.append("prop").append("\"").append(indx).append("\":").append(value).append(",").append("\n");
    }
}

/**
 * Object creation for xml objects.
 *
 */
class XmlObject extends CreateObject {

    protected void objectStart(StringBuilder sb) {
        sb.append("<Object>").append("\n");
    }

    protected void objectEnd(StringBuilder sb) {
        sb.append("</Object>");
    }

    protected void stringValue(StringBuilder sb, Object value, int indx) {
        sb.append("<Property><Key>").append("prop").append(indx).append("</Key><String>").append(value).append("</String>").append("</Property>").append("\n");
    }

    protected void numberValue(StringBuilder sb, Object value, int indx) {
        sb.append("<Property><Key>").append("prop").append(indx).append("</Key><Number>").append(value).append("</Number>").append("</Property>").append("\n");
    }
}

public class DecorateData {

    public static void main(String[] args) {
        CreateObject xml = new JSONObject();
        System.out.println(xml.decorate());
    }
}
