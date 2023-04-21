package onehao.funs.test;

public class BeansConstants {

    public static String dbdriver = "com.mysql.jdbc.Driver";

    public static String dburl = "jdbc:mysql://localhost:3306/test";

    public static String dbuser = "root";

    public static String dbpass = "123456";

    private BeansConstants() {
    }

    ;

    private static final BeansConstants b = new BeansConstants();

    public static BeansConstants getInstance() {
        return b;
    }
}
