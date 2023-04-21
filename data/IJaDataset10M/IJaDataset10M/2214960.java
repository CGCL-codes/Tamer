package org.wat.wcy.isi.mmazur.bp.io.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.wat.wcy.isi.mmazur.bp.io.BinaryObject;

public class BusinessProcess extends BinaryObject {

    private static final int NAME_SIZE = 512;

    private static final int PATH_SIZE = 512;

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String name;

    private static final DateFormat df = new SimpleDateFormat(PATTERN);

    private Date date;

    private String path;

    public BusinessProcess() {
        this.name = "";
        this.date = new Date();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void read(RandomAccessFile raf) {
        try {
            name = readString(raf, NAME_SIZE);
            String d = readString(raf, PATTERN.length());
            if (StringUtils.isEmpty(d)) {
                date = new Date();
            } else {
                date = df.parse(d);
            }
            path = readString(raf, PATH_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String readString(RandomAccessFile raf, int size) throws IOException {
        byte b[] = new byte[size];
        raf.read(b, 0, size);
        return new String(b).trim();
    }

    @Override
    public void write(RandomAccessFile raf) {
        try {
            writeString(raf, name, NAME_SIZE);
            String ds = df.format(date);
            writeString(raf, ds, PATTERN.length());
            writeString(raf, path, PATH_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeString(RandomAccessFile raf, String str, int size) throws IOException {
        raf.write(Arrays.copyOf(str.getBytes(), size));
    }
}
