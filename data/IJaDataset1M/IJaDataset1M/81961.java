package com.yingyonghui.market;

import android.content.Context;
import android.os.Environment;
import com.yingyonghui.market.model.Asset;
import dalvik.annotation.Signature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FileManager {

    public static final int BUFFER_SIZE = 8192;

    public static final String ICON = "icon";

    public static final String SCREENSHOT = "screenshot";

    public static final String TOP_RECOMMEND = "top_recommend";

    @Signature({ "Ljava/util/HashMap", "<", "Ljava/lang/String;", "Ljava/lang/String;", ">;" })
    private static HashMap modifyTimeRec = null;

    public static void addApkTempFileTimestampRecord(Context paramContext, String paramString1, String paramString2) {
        if (modifyTimeRec != null) modifyTimeRec.put(paramString1, paramString2);
        while (true) {
            return;
            readTempFileTimestampRecordFromFile(paramContext);
            modifyTimeRec.put(paramString1, paramString2);
        }
    }

    public static void deleteAllFiles(Context paramContext) {
        File[] arrayOfFile1 = paramContext.getFilesDir().listFiles();
        int i;
        File[] arrayOfFile2;
        if (arrayOfFile1 != null) {
            i = 0;
            int j = arrayOfFile1.length;
            if (i < j) ;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile = Environment.getExternalStorageDirectory();
            arrayOfFile2 = new File(localFile, "Yingyonghui").listFiles();
            if (arrayOfFile2 != null) i = 0;
        }
        while (true) {
            int k = arrayOfFile2.length;
            if (i >= k) {
                return;
                arrayOfFile1[i].delete();
                i += 1;
                break;
            }
            arrayOfFile2[i].delete();
            i += 1;
        }
    }

    public static void deleteApkFiles(Context paramContext) {
        File[] arrayOfFile1 = paramContext.getFilesDir().listFiles();
        int i;
        File[] arrayOfFile2;
        if (arrayOfFile1 != null) {
            i = 0;
            int j = arrayOfFile1.length;
            if (i < j) ;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile1 = Environment.getExternalStorageDirectory();
            arrayOfFile2 = new File(localFile1, "Yingyonghui").listFiles();
            if (arrayOfFile2 != null) i = 0;
        }
        while (true) {
            int k = arrayOfFile2.length;
            if (i >= k) {
                return;
                localFile2 = arrayOfFile1[i];
                if ((!localFile2.getName().startsWith("icon")) && (!localFile2.getName().startsWith("top_recommend"))) localFile2.delete();
                i += 1;
                break;
            }
            File localFile2 = arrayOfFile2[i];
            if ((!localFile2.getName().startsWith("icon")) && (!localFile2.getName().startsWith("top_recommend"))) localFile2.delete();
            i += 1;
        }
    }

    public static void deleteFile(Context paramContext, Asset paramAsset) {
        File[] arrayOfFile1 = paramContext.getFilesDir().listFiles();
        int i;
        File[] arrayOfFile2;
        if (arrayOfFile1 != null) {
            i = 0;
            int j = arrayOfFile1.length;
            if (i < j) ;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile1 = Environment.getExternalStorageDirectory();
            arrayOfFile2 = new File(localFile1, "Yingyonghui").listFiles();
            if (arrayOfFile2 != null) i = 0;
        }
        while (true) {
            int k = arrayOfFile2.length;
            if (i >= k) {
                return;
                localFile2 = arrayOfFile1[i];
                String str1 = localFile2.getName();
                String str2 = String.valueOf(paramAsset.pkgName);
                if (str1.equals(str2)) localFile2.delete();
                i += 1;
                break;
            }
            File localFile2 = arrayOfFile2[i];
            String str3 = localFile2.getName();
            String str4 = String.valueOf(paramAsset.pkgName);
            if (str3.equals(str4)) localFile2.delete();
            i += 1;
        }
    }

    public static void deleteFile(Context paramContext, String paramString) {
        File[] arrayOfFile1 = paramContext.getFilesDir().listFiles();
        int i;
        File[] arrayOfFile2;
        if (arrayOfFile1 != null) {
            i = 0;
            int j = arrayOfFile1.length;
            if (i < j) ;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile1 = Environment.getExternalStorageDirectory();
            arrayOfFile2 = new File(localFile1, "Yingyonghui").listFiles();
            if (arrayOfFile2 != null) i = 0;
        }
        while (true) {
            int k = arrayOfFile2.length;
            if (i >= k) {
                return;
                localFile2 = arrayOfFile1[i];
                if (localFile2.getName().equals(paramString)) localFile2.delete();
                i += 1;
                break;
            }
            File localFile2 = arrayOfFile2[i];
            if (localFile2.getName().equals(paramString)) localFile2.delete();
            i += 1;
        }
    }

    @Signature({ "(", "Landroid/content/Context;", "I", "Ljava/lang/String;", "Ljava/util/HashMap", "<", "Ljava/lang/Integer;", "Ljava/lang/String;", ">;)", "Ljava/io/RandomAccessFile;" })
    public static RandomAccessFile getApkCacheStream(Context paramContext, int paramInt, String paramString, HashMap paramHashMap) throws FileNotFoundException {
        RandomAccessFile localRandomAccessFile;
        File localFile3;
        if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile1 = Environment.getExternalStorageDirectory();
            File localFile2 = new File(localFile1, "Yingyonghui");
            if (!localFile2.exists()) localFile2.mkdirs();
            String str1 = String.valueOf(Environment.getExternalStorageDirectory().toString());
            String str2 = str1 + "/Yingyonghui/" + paramString;
            localRandomAccessFile = new RandomAccessFile(str2, "rw");
            String str3 = String.valueOf(Environment.getExternalStorageDirectory().toString());
            String str4 = str3 + "/Yingyonghui";
            localFile3 = new File(str4, paramString);
        }
        while (true) {
            Integer localInteger = Integer.valueOf(paramInt);
            String str5 = localFile3.getPath();
            paramHashMap.put(localInteger, str5);
            return localRandomAccessFile;
            int i;
            if (!paramContext.getFileStreamPath(paramString).exists()) i = 1;
            try {
                FileOutputStream localFileOutputStream = paramContext.openFileOutput(paramString, i);
                localFile3 = paramContext.getFileStreamPath(paramString);
                String str6 = localFile3.getPath();
                localRandomAccessFile = new RandomAccessFile(str6, "rw");
            } catch (IOException localIOException) {
                while (true) localIOException.printStackTrace();
            }
        }
    }

    public static File getFile(Context paramContext, Asset paramAsset) {
        Object localObject;
        if (paramAsset == null) localObject = null;
        while (true) {
            return localObject;
            try {
                String str1 = String.valueOf(paramContext.getFilesDir().getAbsolutePath());
                StringBuilder localStringBuilder1 = new StringBuilder(str1);
                String str2 = File.separator;
                StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
                String str3 = paramAsset.pkgName;
                String str4 = str3 + ".apk";
                File localFile1 = new File(str4);
                if ((localFile1.exists()) && (localFile1.isFile())) {
                    long l1 = localFile1.length();
                    long l2 = paramAsset.size;
                    if (l1 == l2) {
                        localObject = localFile1;
                        continue;
                    }
                }
                if (Environment.getExternalStorageState().equals("mounted")) {
                    String str5 = String.valueOf(Environment.getExternalStorageDirectory().toString());
                    String str6 = str5 + "/Yingyonghui";
                    String str7 = String.valueOf(paramAsset.pkgName);
                    String str8 = str7 + ".apk";
                    File localFile2 = new File(str6, str8);
                    if ((localFile2.exists()) && (localFile2.isFile())) {
                        long l3 = localFile2.length();
                        int i = paramAsset.size;
                        long l4 = i;
                        if (l3 == l4) localObject = localFile2;
                    }
                }
            } catch (Throwable localThrowable) {
                localObject = null;
            }
        }
    }

    public static File getFile(Context paramContext, String paramString) {
        Object localObject1;
        if (paramString == "") localObject1 = null;
        while (true) {
            return localObject1;
            try {
                String str1 = String.valueOf(paramContext.getFilesDir().getAbsolutePath());
                StringBuilder localStringBuilder = new StringBuilder(str1);
                String str2 = File.separator;
                String str3 = str2 + paramString + ".apk";
                File localFile1 = new File(str3);
                if ((localFile1.exists()) && (localFile1.isFile())) {
                    localObject1 = localFile1;
                    continue;
                }
                if (Environment.getExternalStorageState().equals("mounted")) {
                    String str4 = String.valueOf(Environment.getExternalStorageDirectory().toString());
                    String str5 = str4 + "/Yingyonghui";
                    String str6 = String.valueOf(paramString);
                    String str7 = str6 + ".apk";
                    File localFile2 = new File(str5, str7);
                    if (localFile2.exists()) {
                        boolean bool = localFile2.isFile();
                        if (bool) localObject2 = localFile2;
                    }
                }
            } catch (Throwable localThrowable) {
                Object localObject2 = null;
            }
        }
    }

    public static FileOutputStream getOutputStream(Context paramContext, String paramString) {
        FileOutputStream localFileOutputStream = null;
        try {
            File localFile2;
            if (Environment.getExternalStorageState().equals("mounted")) {
                File localFile1 = Environment.getExternalStorageDirectory();
                new File(localFile1, "Yingyonghui").mkdirs();
                String str1 = String.valueOf(Environment.getExternalStorageDirectory().toString());
                String str2 = str1 + "/Yingyonghui";
                localFile2 = new File(str2, paramString);
            }
            for (localFileOutputStream = new FileOutputStream(localFile2); ; localFileOutputStream = paramContext.openFileOutput(paramString, 1)) return localFileOutputStream;
        } catch (Throwable localThrowable) {
            while (true) localThrowable.printStackTrace();
        }
    }

    @Signature({ "(", "Landroid/content/Context;", "I", "Ljava/util/Hashtable", "<", "Ljava/lang/Integer;", "Landroid/graphics/drawable/Drawable;", ">;)", "Landroid/graphics/drawable/Drawable;" })
    public static android.graphics.drawable.Drawable lookupThumbnail(Context paramContext, int paramInt, java.util.Hashtable paramHashtable) {
    }

    public static File loopupScreenshot(Context paramContext, int paramInt) {
        String str1;
        if (Environment.getExternalStorageState().equals("mounted")) str1 = "screenshot" + paramInt + ".png";
        while (true) {
            try {
                String str2 = String.valueOf(Environment.getExternalStorageDirectory().toString());
                String str3 = str2 + "/Yingyonghui";
                File localFile1 = new File(str3, str1);
                boolean bool = localFile1.exists();
                if (!bool) continue;
                localFile2 = localFile1;
                return localFile2;
            } catch (Exception localException) {
                localException.printStackTrace();
                localFile2 = null;
                continue;
            }
            File localFile2 = null;
        }
    }

    public static android.graphics.drawable.Drawable readDrawableFromFileSystem(Context paramContext, String paramString) {
    }

    private static void readTempFileTimestampRecordFromFile(Context paramContext) {
        modifyTimeRec = new HashMap();
        File localFile2;
        if (Environment.getExternalStorageState().equals("mounted")) {
            File localFile1 = Environment.getExternalStorageDirectory();
            if (new File(localFile1, "Yingyonghui").exists()) {
                String str1 = String.valueOf(Environment.getExternalStorageDirectory().toString());
                String str2 = str1 + "/Yingyonghui";
                localFile2 = new File(str2, "tempFileModifiedTimeStamps");
                if (!localFile2.exists()) ;
            }
        }
        while (true) {
            BufferedReader localBufferedReader;
            String str3;
            try {
                FileInputStream localFileInputStream1 = new FileInputStream(localFile2);
                InputStreamReader localInputStreamReader1 = new InputStreamReader(localFileInputStream1);
                localBufferedReader = new BufferedReader(localInputStreamReader1);
                str3 = localBufferedReader.readLine();
                if (str3 == null) return;
                if (str3.trim() == "") continue;
                String[] arrayOfString1 = str3.split("#");
                HashMap localHashMap1 = modifyTimeRec;
                String str4 = arrayOfString1[0];
                String str5 = arrayOfString1[1];
                localHashMap1.put(str4, str5);
                str3 = localBufferedReader.readLine();
                continue;
            } catch (Throwable localThrowable1) {
                localThrowable1.printStackTrace();
                continue;
            }
            localFile2 = paramContext.getFileStreamPath("tempFileModifiedTimeStamps");
            if ((localFile2 == null) || (!localFile2.exists())) continue;
            try {
                FileInputStream localFileInputStream2 = new FileInputStream(localFile2);
                InputStreamReader localInputStreamReader2 = new InputStreamReader(localFileInputStream2);
                localBufferedReader = new BufferedReader(localInputStreamReader2);
                for (str3 = localBufferedReader.readLine(); str3 != null; str3 = localBufferedReader.readLine()) {
                    if (str3.trim() == "") continue;
                    String[] arrayOfString2 = str3.split("#");
                    HashMap localHashMap2 = modifyTimeRec;
                    String str6 = arrayOfString2[0];
                    String str7 = arrayOfString2[1];
                    localHashMap2.put(str6, str7);
                }
            } catch (Throwable localThrowable2) {
                localThrowable2.printStackTrace();
            }
        }
    }

    public static String readTimeStampFromFileSystem(Context paramContext, String paramString) {
    }

    public static void removeTempFile(Context paramContext, String paramString) {
        if (Environment.getExternalStorageState() == "mounted") ;
        while (true) {
            File localFile2;
            try {
                StringBuilder localStringBuilder = new StringBuilder();
                File localFile1 = Environment.getExternalStorageDirectory();
                String str = localFile1 + "/Yingyonghui";
                localFile2 = new File(str, paramString);
                if (!localFile2.exists()) continue;
                localFile2.delete();
                return;
            } catch (Throwable localThrowable1) {
                localThrowable1.printStackTrace();
                continue;
            }
            try {
                localFile2 = paramContext.getFileStreamPath(paramString);
                if (!localFile2.exists()) continue;
                localFile2.delete();
            } catch (Throwable localThrowable2) {
                localThrowable2.printStackTrace();
            }
        }
    }

    public static void removeTimestampforApk(Context paramContext, String paramString) {
        if ((modifyTimeRec != null) && (modifyTimeRec.get(paramString) != null)) modifyTimeRec.remove(paramString);
    }

    @Signature({ "(", "Landroid/content/Context;", "I", "Ljava/lang/String;", "Ljava/util/HashMap", "<", "Ljava/lang/Integer;", "Ljava/lang/String;", ">;)V" })
    public static void renameFileToEndWithApk(Context paramContext, int paramInt, String paramString, HashMap paramHashMap) throws FileNotFoundException {
        try {
            Integer localInteger1 = Integer.valueOf(paramInt);
            String str1 = (String) paramHashMap.get(localInteger1);
            File localFile1 = new File(str1);
            String str2 = String.valueOf(str1);
            String str3 = str2 + ".apk";
            File localFile2 = new File(str3);
            if (localFile1.exists()) {
                localFile1.renameTo(localFile2);
                Integer localInteger2 = Integer.valueOf(paramInt);
                paramHashMap.put(localInteger2, str3);
            }
            return;
        } catch (Exception localException) {
            while (true) localException.printStackTrace();
        }
    }

    public static boolean timestampEqual(Context paramContext, String paramString1, String paramString2) {
        int i = 0;
        if (modifyTimeRec != null) {
            String str1 = (String) modifyTimeRec.get(paramString1);
            if (!paramString2.equals(str1)) ;
        }
        for (i = 1; ; i = 1) {
            String str2;
            do {
                return i;
                readTempFileTimestampRecordFromFile(paramContext);
                str2 = (String) modifyTimeRec.get(paramString1);
            } while ((paramString2 == null) || (!paramString2.trim().equals(str2)));
        }
    }

    public static void writeDrawableToFileSystem(Context paramContext, String paramString, android.graphics.drawable.Drawable paramDrawable) {
    }

    public static void writeFirstScreetShotToFileSystem(Context paramContext, int paramInt, android.graphics.drawable.Drawable paramDrawable) {
    }

    public static void writeIconToFileSystem(Context paramContext, int paramInt, android.graphics.drawable.Drawable paramDrawable) {
    }

    public static boolean writeStreamToFileSystem(Context paramContext, String paramString, java.io.InputStream paramInputStream) {
    }

    public static void writeTimeStampToFileSystem(Context paramContext, String paramString1, String paramString2) {
    }

    public static void writeTimestampRecordToFileSystem(Context paramContext) {
        if (modifyTimeRec == null) ;
        while (true) {
            return;
            String str1 = "tempFileModifiedTimeStamps";
            FileOutputStream localFileOutputStream;
            if (Environment.getExternalStorageState().equals("mounted")) {
                try {
                    File localFile1 = Environment.getExternalStorageDirectory();
                    File localFile2 = new File(localFile1, "Yingyonghui");
                    if (!localFile2.exists()) localFile2.mkdir();
                    StringBuilder localStringBuilder1 = new StringBuilder();
                    File localFile3 = Environment.getExternalStorageDirectory();
                    String str2 = localFile3 + "/Yingyonghui";
                    File localFile4 = new File(str2, str1);
                    if (localFile4.exists()) localFile4.delete();
                    localFileOutputStream = new FileOutputStream(localFile4);
                    Iterator localIterator1 = modifyTimeRec.entrySet().iterator();
                    while (localIterator1.hasNext()) {
                        Map.Entry localEntry1 = (Map.Entry) localIterator1.next();
                        String str3 = String.valueOf(localEntry1.getKey().toString());
                        StringBuilder localStringBuilder2 = new StringBuilder(str3).append("#");
                        String str4 = localEntry1.getValue().toString();
                        StringBuilder localStringBuilder3 = localStringBuilder2.append(str4);
                        String str5 = System.getProperty("line.separator");
                        byte[] arrayOfByte1 = str5.getBytes();
                        localFileOutputStream.write(arrayOfByte1);
                    }
                } catch (Throwable localThrowable1) {
                    localThrowable1.printStackTrace();
                }
                continue;
            }
            if (paramContext.getFileStreamPath(str1).exists()) paramContext.getFileStreamPath(str1).delete();
            int i = 1;
            try {
                localFileOutputStream = paramContext.openFileOutput(str1, i);
                Iterator localIterator2 = modifyTimeRec.entrySet().iterator();
                while (localIterator2.hasNext()) {
                    Map.Entry localEntry2 = (Map.Entry) localIterator2.next();
                    String str6 = String.valueOf(localEntry2.getKey().toString());
                    StringBuilder localStringBuilder4 = new StringBuilder(str6).append("-");
                    String str7 = localEntry2.getValue().toString();
                    StringBuilder localStringBuilder5 = localStringBuilder4.append(str7);
                    String str8 = System.getProperty("line.separator");
                    byte[] arrayOfByte2 = str8.getBytes();
                    localFileOutputStream.write(arrayOfByte2);
                }
            } catch (Throwable localThrowable2) {
                localThrowable2.printStackTrace();
            }
        }
    }
}
