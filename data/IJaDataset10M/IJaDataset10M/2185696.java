package bricks;

public class BlockBrick extends GameBrick {

    public BlockBrick() {
        super();
        this.getBody().add(new ConcreteSquare(this.getHorizontalCenterPos(), 0));
        this.getBody().add(new ConcreteSquare(this.getHorizontalCenterPos() + 1, 0));
        this.getBody().add(new ConcreteSquare(this.getHorizontalCenterPos(), 1));
        this.getBody().add(new ConcreteSquare(this.getHorizontalCenterPos() + 1, 1));
    }

    @Override
    protected void moveToPos1() {
    }

    @Override
    protected void moveToPos2() {
    }

    @Override
    protected void moveToPos3() {
    }

    @Override
    protected void moveToPos4() {
    }
}
