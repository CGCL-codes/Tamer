package bluffinmuffin.protocol.commands.lobby.career;

import java.util.StringTokenizer;
import bluffinmuffin.protocol.commands.lobby.AbstractLobbyResponse;

public class CheckUserExistResponse extends AbstractLobbyResponse<CheckUserExistCommand> {

    @Override
    protected String getCommandName() {
        return CheckUserExistResponse.COMMAND_NAME;
    }

    public static String COMMAND_NAME = "lobbyCAREER_CHECK_USER_EXIST_RESPONSE";

    private final boolean m_isExist;

    public CheckUserExistResponse(StringTokenizer argsToken) {
        super(new CheckUserExistCommand(argsToken));
        m_isExist = Boolean.parseBoolean(argsToken.nextToken());
    }

    public CheckUserExistResponse(CheckUserExistCommand command, boolean exist) {
        super(command);
        m_isExist = exist;
    }

    @Override
    public void encode(StringBuilder sb) {
        super.encode(sb);
        append(sb, m_isExist);
    }

    public boolean isExist() {
        return m_isExist;
    }
}
