package pl.edu.mimuw.mas.utils;

public final class AgentUtils {

    public static String extractLocalName(String aid) {
        String[] split = aid.split("@");
        return split[0];
    }

    private AgentUtils() {
    }
}
