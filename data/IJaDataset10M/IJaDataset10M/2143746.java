package com.sts.webmeet.content.client.appshare.test;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import com.sts.webmeet.api.MessageRouter;
import com.sts.webmeet.client.test.SocketMessageRouter;
import com.sts.webmeet.content.client.appshare.Content;
import com.sts.webmeet.content.common.ConfigMessage;
import com.sts.webmeet.content.common.appshare.RegionModeMessage;
import com.sts.webmeet.content.common.appshare.StartScrapingMessage;

public class TestSender extends java.awt.Frame {

    private static String CONFIG = "webhuddle.property.appshare.kilobits.per.second=300.0\n" + "webhuddle.property.appshare.minimum.sleep.milliseconds=300\n" + "webhuddle.property.appshare.key.frame.period.seconds=30\n" + "webhuddle.property.appshare.default.compression.type=jpeg\n" + "webhuddle.property.appshare.default.share.mode=region\n\n";

    public TestSender() {
        this.setSize(400, 400);
        this.content = new Content();
        this.setLayout(new BorderLayout());
        this.add(this.content.getAWTComponent(), BorderLayout.CENTER);
        this.setVisible(true);
        this.content.init();
        this.content.contentActivated(true);
    }

    public void connect() throws Exception {
        this.content.onModerator(true);
        ServerSocket ss = new ServerSocket(9999);
        System.out.println("listening for connections...");
        Socket sock = ss.accept();
        System.out.println("...got connection");
        router = new SocketMessageRouter(sock);
        this.content.subscribe(this.router);
        Properties props = new Properties();
        props.load(new ByteArrayInputStream(CONFIG.getBytes()));
        content.readMessage(new ConfigMessage(props, "appshare"));
        router.sendMessageToSelf(new StartScrapingMessage());
    }

    protected com.sts.webmeet.content.client.appshare.Content content;

    protected MessageRouter router;

    public static void main(String[] args) throws Exception {
        TestSender sender = new TestSender();
        sender.connect();
    }
}
