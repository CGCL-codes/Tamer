package org.jefb.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.jefb.entity.dto.FileToken;
import org.jefb.entity.dto.TransmissionCallback;
import org.jefb.service.INIOService;
import org.jefb.util.JefbUtils;
import org.jefb.util.service.impl.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NIOService implements INIOService {

    private static Logger log = LoggerFactory.getLogger(NIOService.class);

    @Autowired
    private Config config;

    public void readToken(FileToken token, String absoluteFileName) {
        try {
            FileChannel fileChannel = new RandomAccessFile(absoluteFileName, "r").getChannel();
            Long newLength = config.getMaxTokenLength();
            if (config.getMaxTokenLength() + token.getOffset() > token.getFileLength()) {
                newLength = token.getFileLength() - token.getOffset();
            }
            MappedByteBuffer targetBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, token.getOffset(), newLength);
            token.setData(new byte[newLength.intValue()]);
            token.setLength(newLength);
            targetBuffer.get(token.getData());
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToken(FileToken fileTokenDTO, TransmissionCallback transmissionCallback) {
        String fullFileName = "/cinbox" + fileTokenDTO.getPath() + JefbUtils.FILE_SEPARATOR + fileTokenDTO.getFilename();
        try {
            FileChannel targetChannel = new RandomAccessFile(fullFileName, "rw").getChannel();
            MappedByteBuffer targetBuffer = targetChannel.map(FileChannel.MapMode.READ_WRITE, fileTokenDTO.getOffset(), fileTokenDTO.getLength());
            targetBuffer.put(fileTokenDTO.getData());
            targetChannel.close();
        } catch (FileNotFoundException fnfe) {
            transmissionCallback.getMessages().add("Unable to find file named:" + fullFileName);
        } catch (IOException e) {
            transmissionCallback.getMessages().add("Unable to access target file:" + fullFileName);
        } catch (Exception e) {
            transmissionCallback.getMessages().add("Unexpected exception by writing token for file:" + fullFileName);
        }
    }

    private void createDirs(String currentPath) {
        File dir = new File(currentPath);
        dir.mkdirs();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
