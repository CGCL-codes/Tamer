package net.sf.freenote.figure;

import java.io.ByteArrayInputStream;
import org.eclipse.draw2d.AbstractBackground;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import sun.misc.BASE64Decoder;

/**
 * 依据文件路径显示图片
 * @author levin
 * @since 2008-1-20 上午10:12:12
 */
public class ImageFileFigure extends ImageFigure implements FileFigure {

    private String[] fileEmbed;

    protected void paintFigure(Graphics graphics) {
        if (isOpaque()) graphics.fillRectangle(getBounds());
        if (getBorder() instanceof AbstractBackground) ((AbstractBackground) getBorder()).paintBackground(this, graphics, NO_INSETS);
        if (fileEmbed.length != 2 || fileEmbed[0] == null) return;
        Rectangle area = getClientArea();
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(fileEmbed[1]);
            ImageData imageData = new ImageData(new ByteArrayInputStream(bytes));
            if (area.width > 0) {
                imageData = imageData.scaledTo(area.width, area.height);
            }
            Image image = new Image(null, imageData);
            graphics.drawImage(image, area.x, area.y);
            image.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        repaint();
    }

    @Override
    public void setFileEmbed(String[] fileEmbed) {
        this.fileEmbed = fileEmbed;
        repaint();
    }
}
