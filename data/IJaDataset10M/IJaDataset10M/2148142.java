package yarfraw.core.datamodel;

import java.net.URI;
import java.net.URISyntaxException;
import yarfraw.utils.ValidationUtils;

/**
 * <b>This is only used by Rss 2.0.</b>
 * <br/>
 * Describes a media object that is attached to the item.
 * 
 * It has three required attributes. url says where the enclosure is located, length says how big it is in bytes, and type says what its type is, a standard MIME type.
 * <br/>
 * The url must be an http url.
 * <br/>
 * example: &lt;enclosure url="http://www.scripting.com/mp3s/weatherReportSuite.mp3" length="12216320" type="audio/mpeg" />
 * @author jliang
 *
 */
public class Enclosure extends AbstractBaseObject {

    private String _url;

    private String _length;

    private String _mimeType;

    private String _value;

    public Enclosure() {
    }

    /**
   * It has three required attributes. url says where the enclosure is located, length says how big it is in bytes, and type says what its type is, a standard MIME type.
   * @throws URISyntaxException 
   */
    public Enclosure(String url, String length, String mimeType, String value) {
        super();
        setUrl(url);
        setLength(length);
        setMimeType(mimeType);
        setValue(value);
    }

    /**
   * Parse field Url to a {@link URI} object and returns it. 
   * @return field Url as a {@link URI} object.
   * @throws URISyntaxException
   */
    public URI getUrlAsUri() throws URISyntaxException {
        if (_url != null) {
            return new URI(_url.trim());
        }
        return null;
    }

    /**
   * Parse length attribute into a {@link Long} and returns it.
   * @return 
   */
    public Long getLengthAsLong() {
        if (_length != null) {
            return Long.parseLong(_length);
        }
        return null;
    }

    public String getUrl() {
        return _url;
    }

    public Enclosure setUrl(String url) {
        _url = url;
        return this;
    }

    public String getLength() {
        return _length;
    }

    public Enclosure setLength(String length) {
        _length = length;
        return this;
    }

    public String getMimeType() {
        return _mimeType;
    }

    public Enclosure setMimeType(String mimeType) {
        _mimeType = mimeType;
        return this;
    }

    public String getValue() {
        return _value;
    }

    public Enclosure setValue(String value) {
        _value = value;
        return this;
    }

    @Override
    public void validate(FeedFormat format) throws ValidationException {
        if (format == FeedFormat.ATOM10) {
            return;
        }
        ValidationUtils.validateNotNull("Encloure: All fields in the enclosure object should be not null", _length, _mimeType, _url, _value);
    }
}
