package edu.clemson.cs.nestbed.common.management.instrumentation;

import java.rmi.RemoteException;
import edu.clemson.cs.nestbed.common.util.RemoteObservable;

public interface ProgramCompileManager extends RemoteObservable {

    public enum Message {

        COMPILE_STARTED, COMPILE_PROGRESS, COMPILE_COMPLETED
    }

    public void compileProgram(int programID, String tosPlatform) throws RemoteException;
}
