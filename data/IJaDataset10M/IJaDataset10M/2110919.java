package co.edu.unal.ungrid.services.server.db.util;

public class ErrorMessages {

    private static String p1 = "<script type='text/javascript'>alert('";

    private static String p2 = "');location.href='";

    private static String p3 = "';</script>";

    public static String error(String pagina, String mensaje) {
        return (p1 + mensaje + p2 + pagina + p3);
    }
}
