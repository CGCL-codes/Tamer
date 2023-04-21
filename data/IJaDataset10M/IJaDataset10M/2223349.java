package presentation;

public interface Presentable {

    String getTextLabel();

    String getCategoryLabel();

    void displaySlide(SlideView slide_view);

    void hideSlide(SlideView sv);

    public PresentationComponent getPresentationComponent();
}
