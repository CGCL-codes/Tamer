package org.snova.c4.client.connection.v2;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.arch.buffer.Buffer;
import org.arch.misc.crypto.base64.Base64;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.DefaultHttpChunkTrailer;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snova.c4.client.config.C4ClientConfiguration;
import org.snova.c4.client.config.C4ClientConfiguration.C4ServerAuth;
import org.snova.c4.client.connection.util.ConnectionHelper;
import org.snova.c4.common.event.EventRestRequest;
import org.snova.framework.util.SharedObjectHelper;
import org.snova.framework.util.proxy.ProxyInfo;

/**
 * @author wqy
 * 
 */
class PushConnection extends HTTPPersistentConnection {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected AtomicBoolean transactionStarted = new AtomicBoolean(false);

    protected LinkedList<Buffer> queuedContents = new LinkedList<Buffer>();

    private ChannelFuture writeFuture;

    private int transactionChunkSize;

    PushConnection(C4ServerAuth auth, HTTPProxyConnectionV2 conn) {
        super(auth, conn);
    }

    private void finishQueuedContens() {
        ChannelBuffer sizeBuf = ChannelBuffers.buffer(4);
        int size = 0;
        ChannelBuffer wrap = null;
        for (Buffer c : queuedContents) {
            size += c.readableBytes();
            ChannelBuffer tmp = ChannelBuffers.wrappedBuffer(c.getRawBuffer(), c.getReadIndex(), c.readableBytes());
            if (null == wrap) {
                wrap = tmp;
            } else {
                wrap = ChannelBuffers.wrappedBuffer(wrap, tmp);
            }
        }
        sizeBuf.writeInt(size);
        final HttpChunk chunk = new DefaultHttpChunk(ChannelBuffers.wrappedBuffer(sizeBuf, wrap));
        transactionChunkSize++;
        if (writeFuture != null && !writeFuture.isDone()) {
            writeFuture.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    writeFuture = getRemoteFuture().getChannel().write(chunk);
                }
            });
        } else {
            writeFuture = getRemoteFuture().getChannel().write(chunk);
        }
        queuedContents.clear();
    }

    private synchronized void sendContent(Buffer content) {
        if (!waitingResponse.get()) {
            String url = "http://" + auth.domain + "/invoke/push";
            HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url);
            request.setHeader("Host", auth.domain);
            request.setHeader(HttpHeaders.Names.CONNECTION, "keep-alive");
            if (null != C4ClientConfiguration.getInstance().getLocalProxy()) {
                ProxyInfo info = C4ClientConfiguration.getInstance().getLocalProxy();
                if (null != info.user) {
                    String userpass = info.user + ":" + info.passwd;
                    String encode = Base64.encodeToString(userpass.getBytes(), false);
                    request.setHeader(HttpHeaders.Names.PROXY_AUTHORIZATION, "Basic " + encode);
                }
            }
            request.setHeader(HttpHeaders.Names.CONTENT_TRANSFER_ENCODING, HttpHeaders.Values.BINARY);
            request.setHeader("UserToken", ConnectionHelper.getUserToken());
            request.setHeader(HttpHeaders.Names.USER_AGENT, C4ClientConfiguration.getInstance().getUserAgent());
            request.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/octet-stream");
            boolean isPullEnabled = C4ClientConfiguration.getInstance().isClientPullEnable();
            if (isPullEnabled) {
                request.setHeader(HttpHeaders.Names.TRANSFER_ENCODING, "chunked");
                getRemoteFuture().getChannel().write(request);
                waitingResponse.set(true);
                transactionStarted.set(true);
                transactionChunkSize = 0;
                SharedObjectHelper.getGlobalTimer().schedule(this, C4ClientConfiguration.getInstance().getPullTransactionTime(), TimeUnit.SECONDS);
            } else {
                ChannelBuffer sent = ChannelBuffers.wrappedBuffer(content.getRawBuffer(), content.getReadIndex(), content.readableBytes());
                ChannelBuffer sizeBuf = ChannelBuffers.buffer(4);
                sizeBuf.writeInt(sent.readableBytes());
                ChannelBuffer tmp = ChannelBuffers.wrappedBuffer(sizeBuf, sent);
                request.setHeader("Content-Length", String.valueOf(tmp.readableBytes()));
                request.setContent(tmp);
                getRemoteFuture().getChannel().write(request);
                if (logger.isDebugEnabled()) {
                    logger.debug("#############" + request);
                }
                waitingResponse.set(true);
                transactionStarted.set(true);
                return;
            }
        }
        if (null != content) {
            queuedContents.add(content);
        }
        if (transactionStarted.get()) {
            finishQueuedContens();
        }
    }

    protected void onFullResponseReceived() {
        if (!queuedContents.isEmpty()) {
            send(null);
        }
    }

    protected synchronized void doFinishTransaction() {
        if (null != remoteFuture && remoteFuture.getChannel().isConnected() && waitingResponse.get()) {
            if (transactionChunkSize == 0) {
                EventRestRequest ev = new EventRestRequest();
                Buffer buffer = new Buffer();
                ev.encode(buffer);
                sendContent(buffer);
            }
            HttpChunk trailer = new DefaultHttpChunkTrailer();
            remoteFuture.getChannel().write(trailer);
        } else {
        }
        transactionStarted.set(false);
    }

    void send(final Buffer content) {
        if (logger.isDebugEnabled()) {
            logger.debug("#####Push " + content.readableBytes());
        }
        ChannelFuture future = getRemoteFuture();
        if (future.getChannel().isConnected()) {
            sendContent(content);
        } else {
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        sendContent(content);
                    } else {
                        send(content);
                    }
                }
            });
        }
    }
}
