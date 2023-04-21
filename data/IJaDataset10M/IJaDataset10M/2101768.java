package example;

public class ExampleBean {

    public int years;

    public String ultimateAnswer;

    public String email = "cat@dog.com";

    public ExampleBean() {
    }

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUltimateAnswer() {
        return ultimateAnswer;
    }

    public void setUltimateAnswer(String ultimateAnswer) {
        this.ultimateAnswer = ultimateAnswer;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
}
