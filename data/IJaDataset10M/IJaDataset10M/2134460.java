package jlib.Helpers;

import java.io.IOException;
import java.io.InputStream;

/**
 * Performs binary to BASE64 transformation on a InputStream.
 * To convert data to base64, the first byte is placed in the most significant 
 * eight bits of a 24-bit buffer, the next in the middle eight, 
 * and the third in the least significant eight bits. If there are fewer than three 
 * bytes left to encode (or in total), the remaining buffer bits will be zero. 
 * The buffer is then used, six bits at a time, most significant first, as 
 * indices into the string:
 * <pre>ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/</pre>
 * and the indicated character is output.<p/>
 * If there is one input byte remaining (the remainder of the total input bytes 
 * divided by three is two), pad with one "=". If there are two input bytes remaining 
 * (remainder was one), pad with two "=", otherwise, don?t pad. This 
 * prevents extra bits being added to the reconstructed data.<p/>
 * <i>(from the <a href="http://en.wikipedia.org/wiki/Base64">Wikipedia</a>)</i>.<p/>
 * Here is a typical example showing how to retrieve an image from a
 * web server:
 * <pre>
 * 	URL url=new URL([String with the full URL to the image]);
 * 	URLConnection connection = url.openConnection(); 
 * 	BASE64InputStream encoder = new BASE64InputStream(connection.getInputStream());
 * 	InputStram is = new BufferedInputStream(imageEncoder);
 * 	StringBuffer encodedImage=new StringBuffer();
 * 	for(;;) {
 * 		int data=is.read();
 * 		if (data>=0)
 * 			encodedImage.append((char)data);
 * 		else
 * 			break;
 * 	}
 * 	int imageSize = encoder.getDataCount();
 *	is.close();
 * </pre>
 */
public class BASE64InputStream extends InputStream {

    /**
 * The constructor admits any <code>InputStream</code> connected
 * to a binary source.
 * @param binaryInput An input connected to a binary source. 
 */
    public BASE64InputStream(InputStream binaryInput) {
        _binaryInput = binaryInput;
        _buffer = new byte[4];
        _bufferIndex = -1;
        _dataCount = 0;
    }

    /**
 * Contains the data source.
 * The source is assumed to be binary, so retrieved data are considered
 * as bytes.
 */
    private InputStream _binaryInput;

    /**
 * The 6bit base64 encoding table.
 */
    static byte _base64[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    /**
 * BASE64 consists of encoding 3 bytes from the input into 4 bytes to the
 * output. As the output is read one byte at a time ({@link #read()}, a buffer
 * is needed for absorbing the difference in the read rythm.
 */
    private byte _buffer[];

    private int _bufferIndex;

    /**
 * Counts the number of data read from the input source.
 * This information can't be retrieved from the base64 encoding, as
 * the number of bytes varies (3 bytes input are 4 bytes output, plus
 * the padding).
 */
    private int _dataCount;

    /**
 * Transforms the data from the provided input source (see {@link #BASE64InputStream(InputStream)}
 * into a base64 string.
 */
    @Override
    public int read() throws IOException {
        int data;
        if (_bufferIndex < 0) {
            long union;
            byte b;
            int n;
            int padding;
            padding = 0;
            union = 0;
            for (n = 0; n < 3; n++) {
                data = _binaryInput.read();
                if (data < 0) {
                    if (n == 0) {
                        return -1;
                    } else {
                        padding = 3 - n;
                        for (; n < 3; n++) union <<= 8;
                        break;
                    }
                }
                _dataCount++;
                data &= 255;
                union <<= 8;
                union |= data;
            }
            for (n = 0; n < 4; n++) {
                data = (int) (union & 63);
                b = (byte) data;
                if (padding-- > 0) _buffer[n] = '='; else _buffer[n] = _base64[b];
                union >>>= 6;
            }
            _bufferIndex = 3;
        }
        data = _buffer[_bufferIndex];
        _bufferIndex--;
        return data;
    }

    /**
 * Returns the number of bytes read from the source.
 * This information can't be retrieved from the base64 encoding, as
 * the number of bytes varies (3 bytes input are 4 bytes output, plus
 * the padding).
 */
    public int getDataCount() {
        return _dataCount;
    }
}
