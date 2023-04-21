package meta.library.model.bean;

/**
 * @author Biao Zhang
 *
 */
public class User {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private int priviledge;

    private int haveBook;

    public User() {
    }

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPriviledge() {
        return priviledge;
    }

    public void setPriviledge(int priviledge) {
        this.priviledge = priviledge;
    }

    public int getHaveBook() {
        return haveBook;
    }

    public void setHaveBook(int haveBook) {
        this.haveBook = haveBook;
    }

    @Override
    public String toString() {
        return this.username;
    }

    public boolean borrow() {
        if (this.haveBook < this.priviledge) {
            this.haveBook++;
            return true;
        } else {
            return false;
        }
    }

    public boolean returnBook() {
        if (this.haveBook > 0) {
            this.haveBook--;
            return true;
        } else {
            return false;
        }
    }
}
