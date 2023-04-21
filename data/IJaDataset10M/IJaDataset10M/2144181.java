package it.diamonds.droppable.interfaces;

public interface AnimatedObject {

    void update(long timer);

    void createAnimationSequence(int animationUpdateRate);

    void addFrame(int x, int y, int delay);

    int getNumberOfFrames();

    void setCurrentFrame(int frameIndex);

    int getCurrentFrame();

    int getFrameDuration(int index);
}
