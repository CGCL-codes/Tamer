package org.strophe.sph;

import java.util.Vector;

public interface SphEnvironment {

    public String sphEnv_MakeFilePath(String directoryPath, String fileName, String fileExtension);

    public String sphEnv_MakeDirectoryPath(String parentPath, String directoryName);

    public String sphEnv_GetDirectoryPath(String filePath);

    public String sphEnv_GetName(String filePath);

    public String sphEnv_GetExtension(String filePath);

    public String sphEnv_GetExtendedName(String filePath);

    public String sphEnv_ReadTextFile(String filePath);

    public void sphEnv_WriteTextFile(String filePath, String text);

    public boolean sphEnv_IsDirectory(String filePath);

    public String[] sphEnv_ReadDirectory(String directoryPath);

    public String sphEnv_ReadBinaryFile(String filePath);

    public void sphEnv_WriteBinaryFile(String filePath, String data);

    public void sphEnv_SetHome(String directoryPath);

    public String sphEnv_GetHome();

    public String sphEnv_GetStandardPath();

    public String sphEnv_GetSystemPath();
}
