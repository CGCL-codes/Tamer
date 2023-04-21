package ua.in.hutorny.commentsprimer;

import ua.in.hutorny.util.Comments;
import ua.in.hutorny.util.CommentRetriever.Option;

public class SQLinCommentsPrimer {

    static {
    }

    public static String DBMS() {
        return "pgsql";
    }

    public static String sqlSELECT = Comments.load();

    public static String sqlERROR1 = Comments.load();

    public static String sqlUPDATE = Comments.load();

    public static String sqlINSERT = Comments.load();

    public static String sqlSELECTtopN = Comments.load(DBMS());

    public static String sql2 = Comments.load();

    public static String sqlERROR2 = Comments.load();

    public static void main(String[] args) {
        System.out.println("SELECT = " + sqlSELECT);
        System.out.println("SELECT N= " + sqlSELECTtopN);
        System.out.println("UPDATE = " + sqlUPDATE);
        System.out.println("INSERT = " + sqlINSERT);
        System.out.println("sql2 = " + sql2);
        System.out.println("ERROR1 = " + sqlERROR1);
        System.out.println("ERROR2 = " + sqlERROR2);
    }
}
