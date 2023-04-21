package edu.drexel.p2pftp.handler.message.impl;

import static edu.drexel.p2pftp.handler.message.MessageHeader.HeaderNameEnum.P2PFTP_FILE_NAME;
import static edu.drexel.p2pftp.handler.message.MessageHeader.HeaderNameEnum.P2PFTP_FILE_SIZE;
import static edu.drexel.p2pftp.handler.message.MessageHeader.HeaderNameEnum.P2PFTP_SENDER;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import edu.drexel.p2pftp.P2pConfig;
import edu.drexel.p2pftp.handler.message.Message;
import edu.drexel.p2pftp.handler.message.MessageMetadata;
import edu.drexel.p2pftp.handler.message.Message.MessageType;

public class InviteMetadata extends MessageMetadata {

    private String _sender;

    private long _fileSize;

    private String _fileName;

    public void setRequiredHeaders(Map<String, String> requiredHeaders) {
        _sender = requiredHeaders.remove(P2PFTP_SENDER.name());
        _fileName = requiredHeaders.remove(P2PFTP_FILE_NAME.name());
        _fileSize = Integer.parseInt(requiredHeaders.remove(P2PFTP_FILE_SIZE.name()));
    }

    @Override
    public boolean isValid() {
        return super.isValid() && _sender != null && _fileName != null && _fileSize >= 0;
    }

    public String getSender() {
        return _sender;
    }

    public void setSender(String sender) {
        _sender = sender;
    }

    public long getFileSize() {
        return _fileSize;
    }

    public void setFileSize(long fileSize) {
        _fileSize = fileSize;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String fileName) {
        _fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return Message.MessageType.INVITE;
    }

    @Override
    protected void writeRequiredHeaders(Writer bw) throws IOException {
        bw.append(P2PFTP_SENDER.name() + ":" + _sender + P2pConfig.DELIMITER);
        bw.append(P2PFTP_FILE_NAME.name() + ":" + _fileName + P2pConfig.DELIMITER);
        bw.append(P2PFTP_FILE_SIZE.name() + ":" + _fileSize + P2pConfig.DELIMITER);
    }
}
