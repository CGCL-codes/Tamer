package eu.chainfire.gingerbreak;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Asset {

    public static String assetFolder(Context paramContext) {
        return paramContext.getFilesDir() + File.separator;
    }

    public static String extractAssetToFile(Context paramContext, String paramString) {
        String str1 = paramString;
        String str2;
        InputStream localInputStream = null;
        FileOutputStream localFileOutputStream = null;
        try {
            String str3 = paramContext.getFilesDir() + File.separator + str1;
            File localFile = new File(str3);
            if (localFile.exists()) localFile.delete();
            localInputStream = paramContext.getAssets().open(paramString);
            localFileOutputStream = new FileOutputStream(str3);
            byte[] arrayOfByte = new byte[4096];
            while (true) {
                int i = localInputStream.available();
                if (i <= 0) {
                    localFileOutputStream.close();
                    localInputStream.close();
                    str2 = str3;
                    break;
                } else {
                    i = localInputStream.read(arrayOfByte, 0, 4096);
                    localFileOutputStream.write(arrayOfByte, 0, i);
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            str2 = "";
        } finally {
            try {
                if (localFileOutputStream != null) localFileOutputStream.close();
                if (localInputStream != null) localInputStream.close();
            } catch (Exception e) {
            }
        }
        return str2;
    }
}
