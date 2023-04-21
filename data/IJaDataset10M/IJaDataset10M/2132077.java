package dryven.unittest.mocks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import dryven.request.http.AbstractResponse;
import dryven.request.http.Cookie;
import dryven.request.http.Response;

public class MockResponse extends AbstractResponse {

    private OutputStream _output = new ByteArrayOutputStream();

    private int _status = 200;

    private Map<String, String> _headers = new HashMap<String, String>();

    @Override
    public void addCookie(Cookie c) {
    }

    @Override
    public OutputStream getResponseStream() {
        return _output;
    }

    @Override
    public void setHeader(String name, String value) {
        _headers.put(name, value);
    }

    @Override
    public void setStatus(int status) {
        _status = status;
        ;
    }

    public int getStatus() {
        return _status;
    }
}
