package com.jawise.sb2jmsbridge;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

class SbMessageSender {

    private static Logger logger = Logger.getLogger(SbMessageSender.class);

    private DataSource dataSource;

    private SbQueueConfiguration queueconfiguration;

    public SbMessageSender(DataSource dataSource, SbQueueConfiguration queueconfiguration) {
        this.dataSource = dataSource;
        this.queueconfiguration = queueconfiguration;
    }

    public void send(SbMessage msg) throws QueueDisabledException, MessageDeliveryException {
        Connection conn = null;
        Statement state = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            String conversationhandle = msg.getConversationhandle();
            String sql = "";
            if (conversationhandle == null || conversationhandle.isEmpty()) {
                logger.debug("starting new conversation");
                sql = "DECLARE @h UNIQUEIDENTIFIER;\r\n" + "BEGIN DIALOG CONVERSATION @h FROM SERVICE @fromservice TO SERVICE '@toservice' ON CONTRACT @contract WITH ENCRYPTION=OFF;\r\n";
                sql = sql + "SEND ON CONVERSATION @h  MESSAGE TYPE @messagetype ('@msg');";
                sql = sql + "SELECT @h;\r\n";
                sql = sql.replaceAll("@fromservice", queueconfiguration.getFromservice());
                sql = sql.replaceAll("@toservice", queueconfiguration.getService());
                sql = sql.replaceAll("@contract", queueconfiguration.getContract());
                sql = sql.replaceAll("@messagetype", queueconfiguration.getMessagetype());
                sql = sql.replaceAll("@msg", msg.getMessagebody());
                state = conn.createStatement();
                ResultSet rs = state.executeQuery(sql);
                if (rs.next()) {
                    logger.info("Handle : " + rs.getString(1));
                    endConversation(state, rs.getString(1));
                } else {
                    logger.error("no handle return");
                }
            } else if ("WithOutSending".equals(msg.getEndconversation())) {
                state = conn.createStatement();
                endConversation(state, conversationhandle);
            } else {
                logger.debug("using existing conversation handle : " + conversationhandle);
                sql = "SEND ON CONVERSATION '@h'  MESSAGE TYPE @messagetype ('@msg');";
                sql = sql.replaceAll("@h", conversationhandle);
                sql = sql.replaceAll("@messagetype", queueconfiguration.getMessagetype());
                sql = sql.replaceAll("@msg", msg.getMessagebody());
                state = conn.createStatement();
                state.executeUpdate(sql);
                if (queueconfiguration.getEndConversationAfterMessageSend() || "AfterSending".equals(msg.getEndconversation())) {
                    endConversation(state, conversationhandle);
                }
            }
            DataSourceUtils.releaseConnection(conn, dataSource);
        } catch (SQLException ex) {
            try {
                if (conn != null) DataSourceUtils.releaseConnection(conn, dataSource);
            } catch (Exception e) {
                logger.error(ex.getMessage(), e);
            }
            if (ex.getMessage().indexOf("is currently disabled.") >= 0) {
                throw new QueueDisabledException(ex.getMessage());
            } else throw new MessageDeliveryException(ex.getMessage());
        }
    }

    public void send(SbMessage msg, Connection conn) throws QueueDisabledException, MessageDeliveryException {
        Statement state = null;
        try {
            String conversationhandle = msg.getConversationhandle();
            String sql = "";
            if (conversationhandle == null || conversationhandle.isEmpty()) {
                logger.debug("starting new conversation");
                sql = "DECLARE @h UNIQUEIDENTIFIER;\r\n" + "BEGIN DIALOG CONVERSATION @h FROM SERVICE @fromservice TO SERVICE '@toservice' ON CONTRACT @contract WITH ENCRYPTION=OFF;\r\n";
                sql = sql + "SEND ON CONVERSATION @h  MESSAGE TYPE @messagetype ('@msg');";
                sql = sql + "SELECT @h;\r\n";
                sql = sql.replaceAll("@fromservice", queueconfiguration.getFromservice());
                sql = sql.replaceAll("@toservice", queueconfiguration.getService());
                sql = sql.replaceAll("@contract", queueconfiguration.getContract());
                sql = sql.replaceAll("@messagetype", queueconfiguration.getMessagetype());
                sql = sql.replaceAll("@msg", msg.getMessagebody());
                state = conn.createStatement();
                ResultSet rs = state.executeQuery(sql);
                if (rs.next()) {
                    logger.info("Handle : " + rs.getString(1));
                    endConversation(state, rs.getString(1));
                } else {
                    logger.error("no handle return");
                }
            } else {
                logger.debug("using existing conversation handle : " + conversationhandle);
                sql = "SEND ON CONVERSATION '@h'  MESSAGE TYPE @messagetype ('@msg');";
                sql = sql.replaceAll("@h", conversationhandle);
                sql = sql.replaceAll("@messagetype", queueconfiguration.getMessagetype());
                sql = sql.replaceAll("@msg", msg.getMessagebody());
                state = conn.createStatement();
                state.executeUpdate(sql);
                if (queueconfiguration.getEndConversationAfterMessageSend()) {
                    endConversation(state, conversationhandle);
                }
            }
        } catch (SQLException ex) {
            if (ex.getMessage().indexOf("is currently disabled.") >= 0) {
                throw new QueueDisabledException(ex.getMessage());
            } else throw new MessageDeliveryException(ex.getMessage());
        }
    }

    private void endConversation(Statement state, String hanlde) throws SQLException {
        logger.debug("ending conversaion");
        if (queueconfiguration.getEndConversationWithCleanup()) {
            state.execute("END CONVERSATION " + "'" + hanlde + "' WITH CLEANUP");
        } else {
            state.execute("END CONVERSATION " + "'" + hanlde + "'");
        }
    }
}
