package net.sf.freenote;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.sf.freenote.model.ImageFileShape;
import net.sf.util.TousleUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.RGB;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 一组图集的helper类
 * @author levin
 * @since 2008-2-17 下午11:01:52
 */
public class GalleryHelper {

    private static List<String> registry = new ArrayList<String>();

    static {
        registry.add("icons/gallery/mm_number_1.png");
        registry.add("icons/gallery/mm_number_2.png");
        registry.add("icons/gallery/mm_number_3.png");
        registry.add("icons/gallery/mm_pc_file.gif");
        registry.add("icons/gallery/mm_pc_internet.gif");
        registry.add("icons/gallery/mm_pc_mail.gif");
        registry.add("icons/gallery/mm_sign_star_blue.png");
        registry.add("icons/gallery/mm_sign_star_green.png");
        registry.add("icons/gallery/mm_sign_star_red.png");
        registry.add("icons/gallery/mm_arrow_left.gif");
        registry.add("icons/gallery/mm_arrow_right.gif");
        registry.add("icons/gallery/mm_arrow_up.gif");
        registry.add("icons/gallery/mm_face_angry.png");
        registry.add("icons/gallery/mm_face_happy.png");
        registry.add("icons/gallery/mm_face_sad.png");
        registry.add("icons/gallery/mm_image_clock.gif");
        registry.add("icons/gallery/mm_image_idea.gif");
        registry.add("icons/gallery/mm_image_money.png");
    }

    public static ImageFileShape createImageFileShape(String path) {
        try {
            ImageFileShape shape = new ImageFileShape();
            shape.setSize(new Dimension(16, 16));
            shape.setBackColor(new RGB(255, 255, 255));
            URL url = FileLocator.find(ShapesPlugin.getDefault().getBundle(), new Path(path), null);
            String s0 = path;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TousleUtil.copyIntput2Output(url.openStream(), baos);
            String s1 = new Base64Encoder().encode(baos.toByteArray());
            shape.setFileEmbed(new String[] { s0, s1 });
            return shape;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getRegistry() {
        return registry;
    }
}
