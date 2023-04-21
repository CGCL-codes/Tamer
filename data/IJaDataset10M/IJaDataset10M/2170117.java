package org.fpse.server.handler.impl.pop3;

import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fpse.forum.Forum;
import org.fpse.forum.ForumFactory;
import org.fpse.forum.NoSuchPostException;
import org.fpse.forum.NoSuchQuestionException;
import org.fpse.forum.NoSuchTopicAreaException;
import org.fpse.forum.Question;
import org.fpse.forum.TopicArea;
import org.fpse.server.Command;
import org.fpse.server.CommandFailedException;
import org.fpse.server.CommandStatus;
import org.fpse.server.ICommandHandler;
import org.fpse.server.ISession;
import org.fpse.server.TaggedOutputStream;
import org.fpse.server.UserSession;
import org.fpse.store.impl.PostMeta;
import org.fpse.topic.Post;
import org.fpse.utils.LoggingContext;

/**
 * Created on Dec 14, 2006 11:30:00 PM by Ajay
 */
public class TOPCommand implements ICommandHandler {

    private static final Log LOG = LogFactory.getLog(UIDLCommand.class);

    public CommandStatus handleCommand(String tag, Command command, String[] arguments, ISession session) throws IOException, CommandFailedException {
        if (arguments == null || arguments.length < 2) {
            LOG.warn("Not enough argument supplied.");
            return CommandStatus.FAILURE;
        }
        int num = -1;
        int lines = -1;
        try {
            num = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            LOG.error("Unable to parse message number: " + arguments[0], e);
            return CommandStatus.FAILURE;
        }
        try {
            lines = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException e) {
            LOG.error("Unable to parse lines: " + arguments[1], e);
            return CommandStatus.FAILURE;
        }
        if (lines > 0) {
            LOG.warn("Got more than zero (" + lines + ") number of lines, which is not supported.");
            return CommandStatus.FAILURE;
        }
        UserSession us = UserSession.getSession(session.getUser());
        if (null == us) {
            LOG.warn("No sesion found for the user: " + session.getUser());
            return CommandStatus.FAILURE;
        }
        LoggingContext.set(us.getForumName(), null, null, null);
        TaggedOutputStream out = session.getOutputStream(tag);
        PostMeta meta;
        try {
            meta = us.getPost(num);
        } catch (SQLException e) {
            LOG.error("Unable to load post.", e);
            return CommandStatus.FAILURE;
        }
        if (null == meta) {
            return CommandStatus.FAILURE;
        }
        LoggingContext.set(meta);
        Forum forum;
        TopicArea ta;
        Question question;
        Post post;
        try {
            forum = ForumFactory.createOrFindForum(us.getForumName());
            ta = forum.find(meta.getTopicArea());
            question = ta.find(meta.getQuestion());
            post = question.find(meta.getPost());
        } catch (NoSuchTopicAreaException e) {
            LOG.error("Unable to find topic area.", e);
            return CommandStatus.FAILURE;
        } catch (NoSuchQuestionException e) {
            LOG.error("Unable to find question.", e);
            return CommandStatus.FAILURE;
        } catch (NoSuchPostException e) {
            LOG.error("Unable to find post.", e);
            return CommandStatus.FAILURE;
        }
        RetrieveCommand.addHeaders(meta, session, out, post, question, ta, forum);
        out.writeLine(".");
        out.flush();
        return CommandStatus.SUCCESS;
    }

    public static String getuidl(PostMeta meta, String forum) {
        return getuidl(forum, meta.getQuestion(), meta.getPost(), meta.isUserMessage());
    }

    public static String getuidl(Post post) {
        Question question = post.getQuestion();
        TopicArea ta = question.getTopicArea();
        Forum forum = ta.getForum();
        return getuidl(forum.getName(), question.getIdentifier(), post.getIdentifier(), false);
    }

    public static String getuidl(String forum, String question, String post, boolean userMessage) {
        return (userMessage ? "1" : "0") + forum + question + post;
    }
}
