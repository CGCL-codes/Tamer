package com.nullfish.app.jfd2.command.embed;

import java.io.IOException;
import java.text.MessageFormat;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.JFDModel;
import com.nullfish.app.jfd2.command.Command;
import com.nullfish.app.jfd2.config.DefaultConfig;
import com.nullfish.app.jfd2.ext_command.CommandExecuter;
import com.nullfish.app.jfd2.util.WindowsUtil;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.local.LocalFile;

/**
 * 拡張子関連付け実行コマンド
 * 
 * @author shunji
 */
public class ExtensionMapCommand extends Command {

    public static final String PARAM_MAPPER = "extension_mapper";

    public static final String PARAM_DIR_OPEN = "dir_open_command";

    public void doExecute() throws VFSException {
        JFD jfd = getJFD();
        JFDModel model = jfd.getModel();
        try {
            model.lockAutoUpdate(this);
            String fileName = model.getSelectedFile().getSecurePath();
            String shell = (String) jfd.getCommonConfiguration().getParam("shell", DefaultConfig.getDefaultConfig().getShell());
            if (fileName.indexOf(' ') != -1 && shell.indexOf('\n') == -1) {
                fileName = "\"" + fileName + "\"";
            }
            fileName = WindowsUtil.escapeFileName(fileName);
            String mapper = model.getSelectedFile().isFile(this) ? (String) jfd.getCommonConfiguration().getParam(PARAM_MAPPER, DefaultConfig.getDefaultConfig().getExtensionMapping()) : (String) jfd.getCommonConfiguration().getParam(PARAM_DIR_OPEN, DefaultConfig.getDefaultConfig().getOpenDirCommand());
            String[] values = { fileName };
            MessageFormat format = new MessageFormat(mapper);
            String command = format.format(values);
            VFile currentDir = model.getCurrentDirectory();
            if (currentDir instanceof LocalFile) {
                CommandExecuter.getInstance().exec(command, true, ((LocalFile) currentDir).getFile());
            } else {
                CommandExecuter.getInstance().exec(command, true);
            }
        } catch (IOException e) {
            throw new VFSIOException(e);
        } finally {
            model.unlockAutoUpdate(this);
        }
    }

    public boolean closesUnusingFileSystem() {
        return false;
    }
}
