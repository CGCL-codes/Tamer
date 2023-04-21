package Resources;

/**
 *
 * @author mclord
 */
public class SqlFunctions {

    public static String AnswerSelectById() {
        return "Select Id as AnswerId, Response,IsCorrect,QuestionId From Answer Where Id = ?";
    }

    public static String AnswerInsert() {
        return "Insert Into Answer (Id,Response,IsCorrect,QuestionId) Values(?,?,?,?)";
    }

    public static String AnswerUpdate() {
        return "Update Answer Set Response=?,IsCorrect=?,QuestionId=? Where Id=?";
    }

    public static String AnswerDelete() {
        return "Delete From Answer Where Id =?";
    }

    public static String AnswersSelect() {
        return "Select Id as AnswerId, Response,IsCorrect,QuestionId From Answer";
    }
}
