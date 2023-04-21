package org.activision.util.rs2cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CacheManager {

    private static FileInformationTable main_fit;

    private static FileInformationTable[] cache_fits;

    private static JagexFS fit_filesystem;

    private static JagexFS[] filesystems;

    private static boolean remake;

    private static Object[][][] cached_sub_entry_data;

    public static void load(String path) throws Exception {
        int cache_len = -1;
        for (File f : new File(path).listFiles()) {
            String name = f.getName();
            if (name.startsWith("main_file_cache.idx")) {
                if (Integer.parseInt(name.split(".idx")[1]) != 255 && Integer.parseInt(name.split(".idx")[1]) > cache_len) {
                    cache_len = Integer.parseInt(name.split(".idx")[1]);
                }
            }
        }
        fit_filesystem = new JagexFS(255, path);
        cache_fits = new FileInformationTable[cache_len + 1];
        filesystems = new JagexFS[cache_len + 1];
        try {
            main_fit = new FileInformationTable(FileOperations.ReadFile(path + "main_fit.jct"));
        } catch (Exception e) {
            remake = true;
            main_fit = new FileInformationTable(cache_len + 1);
        }
        cached_sub_entry_data = new Object[cache_len + 1][][];
        for (int fs_id = 0; fs_id < cache_fits.length; fs_id++) {
            cache_fits[fs_id] = new FileInformationTable(fs_id, fit_filesystem, main_fit);
            cached_sub_entry_data[fs_id] = new Object[cache_fits[fs_id].getEntry_real_count()][];
        }
        for (int fs_id = 0; fs_id < filesystems.length; fs_id++) {
            filesystems[fs_id] = new JagexFS(fs_id, path);
        }
        remake |= FileInformationTable.need_rebuild();
        if (remake) {
            rebuild(path);
        }
    }

    public static JagexFS getCrc() {
        return fit_filesystem;
    }

    public static FileInformationTable getFIT(int cache) {
        return cache_fits[cache];
    }

    public static void rebuild(String path) {
        main_fit = new FileInformationTable(cache_fits);
        try {
            RandomAccessFile f = new RandomAccessFile(path + "main_fit.jct", "rw");
            main_fit.write(f, true);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        remake = false;
    }

    public static byte[] getByName(int cache, String name) {
        int id = cache_fits[cache].findName(name);
        return filesystems[cache].get(id);
    }

    public static byte[] getFile(int cache, int id) {
        if (cache == 255) {
            return fit_filesystem.get(id);
        }
        return filesystems[cache].get(id);
    }

    public static boolean init_sub_entrys(int cache, int main) throws IOException {
        if (cached_sub_entry_data[cache].length < main) {
            main = cached_sub_entry_data[cache].length;
        }
        int[] is_11_ = cache_fits[cache].getEntry_sub_ptr()[main];
        boolean bool = true;
        int i_12_ = cache_fits[cache].getEntry_sub_count()[main];
        if (cached_sub_entry_data[cache][main] == null) cached_sub_entry_data[cache][main] = new Object[cache_fits[cache].getEntry_real_sub_count()[main]];
        Object[] objects = cached_sub_entry_data[cache][main];
        for (int i_13_ = 0; i_13_ < i_12_; i_13_++) {
            int i_14_;
            if (is_11_ != null) i_14_ = is_11_[i_13_]; else i_14_ = i_13_;
            if (objects[i_14_] == null) {
                bool = false;
                break;
            }
        }
        if (bool) {
            return true;
        }
        byte[] is_16_ = filesystems[cache].get(main);
        byte[] data = null;
        try {
            data = new CacheContainer(is_16_).decompress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null) return false;
        if ((i_12_ ^ 0xffffffff) < -2) {
            int i_18_ = data.length;
            int i_19_ = data[--i_18_] & 0xff;
            RSInputStream stream = new RSInputStream(new RSByteArrayInputStream(data));
            i_18_ -= i_19_ * (i_12_ * 4);
            stream.seek(i_18_);
            int[] subWritePos = new int[i_12_];
            for (int i_21_ = 0; i_19_ > i_21_; i_21_++) {
                int i_22_ = 0;
                for (int i_23_ = 0; (i_12_ ^ 0xffffffff) < (i_23_ ^ 0xffffffff); i_23_++) {
                    i_22_ += stream.readInt();
                    subWritePos[i_23_] += i_22_;
                }
            }
            byte[][] subData = new byte[i_12_][];
            for (int i_25_ = 0; (i_25_ ^ 0xffffffff) > (i_12_ ^ 0xffffffff); i_25_++) {
                subData[i_25_] = new byte[subWritePos[i_25_]];
                subWritePos[i_25_] = 0;
            }
            int readPos = 0;
            stream.seek(i_18_);
            for (int i_27_ = 0; (i_19_ ^ 0xffffffff) < (i_27_ ^ 0xffffffff); i_27_++) {
                int i_28_ = 0;
                for (int subId = 0; subId < i_12_; subId++) {
                    i_28_ += stream.readInt();
                    System.arraycopy(data, readPos, subData[subId], subWritePos[subId], i_28_);
                    readPos += i_28_;
                    subWritePos[subId] += i_28_;
                }
            }
            for (int i_30_ = 0; (i_30_ ^ 0xffffffff) > (i_12_ ^ 0xffffffff); i_30_++) {
                int i_31_;
                if (is_11_ != null) i_31_ = is_11_[i_30_]; else i_31_ = i_30_;
                objects[i_31_] = subData[i_30_];
            }
        } else {
            int i_32_;
            if (is_11_ != null) i_32_ = is_11_[0]; else i_32_ = 0;
            objects[i_32_] = data;
        }
        cached_sub_entry_data[cache][main] = objects;
        return true;
    }

    public static byte[] getData(int cache, int main, int child) throws IOException {
        if (!init_sub_entrys(cache, main)) throw new IOException("Data not available");
        if (cached_sub_entry_data[cache].length < main) {
            main = cached_sub_entry_data[cache].length;
        }
        return (byte[]) cached_sub_entry_data[cache][main][child];
    }

    public static int containerCount(int cache) {
        return cached_sub_entry_data[cache].length;
    }

    public static int cacheCFCount(int cache) {
        int lastcontainer = containerCount(cache) - 1;
        return 256 * lastcontainer + getRealContainerChildCount(cache, lastcontainer);
    }

    public static int getRealContainerChildCount(int cache, int lastcontainer) {
        return cache_fits[cache].getEntry_real_sub_count()[lastcontainer];
    }

    public static int getContainerChildCount(int cache, int lastcontainer) {
        return cache_fits[cache].getEntry_sub_count()[lastcontainer];
    }
}
