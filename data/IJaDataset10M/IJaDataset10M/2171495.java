package iwant.util;

/**
 * 设定图片区域
 * 
 * @author akwei
 */
public class PicPoint {

    /**
	 * 设定图片区域
	 * 
	 * @param x0
	 *            图片文件的起始x轴坐标
	 * @param y0
	 *            图片文件的起始y轴坐标
	 * @param x1
	 *            图片文件的结束x轴坐标
	 * @param y1
	 *            图片文件的结束y轴坐标
	 */
    public PicPoint(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    private int x0;

    private int y0;

    private int x1;

    private int y1;

    /**
	 * 设定图片起始位置
	 * 
	 * @param x0
	 *            图片文件的起始x轴坐标
	 */
    public void setX0(int x0) {
        this.x0 = x0;
    }

    /**
	 * 设定图片起始位置
	 * 
	 * @param y0
	 *            图片文件的起始y轴坐标
	 */
    public void setY0(int y0) {
        this.y0 = y0;
    }

    /**
	 * 设定图片结束位置
	 * 
	 * @param x1
	 *            图片文件的结束x轴坐标
	 */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
	 * 设定图片结束位置
	 * 
	 * @param y1
	 *            图片文件的结束y轴坐标
	 */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX0() {
        return x0;
    }

    public int getX1() {
        return x1;
    }

    public int getY0() {
        return y0;
    }

    public int getY1() {
        return y1;
    }
}
