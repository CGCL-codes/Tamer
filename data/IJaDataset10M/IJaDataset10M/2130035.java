package com.vin.scriptutils.utils.unix;

import com.vin.scriptutils.utils.Base64;
import com.vin.scriptutils.utils.UnixCommandBuilder;
import com.vin.scriptutils.utils.ssh.ResultReader;
import com.vin.scriptutils.utils.ssh.SshCommand;
import com.vin.scriptutils.utils.ssh.SshTaskExec;
import com.vin.scriptutils.utils.ssh.exception.SshTaskExecException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author igor
 */
public class UnixShellUtils extends SshCommand {

    public UnixShellUtils(SshTaskExec sshTask) {
        super(sshTask);
    }

    /**
     * extractTarFile
     * @param scriptInput .tar file input stream
     * @param remDir remote dir, where are files will be extracted
     * @return
     * @throws java.io.IOException
     * @throws com.vin.scriptutils.utils.ssh.exception.SshTaskExecException
     * @throws java.lang.InterruptedException
     */
    public ResultReader extractTarFile(InputStream tarFileInput, String remDir) throws IOException, SshTaskExecException, InterruptedException {
        UnixCommandBuilder unixBuilder = new UnixCommandBuilder();
        ResultReader rd;
        unixBuilder.command("tar").argv("-xf -").argv("-C").argvQ(remDir);
        rd = execute(unixBuilder.toCommandLine(), tarFileInput);
        if (rd.isError()) {
            return null;
        }
        return rd;
    }

    /**
     * putBinFile
     * @param scrStream
     * @param remFilePath remote dir, where is file will be stored
     * @return
     * @throws java.io.IOException
     * @throws com.vin.scriptutils.utils.ssh.exception.SshTaskExecException
     * @throws java.lang.InterruptedException
     */
    public ResultReader putBinFile(InputStream scrFileStream, String remFilePath) throws IOException, SshTaskExecException, InterruptedException {
        byte[] bytes = new byte[(int) scrFileStream.available()];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = scrFileStream.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        String encStr = Base64.encodeBytes(bytes, Base64.DO_BREAK_LINES);
        UnixCommandBuilder unixBuilder = new UnixCommandBuilder();
        ResultReader rd;
        String b64Name = remFilePath + ".b64";
        unixBuilder.command("cat").redirectOut().argvQ(b64Name);
        rd = execute(unixBuilder.toCommandLine(), new ByteArrayInputStream(encStr.getBytes()));
        if (rd.isError()) {
            return null;
        }
        unixBuilder.reset();
        unixBuilder.command("openssl").argv("enc").argv("-d").argv("-base64").argv("-in").argvQ(b64Name).argv("-out").argvQ(remFilePath);
        rd = execute(unixBuilder);
        if (rd.isError()) {
            return null;
        }
        unixBuilder.command("rm").argv("-f").argvQ(b64Name);
        rd = execute(unixBuilder);
        if (rd.isError()) {
            return null;
        }
        return rd;
    }
}
