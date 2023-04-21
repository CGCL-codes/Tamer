package bluffinmuffin.protocol.commands.lobby;

import java.util.StringTokenizer;

public class JoinTableResponse extends AbstractLobbyResponse<JoinTableCommand> {

    @Override
    protected String getCommandName() {
        return JoinTableResponse.COMMAND_NAME;
    }

    private final int m_noSeat;

    public static String COMMAND_NAME = "lobbyJOIN_TABLE_RESPONSE";

    public JoinTableResponse(StringTokenizer argsToken) {
        super(new JoinTableCommand(argsToken));
        m_noSeat = Integer.parseInt(argsToken.nextToken());
    }

    public JoinTableResponse(JoinTableCommand command, int seat) {
        super(command);
        m_noSeat = seat;
    }

    @Override
    public void encode(StringBuilder sb) {
        super.encode(sb);
        append(sb, m_noSeat);
    }

    public int getNoSeat() {
        return m_noSeat;
    }
}
