package telnetd.io.toolkit;

import telnetd.io.BasicTerminalIO;
import telnetd.io.terminal.ColorHelper;
import java.io.IOException;

/** * Class that represents a label. * * @author Dieter Wimberger * @version 2.0 (16/07/2006) */
public class Label extends InertComponent {

    private String m_Content;

    /**   * Constructs a Label instance.   *   * @param io   Instance of a class implementing the BasicTerminalIO interface.   * @param name String that represents the components name.   * @param text String that represents the visible label.   */
    public Label(BasicTerminalIO io, String name, String text) {
        super(io, name);
        setText(text);
    }

    /**   * Constructs a Label instance, using the name as visible content.   *   * @param io   Instance of a class implementing the BasicTerminalIO interface.   * @param name String that represents the components name.   */
    public Label(BasicTerminalIO io, String name) {
        super(io, name);
        setText(name);
    }

    /**   * Mutator method for the text property of the label component.   *   * @param text String displayed on the terminal.   */
    public void setText(String text) {
        m_Content = text;
        m_Dim = new Dimension((int) ColorHelper.getVisibleLength(text), 1);
    }

    /**   * Accessor method for the text property of the label component.   *   * @return String that is displayed when the label is drawn.   */
    public String getText() {
        return m_Content;
    }

    /**   * Method that draws the label on the screen.   */
    public void draw() throws IOException {
        if (m_Position == null) {
            m_IO.write(m_Content);
        } else {
            m_IO.storeCursor();
            m_IO.setCursor(m_Position.getRow(), m_Position.getColumn());
            m_IO.write(m_Content);
            m_IO.restoreCursor();
            m_IO.flush();
        }
    }
}
