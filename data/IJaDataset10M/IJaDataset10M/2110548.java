package co.fxl.gui.api;

public interface ILabel extends IClickable<ILabel>, ITextElement<ILabel>, IMouseOverElement<ILabel>, IDraggable<ILabel>, IDropTarget<ILabel> {

    ILabel html(String html);

    ILabel autoWrap(boolean autoWrap);

    ILabel hyperlink();
}
