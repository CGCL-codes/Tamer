package domain;

public abstract class LinkedableObject<T> {

    private T previous;

    private T following;

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public T getFollowing() {
        return following;
    }

    public void setFollowing(T following) {
        this.following = following;
    }
}
