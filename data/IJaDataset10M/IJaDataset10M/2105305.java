package chat.data.user;

public class UserInfo {

    private String name;

    private String nickname;

    private int age;

    public UserInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
