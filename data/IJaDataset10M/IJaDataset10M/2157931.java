package hambo.mobiledirectory.cronjobs;

public class ObjectReview {

    private String id;

    private String rating;

    private String comment;

    private String name;

    private String email;

    private String date;

    public ObjectReview(String id, String rating, String comment, String name, String email, String date) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.name = name;
        this.email = email;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }
}
