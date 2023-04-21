package gg.arkehion.http;

import gg.arkehion.configuration.Configuration;
import gg.arkehion.exceptions.ArFileException;
import gg.arkehion.exceptions.ArFileWormException;
import gg.arkehion.exceptions.ArUnvalidIndexException;
import gg.arkehion.store.ArkDirConstants;
import gg.arkehion.store.abstimpl.ArkAbstractDualDoc;
import gg.arkheion.http.TestStoreAndHttpThread;
import goldengate.common.file.DataBlock;
import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import goldengate.common.utility.GgStringUtils;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http2.Attribute;
import org.jboss.netty.handler.codec.http2.Cookie;
import org.jboss.netty.handler.codec.http2.CookieDecoder;
import org.jboss.netty.handler.codec.http2.CookieEncoder;
import org.jboss.netty.handler.codec.http2.DefaultCookie;
import org.jboss.netty.handler.codec.http2.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http2.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http2.DiskAttribute;
import org.jboss.netty.handler.codec.http2.DiskFileUpload;
import org.jboss.netty.handler.codec.http2.FileUpload;
import org.jboss.netty.handler.codec.http2.HttpChunk;
import org.jboss.netty.handler.codec.http2.HttpDataFactory;
import org.jboss.netty.handler.codec.http2.HttpHeaders;
import org.jboss.netty.handler.codec.http2.HttpMethod;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http2.HttpRequest;
import org.jboss.netty.handler.codec.http2.HttpResponse;
import org.jboss.netty.handler.codec.http2.HttpResponseStatus;
import org.jboss.netty.handler.codec.http2.HttpVersion;
import org.jboss.netty.handler.codec.http2.InterfaceHttpData;
import org.jboss.netty.handler.codec.http2.QueryStringDecoder;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http2.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http2.InterfaceHttpData.HttpDataType;
import org.jboss.netty.util.CharsetUtil;

/**
 * Handler for HTTP support
 * 
 * @author Frederic Bregier
 * 
 */
public class HttpFormattedHandler extends SimpleChannelUpstreamHandler {

    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory.getLogger(HttpFormattedHandler.class);

    public static AtomicBoolean lock = new AtomicBoolean(false);

    private volatile HttpRequest request;

    private volatile boolean willClose = false;

    private volatile boolean readingChunks = false;

    private final StringBuilder responseTempContent = new StringBuilder();

