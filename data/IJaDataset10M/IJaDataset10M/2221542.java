package it.crx.software.yajngi.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import it.crx.software.yajngi.Headers;
import it.crx.software.yajngi.ng.NewsArticle;
import it.crx.software.yajngi.ng.NewsGroup;
import it.crx.software.yajngi.ng.NewsServer;

public class Tester {

    public static void main(String[] args) {
        NewsServer ns = new NewsServer("news.fastwebnet.it", 119, true);
        NewsGroup ng = new NewsGroup(ns, "it.economia.investire", 3);
        NewsArticle article = ng.getArticle(93614);
        try {
            MimeMessage msg = new MimeMessage(null, new ByteArrayInputStream(article.getCompleteMessage().getBytes("UTF-8")));
            msg.setContent(article.getCompleteMessage(), article.getHeader(Headers.CONTENT_TYPE));
            System.out.println("--" + article.getCompleteMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
