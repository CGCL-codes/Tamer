package djudge.acmcontester.server.interfaces;

public interface TeamUsersInterface {

    public String registerTeam(String username, String password);

    public boolean enterContestTeam(String username, String password);

    public boolean changePasswordTeam(String username, String oldPassword, String newPassword);
}
