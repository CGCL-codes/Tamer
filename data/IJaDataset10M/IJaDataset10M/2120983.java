package com.zhongkai.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.upload.FormFile;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class UploadTool {

    private int diskBufferSize = 1024;

    private int imageWidth = 0;

    private int imagehight = 0;

    private int iswatermark = 0;

    private String watermarkString = null;

    private String watermarkImagePath = null;

    private int imagemaxsize = 0;

    private int imageminsize = 0;

    private FormFile formFile = null;

    private String filePath = null;

    private int watermark_width = 100;

    private int watermark_hight = 100;

    private String position = "5";

    private int watermarkStringSize = 10;

    private Logger log = Logger.getLogger(this.getClass());

    public void UpLoadImage() throws Exception {
        if (formFile == null) {
        } else if (imagemaxsize != 0 && imagemaxsize * 1024 < formFile.getFileSize()) {
        } else if (imageminsize != 0 && imageminsize * 1024 > formFile.getFileSize()) {
        } else if (filePath == null) {
        } else if (iswatermark != 0) {
            if (watermarkString != null && !watermarkString.equals("")) {
                BufferedImage bi = ImageIO.read(formFile.getInputStream());
                Graphics2D g = bi.createGraphics();
                g.setColor(Color.BLACK);
                g.setFont(new Font("宋体", Font.PLAIN, watermarkStringSize));
                int Xcoord = 0;
                int Ycoord = 0;
                if ("1".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = 0;
                }
                if ("2".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = 0;
                }
                if ("3".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = 0;
                }
                if ("4".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("5".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("6".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = (bi.getHeight() - watermarkStringSize) / 2;
                }
                if ("7".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                if ("8".equalsIgnoreCase(position)) {
                    Xcoord = (bi.getWidth() - watermarkString.length()) / 2;
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                if ("9".equalsIgnoreCase(position)) {
                    Xcoord = bi.getWidth() - watermarkString.length();
                    Ycoord = bi.getHeight() - watermarkStringSize;
                }
                g.drawString(watermarkString, Xcoord, Ycoord);
                g.dispose();
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                param.setQuality(0.9f, false);
                encoder.encode(bi);
                bi = null;
                fos.close();
                fos = null;
            } else if (watermarkImagePath != null) {
                BufferedImage bi = ImageIO.read(formFile.getInputStream());
                int width = bi.getWidth();
                int height = bi.getHeight();
                Image Itemp = compress(watermarkImagePath, width, height);
                Graphics2D g = bi.createGraphics();
                g.setBackground(Color.black);
                int pits[] = new int[2];
                int Xcoord = 0;
                int Ycoord = 0;
                if ("1".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = 0;
                }
                if ("2".equalsIgnoreCase(position)) {
                    Xcoord = width / 2 - Itemp.getWidth(null) / 2;
                    Ycoord = 0;
                }
                if ("3".equalsIgnoreCase(position)) {
                    Xcoord = width - Itemp.getWidth(null);
                    Ycoord = 0;
                }
                if ("4".equalsIgnoreCase(position)) {
                    Xcoord = 0;
                    Ycoord = height / 2 - Itemp.getHeight(null) / 2;
                }
                if ("5".equalsIgnoreCase(position)) {
                    pits = getMidPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("6".equalsIgnoreCase(position)) {
                    Xcoord = width - Itemp.getWidth(null);
                    Ycoord = height / 2 - Itemp.getHeight(null) / 2;
                }
                if ("7".equalsIgnoreCase(position)) {
                    pits = getLeftDownPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("9".equalsIgnoreCase(position)) {
                    pits = getRightDownPels(width, height, Itemp);
                    Xcoord = pits[0];
                    Ycoord = pits[1];
                }
                if ("8".equalsIgnoreCase(position)) {
                    Xcoord = (width - Itemp.getWidth(null)) / 2;
                    Ycoord = height - Itemp.getHeight(null);
                }
                g.drawImage(Itemp, Xcoord, Ycoord, null);
                g.dispose();
                FileOutputStream out = new FileOutputStream(filePath);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                param.setQuality(0.95f, true);
                encoder.encode(bi, param);
                out.close();
                Itemp.flush();
                Itemp = null;
                bi.flush();
                bi = null;
            } else {
                BufferedInputStream in = null;
                BufferedOutputStream fos = null;
                try {
                    in = new BufferedInputStream(formFile.getInputStream());
                    fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                    int read = 0;
                    byte buffer[] = new byte[diskBufferSize];
                    while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                        fos.write(buffer, 0, read);
                    }
                    buffer = null;
                    fos.flush();
                    in.close();
                    fos.close();
                } finally {
                    in = null;
                    fos = null;
                }
            }
        } else {
            BufferedInputStream in = null;
            BufferedOutputStream fos = null;
            try {
                in = new BufferedInputStream(formFile.getInputStream());
                fos = new BufferedOutputStream(new FileOutputStream(new File(filePath)), diskBufferSize);
                int read = 0;
                byte buffer[] = new byte[diskBufferSize];
                while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                    fos.write(buffer, 0, read);
                }
                buffer = null;
                fos.flush();
                in.close();
                fos.close();
            } finally {
                in = null;
                fos = null;
            }
        }
    }

    public String UpLoadImage(FormFile formfile, HttpServletRequest request, ActionServlet servlet) throws Exception {
        String startPath = "";
        String AutoDir = StringTool.createAutoDir();
        File file = new File(servlet.getServletContext().getRealPath(startPath + AutoDir));
        if (!file.exists()) {
            file.mkdirs();
        }
        String DatabasePath = AutoDir + StringTool.getAutoFileName(formfile.getFileName());
        String fullPath = servlet.getServletContext().getRealPath(startPath + DatabasePath);
        this.setFilePath(fullPath);
        UpLoadImage();
        return DatabasePath;
    }

    public String UpLoadfile(FormFile formfile, HttpServletRequest request, ActionServlet servlet) throws Exception {
        Pattern p = Pattern.compile(".html|.jsp|.exe|.bat");
        Matcher m = p.matcher(formfile.getFileName());
        if (m.matches()) {
            throw new RuntimeException("非法的文件名!");
        }
        String startPath = "";
        File file = new File(servlet.getServletContext().getRealPath(startPath));
        if (!file.exists()) {
            file.mkdirs();
        }
        String DatabasePath = StringTool.getAutoFileName(formfile.getFileName());
        String fullPath = servlet.getServletContext().getRealPath(startPath + "/" + DatabasePath);
        BufferedInputStream in = null;
        BufferedOutputStream fos = null;
        try {
            in = new BufferedInputStream(formfile.getInputStream());
            fos = new BufferedOutputStream(new FileOutputStream(new File(fullPath)), diskBufferSize);
            int read = 0;
            byte buffer[] = new byte[diskBufferSize];
            while ((read = in.read(buffer, 0, diskBufferSize)) > 0) {
                fos.write(buffer, 0, read);
            }
            buffer = null;
            fos.flush();
            in.close();
            fos.close();
        } finally {
            in = null;
            fos = null;
        }
        return DatabasePath;
    }

    private static Image compress(String file_path, int width, int height) {
        BufferedImage Bi = null;
        FileInputStream fin = null;
        try {
            if (width < 400) {
                String t_path = file_path.replaceFirst(".png", "-s.png");
                try {
                    fin = new FileInputStream(t_path);
                } catch (Exception e) {
                    System.out.println("can not find small watermark");
                    fin = new FileInputStream(file_path);
                }
            } else fin = new FileInputStream(file_path);
            Bi = ImageIO.read(fin);
            fin.close();
        } catch (Exception es) {
            System.out.println(es.getMessage());
        }
        int in_h = Bi.getHeight();
        int in_w = Bi.getWidth();
        float Ratio = getRatio(width, height, in_w, in_h);
        double s_h = in_h;
        double s_w = in_w;
        int int_s_h = (int) s_h;
        int int_s_w = (int) s_w;
        Graphics2D g = Bi.createGraphics();
        g.rotate(Math.toRadians(30));
        Image Itemp = Bi.getScaledInstance(int_s_w, int_s_h, Image.SCALE_SMOOTH);
        System.out.println(Itemp.getHeight(null));
        System.out.println(Itemp.getWidth(null));
        System.out.println(Itemp.getHeight(null));
        System.out.println(Itemp.getWidth(null));
        return Itemp;
    }

    private static float getRatio(int X, int Y, int Xw, int Yw) {
        float x_r = (float) (X * 4 / 5) / Xw;
        float y_r = (float) (Y * 4 / 5) / Yw;
        if (x_r > 1 && y_r > 1) return 1;
        if (x_r < y_r) return x_r; else return y_r;
    }

    private static int[] getMidPels(int X, int Y, Image img) {
        int[] stat_cood = new int[2];
        stat_cood[0] = 0;
        stat_cood[1] = 0;
        int Xend = 0;
        int Yend = 0;
        int Xw = 0;
        int Yw = 0;
        Xw = img.getWidth(null);
        Yw = img.getHeight(null);
        if (X >= Xw) {
            Xend = (X / 2 - Xw / 2);
            stat_cood[0] = Xend;
        }
        if (Y >= Yw) {
            Yend = (Y / 2 - Yw / 2);
            stat_cood[1] = Yend;
        }
        return stat_cood;
    }

    private static int[] getMidDownPels(int X, int Y, int X1, int Y1, Image img) {
        int[] stat_cood = new int[2];
        stat_cood[0] = 0;
        stat_cood[1] = 0;
        int Xend = 0;
        int Yend = 0;
        int Xw = 0;
        int Yw = 0;
        Xw = img.getWidth(null);
        Yw = img.getHeight(null);
        if (X >= Xw) {
            Xend = (X / 2 - Xw / 2);
            stat_cood[0] = Xend;
        }
        if (Y >= Yw) {
            Yend = ((Y + Y1) / 2 - Yw);
            stat_cood[1] = Yend;
        }
        return stat_cood;
    }

    private static int[] getLeftDownPels(int X, int Y, Image img) {
        int[] stat_cood = new int[2];
        stat_cood[0] = 0;
        stat_cood[1] = 0;
        int Xend = 0;
        int Yend = 0;
        int Xw = 0;
        int Yw = 0;
        Xw = img.getWidth(null);
        Yw = img.getHeight(null);
        if (X >= Xw) {
            Xend = (int) (0 + X * 0.02345);
            stat_cood[0] = Xend;
        }
        if (Y >= Yw) {
            Yend = (int) (Y - Yw - Y * 0.02345);
            stat_cood[1] = Yend;
        }
        return stat_cood;
    }

    private static int[] getRightDownPels(int X, int Y, Image img) {
        int[] stat_cood = new int[2];
        stat_cood[0] = 0;
        stat_cood[1] = 0;
        int Xend = 0;
        int Yend = 0;
        int Xw = 0;
        int Yw = 0;
        Xw = img.getWidth(null);
        Yw = img.getHeight(null);
        if (X >= Xw && Y >= Yw) {
            if (((X - Xw) > (int) (X * 0.02345)) && ((Y - Yw) > (int) (Y * 0.02345))) {
                Xend = (int) (X - Xw - X * 0.02345);
                Yend = (int) (Y - Yw - Y * 0.02345);
            } else {
                Xend = (X - Xw);
                Yend = (Y - Yw);
            }
            stat_cood[0] = Xend;
            stat_cood[1] = Yend;
            return stat_cood;
        }
        return stat_cood;
    }

    public int getDiskBufferSize() {
        return diskBufferSize;
    }

    public void setDiskBufferSize(int diskBufferSize) {
        this.diskBufferSize = diskBufferSize;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImagehight() {
        return imagehight;
    }

    public void setImagehight(int imagehight) {
        this.imagehight = imagehight;
    }

    public int getIswatermark() {
        return iswatermark;
    }

    public void setIswatermark(int iswatermark) {
        this.iswatermark = iswatermark;
    }

    public String getWatermarkString() {
        return watermarkString;
    }

    public void setWatermarkString(String watermarkString) {
        this.watermarkString = watermarkString;
    }

    public String getWatermarkImagePath() {
        return watermarkImagePath;
    }

    public void setWatermarkImagePath(String watermarkImagePath) {
        this.watermarkImagePath = watermarkImagePath;
    }

    public int getImagemaxsize() {
        return imagemaxsize;
    }

    public void setImagemaxsize(int imagemaxsize) {
        this.imagemaxsize = imagemaxsize;
    }

    public int getImageminsize() {
        return imageminsize;
    }

    public void setImageminsize(int imageminsize) {
        this.imageminsize = imageminsize;
    }

    public FormFile getFormFile() {
        return formFile;
    }

    public void setFormFile(FormFile formFile) {
        this.formFile = formFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWatermark_width() {
        return watermark_width;
    }

    public void setWatermark_width(int watermark_width) {
        this.watermark_width = watermark_width;
    }

    public int getWatermark_hight() {
        return watermark_hight;
    }

    public void setWatermark_hight(int watermark_hight) {
        this.watermark_hight = watermark_hight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getWatermarkStringSize() {
        return watermarkStringSize;
    }

    public void setWatermarkStringSize(int watermarkStringSize) {
        this.watermarkStringSize = watermarkStringSize;
    }
}
