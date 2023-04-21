package halo.util.image;

public class ImageRectMaker {

    /**
	 * 设定方图尺寸。如果图片尺寸比指定尺寸大，则从中间区域进行设定
	 * 
	 * @param width
	 * @param height
	 * @param size
	 * @return
	 */
    public static ImageRect cutAndMiddleSquare(int width, int height, int size) {
        ImageRect rect = new ImageRect();
        if (width <= size && height <= size) {
            rect.setImageOrigin(new ImageOrigin(0, 0));
            rect.setImageSize(new ImageSize(width, height));
            return rect;
        }
        if (width > size && height > size) {
            int pw = (width - size) / 2;
            int ph = (height - size) / 2;
            rect.setImageOrigin(new ImageOrigin(pw, ph));
            rect.setImageSize(new ImageSize(size, size));
            return rect;
        }
        if (width > height) {
            int pw = (width - size) / 2;
            int ph = (size - height) / 2;
            rect.setImageOrigin(new ImageOrigin(pw, ph));
            rect.setImageSize(new ImageSize(size, height));
            return rect;
        }
        int pw = (size - width) / 2;
        int ph = (height - size) / 2;
        rect.setImageOrigin(new ImageOrigin(pw, ph));
        rect.setImageSize(new ImageSize(width, size));
        return rect;
    }
}
