package cn.fantix.gnualbumalpha2;

import org.dom4j.Element;
import java.io.IOException;

/**
 * <pre>
 * 还没有注释。
 * </pre>
 *
 * @author fantix
 * @date 2007-7-11 20:57:09
 */
public class StringResourceObject extends ResourceObject {

    private String data;

    public StringResourceObject(Element el) {
        data = el.getTextTrim();
    }

    public byte[] getData(AlbumRequest request) throws IOException {
        return request.replace(data).getBytes();
    }
}
