package es.seat131.viewerfree.image;

import java.io.IOException;
import java.io.OutputStream;

public interface IManageImage {

    public void resize(String file, int height, OutputStream out) throws IOException;

    public void getDefaultImage(OutputStream out) throws IOException;
}
