package net.sourceforge.hlm.generic;

import java.io.*;

public interface MessageHandler {

    boolean error(Throwable message);

    boolean warning(Throwable message);

    boolean info(Throwable message);

    void startSection(String message);

    ProgressHandler startSection(String message, float progressFraction);

    void endSection();

    void startFile(File file);

    void endFile();
}
