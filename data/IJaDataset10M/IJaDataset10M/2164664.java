package uia.alumni.web;

import java.util.ArrayList;
import uia.alumni.form.FieldType;

/**
 * An Interface with for tags availible to the view's.
 *
 * @author Simon Zimmermann
 */
public interface Tag {

    String tagUl(ArrayList<String> list);

    String tagLi(String text);

    String tagH1(String text);

    String tagH2(String text);

    String tagP(String text);

    String tagDD(String dt, String dd);

    String tagA(String url, String title, String p1, String v1, String p2, String v2, String p3, String v3);

    String tagForm(String target, String p1, String v1);

    String tagField(String name, FieldType type, String label, String value);
}
