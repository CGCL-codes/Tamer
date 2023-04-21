package com.google.code.hibernate.rest.method;

import static com.google.code.hibernate.rest.example.xml.Examples.assertXMLEquals;
import static com.google.code.hibernate.rest.example.xml.Examples.contentOf;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import org.junit.Test;

/**
 * 
 * @author wangzijian
 * 
 */
public class GetTest extends MethodTestSupport {

    @Test
    public void testGet() throws Exception {
        assertGet("http://localhost/test/Entity", "entities.xml");
        assertGet("http://localhost/test/Entity/Student/101", "jack-student.xml");
        assertGet("http://localhost/test/Entity/Book", "books.xml");
    }

    private void assertGet(String url, String expectedExample) throws IOException {
        Response response = Http.get(url);
        assertThat(response.getCode(), is(HTTP_OK));
        assertThat(response.getHeaders().get("Content-Type"), is("text/xml; charset=utf-8"));
        assertXMLEquals(contentOf(expectedExample), response.getBody());
    }

    @Test
    public void testGetInexistsEntity() throws Exception {
        assertThat(Http.get("http://localhost/test/Entity/Student/102").getCode(), is(HTTP_NOT_FOUND));
        assertThat(Http.get("http://localhost/test/Entity/Student/abc").getCode(), is(HTTP_NOT_FOUND));
        assertThat(Http.get("http://localhost/test/Entity/Bus/102").getCode(), is(HTTP_NOT_FOUND));
        assertThat(Http.get("http://localhost/test/Entity/Bus").getCode(), is(HTTP_NOT_FOUND));
    }
}
