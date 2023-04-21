package de.flaxen.jdvdslideshow.effects;

/**
 *
 * @author develop
 */
public class Crop extends BoxEffect implements Cloneable {

    Box box = new Box();

    public static String name = "crop";

    public de.flaxen.jdvdslideshow.effects.BoxEffect.Box getBox() {
        return box;
    }

    @Override
    public void generate(StringBuilder builder) {
        builder.append(name);
        builder.append(":");
        box.generate(builder);
    }

    public void parse(String[] elements) {
        String[] boxElements = elements[4].split(";");
        box.parse(boxElements[0], boxElements[1]);
    }

    @Override
    public Object clone() {
        Crop crop = (Crop) super.clone();
        crop.box = (Box) crop.box.clone();
        return crop;
    }
}
