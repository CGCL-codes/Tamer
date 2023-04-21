package org.scohen.juploadr.app.scalers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.scohen.juploadr.ui.ReusableUIFactory;

public class SWTScaler extends Scaler {

    public void doScale(File origFile, File scaledFile, Point newSize) {
        Image scaled = new Image(ReusableUIFactory.getInstance().getDisplay(), newSize.x, newSize.y);
        GC gc = new GC(scaled);
        Image orig = new Image(ReusableUIFactory.getInstance().getDisplay(), origFile.getAbsolutePath());
        ImageData imageData = orig.getImageData();
        gc.drawImage(orig, 0, 0, imageData.width, imageData.height, 0, 0, newSize.x, newSize.y);
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[1];
        loader.data[0] = scaled.getImageData();
        try {
            if (imageData.type < 0) {
                loader.save(new FileOutputStream(scaledFile), SWT.IMAGE_JPEG);
            } else {
                loader.save(new FileOutputStream(scaledFile), imageData.type);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isOptimal() {
        Image img = new Image(ReusableUIFactory.getInstance().getDisplay(), 5, 5);
        GC gc = new GC(img);
        boolean advanced = gc.getAdvanced();
        gc.dispose();
        img.dispose();
        return advanced;
    }
}
