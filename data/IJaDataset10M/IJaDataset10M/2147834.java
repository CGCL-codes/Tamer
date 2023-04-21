package org.fpse.forum.javaranch;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.fpse.download.BadResponseException;
import org.fpse.download.Downloader;
import org.fpse.download.IDownloadParameters.Method;
import org.fpse.download.impl.DownloadParameterAdaptor;
import org.fpse.forum.Forum;
import org.fpse.forum.PostFailedException;
import org.fpse.forum.Question;
import org.fpse.forum.TopicArea;
import org.fpse.forum.determinstic.DeterministicForumTopicArea;
import org.fpse.forum.determinstic.DetermisticForumQuestion;
import org.fpse.forum.impl.AbstractForumConfiguration;
import org.fpse.parser.HTMLParser;
import org.fpse.parser.Parsable;
import org.fpse.parser.XPathUtils;
import org.fpse.parser.javaranch.PostParser;
import org.fpse.parser.javaranch.QuestionListParser;
import org.fpse.parser.javaranch.TopicAreaParser;
import org.fpse.server.AuthenticationFailedException;
import org.fpse.topic.Post;
import org.fpse.topic.PostType;
import org.jaxen.JaxenException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Created on Jan 1, 2007 10:40:56 PM by Deepak Sharma
 */
public class JavaRanchForumConfiguration extends AbstractForumConfiguration {

    private static final long serialVersionUID = 664401934404411373L;

    public JavaRanchForumConfiguration(String forumName) {
        super(forumName);
    }

    public Parsable<TopicArea> getForumTopicAreaParser() {
        return new TopicAreaParser();
    }

    public Parsable<Question> getForumQuestionListParser() {
        return new QuestionListParser();
    }

    public Parsable<Post> gePostsParser() {
        return new PostParser();
    }

    public String getReplyLocation() {
        return getURL("reply");
    }

    public String getPostLocation() {
        return getURL("reply-sent");
    }

    public void post(Downloader downloader, Post post, final String message) throws PostFailedException {
        try {
            try {
                downloader.download(new DownloadParameterAdaptor(Downloader.getLocation(getPostsLocation(), post.getQuestion().getTopicArea().getIdentifier(), post.getQuestion().getIdentifier()), Method.Get, false, HttpURLConnection.HTTP_OK));
            } catch (BadResponseException e) {
                throw new PostFailedException("Unable to get the reply page.");
            }
            Document doc = null;
            InputStream in = null;
            try {
                in = downloader.download(new DownloadParameterAdaptor(Downloader.getLocation(getReplyLocation(), post.getQuestion().getTopicArea().getIdentifier(), post.getQuestion().getIdentifier()), Method.Get, HttpURLConnection.HTTP_OK));
                doc = HTMLParser.parse(in);
            } catch (BadResponseException e) {
                throw new PostFailedException("Unable to get the reply page.");
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException _) {
                    }
                }
            }
            Element node = (Element) XPathUtils.getFirstNode("//form[@name='REPLIER']", doc);
            final List list = XPathUtils.getXPath(".//input", node);
            in = null;
            try {
                in = downloader.download(new DownloadParameterAdaptor(getPostLocation(), Method.Post, HttpURLConnection.HTTP_OK) {

                    @Override
                    public void customize(HttpMethod method) {
                        PostMethod postMethod = (PostMethod) method;
                        boolean bodyAdded = false;
                        for (Object o : list) {
                            Element x = (Element) o;
                            String name = x.getAttributeValue("name");
                            String value = x.getAttributeValue("value");
                            if ("message".equals(name)) {
                                value = message;
                                bodyAdded = true;
                            }
                            String type = x.getAttributeValue("type");
                            if ("submit".equalsIgnoreCase(type)) {
                                if ("submit".equals(name)) {
                                    if (postMethod.getParameter(name) == null) postMethod.addParameter(name, value);
                                }
                            } else {
                                if (!"disable_smilies".equals(name)) postMethod.addParameter(name, value);
                            }
                        }
                        if (!bodyAdded) postMethod.addParameter("message", message);
                    }
                });
                doc = HTMLParser.parse(in);
                List list1 = XPathUtils.getXPath("//b", doc);
                if (null == list1 || list1.isEmpty()) {
                    downloader.clearCookies(".javaranch.com");
                    throw new PostFailedException("Unable to post.");
                }
                boolean found = false;
                for (Object o : list1) {
                    node = (Element) o;
                    if ("Thanks for posting!".equalsIgnoreCase(node.getTextTrim())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    downloader.clearCookies(".javaranch.com");
                    throw new PostFailedException("Unable to post.");
                }
            } catch (BadResponseException e) {
                downloader.clearCookies(".javaranch.com");
                throw new PostFailedException("Post Failed - most likely the user session has expired.");
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException _) {
                    }
                }
            }
        } catch (IOException e) {
            throw new PostFailedException(e);
        } catch (JaxenException e) {
            throw new PostFailedException(e);
        } catch (JDOMException e) {
            throw new PostFailedException(e);
        }
    }

    public void login(Downloader downloader, Forum forum, final String user, final String password) throws AuthenticationFailedException {
        if (downloader.hasCookies("ubber2452372.103")) return;
        try {
            downloader.download(new DownloadParameterAdaptor(getLoginURL(), Method.Post, false, HttpURLConnection.HTTP_OK) {

                @Override
                public void customize(HttpMethod m) {
                    PostMethod method = (PostMethod) m;
                    method.addParameter("ubb", "do_login");
                    method.addParameter("refer", "");
                    method.addParameter("username", user);
                    method.addParameter("password", password);
                    method.addParameter("submit", "Login");
                }
            });
        } catch (BadResponseException e) {
            throw new AuthenticationFailedException("Login Failed.");
        } catch (IOException e) {
            throw new AuthenticationFailedException(e);
        }
        if (!downloader.hasCookies("ubber2452372.103")) {
            throw new AuthenticationFailedException("Login Failed.");
        }
    }

    public Post createPost(Question question, String auther, Date postedDate, String id, PostType type) {
        return new Post(question, auther, postedDate, id, type);
    }

    public Question createQuestion(String questionID, TopicArea topicArea, String title, String asker) {
        return new DetermisticForumQuestion(questionID, topicArea, title, asker);
    }

    public TopicArea createTopicArea(Forum forum, String name, String downloadLocation) {
        return new DeterministicForumTopicArea(forum, name, downloadLocation);
    }
}
