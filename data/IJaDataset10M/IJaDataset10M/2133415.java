package jdos.win.system;

import jdos.hardware.Memory;
import jdos.win.builtin.WinAPI;
import jdos.win.utils.FilePath;
import jdos.win.utils.StringUtil;

public class WinFindFile extends WinObject {

    public static WinFindFile create(FilePath[] results) {
        return new WinFindFile(nextObjectId(), results);
    }

    public static WinFindFile get(int handle) {
        WinObject object = getObject(handle);
        if (object == null || !(object instanceof WinFindFile)) return null;
        return (WinFindFile) object;
    }

    FilePath[] results;

    int index = 0;

    private WinFindFile(int id, FilePath[] results) {
        super(id);
        this.results = results;
    }

    public int getNextResult(int address) {
        if (index >= results.length) {
            Scheduler.getCurrentThread().setLastError(jdos.win.utils.Error.ERROR_NO_MORE_FILES);
            return WinAPI.FALSE;
        }
        FilePath file = results[index++];
        String name = file.getName();
        long timestamp = WinFile.millisToFiletime(file.lastModified());
        long size = file.length();
        Memory.mem_writed(address, 0x80);
        address += 4;
        WinFile.writeFileTime(address, timestamp);
        address += 8;
        WinFile.writeFileTime(address, timestamp);
        address += 8;
        WinFile.writeFileTime(address, timestamp);
        address += 8;
        Memory.mem_writed(address, (int) (size >>> 32));
        address += 4;
        Memory.mem_writed(address, (int) size);
        address += 4;
        Memory.mem_writed(address, 0);
        address += 4;
        Memory.mem_writed(address, 0);
        address += 4;
        StringUtil.strcpy(address, name);
        address += WinAPI.MAX_PATH;
        StringUtil.strncpy(address, name, 14);
        address += 14;
        return WinAPI.TRUE;
    }
}
