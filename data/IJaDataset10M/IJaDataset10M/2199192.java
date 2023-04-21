package net.sf.brightside.mockfantasydrafts.service.commands;

import net.sf.brightside.mockfantasydrafts.core.service.Command;

public interface ChangePasswordCommand extends Command {

    void setUsername(String username);

    void setOldPassword(String oldPassword);

    void setNewPassword(String newPassword);
}
