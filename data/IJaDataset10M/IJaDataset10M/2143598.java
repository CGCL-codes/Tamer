package co.fxl.gui.api;

public interface IComboBox extends ITextElement<IComboBox>, IUpdateable<String>, IColored, IBordered, IFocusable<IComboBox>, IKeyRecipient<IComboBox>, IEditable<IComboBox> {

    IComboBox clear();

    IComboBox addNull();

    IComboBox addText(String... texts);
}
