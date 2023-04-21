package gomoku;

import javax.microedition.lcdui.*;

public class LoginForm extends Form implements CommandListener {

    private TextField m_NicknameTextField;

    private TextField m_IPTextField;

    private TextField m_PortTextField;

    private final int MAX_TEXT_FIELD_LENGTH = 32;

    public LoginForm() {
        super("Enter login: ");
        m_IPTextField = new TextField("Server Ip Address", "127.0.0.1", MAX_TEXT_FIELD_LENGTH, TextField.ANY);
        m_PortTextField = new TextField("Server Port", "28801", MAX_TEXT_FIELD_LENGTH, TextField.NUMERIC);
        m_NicknameTextField = new TextField("Nickname", "Gr8Pl4y3r", MAX_TEXT_FIELD_LENGTH, TextField.ANY);
        append(m_IPTextField);
        append(m_PortTextField);
        append(m_NicknameTextField);
    }

    public void commandAction(Command cmd, Displayable dsp) {
    }

    public String getNickname() {
        return m_NicknameTextField.getString();
    }

    public String getServerIP() {
        return m_IPTextField.getString();
    }

    public int getServerPort() {
        return Integer.valueOf(m_PortTextField.getString()).intValue();
    }
}
