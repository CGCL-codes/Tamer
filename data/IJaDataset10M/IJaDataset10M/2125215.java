package org.jboss.netty.example.http2.snoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http2.Attribute;
import org.jboss.netty.handler.codec.http2.Cookie;
import org.jboss.netty.handler.codec.http2.CookieEncoder;
import org.jboss.netty.handler.codec.http2.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http2.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http2.DiskAttribute;
import org.jboss.netty.handler.codec.http2.DiskFileUpload;
import org.jboss.netty.handler.codec.http2.FileUpload;
import org.jboss.netty.handler.codec.http2.HttpChunk;
import org.jboss.netty.handler.codec.http2.InterfaceHttpData;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http2.HttpDataFactory;
import org.jboss.netty.handler.codec.http2.HttpHeaders;
import org.jboss.netty.handler.codec.http2.HttpRequest;
import org.jboss.netty.handler.codec.http2.HttpResponse;
import org.jboss.netty.handler.codec.http2.HttpResponseStatus;
import org.jboss.netty.handler.codec.http2.HttpVersion;
import org.jboss.netty.handler.codec.http2.QueryStringDecoder;
import org.jboss.netty.handler.codec.http2.InterfaceHttpData.HttpDataType;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.NotEnoughDataDecoderException;

/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev: 386 $, $Date: 2009-10-04 14:02:03 -0400 (Sun, 04 Oct 2009) $
 */
@ChannelPipelineCoverage("one")
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private volatile HttpRequest request;

    private volatile boolean readingChunks = false;

    private final StringBuilder responseContent = new StringBuilder();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private HttpPostRequestDecoder decoder = null;

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = null;
        DiskAttribute.deleteOnExitTemporaryFile = true;
        DiskAttribute.baseDirectory = null;
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (!readingChunks) {
            if (decoder != null) {
                decoder.cleanFiles();
                decoder = null;
            }
            HttpRequest request = this.request = (HttpRequest) e.getMessage();
            URI uri = null;
            try {
                uri = new URI(request.getUri());
            } catch (URISyntaxException e2) {
            }
            if (!uri.getPath().startsWith("/form")) {
                writeMenu(e);
                return;
            }
            responseContent.setLength(0);
            responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
            responseContent.append("===================================\r\n");
            responseContent.append("VERSION: " + request.getProtocolVersion().getText() + "\r\n");
            responseContent.append("REQUEST_URI: " + request.getUri() + "\r\n\r\n");
            responseContent.append("\r\n\r\n");
            Map<String, List<String>> headers = request.getHeaders();
            for (String key : headers.keySet()) {
                for (String value : headers.get(key)) {
                    responseContent.append("HEADER: " + key + "=" + value + "\r\n");
                }
            }
            responseContent.append("\r\n\r\n");
            Set<Cookie> cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                responseContent.append("COOKIE: " + cookie.toString() + "\r\n");
            }
            responseContent.append("\r\n\r\n");
            QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
            Map<String, List<String>> uriAttributes = decoderQuery.getParameters();
            for (String key : uriAttributes.keySet()) {
                for (String value : uriAttributes.get(key)) {
                    responseContent.append("URI: " + key + "=" + value + "\r\n");
                }
            }
            responseContent.append("\r\n\r\n");
            try {
                decoder = new HttpPostRequestDecoder(factory, request);
            } catch (ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                writeResponse(e.getChannel());
                Channels.close(e.getChannel());
                return;
            } catch (IncompatibleDataDecoderException e1) {
                responseContent.append(e1.getMessage());
                responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
                writeResponse(e.getChannel());
                return;
            }
            responseContent.append("Is Chunked: " + request.isChunked() + "\r\n");
            responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");
            if (request.isChunked()) {
                responseContent.append("Chunks: ");
                readingChunks = true;
            } else {
                readHttpDataAllReceive(e.getChannel());
                responseContent.append("\r\n\r\nEND OF NOT CHUNKED CONTENT\r\n");
                writeResponse(e.getChannel());
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            try {
                decoder.offer(chunk);
            } catch (ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                writeResponse(e.getChannel());
                Channels.close(e.getChannel());
                return;
            }
            responseContent.append("o");
            readHttpDataChunkByChunk(e.getChannel());
            if (chunk.isLast()) {
                readHttpDataAllReceive(e.getChannel());
                writeResponse(e.getChannel());
                readingChunks = false;
            }
        }
    }

    /**
     * Example of reading all InterfaceHttpData from finished transfer
     * @param channel
     */
    private void readHttpDataAllReceive(Channel channel) {
        List<InterfaceHttpData> datas = null;
        try {
            datas = decoder.getBodyHttpDatas();
        } catch (NotEnoughDataDecoderException e1) {
            e1.printStackTrace();
            responseContent.append(e1.getMessage());
            writeResponse(channel);
            Channels.close(channel);
            return;
        }
        for (InterfaceHttpData data : datas) {
            writeHttpData(data);
        }
        responseContent.append("\r\n\r\nEND OF CONTENT AT FINAL END\r\n");
    }

    /**
     * Example of reading request by chunk and getting values from chunk to chunk
     * @param channel
     */
    private void readHttpDataChunkByChunk(Channel channel) {
        try {
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    writeHttpData(data);
                }
            }
        } catch (EndOfDataDecoderException e1) {
            responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
            return;
        }
    }

    private void writeHttpData(InterfaceHttpData data) {
        if (data.getHttpDataType() == HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            String value;
            try {
                value = attribute.getValue();
            } catch (IOException e1) {
                e1.printStackTrace();
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n");
                return;
            }
            if (value.length() > 100) {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " data too long\r\n");
            } else {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.toString() + "\r\n");
            }
        } else {
            responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data.toString() + "\r\n");
            if (data.getHttpDataType() == HttpDataType.FileUpload) {
                FileUpload fileUpload = (FileUpload) data;
                if (fileUpload.isCompleted()) {
                    if (fileUpload.length() < 10000) {
                        responseContent.append("\tContent of file\r\n");
                        try {
                            responseContent.append(((FileUpload) data).getString(((FileUpload) data).getCharset()));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        responseContent.append("\r\n");
                    } else {
                        responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
                    }
                } else {
                    responseContent.append("\tFile to be continued but should not!\r\n");
                }
            }
        }
    }

    private void writeResponse(Channel channel) {
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(responseContent.toString(), "UTF-8");
        responseContent.setLength(0);
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0) && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION));
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        if (!close) {
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        }
        Set<Cookie> set = request.getCookies();
        if (!set.isEmpty()) {
            CookieEncoder cookieEncoder = new CookieEncoder(true);
            for (Cookie cookie : set) {
                cookieEncoder.addCookie(cookie);
            }
            response.addHeader(HttpHeaders.Names.SET_COOKIE, cookieEncoder.encode());
        }
        ChannelFuture future = channel.write(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void writeMenu(MessageEvent e) {
        responseContent.setLength(0);
        responseContent.append("<html>");
        responseContent.append("<head>");
        responseContent.append("<title>Netty Test Form</title>\r\n");
        responseContent.append("</head>\r\n");
        responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr>");
        responseContent.append("<td>");
        responseContent.append("<h1>Netty Test Form</h1>");
        responseContent.append("Choose one FORM");
        responseContent.append("</td>");
        responseContent.append("</tr>");
        responseContent.append("</table>\r\n");
        responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
        responseContent.append("<input type=hidden name=getform value=\"GET\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("</body>");
        responseContent.append("</html>");
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(responseContent.toString(), "UTF-8");
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        e.getChannel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        System.err.println(responseContent.toString());
        e.getChannel().close();
    }
}
