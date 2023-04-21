package it.mobistego.form;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

/**
 *
 * @author Pasquale
 */
public class ChooseImageForm extends List {

    public ChooseImageForm(String str) {
        super("Images", List.IMPLICIT);
        try {
            Image im = Image.createImage("/pippo.jpg");
            append(str, im);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
