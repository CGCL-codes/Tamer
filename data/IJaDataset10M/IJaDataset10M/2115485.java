package net.narusas.aceauction.pdf;

public class Entity {

    private final int pageNo;

    private final String text;

    private final float width;

    private final float x;

    private final float y;

    public Entity(String text, int pageNo, float y, float x, float width) {
        this.text = text;
        this.pageNo = pageNo;
        this.x = x;
        this.width = width;
        this.y = pageNo * 1000 + y;
    }

    public int getPageNo() {
        return pageNo;
    }

    public String getText() {
        return text;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return text + " P:" + pageNo + " Y:" + y;
    }
}
