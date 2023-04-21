package it.diamonds.gems;

public final class Stone extends AbstractDroppable {

    private static final int FIRST_FRAME_DELAY = 0;

    private Stone(DroppableColor color) {
        super(DroppableType.STONE, color, FIRST_FRAME_DELAY);
        setNumberOfFramesInTexture(8);
    }

    public static Stone create(DroppableColor color) {
        return new Stone(color);
    }
}
