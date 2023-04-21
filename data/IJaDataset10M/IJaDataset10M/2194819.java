package ru.dpelevin.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;

/**
 * Context for WebClient.
 * 
 * @author Roman Kuzmin
 */
public class WebClientContext {

    /** The serialized state. */
    private String serializedState;

    /**
	 * Instantiates a new web client context impl.
	 * 
	 * @param state
	 *            the state
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public WebClientContext(final HttpState state) throws IOException {
        Cookie[] cookies = state.getCookies();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        if (cookies != null) {
            os.writeByte(cookies.length);
            for (int i = 0; i < cookies.length; i++) {
                os.writeObject(cookies[i]);
            }
        } else {
            os.writeByte(0);
        }
        os.close();
        byte[] bytes = bos.toByteArray();
        serializedState = new String(Base64.encodeBase64(bytes), "iso-8859-1");
    }

    /**
	 * Instantiates a new web client context impl.
	 * 
	 * @param serializedState
	 *            the serialized state
	 */
    public WebClientContext(final String serializedState) {
        this.serializedState = serializedState;
    }

    /**
	 * Instantiates a new web client context impl.
	 */
    public WebClientContext() {
        this.serializedState = null;
    }

    /**
	 * Gets the serialized state.
	 * 
	 * @return the serialized state
	 */
    public final String getSerializedState() {
        return serializedState;
    }

    /**
	 * Gets the state.
	 * 
	 * @return the state
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final HttpState getState() throws IOException {
        HttpState state = new HttpState();
        if (serializedState != null) {
            try {
                byte[] bytes = Base64.decodeBase64(serializedState.getBytes("iso-8859-1"));
                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
                int len = is.readByte();
                Cookie[] cookies = new Cookie[len];
                for (int i = 0; i < len; i++) {
                    cookies[i] = (Cookie) is.readObject();
                }
                state.addCookies(cookies);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return state;
    }
}
