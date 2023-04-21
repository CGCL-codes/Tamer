package com.sf.plctest.s7emulator;

import com.sf.plctest.shared.ErrorLog;

public interface EventListener {

    void updateBlockLoaded(String blockId);

    void updateBlockImported(String blockId);

    void updateRunmode(int rm);

    void updateCycleFinished(long cycleNo);

    void updateBlockUnloaded(String blockName);

    void updateGeneralReset();

    void updateNewLogEntry(ErrorLog err);

    void updateBlockCompiled(String blockName, ErrorLog errLog);
}
