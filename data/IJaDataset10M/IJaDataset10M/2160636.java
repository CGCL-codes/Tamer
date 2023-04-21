package dev.cinema.struts;

/**
 *
 * @author alessia
 */
public class CreateUserForm extends org.apache.struts.validator.ValidatorForm {

    private String username;

    private String password;

    private String confirmPassword;

    private Integer role;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     *
     */
    public CreateUserForm() {
        super();
    }
}
