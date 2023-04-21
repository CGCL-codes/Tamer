package cn.edu.ynu.sei.liva.lrcShow.ui;

import cn.edu.ynu.sei.liva.lrcShow.lrcSearch.LrcScanner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * 歌词显示面板界面
 * 
 * @author 88250
 */
public class LrcShowPanelUI {

    /**
     * 歌词文件搜索
     */
    private LrcScanner sf = new LrcScanner();

    /**
     * 歌词文件名
     */
    private String lrcFileName = null;

    /**
     * 歌曲演唱者
     */
    private String singer = null;

    /**
     * 歌词显示面板窗口
     */
    protected Shell shell = null;

    /**
     * 歌词显示面板皮肤
     */
    private Image lrcShowIm = null;

    /**
     * 歌词显示线程
     */
    private DrawLrcThread dt = null;

    /**
     * 绘图控制器
     */
    private GC gc = null;

    /**
     * 标识是否正在显示歌词
     */
    public static boolean isDrawing = false;

    /**
     * 播放累计时间
     */
    public static int playedTime = 0;

    /**
     * 创建一个歌词显示面板界面
     * 
     * @param parent
     *                父窗口
     */
    public LrcShowPanelUI(Shell parent) {
        shell = new Shell(parent, SWT.NONE);
        shell.setText("Lyric Show");
    }

    /**
     * 设置播放累计时间
     * 
     * @param playedTime
     *                播放累计时间
     */
    public void setPlayedTime(int playedTime) {
        LrcShowPanelUI.playedTime = playedTime;
    }

    /**
     * 打开/隐藏歌词显示面板
     * 
     * @param b
     *                可见性
     */
    public void setVisible(boolean b) {
        if (b) {
            shell.addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    e.gc.drawImage(lrcShowIm, 0, 0);
                    if (!isDrawing) {
                        gc = new GC(shell);
                        if (lrcFileName != null) {
                            if (sf.exists(lrcFileName)) {
                                dt = new DrawLrcThread(gc, LrcScanner.getLrcDir() + lrcFileName + ".lrc");
                                dt.start();
                                isDrawing = true;
                            } else {
                                sf.downloadLrcFile(lrcFileName, singer);
                                isDrawing = false;
                            }
                        }
                    }
                }
            });
            shell.setSize(lrcShowIm.getBounds().width, lrcShowIm.getBounds().height);
        } else {
            isDrawing = false;
        }
        shell.setVisible(b);
    }

    /**
     * 结束显示歌词
     */
    public void showLyricsStop() {
        isDrawing = false;
    }

    /**
     * 设置歌词显示面板位置
     * 
     * @param x
     *                横坐标(Px)
     * @param y
     *                纵坐标(Px)
     */
    public void setLocation(int x, int y) {
        shell.setLocation(x, y);
    }

    /**
     * 设置歌词显示面板皮肤
     * 
     * @param lrcShowImage
     *                歌词显示面板皮肤文件
     */
    public void setImage(Image lrcShowImage) {
        this.lrcShowIm = lrcShowImage;
    }

    /**
     * 返回是否正在显示歌词
     * 
     * @return 显示歌词标志
     */
    public boolean isDrawing() {
        return isDrawing;
    }

    /**
     * 设置歌词文件名
     * 
     * @param lrcFileName
     *                歌词文件名
     */
    public void setLrcFileName(String lrcFileName) {
        this.lrcFileName = lrcFileName;
    }

    /**
     * 设置歌曲演唱者
     * 
     * @param musicSinger
     *                歌曲演唱者
     */
    public void setMusicSinger(String musicSinger) {
        singer = musicSinger;
    }
}
