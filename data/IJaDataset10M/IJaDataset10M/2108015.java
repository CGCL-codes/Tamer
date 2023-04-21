package errorhandling.server;

public class Server {

    String name = null;

    public Server(String name) {
        this.name = name;
    }

    public ErrorCode request1(Object param) {
        System.out.println("[" + name + "]: processing request1");
        if (!(param instanceof String)) return ErrorCode.WRONG_PARAMETER_ERROR;
        System.out.println("[" + name + "]: parameter was >" + (String) param + "<");
        return ErrorCode.NO_ERROR;
    }

    public ErrorCode request2(Object param) {
        System.out.println("[" + name + "]: processing request2");
        if (param == null) return ErrorCode.INTERNAL_ERROR;
        if (param instanceof String) return ErrorCode.WRONG_PARAMETER_ERROR;
        return ErrorCode.NO_ERROR;
    }
}
