package com.windsor.node.service.helper;

import java.io.File;

public interface SettingsService {

    String LOG_EXT = ".log";

    String WNA_LOG = "wna.log";

    String WNE_LOG = "wne.log";

    String WNE2_LOG = "wne2.log";

    String WNOS_LOG = "wnos.log";

    File getTempDir();

    String getTempFilePath();

    String getTempFilePath(String ext);

    File getLogDir();

    String getLogFilePath();
}