    private final StringBuilder responseFinalContent = new StringBuilder();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private HttpPostRequestDecoder decoder = null;

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = "J:/GG/ARK/TMP/";
        DiskAttribute.deleteOnExitTemporaryFile = true;
        DiskAttribute.baseDirectory = "J:/GG/ARK/TMP/";
    }

    public long legacy = ArkDirConstants.invalide_idx;

    public long store = ArkDirConstants.invalide_idx;

    public long document = ArkDirConstants.invalide_idx;

    public String metadata;

    public FileUpload fileUpload;

    public String filename;

    public ArkAbstractDualDoc doc;

    public String contentType = "text/html";

    public ArkRequest reqType = null;

    public HttpResponseStatus status = HttpResponseStatus.OK;

    public String dkey = null;

    public static enum ArkRequest {

        Stop, Menu, Get, GetMeta, Post, PostUpload
    }

    public static enum ArkArgs {

        LEG, STO, DID, META, FILEDOC, FILENAME, CTYPE, REQTYPE, DKEY
    }

    private void initialize() {
        if (decoder != null) {
            decoder.cleanFiles();
            decoder = null;
        }
        willClose = false;
        legacy = ArkDirConstants.invalide_idx;
        store = ArkDirConstants.invalide_idx;
        document = ArkDirConstants.invalide_idx;
        metadata = null;
        if (fileUpload != null) {
            fileUpload.delete();
        }
        fileUpload = null;
        filename = null;
        doc = null;
        contentType = "text/html";
        reqType = null;
        status = HttpResponseStatus.OK;
        dkey = null;
        responseFinalContent.setLength(0);
        responseTempContent.setLength(0);
    }

    private void getUriArgs() {
        QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> uriAttributes = decoderQuery.getParameters();
        List<String> values = uriAttributes.get(ArkArgs.LEG.name());
        if (values != null) {
            if (values.size() == 1) {
                legacy = Long.parseLong(values.get(0));
            }
            values.clear();
        }
        values = uriAttributes.get(ArkArgs.STO.name());
        if (values != null) {
            if (values.size() == 1) {
                store = Long.parseLong(values.get(0));
            }
            values.clear();
        }
        values = uriAttributes.get(ArkArgs.DID.name());
        if (values != null) {
            if (values.size() == 1) {
                document = Long.parseLong(values.get(0));
            }
            values.clear();
        }
        values = uriAttributes.get(ArkArgs.CTYPE.name());
        if (values != null) {
            if (values.size() == 1) {
                contentType = values.get(0);
            }
            values.clear();
        }
        values = uriAttributes.get(ArkArgs.REQTYPE.name());
        if (values != null) {
            if (values.size() == 1) {
                String value = values.get(0);
                if (value.equals(ArkRequest.Get.name())) {
                    reqType = ArkRequest.Get;
                } else if (value.equals(ArkRequest.GetMeta.name())) {
                    reqType = ArkRequest.GetMeta;
                } else if (value.equals(ArkRequest.Stop.name())) {
                    reqType = ArkRequest.Stop;
                } else {
                    reqType = ArkRequest.Menu;
                }
            }
            values.clear();
        }
        values = null;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (!readingChunks) {
            initialize();
            this.request = (HttpRequest) e.getMessage();
            if (this.request.getMethod() == HttpMethod.GET) {
                getUriArgs();
                if (reqType == null) {
                    reqType = ArkRequest.Menu;
                }
            } else if (this.request.getMethod() == HttpMethod.POST) {
                reqType = null;
            } else {
                if (this.request.getUri().startsWith("/stop")) {
                    reqType = ArkRequest.Stop;
                } else {
                    reqType = ArkRequest.Menu;
                }
            }
            if (reqType != null) {
                switch(reqType) {
                    case Stop:
                        willClose = true;
                        writeMenu(e);
                        lock.set(true);
                        break;
                    case Menu:
                        writeMenu(e);
                        break;
                    case Get:
                        get(e);
                        break;
                    case GetMeta:
                        getMeta(e);
                        break;
                    default:
                        post(e);
                }
            } else {
                post(e);
            }
        } else {
            postChunk(e);
        }
    }

    private HttpResponse getResponse() {
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        willClose = willClose || status != HttpResponseStatus.OK || HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0) && !keepAlive;
        if (willClose) {
            keepAlive = false;
        }
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), status);
        if (keepAlive) {
            response.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        Set<Cookie> cookies;
        String value = request.getHeader(HttpHeaders.Names.COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            CookieDecoder decoder = new CookieDecoder();
            cookies = decoder.decode(value);
        }
        if (!cookies.isEmpty()) {
            CookieEncoder cookieEncoder = new CookieEncoder(true);
            for (Cookie cookie : cookies) {
                cookieEncoder.addCookie(cookie);
            }
            if (dkey != null) {
                Cookie cookie = new DefaultCookie(ArkArgs.DKEY.name(), dkey);
                cookieEncoder.addCookie(cookie);
            }
            response.addHeader(HttpHeaders.Names.SET_COOKIE, cookieEncoder.encode());
        }
        return response;
    }

    private void writeResponse(Channel channel) {
        HttpResponse response = getResponse();
        int length = 0;
        ChannelBuffer buf = ChannelBuffers.wrappedBuffer(responseFinalContent.toString().getBytes(CharsetUtil.UTF_8));
        responseFinalContent.setLength(0);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/xml");
        length = buf.readableBytes();
        response.setContent(buf);
        if (!willClose) {
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(length));
        }
        ChannelFuture future = channel.write(response);
        logger.debug("Send Resp XML: " + reqType);
        if (willClose) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void responseLSD(ArkRequest req) {
        responseTempContent.append("<tr><td>Fill with value Legacy: <br> <input type=text name=\"" + ArkArgs.LEG.name() + "\" size=20></td></tr>");
        responseTempContent.append("<tr><td>Fill with value Store: <br> <input type=text name=\"" + ArkArgs.STO.name() + "\" size=20></td></tr>");
        responseTempContent.append("<tr><td>Fill with value Did: <br> <input type=text name=\"" + ArkArgs.DID.name() + "\" size=20></td></tr>");
        responseTempContent.append("<input type=hidden name=" + ArkArgs.REQTYPE.name() + " value=\"" + req.name() + "\">");
    }

    private void contentTypeChoice() {
        responseTempContent.append("<tr><td>Fill with value ContentType: <br> <select name=\"" + ArkArgs.CTYPE.name() + "\">");
        responseTempContent.append("<OPTION VALUE=\"text/html\">text/html</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"text/xml\">text/xml</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"text/plain\">text/plain</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"image/png\">image/png</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"image/tiff\">image/tiff</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"image/png\">image/png</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"application/zip\">application/zip</OPTION>");
        responseTempContent.append("<OPTION VALUE=\"application/octet-stream\">application/octet-stream</OPTION>");
        responseTempContent.append("</td></tr>");
    }

    private void writeMenu(MessageEvent e) {
        responseTempContent.setLength(0);
        responseTempContent.append("<html>");
        responseTempContent.append("<head>");
        responseTempContent.append("<title>Arkheion Test Form</title><br>");
        responseTempContent.append("</head><br>");
        responseTempContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
        responseTempContent.append("<table border=\"0\">");
        responseTempContent.append("<tr>");
        responseTempContent.append("<td>");
        responseTempContent.append("<h1>Arkheion Test Form</h1>");
        responseTempContent.append("Choose one FORM");
        responseTempContent.append("</td>");
        responseTempContent.append("</tr>");
        responseTempContent.append("</table><br>");
        responseTempContent.append("<CENTER>STOP FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<FORM ACTION=\"/" + ArkRequest.Stop.name() + "\" METHOD=\"GET\">");
        responseTempContent.append("<table border=\"0\">");
        responseLSD(ArkRequest.Stop);
        responseTempContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseTempContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseTempContent.append("</table></FORM><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<FORM ACTION=\"/" + ArkRequest.Get.name() + "\" METHOD=\"GET\">");
        responseTempContent.append("<table border=\"0\">");
        responseLSD(ArkRequest.Get);
        contentTypeChoice();
        responseTempContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseTempContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseTempContent.append("</table></FORM><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<CENTER>GET META FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<FORM ACTION=\"/" + ArkRequest.GetMeta.name() + "\" METHOD=\"GET\">");
        responseTempContent.append("<table border=\"0\">");
        responseLSD(ArkRequest.GetMeta);
        contentTypeChoice();
        responseTempContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseTempContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseTempContent.append("</table></FORM><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<FORM ACTION=\"/" + ArkRequest.Post.name() + "\" METHOD=\"POST\">");
        responseTempContent.append("<table border=\"0\">");
        responseLSD(ArkRequest.Post);
        responseTempContent.append("<tr><td>Fill with value Metadata: <br> <textarea name=\"" + ArkArgs.META.name() + "\" cols=60 rows=10></textarea>");
        responseTempContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"" + ArkArgs.FILENAME.name() + "\">");
        responseTempContent.append("</td></tr>");
        responseTempContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseTempContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseTempContent.append("</table></FORM><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("<FORM ACTION=\"/" + ArkRequest.PostUpload.name() + "\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseTempContent.append("<table border=\"0\">");
        responseLSD(ArkRequest.PostUpload);
        responseTempContent.append("<tr><td>Fill with value Metadata: <br> <textarea name=\"" + ArkArgs.META.name() + "\" cols=60 rows=10></textarea>");
        responseTempContent.append("<tr><td>Fill with file: <br> <input type=file name=\"" + ArkArgs.FILEDOC.name() + "\">");
        responseTempContent.append("</td></tr>");
        responseTempContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseTempContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseTempContent.append("</table></FORM><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("</body>");
        responseTempContent.append("</html>");
        ChannelBuffer buf = ChannelBuffers.wrappedBuffer(responseTempContent.toString().getBytes(CharsetUtil.UTF_8));
        responseTempContent.setLength(0);
        HttpResponse response = getResponse();
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        ChannelFuture future = e.getChannel().write(response);
        if (willClose) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void initDoc(Channel channel) {
        if (doc != null || legacy == ArkDirConstants.invalide_idx || store == ArkDirConstants.invalide_idx || document == ArkDirConstants.invalide_idx) {
            status = HttpResponseStatus.BAD_REQUEST;
            sendError(channel, "Doc already initialized");
            return;
        }
        if (legacy == TestStoreAndHttpThread.legacy1T1.getLID()) {
            try {
                doc = (ArkAbstractDualDoc) TestStoreAndHttpThread.legacy1T1.getDocument(store, document);
            } catch (ArUnvalidIndexException e1) {
                status = HttpResponseStatus.BAD_REQUEST;
                sendError(channel, "Doc badly initialized");
                return;
            }
        } else if (legacy == TestStoreAndHttpThread.legacy2.getLID()) {
            try {
                doc = (ArkAbstractDualDoc) TestStoreAndHttpThread.legacy2.getDocument(store, document);
            } catch (ArUnvalidIndexException e1) {
                status = HttpResponseStatus.BAD_REQUEST;
                sendError(channel, "Doc badly initialized");
                return;
            }
        }
    }

    private void storeFile(Channel channel) {
        if (reqType == ArkRequest.Post) {
            if (doc != null && filename != null && metadata != null) {
                try {
                    dkey = doc.storeFromLocalFile(filename, metadata);
                } catch (ArUnvalidIndexException e1) {
                    status = HttpResponseStatus.NOT_ACCEPTABLE;
                    sendError(channel, "While storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                } catch (ArFileException e1) {
                    status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                    sendError(channel, "While Storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                } catch (ArFileWormException e1) {
                    status = HttpResponseStatus.CONFLICT;
                    sendError(channel, "While Storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                }
                responseFinalContent.setLength(0);
                responseFinalContent.append("<POST>");
                responseFinalContent.append("<LID>");
                responseFinalContent.append(legacy);
                responseFinalContent.append("</LID>");
                responseFinalContent.append("<SID>");
                responseFinalContent.append(store);
                responseFinalContent.append("</SID>");
                responseFinalContent.append("<DID>");
                responseFinalContent.append(document);
                responseFinalContent.append("</DID>");
                responseFinalContent.append("<DKEY>");
                responseFinalContent.append(dkey);
                responseFinalContent.append("</DKEY>");
                responseFinalContent.append("<CTYPE>");
                responseFinalContent.append(contentType);
                responseFinalContent.append("</CTYPE>");
                responseFinalContent.append("</POST>");
            } else {
                status = HttpResponseStatus.NOT_ACCEPTABLE;
                sendError(channel, "Missing Informations(1): " + this.reqType + ":" + (doc != null) + ":" + (filename != null) + ":" + (metadata != null));
                return;
            }
        } else {
            if (doc != null && fileUpload != null && metadata != null) {
                if (fileUpload.isInMemory()) {
                    try {
                        doc.store(Configuration.BLOCKSIZE, metadata);
                        ChannelBuffer buffer = fileUpload.getChannelBuffer();
                        DataBlock dataBlock = new DataBlock();
                        dataBlock.setBlock(buffer);
                        dataBlock.setEOF(true);
                        dkey = doc.writeDataBlock(dataBlock);
                    } catch (ArUnvalidIndexException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.NOT_ACCEPTABLE;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileWormException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.CONFLICT;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (IOException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    }
                } else {
                    try {
                        dkey = doc.storeFromLocalFile(fileUpload.getFile().getAbsolutePath(), metadata);
                    } catch (ArUnvalidIndexException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.NOT_ACCEPTABLE;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileWormException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.CONFLICT;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (IOException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    }
                }
                fileUpload.delete();
                responseFinalContent.setLength(0);
                responseFinalContent.append("<POSTUPLOAD>");
                responseFinalContent.append("<LID>");
                responseFinalContent.append(legacy);
                responseFinalContent.append("</LID>");
                responseFinalContent.append("<SID>");
                responseFinalContent.append(store);
                responseFinalContent.append("</SID>");
                responseFinalContent.append("<DID>");
                responseFinalContent.append(document);
                responseFinalContent.append("</DID>");
                responseFinalContent.append("<DKEY>");
                responseFinalContent.append(dkey);
                responseFinalContent.append("</DKEY>");
                responseFinalContent.append("<CTYPE>");
                responseFinalContent.append(contentType);
                responseFinalContent.append("</CTYPE>");
                responseFinalContent.append("</POSTUPLOAD>");
            } else {
                if (fileUpload != null) fileUpload.delete();
                status = HttpResponseStatus.NOT_ACCEPTABLE;
                sendError(channel, "Missing Informations(2): " + this.reqType + ":" + (doc != null) + ":" + (fileUpload != null) + ":" + (metadata != null));
                return;
            }
        }
        status = HttpResponseStatus.OK;
        writeResponse(channel);
    }

    private void post(MessageEvent e) {
        getUriArgs();
        try {
            decoder = new HttpPostRequestDecoder(factory, request);
        } catch (ErrorDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoder creation: " + e1.getMessage());
            return;
        } catch (IncompatibleDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoder creation: " + e1.getMessage());
            return;
        }
        if (request.isChunked()) {
            readingChunks = true;
        } else {
            readHttpDataAllReceive(e.getChannel());
            storeFile(e.getChannel());
        }
    }

    private void postChunk(MessageEvent e) {
        HttpChunk chunk = (HttpChunk) e.getMessage();
        try {
            decoder.offer(chunk);
        } catch (ErrorDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoding chunk: " + e1.getMessage());
            return;
        }
        readHttpDataChunkByChunk(e.getChannel());
        if (chunk.isLast()) {
            storeFile(e.getChannel());
            readingChunks = false;
        }
    }

    private void writeFileResponse(Channel channel) {
        if (doc == null) {
            status = HttpResponseStatus.BAD_REQUEST;
            sendError(channel, "Document referenced not initialized");
            return;
        }
        status = HttpResponseStatus.OK;
        HttpResponse response = getResponse();
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, contentType);
        ChannelBuffer buffer = null;
        if (!willClose) {
            long length = 0;
            if (this.reqType == ArkRequest.Get) {
                try {
                    length = this.doc.length();
                    response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(length));
                } catch (ArUnvalidIndexException e) {
                    willClose = true;
                }
            } else {
                try {
                    buffer = ChannelBuffers.wrappedBuffer(this.doc.readMetadata().getBytes());
                } catch (ArUnvalidIndexException e) {
                    status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                    sendError(channel, "While getting File Response: " + e.getMessage());
                    return;
                } catch (ArFileException e) {
                    status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                    sendError(channel, "While getting File Response: " + e.getMessage());
                    return;
                }
                length = buffer.readableBytes();
                response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(length));
            }
        }
        ChannelFuture future = null;
        if (buffer != null) {
            channel.write(response);
            future = channel.write(buffer);
        } else {
            ArkChunkedInput realdocOrMeta;
            try {
                realdocOrMeta = new ArkChunkedInput(this.doc, this.reqType == ArkRequest.Get);
            } catch (ArUnvalidIndexException e) {
                status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                sendError(channel, "While getting File Response: " + e.getMessage());
                return;
            } catch (ArFileException e) {
                status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                sendError(channel, "While getting File Response: " + e.getMessage());
                return;
            }
            channel.write(response);
            future = channel.write(realdocOrMeta);
        }
        logger.debug("Send Resp File: " + reqType);
        if (willClose) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void get(MessageEvent e) {
        initDoc(e.getChannel());
        writeFileResponse(e.getChannel());
    }

    private void getMeta(MessageEvent e) {
        initDoc(e.getChannel());
        writeFileResponse(e.getChannel());
    }

    /**
     * Send an error and close
     * 
     * @param channel
     * @param mesg
     */
    private void sendError(Channel channel, String mesg) {
        if (!channel.isConnected()) {
            if (this.reqType != null) logger.warn("Error from " + this.reqType.name() + ": " + mesg); else logger.warn("Error: " + mesg);
            return;
        }
        HttpResponse response = getResponse();
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html");
        responseTempContent.setLength(0);
        responseTempContent.append("<html>");
        responseTempContent.append("<head>");
        responseTempContent.append("<title>Netty Test Form</title><br>");
        responseTempContent.append("</head><br>");
        responseTempContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
        responseTempContent.append("<table border=\"0\">");
        responseTempContent.append("<tr>");
        responseTempContent.append("<td>");
        responseTempContent.append("<h1>Netty Test Form</h1>");
        responseTempContent.append("OpenR66 Web Failure: ");
        responseTempContent.append(status.toString());
        responseTempContent.append(" from ");
        responseTempContent.append(mesg);
        responseTempContent.append("</td>");
        responseTempContent.append("</tr>");
        responseTempContent.append("</table><br>");
        responseTempContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseTempContent.append("</body>");
        responseTempContent.append("</html>");
        if (this.reqType != null) logger.warn("Error from " + this.reqType.name() + ": " + mesg); else logger.warn("Error: " + mesg);
        response.setContent(ChannelBuffers.wrappedBuffer(responseTempContent.toString().getBytes(GgStringUtils.UTF8)));
        responseTempContent.setLength(0);
        if (channel.isConnected()) {
            channel.write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (e.getChannel().isConnected()) {
            logger.warn("Exception {}", e.getCause().getMessage(), e.getCause());
            if (e.getCause() instanceof ClosedChannelException) {
                return;
            }
            status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            sendError(ctx.getChannel(), "Exception get: " + e.getCause().getMessage());
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ChannelGroup group = Configuration.getHttpChannelGroup();
        if (group != null) {
            group.add(e.getChannel());
        }
        super.channelConnected(ctx, e);
    }

    /**
     * Example of reading all InterfaceHttpData from finished transfer
     * 
     * @param channel
     */
    private void readHttpDataAllReceive(Channel channel) {
        List<InterfaceHttpData> datas = null;
        try {
            datas = decoder.getBodyHttpDatas();
        } catch (NotEnoughDataDecoderException e1) {
            logger.warn("decoder issue", e1);
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(channel, "Decoding Issue: " + e1.getMessage());
            return;
        }
        for (InterfaceHttpData data : datas) {
            writeHttpData(data, channel);
        }
    }

    /**
     * Example of reading request by chunk and getting values from chunk to
     * chunk
     * 
     * @param channel
     */
    private void readHttpDataChunkByChunk(Channel channel) {
        try {
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    writeHttpData(data, channel);
                }
            }
        } catch (EndOfDataDecoderException e1) {
            return;
        }
    }

    private void writeHttpData(InterfaceHttpData data, Channel channel) {
        boolean docid = false;
        if (data.getHttpDataType() == HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            String name = attribute.getName();
            ArkArgs arg = null;
            try {
                arg = ArkArgs.valueOf(name);
            } catch (IllegalArgumentException e) {
                attribute.delete();
                return;
            }
            try {
                String value = attribute.getValue();
                switch(arg) {
                    case LEG:
                        legacy = Long.parseLong(value);
                        docid = true;
                        break;
                    case STO:
                        store = Long.parseLong(value);
                        docid = true;
                        break;
                    case DID:
                        document = Long.parseLong(value);
                        docid = true;
                        break;
                    case META:
                        metadata = value;
                        break;
                    case FILENAME:
                        filename = value;
                        break;
                    case CTYPE:
                        contentType = value;
                        break;
                    case REQTYPE:
                        if (value.equals(ArkRequest.Post.name())) {
                            reqType = ArkRequest.Post;
                        } else if (value.equals(ArkRequest.PostUpload.name())) {
                            reqType = ArkRequest.PostUpload;
                        } else if (value.equals(ArkRequest.Get.name())) {
                            reqType = ArkRequest.Get;
                        } else if (value.equals(ArkRequest.GetMeta.name())) {
                            reqType = ArkRequest.GetMeta;
                        } else if (value.equals(ArkRequest.Menu.name())) {
                            reqType = ArkRequest.Menu;
                        } else if (value.equals(ArkRequest.Stop.name())) {
                            reqType = ArkRequest.Stop;
                        } else {
                            reqType = null;
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                attribute.delete();
                status = HttpResponseStatus.BAD_REQUEST;
                sendError(channel, "While getting Long Value for Index: " + e.getMessage());
                return;
            } catch (IOException e) {
                attribute.delete();
                status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                sendError(channel, "While storing partial inputs: " + e.getMessage());
                return;
            }
            attribute.delete();
        } else if (data.getHttpDataType() == HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;
            if (fileUpload.isCompleted()) {
                if (fileUpload.getName().equals(ArkArgs.FILEDOC.name())) {
                    this.fileUpload = fileUpload;
                }
            } else {
                logger.warn("File still pending but should not");
            }
        } else {
            logger.warn("Unknown element: " + data.toString());
        }
        if (docid && legacy != ArkDirConstants.invalide_idx && store != ArkDirConstants.invalide_idx && document != ArkDirConstants.invalide_idx) {
            initDoc(channel);
        }
    }
}
