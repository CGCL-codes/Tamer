package com.google.code.http4j.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.google.code.http4j.HTTP;
import com.google.code.http4j.Header;
import com.google.code.http4j.Headers;
import com.google.code.http4j.Parser;
import com.google.code.http4j.Response;
import com.google.code.http4j.StatusLine;
import com.google.code.http4j.utils.IOUtils;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 * 
 */
public final class ResponseParser implements Parser<Response, InputStream> {

    @Override
    public Response parse(InputStream in) throws IOException {
        StatusLine line = parseStatusLine(in);
        List<Header> headers = parseHeaders(in);
        return createResponse(line, headers, in);
    }

    private Response createResponse(StatusLine line, List<Header> headers, InputStream in) throws IOException {
        return Headers.isChunked(headers) ? new ChunkedResponse(line, headers, in) : new IdentityResponse(line, headers, in);
    }

    private List<Header> parseHeaders(InputStream in) throws IOException {
        byte[] source = IOUtils.extractByEnd(in, HTTP.CR, HTTP.LF, HTTP.CR, HTTP.LF);
        Parser<List<Header>, byte[]> parser = new HeadersParser();
        return parser.parse(source);
    }

    private StatusLine parseStatusLine(InputStream in) throws IOException {
        byte[] line = IOUtils.extractByEnd(in, HTTP.CR, HTTP.LF);
        Parser<StatusLine, byte[]> parser = new StatusLineParser();
        return parser.parse(line);
    }
}
