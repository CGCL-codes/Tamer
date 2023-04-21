package adventure.placeable;

/**
 *
 * @author Michael Hanns
 *
 */
public interface PlaceableEdit extends Placeable {

    public void setName(String name);

    public void setDescription(String desc);

    public void setVisible(boolean flag);

    public void setLightSource(boolean flag);
}
