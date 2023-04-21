package name.huzhenbo.java.patterns.mediator;

class NaiveClient implements Client {

    private Director director;

    public NaiveClient(Director director) {
        this.director = director;
    }

    public String changed() {
        return director.clientChanged(this);
    }
}
